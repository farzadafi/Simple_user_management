package ir.farzadafi;

import ir.farzadafi.exception.InformationDuplicateException;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

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
        user = new User(null, "mahboobeh", "dorali", "1111111111",
                "mahboobDorali@gmail.com", "password123", localDate, addressSaveRequest, localDateTime,
                null, true, false);
    }

    @DisplayName("all scenario for user login method")
    @Nested
    class saveUserScenario {

        @Test
        @DisplayName("When every thing is OK and user save successfully")
        void itShouldSaveUser() {
            String plainTextPassword = "password123";
            String expectedEncodedValue = "$2a$10$Qe5rjHk8y7iF9I6RJvMlKeXbzlC9UgPZu/3hTqLxOcWnGdDmBZwK";
            when(bCryptPasswordEncoder.encode(plainTextPassword)).thenReturn(expectedEncodedValue);
            when(addressService.save(any(Address.class))).thenReturn(addressSaveRequest);
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
        }

        @Test
        @DisplayName("when national code is duplicate")
        public void firstInvalidScenario() {
            DataIntegrityViolationException exception = new DataIntegrityViolationException("user.UK_national_code");
            when(userRepository.save(any())).thenThrow(exception);
            assertThrows(InformationDuplicateException.class,
                    () -> underTest.save(user));
        }

        @Test
        @DisplayName("when email is duplicate")
        public void secondInvalidScenario() {
            DataIntegrityViolationException exception = new DataIntegrityViolationException("user.UK_email");
            when(userRepository.save(any())).thenThrow(exception);
            assertThrows(InformationDuplicateException.class,
                    () -> underTest.save(user));
        }
    }
}
