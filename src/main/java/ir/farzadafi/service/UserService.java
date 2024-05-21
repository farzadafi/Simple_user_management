package ir.farzadafi.service;

import ir.farzadafi.exception.UserNameDuplicateException;
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
            if (e.getMessage().contains("duplicate key value violates unique constraint"))
                throw new UserNameDuplicateException
                        (String.format("%s is duplicate", user.getNationalCode()));
        }
        return user;
    }
}
