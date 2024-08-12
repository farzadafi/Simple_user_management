package ir.farzadafi.controller;

import ir.farzadafi.controller.aspect.CheckAuthorize;
import ir.farzadafi.dto.*;
import ir.farzadafi.mapper.UserMapper;
import ir.farzadafi.model.User;
import ir.farzadafi.model.jaksonClass.BaseClass;
import ir.farzadafi.model.jaksonClass.SimpleSubClass;
import ir.farzadafi.model.jaksonClass.SubClassA;
import ir.farzadafi.model.jaksonClass.SubClassB;
import ir.farzadafi.service.TokenService;
import ir.farzadafi.service.UserService;
import ir.farzadafi.service.VerificationUserService;
import ir.farzadafi.validation.ValidSearchColumn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final VerificationUserService verificationUserService;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<UserSaveResponse> save(@Valid @RequestBody UserSaveRequest request) {
        User user = UserMapper.INSTANCE.requestToModel(request);
        user.getAddress().getCounty().setLocationHierarchyById(user.getAddress().getProvince().getId());
        user.getAddress().getCity().setLocationHierarchyById(user.getAddress().getCounty().getId());
        User savedUser = userService.save(user);
        UserSaveResponse userSaveResponse = UserMapper.INSTANCE.modelToResponse(savedUser);
        return new ResponseEntity<>(userSaveResponse, HttpStatus.CREATED);
    }

    @PostMapping("/verification-account")
    public void verificationAccount(@RequestParam int code) {
        verificationUserService.verificationUser(code);
    }

    @PostMapping("/generate-new-verification-code")
    public void generateNewVerificationCode(@Valid @RequestBody GenerateNewVerificationCodeRequest request) {
        userService.generateNewVerificationCodeAndSentIt(request);
    }

    @PutMapping
    @CheckAuthorize
    public UserUpdateResponse update(@Valid @RequestBody UserUpdateRequest request) {
        User user = UserMapper.INSTANCE.updateRequestToModel(request);
        User updatedUser = userService.update(user);
        return UserMapper.INSTANCE.modelToUpdateResponse(updatedUser);
    }

    @PatchMapping("/change-password")
    @CheckAuthorize
    public void updatePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.updatePassword(request);
    }

    @DeleteMapping
    @CheckAuthorize
    public ResponseEntity<Void> remove() {
        userService.remove();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/dynamic-find")
    @CheckAuthorize
    public List<UserSearchResponse> findWithCriteria(@ValidSearchColumn @RequestParam String column,
                                                     @RequestParam String value) {
        return userService.findAllByCriteria(column, value)
                .stream()
                .map(UserMapper.INSTANCE::modelToSearchResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("get-all")
    @CheckAuthorize
    public List<UserSearchResponse> getAllUser(@RequestParam(defaultValue = "10") int size,
                                               @RequestParam(defaultValue = "0") int page) {
        return userService
                .getAllUser(PageRequest.of(page, size))
                .stream()
                .map(UserMapper.INSTANCE::modelToSearchResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/get-token")
    public String getToken(@Valid JwtTokenRequest request) {
        return tokenService.createToken(request);
    }

    @GetMapping("/test-json")
    public String testJson(@RequestBody BaseClass baseClass) {
        if (baseClass instanceof SimpleSubClass)
            return "simple type json";
        else if (baseClass instanceof SubClassA)
            return "sub class a type json";
        else if (baseClass instanceof SubClassB)
            return "sub class b type json";
        else
            return "chom :|";
    }
}
