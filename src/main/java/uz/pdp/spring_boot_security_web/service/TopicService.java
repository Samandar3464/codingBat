package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.exception.RecordAlreadyExistException;
import uz.pdp.spring_boot_security_web.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.model.dto.TopicEditRequestDto;
import uz.pdp.spring_boot_security_web.model.dto.TopicRequestDto;
import uz.pdp.spring_boot_security_web.repository.SubjectRepository;
import uz.pdp.spring_boot_security_web.repository.TopicRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicService implements BaseService<TopicEntity, TopicRequestDto> {
    private final TopicRepository topicRepository;
    private final SubjectRepository subjectRepository;

    @Override
    public List<TopicEntity> getList() {
        return topicRepository.findAll();
    }

    public List<TopicEntity> getBySubjectTitleList(String subjectTittle) {
        List<TopicEntity> allBySubjectEntitiesTitle = topicRepository.findAllBySubjectEntityTitle(subjectTittle);
        if (allBySubjectEntitiesTitle.isEmpty()) {
            throw new RecordNotFountException(String.format("Topic list not fount by subject %s ", subjectTittle));
        }
        return allBySubjectEntitiesTitle;
    }

    @Override
    public TopicEntity getById(int id) {
        TopicEntity topic = topicRepository.findById(id);
        if (Objects.isNull(topic)) {
            throw new RecordNotFountException(String.format("Topic  not fount by  %s ", id));
        }
        return topic;
    }

    @Override
    public boolean delete(int id) {
        TopicEntity topic = topicRepository.findById(id);
        if (Objects.isNull(topic)) {
            throw new RecordNotFountException(String.format("Topic  not fount by  %s ", id));
        }
        topicRepository.deleteById(id);
        return true;
    }

    @Override
    public TopicEntity add(TopicRequestDto topicRequestDto) {
        SubjectEntity subjectEntity = checkToExistence(topicRequestDto.getName(), topicRequestDto.getSubject());

        TopicEntity build = TopicEntity.builder()
                .name(topicRequestDto.getName())
                .subjectEntity(subjectEntity)
                .build();
        return topicRepository.save(build);
    }

    public void edit(int id,TopicEditRequestDto editRequestDto) {
        String newName = editRequestDto.getNewName();
        TopicEntity topicEntity = topicRepository.findById(id);
        if(Objects.isNull(topicEntity)){
            throw new RecordNotFountException("Could not find record belonged to topic with id: "+id);
        }
        checkToExistence(newName,topicEntity.getSubjectEntity().getId());
        topicEntity.setName(newName);
        topicRepository.save(topicEntity);
    }

    private SubjectEntity checkToExistence(String topicName, int subjectId){
        Optional<TopicEntity> topic = topicRepository.findByNameAndSubjectEntityId(topicName,subjectId);
        Optional<SubjectEntity> subject = subjectRepository.findById(subjectId);
        if(topic.isPresent()){
            throw new RecordAlreadyExistException("This topic already exists within "+subject.get().getTitle()+" subject");
        }
        return subject.orElse(null);
    }

    private SubjectEntity checkToExistence(String topicName, String subject){
        Optional<SubjectEntity> subjectEntity = subjectRepository.findByTitle(subject);
        if(subjectEntity.isEmpty()){
            throw new RecordNotFountException("Subject with name "+subject+" does not exist.");
        }
        int subjectId = subjectEntity.get().getId();
        Optional<TopicEntity> topic = topicRepository.findByNameAndSubjectEntityId(topicName,subjectId);
        if(topic.isPresent()){
            throw new RecordAlreadyExistException("This topic already exists within "+subjectEntity.get().getTitle()+" subject");
        }
        return subjectEntity.get();
    }
}
