package org.yapp.domain.profile.application;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileImage;
import org.yapp.core.domain.profile.ProfileImageStatus;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.ProfileErrorCode;
import org.yapp.domain.profile.dao.ProfileImageRepository;
import org.yapp.infra.s3.application.S3Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final S3Service s3Service;
    private final ProfileImageRepository profileImageRepository;
    private static final List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg",
            "image/png", "image/webp");

    public String uploadProfileImage(MultipartFile file) throws IOException {
        String uniqueFileName = "profiles/image/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new ApplicationException(ProfileErrorCode.INVALID_PROFILE_IMAGE_TYPE);
        }

        return s3Service.upload(file, uniqueFileName);
    }

    @Transactional
    public ProfileImage create(Long profileId, String imageUrl) {
        return profileImageRepository.save(ProfileImage.builder()
                .profile(Profile.builder().id(profileId).build())
                .imageUrl(imageUrl)
                .status(ProfileImageStatus.PENDING)
                .build());
    }

}
