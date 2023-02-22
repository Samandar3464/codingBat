package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.entity.QuestionEntity;
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
        if (!byId.isPresent()){
            throw new RecordNotFountException("question not found ");
        }
        return byId.get();
    }

    @Override
    public boolean delete(int id) {
        List<QuestionEntity> all = questionRepository.findAll();
        Optional<QuestionEntity> byId = questionRepository.findById(id);
        if (!byId.isPresent()) {
           throw new RecordNotFountException("question not found");
        } questionRepository.deleteById(id);
        return true;
    }

    @Override
    public QuestionEntity add(QuestionRequestDto questionRequestDto) {
        Optional<QuestionEntity> byNameAndTopicEntityId =
                questionRepository.findByNameAndTopicEntityId(questionRequestDto.getName(), questionRequestDto.getTopicId());
        if (byNameAndTopicEntityId.isPresent()){
            throw new RecordAlreadyExistException("this question already exist into  topic");
        }
        QuestionEntity questionEntity = QuestionEntity.builder()
                .name(questionRequestDto.getName())
                .text(questionRequestDto.getText())
                .example(questionRequestDto.getExample())
                .topicEntity(topicRepository.findById(questionRequestDto.getTopicId()).get())
                .build();
        return questionRepository.save(questionEntity);
    }
}
