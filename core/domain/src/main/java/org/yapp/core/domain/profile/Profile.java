package org.yapp.core.domain.profile;

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
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.BaseEntity;
import org.yapp.core.domain.user.User;

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
