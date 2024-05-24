package ir.farzadafi.service;

import ir.farzadafi.exception.NotFoundException;
import ir.farzadafi.exception.TokenExpireException;
import ir.farzadafi.model.VerificationUser;
import ir.farzadafi.repository.VerificationUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationUserService {

    private final VerificationUserRepository repository;
    private final UserService userService;

    @Transactional
    public void verificationUser(int code) {
        VerificationUser verificationUser = repository.findByCode(code).orElseThrow(
                () -> new NotFoundException("please register again"));
        checkVerificationTime(verificationUser);
        userService.enable(verificationUser.getId());
        repository.customDelete(verificationUser.getId());
    }

    private void checkVerificationTime(VerificationUser verificationUser) {
        int minutesDifference = verificationUser.calculatePastVerificationTime();
        if (minutesDifference > 5)
            throw new TokenExpireException("your token is expired");
    }
}
