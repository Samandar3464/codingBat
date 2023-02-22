package uz.pdp.spring_boot_security_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<SubjectEntity, Integer> {
    Optional<SubjectEntity>  findByTitle(String title);

}
