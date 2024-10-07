package ir.farzadafi;

import ir.farzadafi.dto.ChangePasswordRequest;
import ir.farzadafi.dto.GenerateNewVerificationCodeRequest;
import ir.farzadafi.exception.InformationDuplicateException;
import ir.farzadafi.exception.NotFoundException;
import ir.farzadafi.model.Address;
import ir.farzadafi.model.LocationHierarchy;
import ir.farzadafi.model.User;
import ir.farzadafi.model.VerificationUser;
import ir.farzadafi.repository.UserRepository;
import ir.farzadafi.service.AddressService;
import ir.farzadafi.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService underTest;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    AddressService addressService;

    @Mock
    JavaMailSender javaMailSender;

    private User user;
    private Address addressSaveRequest;

    @BeforeEach
    void setUp() {
        LocalDate localDate = LocalDate.of(1375, 2, 20);
        LocalDateTime localDateTime = LocalDateTime.now();
        LocationHierarchy locationHierarchy = new LocationHierarchy(1, "kerman", null);
        LocationHierarchy locationHierarchy1 = new LocationHierarchy(2, "zarand", locationHierarchy);
        LocationHierarchy locationHierarchy2 = new LocationHierarchy(3, "yazdan_shahr", locationHierarchy1);
        locationHierarchy.setName("test");
        addressSaveRequest = new Address(1, locationHierarchy, locationHierarchy1, locationHierarchy2, "street");
        VerificationUser verificationUser = new VerificationUser(null, null, 1122, LocalDateTime.now());
        user = new User(null, "mahboobeh", "dorali", "1111111111",
                "mahboobDorali@gmail.com", "password123", localDate, addressSaveRequest, localDateTime,
                verificationUser, true, false);
    }

    @DisplayName("all scenario for user login method")
    @Nested
    class saveUserScenario {

        @Test
        @DisplayName("OK -> user save successfully")
        void itShouldSaveUser() {
            String plainTextPassword = "password123";
            String expectedEncodedValue = "$2a$10$Qe5rjHk8y7iF9I6RJvMlKeXbzlC9UgPZu/3hTqLxOcWnGdDmBZwK";
            when(bCryptPasswordEncoder.encode(plainTextPassword)).thenReturn(expectedEncodedValue);
            when(addressService.save(any(Address.class))).thenReturn(addressSaveRequest);
            doNothing().when(javaMailSender).send(any(MimeMessagePreparator.class));
            VerificationUser verificationUser = new VerificationUser(user, 12);
            given(userRepository.save(any(User.class)))
                    .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
            user.setVerificationUser(verificationUser);
            User actual = underTest.save(user);
            Assertions.assertNotNull(actual);
            assertEquals(user.getFirstname(), actual.getFirstname());
            assertEquals(user.getLastname(), actual.getLastname());
            assertEquals(user.getNationalCode(), actual.getNationalCode());
            assertEquals(user.getEmail(), actual.getEmail());
            assertEquals(user.getPassword(), actual.getPassword());
            assertEquals(user.getBirthdate(), actual.getBirthdate());
            assertEquals(user.getCreatedIn(), actual.getCreatedIn());
            verify(javaMailSender, atLeastOnce()).send(any(MimeMessagePreparator.class));
        }

        @Test
        @DisplayName("Exception -> when national code is duplicate")
        void firstInvalidScenario() {
            DataIntegrityViolationException exception = new DataIntegrityViolationException("user.UK_national_code");
            when(userRepository.save(any())).thenThrow(exception);
            Exception e = assertThrows(InformationDuplicateException.class,
                    () -> underTest.save(user));
            assertEquals(user.getNationalCode() + " national code is duplicate", e.getMessage());
        }

        @Test
        @DisplayName("Exception -> when email is duplicate")
        void secondInvalidScenario() {
            DataIntegrityViolationException exception = new DataIntegrityViolationException("user.UK_email");
            when(userRepository.save(any())).thenThrow(exception);
            Exception e = assertThrows(InformationDuplicateException.class,
                    () -> underTest.save(user));
            assertEquals(user.getEmail() + " email is duplicate", e.getMessage());
        }
    }


    @Test
    @DisplayName("OK -> when want to enable user")
    void itShouldCanEnableUser() {
        int id = 10;
        doNothing().when(userRepository).enable(id);
        underTest.enable(10);
        verify(userRepository, atLeastOnce()).enable(id);
    }

    @Nested
    @DisplayName("all scenario for generate new verification code and sent it for user")
    class GenerateNewVerificationCodeAndSentIt {

        @Test
        @DisplayName("Exception -> when user email not found in database")
        void whenEmailNotFound() {
            GenerateNewVerificationCodeRequest generateNewVerificationCodeRequest =
                    new GenerateNewVerificationCodeRequest(user.getEmail(), user.getPassword());
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
            Exception e = assertThrows(NotFoundException.class,
                    () -> underTest.generateNewVerificationCodeAndSentIt(generateNewVerificationCodeRequest));
            assertEquals("username " + user.getEmail() + " not found", e.getMessage());
        }

        @Test
        @DisplayName("Exception -> when password of user is invalid")
        void whenPasswordIsInvalid() {
            GenerateNewVerificationCodeRequest generateNewVerificationCodeRequest =
                    new GenerateNewVerificationCodeRequest(user.getEmail(), "invalidPassword");
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            Exception e = assertThrows(IllegalArgumentException.class,
                    () -> underTest.generateNewVerificationCodeAndSentIt(generateNewVerificationCodeRequest));
            assertEquals("your information invalid", e.getMessage());
        }

        @Test
        @DisplayName("Exception -> when time of create last token less than 5 minutes")
        void whenLastTokenIsValid() {
            GenerateNewVerificationCodeRequest generateNewVerificationCodeRequest =
                    new GenerateNewVerificationCodeRequest(user.getEmail(), user.getPassword());
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            Exception e = assertThrows(IllegalStateException.class,
                    () -> underTest.generateNewVerificationCodeAndSentIt(generateNewVerificationCodeRequest));
            assertEquals("every 5 minuets can create a token", e.getMessage());
        }

        @Test
        @DisplayName("OK -> update user successfully")
        void updateUser() {
            try (MockedStatic<SecurityContextHolder> mocked = Mockito.mockStatic(SecurityContextHolder.class)) {
                SecurityContextImpl securityContextHolder = new SecurityContextImpl();
                securityContextHolder.setAuthentication(new UsernamePasswordAuthenticationToken("308", "test"));
                mocked.when(SecurityContextHolder::getContext).thenReturn(securityContextHolder);
                User userObject = new User();
                when(userRepository.findByNationalCode("308")).thenReturn(Optional.of(userObject));
                LocalDate birthdate = LocalDate.now();
                User userAfterUpdate = User.builder()
                        .firstname("farzad")
                        .lastname("afshar")
                        .birthdate(birthdate).build();
                when(userRepository.save(userObject)).thenReturn(userAfterUpdate);

                User actual = underTest.update(userObject);

                assertEquals(userAfterUpdate.getFirstname(), actual.getFirstname());
                assertEquals(userAfterUpdate.getLastname(), actual.getLastname());
                assertEquals(userAfterUpdate.getBirthdate(), actual.getBirthdate());
                assertNotNull(actual);
                verify(userRepository, atLeastOnce()).save(any());
            }
        }


        @Nested
        @DisplayName("all scenario for update password method")
        class UpdatePassword {

            private final String hashPassword = "$2a$12$EOQ/TPDwYD/Oy0AkRWzHqeS3q0KboH.JcVLlWgRMIzgXvhSW9/pc6"; //hash from test
            private final String nationalCode = "308";
            private final String currentPassword = "test";
            private final String newPassword = "new";

            @Test
            @DisplayName("Exception -> current password isn't valid")
            void invalidCurrentPassword() {
                try (MockedStatic<SecurityContextHolder> mocked = Mockito.mockStatic(SecurityContextHolder.class)) {
                    SecurityContextImpl securityContextHolder = new SecurityContextImpl();
                    securityContextHolder.setAuthentication(new UsernamePasswordAuthenticationToken(nationalCode, hashPassword));
                    mocked.when(SecurityContextHolder::getContext).thenReturn(securityContextHolder);
                    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                            () -> underTest.updatePassword(new ChangePasswordRequest(currentPassword, newPassword, newPassword)));
                    assertEquals("Current password is not valid!", e.getMessage());
                }
            }

            @Test
            @DisplayName("OK -> change password successfully done")
            void changePasswordDone() {
                when(bCryptPasswordEncoder.encode(newPassword)).thenReturn(hashPassword);
                when(bCryptPasswordEncoder.matches(currentPassword, hashPassword)).thenReturn(true);
                try (MockedStatic<SecurityContextHolder> mocked = Mockito.mockStatic(SecurityContextHolder.class)) {
                    SecurityContextImpl securityContextHolder = new SecurityContextImpl();
                    securityContextHolder.setAuthentication(new UsernamePasswordAuthenticationToken(nationalCode, hashPassword));
                    mocked.when(SecurityContextHolder::getContext).thenReturn(securityContextHolder);
                    underTest.updatePassword(new ChangePasswordRequest(currentPassword, newPassword, newPassword));
                    verify(userRepository, atLeastOnce()).updatePassword(hashPassword, nationalCode);
                }
            }
        }

        @Test
        @DisplayName("OK -> remove successfully user")
        void removeUser() {
            try (MockedStatic<SecurityContextHolder> mocked = Mockito.mockStatic(SecurityContextHolder.class)) {
                SecurityContextImpl securityContextHolder = new SecurityContextImpl();
                securityContextHolder.setAuthentication(new UsernamePasswordAuthenticationToken("308", "test"));
                mocked.when(SecurityContextHolder::getContext).thenReturn(securityContextHolder);
                doNothing().when(userRepository).deleteByNationalCode(anyString());
                underTest.remove();
                verify(userRepository, times(1)).deleteByNationalCode(anyString());
            }
        }

        @Nested
        @DisplayName("all scenario for dynamic user find")
        class DynamicSearchTest {

            @Test
            @DisplayName("OK -> find user based on age")
            @SuppressWarnings("unchecked")
            void ageUserFind() {
                when(userRepository.findAll((Specification<User>) any())).thenReturn(List.of());
                List<User> users = underTest.findAllByCriteria("age", "10");
                assertNotNull(users);
                assertEquals(0, users.size());
            }

            @Test
            @DisplayName("OK -> find user based on province")
            @SuppressWarnings("unchecked")
            void provinceUserFind() {
                when(userRepository.findAll((Specification<User>) any())).thenReturn(List.of());
                List<User> users = underTest.findAllByCriteria("province", "kerman");
                assertNotNull(users);
                assertEquals(0, users.size());
            }
        }

        @Test
        @DisplayName("OK -> find all user")
        void findAllUser() {
            Pageable pageable = PageRequest.of(1, 1, Sort.by(Sort.Order.asc("name")));
            when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));
            Page<User> users = underTest.getAllUser(pageable);
            assertNotNull(users);
            assertEquals(0, users.getTotalElements());
        }

        @Nested
        @DisplayName("all scenario for isLoginCheck method")
        class LoginCheckMethod{

            @Test
            @DisplayName("Exception -> when password incorrect")
            void whenPasswordIncorrect() {
                String nationalCode = "308";
                String password = "308";
                when(userRepository.findByNationalCode(nationalCode)).thenReturn(Optional.empty());
                Exception e = assertThrows(BadCredentialsException.class,
                        () -> underTest.isLoginCheck(nationalCode, password));
                assertEquals("Bad credentials", e.getMessage());
            }

            @Test
            @DisplayName("Exception -> when user enter password and DB password not same")
            void whenPasswordIsNotSame() {
                String nationalCode = "308";
                String password = "308";
                User user1 = new User();
                user1.setPassword("309");
                when(userRepository.findByNationalCode(nationalCode)).thenReturn(Optional.of(user1));
                Exception e = assertThrows(BadCredentialsException.class,
                        () -> underTest.isLoginCheck(nationalCode, password));
                assertEquals("Bad credentials", e.getMessage());

            }

            @Test
            @DisplayName("OK -> user can login")
            void userCanLogin() {
                String nationalCode = "308";
                String password = "308";
                User user1 = new User();
                user1.setPassword("308");
                when(bCryptPasswordEncoder.matches(password, user1.getPassword())).thenReturn(true);
                when(userRepository.findByNationalCode(nationalCode)).thenReturn(Optional.of(user1));
                User loginCheck = underTest.isLoginCheck(nationalCode, password);
                assertNotNull(loginCheck);
                assertEquals("308", user1.getPassword());
            }
        }
    }
}