package uz.pdp.spring_boot_security_web.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class QuestionEntity extends BaseEntity {

    private String name;
    private String text;
    private String example;
    private String tickIcon;
    @ManyToOne
    private TopicEntity topicEntity;
}
