package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

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
        UserEntity userEntity = UserEntity.of(userRegisterDTO);
        userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        String code = UUID.randomUUID().toString();
        sendMail(
                userRegisterDTO.getEmail(),
                "Ro'yhatdan o'tishni verify ",
//                userRegisterDTO.getName() +
//                        " saytda royhatdan o'tganinggiz uchun raxmat .Ro'yhatdan o'tishni tugatish uchun verify tugmasini boshing " +
                        "<a href='http://localhost:8080/api/user/verify?code=" + code  + "'>Tasdiqlash </a>"
        );
        return userRepository.save(userEntity);
    }


    public boolean sendMail(String sendingEmail, String massage, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("codinglife2022@gmail.com");
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
}
