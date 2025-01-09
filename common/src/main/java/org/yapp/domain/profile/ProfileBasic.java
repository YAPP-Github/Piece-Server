package org.yapp.domain.profile;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Date;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ProfileBasic {

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "birthdate")
    private Date birthdate;

    @Column(name = "height")
    private Integer height;

    @Column(name = "job")
    private String job;

    @Column(name = "location")
    private String location;

    @Column(name = "smoking_status")
    private String smokingStatus;

    @Column(name = "religion")
    private String religion;

    @Column(name = "sns_activity_level")
    private String snsActivityLevel;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<String, String> contacts;

    @Column(name = "image_url")
    private String imageUrl;
}
