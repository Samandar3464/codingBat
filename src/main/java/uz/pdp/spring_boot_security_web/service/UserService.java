package uz.pdp.spring_boot_security_web.service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.entity.role.RolePermissionEntity;
import uz.pdp.spring_boot_security_web.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRolePermissionDto;
import uz.pdp.spring_boot_security_web.repository.TopicRepository;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements BaseService<UserEntity, UserRegisterDTO> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TopicRepository topicRepository;
    @Qualifier("javasampleapproachMailSender")
    private final JavaMailSender javaMailSender;
    public boolean enableUser(String code) {
        Optional<UserEntity> byCode = userRepository.findByCode(code);
        if (!byCode.isPresent()) {
            throw new RecordNotFountException("This code live time end");
        }
        UserEntity userEntity = byCode.get();
        userEntity.setCode(null);
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        return true;
    }


    @Override
    public List<UserEntity> getList() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity getById(int id) {
        Optional<UserEntity> byId = userRepository.findById(id);
        return byId.orElse(null);
    }

    @Override
    public boolean delete(int id) {
        Optional<UserEntity> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        throw  new RecordNotFountException("User  not found");
    }

    @Override
    public UserEntity add(UserRegisterDTO userRegisterDTO) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(userRegisterDTO.getEmail());
        if (optionalUserEntity.isPresent()) {
            throw new IllegalArgumentException(String.format("email %s already exist", userRegisterDTO.getEmail()));
        }
        String code = UUID.randomUUID().toString();
        UserEntity userEntity = UserEntity.of(userRegisterDTO);
        userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userEntity.setCode(code);
        sendMail(
                userRegisterDTO.getEmail(),
                "Verify code for activate account ",
                "<a href='http://localhost:8080/api/user/verify/" + code + "'>  Confirmation </a>"
        );
        return userRepository.save(userEntity);
    }

    public UserEntity editUserEntity(int userId, MultipartFile file) throws IOException {
        Optional<UserEntity> byId = userRepository.findById(userId);
        if (byId.isEmpty()) {
            throw new RecordNotFountException("User not found");
        }
        UserEntity userEntity = byId.get();
        String photoUrl = savePhoto(file);
        userEntity.setPhotoUrl(photoUrl);
        return userRepository.save(userEntity);
    }

    ;


    public boolean sendMail(String sendingEmail, String massage, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("qweqq@gmail.com");
            message.setTo(sendingEmail);
            message.setSubject(massage);
            message.setText(code);
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String savePhoto(MultipartFile file) throws IOException {
        String linkPhoto = "F:\\Java lessons\\codingBat\\src\\main\\resources\\static\\images";
        if (file != null) {
            String originalFileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            String[] split = originalFileName.split("\\.");
            String randomName = UUID.randomUUID().toString() + "." + split[split.length - 1];
            Path path = Paths.get(linkPhoto + "/" + randomName);
            Files.copy(file.getInputStream(), path);
            return randomName;
        }
        return null;
    }

    public void editUserRolePermission(int id, UserRolePermissionDto userRolePermissionDto) {
        final List<String> ROLE_LIST = List.of("ADMIN", "USER", "SUPER_ADMIN");
        final List<String> PERMISSION_LIST = List.of("ADD", "GET", "UPDATE", "DELETE", "READ");
        Optional<UserEntity> byId = userRepository.findById(id);
        if (byId.isEmpty()){
            throw new RecordNotFountException("User not found");
        }
        UserEntity user = byId.get();
        RolePermissionEntity rolePermissionEntities = user.getRolePermissionEntities();
        List<String> roleEnum = rolePermissionEntities.getRoleEnum();
        List<String> permissionEnum = rolePermissionEntities.getPermissionEnum();
        if (userRolePermissionDto.getRole() != null && !roleEnum.contains(userRolePermissionDto.getRole())) {
            if (ROLE_LIST.contains(userRolePermissionDto.getRole())) {
                roleEnum.add(userRolePermissionDto.getRole());
                rolePermissionEntities.setRoleEnum(roleEnum);
            }
        }

        if (userRolePermissionDto.getPermission() != null && !permissionEnum.contains(userRolePermissionDto.getPermission())) {
            if (PERMISSION_LIST.contains(userRolePermissionDto.getPermission())) {
                permissionEnum.add(userRolePermissionDto.getPermission());
                rolePermissionEntities.setPermissionEnum(permissionEnum);
            }
        }
        user.setRolePermissionEntities(rolePermissionEntities);
        userRepository.save(user);
    }

    public void deleteUserRolePermission(int id, UserRolePermissionDto userRolePermissionDto) {
        UserEntity user = userRepository.getById(id);
        RolePermissionEntity rolePermissionEntities = user.getRolePermissionEntities();
        List<String> roleEnum = rolePermissionEntities.getRoleEnum();
        List<String> permissionEnum = rolePermissionEntities.getPermissionEnum();
        if (userRolePermissionDto.getRole() != null && roleEnum.contains(userRolePermissionDto.getRole())) {
            roleEnum.remove(userRolePermissionDto.getRole());
            rolePermissionEntities.setRoleEnum(roleEnum);
        }
        if (userRolePermissionDto.getPermission() != null && permissionEnum.contains(userRolePermissionDto.getPermission())) {
            permissionEnum.remove(userRolePermissionDto.getPermission());
            rolePermissionEntities.setPermissionEnum(permissionEnum);
        }
        user.setRolePermissionEntities(rolePermissionEntities);
        userRepository.save(user);
    }
}
