package uz.pdp.spring_boot_security_web.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicRequestDto {
    private String name;
    private int subjectId;
}
