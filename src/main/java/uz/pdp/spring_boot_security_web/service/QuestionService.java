package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.entity.QuestionEntity;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.exception.RecordAlreadyExistException;
import uz.pdp.spring_boot_security_web.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.model.dto.QuestionRequestDto;
import uz.pdp.spring_boot_security_web.repository.QuestionRepository;
import uz.pdp.spring_boot_security_web.repository.TopicRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService implements BaseService<QuestionEntity, QuestionRequestDto> {
    private final QuestionRepository questionRepository;
    private final TopicRepository topicRepository;

    @Override
    public List<QuestionEntity> getList() {
        return questionRepository.findAll();
    }
    public List<QuestionEntity> getList(String name) {
        return questionRepository.findAllByTopicEntityName(name);
    }

    @Override
    public QuestionEntity getById(int id) {
        Optional<QuestionEntity> byId = questionRepository.findById(id);
        if (byId.isEmpty()){
            throw new RecordNotFountException("no question found ");
        }
        return byId.get();
    }

    @Override
    public boolean delete(int id) {
        Optional<QuestionEntity> byId = questionRepository.findById(id);
        if (byId.isEmpty()) {
           throw new RecordNotFountException("question not found");
        }
        questionRepository.deleteById(id);
        return true;
    }

    @Override
    public QuestionEntity add(QuestionRequestDto questionRequestDto) {
        Optional<QuestionEntity> byNameAndTopicEntityId =
                questionRepository.findByNameAndTopicEntityName(questionRequestDto.getName(), questionRequestDto.getTopicName());
        if (byNameAndTopicEntityId.isPresent()){
            throw new RecordAlreadyExistException("this question already exist in topic");
        }
        QuestionEntity questionEntity = QuestionEntity.builder()
                .name(questionRequestDto.getName())
                .text(questionRequestDto.getText())
                .example(questionRequestDto.getExample())
                .tickIcon("https://codingbat.com/c1.jpg")
                .topicEntity(topicRepository.findByName(questionRequestDto.getTopicName()))
                .build();
        return questionRepository.save(questionEntity);
    }

    public QuestionEntity update(int id,QuestionRequestDto questionRequestDto){
        Optional<QuestionEntity> optionalQuestion = questionRepository.findById(id);
        if(optionalQuestion.isEmpty()){
            throw new RecordAlreadyExistException("this question does not exist in topic");
        }
        QuestionEntity questionEntity = optionalQuestion.get();
        questionEntity.setId(id);
        questionEntity.setName(questionRequestDto.getName());
        questionEntity.setText(questionRequestDto.getText());
        questionEntity.setExample(questionRequestDto.getExample());
        return questionRepository.save(questionEntity);
    }

    public String getTopicNameByQuestion(QuestionEntity question, List<SubjectEntity> list){
        for (SubjectEntity subjectEntity : list) {
            for (TopicEntity topicEntity : subjectEntity.getTopicEntities()) {
                for (QuestionEntity questionEntity : topicEntity.getQuestionEntities()) {
                    if(questionEntity.equals(question)){
                        return topicEntity.getName();
                    }
                }
            }
        }
        return null;
    }
}
