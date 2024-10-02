package ir.farzadafi.service;

import ir.farzadafi.dto.ChangePasswordRequest;
import ir.farzadafi.dto.GenerateNewVerificationCodeRequest;
import ir.farzadafi.exception.InformationDuplicateException;
import ir.farzadafi.exception.NotFoundException;
import ir.farzadafi.model.Address;
import ir.farzadafi.model.LocationHierarchy;
import ir.farzadafi.model.User;
import ir.farzadafi.repository.UserRepository;
import jakarta.persistence.criteria.Join;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final AddressService addressService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            Address save = addressService.save(user.getAddress());
            user.setAddress(save);
            user = userRepository.save(user);
            sendEmail(user.getEmail(), user.getVerificationUser().getCode());
        } catch (DataIntegrityViolationException e) {
            handleSaveException(e.getMessage(), user.getNationalCode(), user.getEmail());
        }
        return user;
    }

    private void handleSaveException(String message, String nationalCode, String email) {
        if (message.contains("user.UK_national_code"))
            throw new InformationDuplicateException
                    (String.format("%s national code is duplicate", nationalCode));
        if (message.contains("user.UK_email"))
            throw new InformationDuplicateException
                    (String.format("%s email is duplicate", email));
    }

    private void sendEmail(String to, int code) {
        String body = "<p>your verification code is <b>" + code + "<b/><p/>";
        MimeMessagePreparator message = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(to);
            messageHelper.setSubject("Email verification");
            messageHelper.setText(body, true);
        };
        javaMailSender.send(message);
    }

    public void enable(int id) {
        userRepository.enable(id);
    }

    public void generateNewVerificationCodeAndSentIt(GenerateNewVerificationCodeRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(
                () -> new NotFoundException("username " + request.email() + " not found"));
        checkGenerateNewVerificationCodeValidation(user, request.password());

        Integer id = user.getVerificationUser().getId();
        user.setUserVerification();
        user.getVerificationUser().setId(id);
        user.getVerificationUser().setCreated_in(LocalDateTime.now());
        userRepository.save(user);
        sendEmail(user.getEmail(), user.getVerificationUser().getCode());
    }

    private void checkGenerateNewVerificationCodeValidation(User user, String password) {
        if (!user.getPassword().equals(password))
            throw new IllegalArgumentException("your information invalid");
        int minutesDifference = user.getVerificationUser().calculatePastVerificationTime();
        if (minutesDifference <= 0)
            throw new IllegalStateException("every 5 minuets can create a token");
    }

    public User update(User newUserInformation) {
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        User user = userRepository.findByNationalCode(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Optional.ofNullable(newUserInformation.getFirstname()).ifPresent(user::setFirstname);
        Optional.ofNullable(newUserInformation.getLastname()).ifPresent(user::setLastname);
        Optional.ofNullable(newUserInformation.getBirthdate()).ifPresent(user::setBirthdate);
        return userRepository.save(user);
    }

    @Transactional
    public void updatePassword(ChangePasswordRequest request) {
        String nationalCode = SecurityContextHolder.getContext().getAuthentication().getName();
        String currentHashPassword = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (!passwordEncoder.matches(request.getCurrentPassword(), currentHashPassword))
            throw new IllegalArgumentException("Current password is not valid!");
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        userRepository.updatePassword(encodedPassword, nationalCode);
    }

    @Transactional
    public void remove() {
        String nationalCode = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.deleteByNationalCode(nationalCode);
    }

    public List<User> findAllByCriteria(String column, String value) {
        Specification<User> userSpecification = null;
        switch (column) {
            case "age" -> userSpecification = createAgeSpecification(Integer.parseInt(value));
            case "province", "county", "city" ->
                    userSpecification = createLocationHierarchySpecification(column, value);
        }
        assert userSpecification != null;
        return userRepository.findAll(userSpecification);
    }

    private Specification<User> createAgeSpecification(int age) {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int yearOfBirthdate = currentYear - age;
        return (root, query, builder)
                -> builder.equal(builder.function("YEAR", Integer.class, root.get("birthdate")), yearOfBirthdate);
    }

    private Specification<User> createLocationHierarchySpecification(String column, String value) {
        return (root, query, builder)
                -> {
            Join<User, Address> addressJoin = root.join("address");
            Join<Address, LocationHierarchy> hierarchyJoin = addressJoin.join(column);
            return builder.equal(hierarchyJoin.get("name"), value);
        };
    }

    public Page<User> getAllUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User isLoginCheck(String username, String password) {
        User user = findByNationalCode(username);
        if (user == null)
            throw new BadCredentialsException("Bad credentials");
        boolean isTheSamePassword = passwordEncoder.matches(password, user.getPassword());
        if (isTheSamePassword)
            return user;
        else
            throw new BadCredentialsException("Bad credentials");
    }

    public User findByNationalCode(String username) {
        Optional<User> user = userRepository.findByNationalCode(username);
        return user.orElse(null);
    }
}
