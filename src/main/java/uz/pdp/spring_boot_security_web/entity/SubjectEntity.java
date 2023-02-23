package uz.pdp.spring_boot_security_web.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.spring_boot_security_web.model.dto.SubjectRequestDTO;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SubjectEntity extends BaseEntity {
    private String title;
    @OneToMany(mappedBy = "subjectEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<TopicEntity> topicEntities;

    public SubjectEntity of(SubjectRequestDTO subjectRequestDTO) {
        return SubjectEntity.builder()
                .title(subjectRequestDTO.getTitle())
                .build();
    }

    public SubjectEntity(String title) {
        this.title = title;
    }
}
