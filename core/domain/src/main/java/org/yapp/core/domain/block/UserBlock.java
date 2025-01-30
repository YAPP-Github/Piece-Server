package org.yapp.core.domain.block;


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
import org.yapp.core.domain.BaseEntity;
import org.yapp.core.domain.user.User;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserBlock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_block_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blocking_user_id", nullable = false)
    private User BlockingUser;

    @ManyToOne
    @JoinColumn(name = "blocked_user_id", nullable = false)
    private User BlockedUser;
}
