package org.yapp.profile.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.format.CommonResponse;
import org.yapp.profile.application.AdminProfileService;
import org.yapp.profile.presentation.request.UpdateProfileImageStatusRequest;

@RestController
@RequestMapping("/admin/v1/profileImages")
@RequiredArgsConstructor
public class ProfileImagesController {

    private final AdminProfileService adminProfileService;

    @PatchMapping("/{profileImageId}")
    public ResponseEntity<CommonResponse<Void>> updateProfileImageStatus(
        @PathVariable Long profileImageId,
        @RequestBody UpdateProfileImageStatusRequest request) {
        adminProfileService.updateProfileImageStatus(profileImageId, request.accepted());
        return ResponseEntity.ok(CommonResponse.createSuccess(null));
    }
}
