package uz.uolimov.abs.controller;

import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.uolimov.abs.model.dto.user.*;
import uz.uolimov.abs.service.UserService;
import uz.uolimov.abs.validation.UpdateUserRequest;
import uz.uolimov.abs.validation.CreateUserRequest;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsResponse> get(@PathVariable UUID id) {
        UserDetailsResponse response = userService.get(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public Page<UserDetailsResponse> getAll(
        @RequestParam(value = "offset", required = false) Integer offset,
        @RequestParam(value = "pageSize", required = false) Integer pageSize,
        @RequestParam(value = "sortBy", required = false) String sortBy
    ) {
        if(null == offset) offset = 0;
        if(null == pageSize) pageSize = 10;
        if(StringUtils.isEmpty(sortBy)) sortBy ="name";
        return userService.getAll(PageRequest.of(offset, pageSize, Sort.by(sortBy)));
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> create(
        @CreateUserRequest @RequestBody CreateUserRequestDTO request
    ) {
        CreateUserResponse response = userService.create(request);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDetailsResponse> update(
        @PathVariable UUID id,
        @UpdateUserRequest @RequestBody UpdateUserRequestDTO request
    ) {
        UserDetailsResponse response = userService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }
}
