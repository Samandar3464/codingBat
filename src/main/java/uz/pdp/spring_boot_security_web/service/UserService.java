package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class UserService implements BaseService<UserEntity, UserRegisterDTO> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Qualifier("javasampleapproachMailSender")
    private final JavaMailSender javaMailSender;

    public boolean enableUser(String code){
        Optional<UserEntity> byCode = userRepository.findByCode(code);
        if (!byCode.isPresent()){
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
        return false;
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
                "Ro'yhatdan o'tishni verify ",
                        "<a href='http://localhost:8080/api/user/verify/" + code  + "'>Tasdiqlash </a>"
        );
        return userRepository.save(userEntity);
    }

    public UserEntity editUserEntity(int userId, MultipartHttpServletRequest request) throws IOException {
        Optional<UserEntity> byId = userRepository.findById(userId);
        if (byId.isEmpty()){
            throw new RecordNotFountException("User not found");
        }
        UserEntity userEntity = byId.get();
        String photoUrl = savePhoto(request);
        userEntity.setPhotoUrl(photoUrl);
        return userRepository.save(userEntity);
    };


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

    private String savePhoto(MultipartHttpServletRequest request) throws IOException {
        String linkPhoto="/src/main/resources/images/";
        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());
        String name = file.getContentType();
        String[] split = name.split("/");
        String contentType = "."+split[split.length-1];

        byte[] bytes = file.getBytes();
        String namePhoto= UUID.randomUUID().toString();

        FileOutputStream fileOutputStream= new FileOutputStream(linkPhoto+namePhoto+contentType);
        fileOutputStream.write(bytes);
        return linkPhoto+namePhoto+contentType;
    }
}
