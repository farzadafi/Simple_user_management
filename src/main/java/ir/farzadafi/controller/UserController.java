package ir.farzadafi.controller;

import ir.farzadafi.dto.*;
import ir.farzadafi.mapper.UserMapper;
import ir.farzadafi.model.User;
import ir.farzadafi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserSaveResponse> save(@Valid @RequestBody UserSaveRequest request) {
        User user = UserMapper.INSTANCE.requestToModel(request);
        User savedUser = userService.save(user);
        UserSaveResponse userSaveResponse = UserMapper.INSTANCE.modelToResponse(savedUser);
        return new ResponseEntity<>(userSaveResponse, HttpStatus.CREATED);
    }

    @PutMapping
    public UserUpdateResponse update(@Valid @RequestBody UserUpdateRequest request) {
        User user = UserMapper.INSTANCE.updateRequestToModel(request);
        User updatedUser = userService.update(user);
        return UserMapper.INSTANCE.modelToUpdateResponse(updatedUser);
    }

    @PatchMapping("/change-password")
    public void updatePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.updatePassword(request);
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestParam int id) {
        userService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
