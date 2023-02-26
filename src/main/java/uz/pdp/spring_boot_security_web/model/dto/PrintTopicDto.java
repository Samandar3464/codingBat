package uz.pdp.spring_boot_security_web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrintTopicDto {
    private String name;
    private int allQuestions;
    private int solvedByUser;
    private int unsolved;
}
