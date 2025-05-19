package org.yapp.core.domain.profile;

import java.time.LocalDate;
import java.util.Map;

import org.hibernate.annotations.Type;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ProfileBasic {

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "description")
    private String description;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "height")
    private Integer height;

    @Column(name = "job")
    private String job;

    @Column(name = "location")
    private String location;

    @Column(name = "smoking_status")
    private String smokingStatus;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "sns_activity_level")
    private String snsActivityLevel;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<ContactType, String> contacts;

    @Column(name = "image_url")
    private String imageUrl; // 프로필에서 보여줄 이미지. (상대방에게 및 프로필 미리보기에서 보여줄 이미지)

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
