package ir.farzadafi.controller;

import ir.farzadafi.dto.UserSaveRequest;
import ir.farzadafi.dto.UserSaveResponse;
import ir.farzadafi.mapper.UserMapper;
import ir.farzadafi.model.User;
import ir.farzadafi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserSaveResponse> save(UserSaveRequest request) {
        User user = UserMapper.INSTANCE.requestToModel(request);
        User savedUser = userService.save(user);
        UserSaveResponse userSaveResponse = UserMapper.INSTANCE.modelToResponse(savedUser);
        return new ResponseEntity<>(userSaveResponse, HttpStatus.CREATED);
    }
}
