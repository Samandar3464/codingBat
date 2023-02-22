package uz.pdp.spring_boot_security_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;

import java.util.List;

public interface TopicRepository extends JpaRepository<TopicEntity, Integer> {
    List<TopicEntity> findAllBySubjectEntityTitle(String subjectEntity_title);
    TopicEntity findByNameAndSubjectEntityId(String name, int subjectEntity_id);

    TopicEntity findById(int id);

//    @Modifying
//    @Query(
//            value = "truncate table Topic_entity",
//            nativeQuery = true
//    )
//    void truncateMyTable();
}
