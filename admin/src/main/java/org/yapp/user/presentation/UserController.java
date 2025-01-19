package org.yapp.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.user.application.UserService;
import org.yapp.user.presentation.response.UserProfileValidationResponse;
import org.yapp.util.CommonResponse;
import org.yapp.util.PageResponse;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/admin/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<CommonResponse<PageResponse<UserProfileValidationResponse>>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        PageResponse<UserProfileValidationResponse> userProfilesWithPagination = userService.getUserProfilesWithPagination(
            page, size);

        return ResponseEntity.ok(CommonResponse.createSuccess(userProfilesWithPagination));
    }
}
