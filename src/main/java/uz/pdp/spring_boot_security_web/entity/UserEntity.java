package uz.pdp.spring_boot_security_web.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.spring_boot_security_web.entity.role.RoleEnum;
import uz.pdp.spring_boot_security_web.entity.role.RolePermissionEntity;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    private String email;
    private String password;
    private String code;
    private  String photoUrl="F:\\Java lessons\\codingBat\\src\\main\\resources\\static\\images";
    @OneToOne(cascade = CascadeType.ALL)
    private RolePermissionEntity rolePermissionEntities;
    @ManyToMany(cascade =  CascadeType.ALL,fetch = FetchType.EAGER)
    private List<QuestionEntity> questionEntityList;
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = false;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolePermissionEntities.getAuthority();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public static UserEntity of(UserRegisterDTO userRegisterDTO) {
        RolePermissionEntity rolePermissionEntity = new RolePermissionEntity(List.of("USER"), List.of("READ"));
        return UserEntity.builder()
                .email(userRegisterDTO.getEmail())
                .rolePermissionEntities(rolePermissionEntity)
                .photoUrl("avatar.jpeg")
                .isEnabled(false)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .build();
    }
}
