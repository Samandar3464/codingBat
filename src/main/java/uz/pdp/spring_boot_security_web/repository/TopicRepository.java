package uz.pdp.spring_boot_security_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<TopicEntity, Integer> {
    List<TopicEntity> findAllBySubjectEntitiesTitle(String subjectEntities_title);
    Optional<TopicEntity> findByNameAndSubjectEntitiesId(String name, int subjectEntities_id);

//    @Modifying
//    @Query(
//            value = "truncate table Topic_entity",
//            nativeQuery = true
//    )
//    void truncateMyTable();
}
