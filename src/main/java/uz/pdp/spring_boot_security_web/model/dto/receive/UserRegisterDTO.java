package uz.pdp.spring_boot_security_web.model.dto.receive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.spring_boot_security_web.entity.role.RolePermissionEntity;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegisterDTO {
    private String email;
    private String password;
}
