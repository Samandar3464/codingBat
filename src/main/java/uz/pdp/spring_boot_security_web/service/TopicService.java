package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.exception.RecordAlreadyExistException;
import uz.pdp.spring_boot_security_web.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.model.dto.TopicRequestDto;
import uz.pdp.spring_boot_security_web.repository.SubjectRepository;
import uz.pdp.spring_boot_security_web.repository.TopicRepository;

import java.util.List;
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
        List<TopicEntity> allBySubjectEntitiesTitle = topicRepository.findAllBySubjectEntitiesTitle(subjectTittle);
        if (allBySubjectEntitiesTitle.isEmpty()) {
            throw new RecordNotFountException(String.format("Topic list not fount by subject %s ", subjectTittle));
        }
        return allBySubjectEntitiesTitle;
    }

    @Override
    public TopicEntity getById(int id) {
        Optional<TopicEntity> byId = topicRepository.findById(id);
        if (!byId.isPresent()) {
            throw new RecordNotFountException(String.format("Topic  not fount by  %s ", id));
        }
        return byId.get();
    }

    @Override
    public boolean delete(int id) {
        Optional<TopicEntity> byId = topicRepository.findById(id);
        if (!byId.isPresent()) {
            throw new RecordNotFountException(String.format("Topic  not fount by  %s ", id));
        }
        topicRepository.deleteById(id);
        return true;
    }

    @Override
    public TopicEntity add(TopicRequestDto topicRequestDto) {
        if (topicRepository.findByNameAndSubjectEntitiesId(topicRequestDto.getName(), topicRequestDto.getSubjectId()).isPresent()) {
            throw new RecordAlreadyExistException("This topic already exist into this subject ");
        }
        TopicEntity build = TopicEntity.builder()
                .name(topicRequestDto.getName())
                .subjectEntities(subjectRepository.findById(topicRequestDto.getSubjectId()).get())
                .build();
        return topicRepository.save(build);

    }
}
