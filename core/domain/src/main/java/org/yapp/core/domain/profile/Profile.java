package org.yapp.core.domain.profile;

import java.util.List;

import org.yapp.core.domain.BaseEntity;
import org.yapp.core.domain.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "profile")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile extends BaseEntity {

    @Id
    @Column(name = "profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "profile")
    private User user;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProfileStatus profileStatus = ProfileStatus.INCOMPLETE;

    @Embedded
    private ProfileBasic profileBasic;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileValueTalk> profileValueTalks;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileValuePick> profileValuePicks;

    public void updateBasic(ProfileBasic profileBasic) {
        this.profileBasic = profileBasic;
    }

    public void updateProfileImageUrl(String imageUrl) {
        ProfileBasic profileBasic = this.profileBasic;

        if (profileBasic != null) {

            ProfileBasic updatedBasic = ProfileBasic.builder()
                    .nickname(profileBasic.getNickname())
                    .description(profileBasic.getDescription())
                    .birthdate(profileBasic.getBirthdate())
                    .height(profileBasic.getHeight())
                    .job(profileBasic.getJob())
                    .location(profileBasic.getLocation())
                    .smokingStatus(profileBasic.getSmokingStatus())
                    .weight(profileBasic.getWeight())
                    .snsActivityLevel(profileBasic.getSnsActivityLevel())
                    .contacts(profileBasic.getContacts())
                    .imageUrl(imageUrl)
                    .build();

            this.updateBasic(updatedBasic);
        }
    }

    public void updateProfileValuePicks(List<ProfileValuePick> profileValuePicks) {
        if (this.profileValuePicks != null) {
            this.profileValuePicks.clear();
            this.profileValuePicks.addAll(profileValuePicks);
        } else {
            this.profileValuePicks = profileValuePicks;
        }
    }

    public void updateProfileValueTalks(List<ProfileValueTalk> profileValueTalks) {
        if (this.profileValueTalks != null) {
            this.profileValueTalks.clear();
            this.profileValueTalks.addAll(profileValueTalks);
        } else {
            this.profileValueTalks = profileValueTalks;
        }
    }

    public void updateProfileStatus(ProfileStatus profileStatus) {
        this.profileStatus = profileStatus;
    }
}
