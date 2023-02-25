package uz.pdp.spring_boot_security_web.entity.role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum RoleEnum {
    @Enumerated(EnumType.STRING)
    ADMIN,
    @Enumerated(EnumType.STRING)
    USER,
    @Enumerated(EnumType.STRING)
    SUPER_ADMIN
}
