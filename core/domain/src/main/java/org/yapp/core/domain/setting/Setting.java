package org.yapp.core.domain.setting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
public class Setting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "match_notification")
    private boolean matchNotification = true;

    @Column(name = "notification")
    private boolean notification = true;

    @Column(name = "acquaintance_block")
    private boolean acquaintanceBlock = true;

    public Setting(Long userId, boolean matchNotification, boolean notification,
        boolean acquaintanceBlock) {
        this.userId = userId;
        this.matchNotification = matchNotification;
        this.notification = notification;
        this.acquaintanceBlock = acquaintanceBlock;
    }

    public void updateMatchNotification(boolean status) {
        this.matchNotification = status;
    }

    public void updateNotification(boolean status) {
        this.notification = status;
    }

    public void updateAcquaintanceBlock(boolean status) {
        this.acquaintanceBlock = status;
    }
}
