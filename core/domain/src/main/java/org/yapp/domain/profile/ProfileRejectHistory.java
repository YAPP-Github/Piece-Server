package org.yapp.domain.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.domain.BaseEntity;
import org.yapp.domain.user.User;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ProfileRejectHistory extends BaseEntity {

    @Id
    @Column(name = "profile_reject_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    boolean reasonImage;

    boolean reasonDescription;
}
