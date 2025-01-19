package org.yapp.domain.profile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
import org.yapp.domain.BaseEntity;
import org.yapp.domain.user.User;

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

    @Embedded
    private ProfileBasic profileBasic;

    @Embedded
    private ProfileBio profileBio;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileValueTalk> profileValueTalks;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileValuePick> profileValuePicks;

    @Deprecated
    public void updateBio(ProfileBio profileBio) {
        this.profileBio = profileBio;
    }

    public void updateBasic(ProfileBasic profileBasic) {
        this.profileBasic = profileBasic;
    }

    public void updateProfileValuePicks(List<ProfileValuePick> profileValuePicks) {
        this.profileValuePicks = profileValuePicks;
    }

    public void updateProfileValueTalks(List<ProfileValueTalk> profileValueTalks) {
        this.profileValueTalks = profileValueTalks;
    }
}
