package uz.pdp.spring_boot_security_web.model.dto.receive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import uz.pdp.spring_boot_security_web.entity.role.RolePermissionEntity;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegisterDTO {
    private String email;
    private String password;
    private String name;

    private List<String> role;
    private List<String> permissions;

    @JsonIgnore
    public boolean isUser(){
        return role == null && permissions == null;
    }



}
