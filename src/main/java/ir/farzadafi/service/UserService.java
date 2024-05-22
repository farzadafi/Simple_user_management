package ir.farzadafi.service;

import ir.farzadafi.dto.UserUpdateRequestDto;
import ir.farzadafi.exception.InformationDuplicateException;
import ir.farzadafi.model.User;
import ir.farzadafi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("user.UK_national_code"))
                throw new InformationDuplicateException
                        (String.format("%s national code is duplicate", user.getNationalCode()));
            if (e.getMessage().contains("user.UK_email"))
                throw new InformationDuplicateException
                        (String.format("%s email is duplicate", user.getEmail()));
        }
        return user;
    }

    public User update(UserUpdateRequestDto newUserInformation) {
        // TODO: 22.05.24 find user from DB or spring security, nationalCode and email
        User user = new User();
        Optional.ofNullable(newUserInformation.firstname()).ifPresent(user::setFirstname);
        Optional.ofNullable(newUserInformation.lastname()).ifPresent(user::setLastname);
        Optional.ofNullable(newUserInformation.birthdate()).ifPresent(user::setBirthdate);
        return userRepository.save(user);
    }
}
