package uz.pdp.spring_boot_security_web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.spring_boot_security_web.entity.QuestionEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseDto {
    private String firstParam;
    private String secondParam;
    private String result;
    private String questionName;
}
