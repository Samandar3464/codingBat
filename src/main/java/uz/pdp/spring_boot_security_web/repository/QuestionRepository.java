package uz.pdp.spring_boot_security_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.spring_boot_security_web.entity.QuestionEntity;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {
    List<QuestionEntity> findAllByTopicEntityName(String topicEntity_name);
    Optional<QuestionEntity> findByNameAndTopicEntityName(String name, String topicEntity_id);
}
