package org.yapp.core.domain.block;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.BaseEntity;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "direct_block")
public class DirectBlock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "blocking_user_id")
    private Long blockingUserId;
    @Column(name = "blocked_user_id")
    private Long blockedUserId;

    public DirectBlock(Long blockingUserId, Long blockedUserId) {
        this.blockingUserId = blockingUserId;
        this.blockedUserId = blockedUserId;
    }
}
