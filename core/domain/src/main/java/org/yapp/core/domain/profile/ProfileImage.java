package org.yapp.core.domain.profile;

import org.yapp.core.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_image_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "status")
    private ProfileImageStatus status;

    public void accept() {
        this.status = ProfileImageStatus.ACCEPTED;
        this.profile.updateProfileImageUrl(this.imageUrl);
    }

    public void reject() {
        this.status = ProfileImageStatus.REJECTED;
    }
}
