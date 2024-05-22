package ir.farzadafi.service;

import ir.farzadafi.exception.InformationDuplicateException;
import ir.farzadafi.model.User;
import ir.farzadafi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
}
