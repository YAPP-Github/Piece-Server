package org.yapp.core.domain.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.BaseEntity;
import org.yapp.core.domain.common.vo.Puzzle;
import org.yapp.core.domain.profile.Profile;

@Table(name = "user_table")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth_id")
    private String oauthId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phoneNumber;

    @Column(name = "role")
    private String role;

    @OneToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "profile_id", unique = true) // User가 profile_id를 FK로 가짐
    private Profile profile;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Embedded
    private Puzzle puzzleWallet;

    public void initializePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void updateUserRole(String role) {
        this.role = role;
    }

    public void addPuzzle(Puzzle puzzle) {
        if (this.puzzleWallet == null) {
            this.puzzleWallet = Puzzle.of(0L);
        }

        this.puzzleWallet = this.puzzleWallet.add(puzzle);
    }
}
