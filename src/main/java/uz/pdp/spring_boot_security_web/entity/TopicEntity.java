package uz.pdp.spring_boot_security_web.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.spring_boot_security_web.model.dto.TopicRequestDto;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class TopicEntity extends BaseEntity {

   private String name;
   @ManyToOne()
   private SubjectEntity subjectEntity;
   @OneToMany(mappedBy = "topicEntity",
           cascade = CascadeType.ALL)
   private List<QuestionEntity> questionEntities;


}
