package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.exception.RecordAlreadyExistException;
import uz.pdp.spring_boot_security_web.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.model.dto.SubjectRequestDTO;
import uz.pdp.spring_boot_security_web.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService implements BaseService<SubjectEntity, SubjectRequestDTO> {
    private final SubjectRepository subjectRepository;

    @Override
    public List<SubjectEntity> getList() {
        return subjectRepository.findAll();
    }

    @Override
    public SubjectEntity getById(int id) {
        Optional<SubjectEntity> byId = subjectRepository.findById(id);
        if (!byId.isPresent()) {
            throw new RecordNotFountException("subject not found");
        }
        return byId.orElse(null);
    }

    @Override
    public boolean delete(int id) {
        Optional<SubjectEntity> byId = subjectRepository.findById(id);
        if (!byId.isPresent()) {
            throw new RecordNotFountException("subject not found");
        }
        subjectRepository.deleteById(id);
        return true;
    }

    @Override
    public SubjectEntity add(SubjectRequestDTO subjectRequestDTO) {
        Optional<SubjectEntity> byTitle = subjectRepository.findByTitle(subjectRequestDTO.getTitle());
        if (byTitle.isPresent()) {
            throw new RecordAlreadyExistException(String.format("Subject %s already exist", subjectRequestDTO.getTitle()));
        }
        SubjectEntity subjectEntity = new SubjectEntity().of(subjectRequestDTO);
        return subjectRepository.save(subjectEntity);
    }

    public SubjectEntity getByTitle(String title) {
        Optional<SubjectEntity> byId = subjectRepository.findByTitle(title);
        if (!byId.isPresent()) {
            throw new IllegalArgumentException(byId.get().getTitle() + " Subject not found");
        }
        return byId.get();
    }


    public SubjectEntity getById(Integer id) {
        Optional<SubjectEntity> byId = subjectRepository.findById(id);
        if (!byId.isPresent()) {
            throw new IllegalArgumentException(byId.get().getTitle() + " Subject not found");
        }
        return byId.get();
    }

    public SubjectEntity editSubject(Integer id, SubjectRequestDTO newTitle) {
        checkByTitle(newTitle.getTitle());
        SubjectEntity subject = getById(id);
        subject.setTitle(newTitle.getTitle());
        return subjectRepository.save(subject);
    }

    private void checkByTitle(String title) {
        Optional<SubjectEntity> optionalSubjectEntity = subjectRepository.findByTitleIgnoreCase(title);
        if (optionalSubjectEntity.isPresent()) {
            throw new RecordAlreadyExistException(String.format("subject %s already exists", title));
        }
    }
}
