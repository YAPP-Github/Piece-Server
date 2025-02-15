package org.yapp.core.domain.match;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.yapp.core.domain.match.enums.UserMatchStatus;
import org.yapp.core.domain.user.User;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.MatchErrorCode;

@Entity
@Getter
@NoArgsConstructor
public class MatchInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_1")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user1;

    @Column(name = "user_1_match_status")
    @Enumerated(EnumType.STRING)
    private UserMatchStatus user1MatchStatus = UserMatchStatus.UNCHECKED;

    @ManyToOne
    @JoinColumn(name = "user_2")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user2;

    @Column(name = "user_2_match_status")
    @Enumerated(EnumType.STRING)
    private UserMatchStatus user2MatchStatus = UserMatchStatus.UNCHECKED;

    public MatchInfo(LocalDate date, User user1, User user2) {
        this.date = date;
        this.user1 = user1;
        this.user2 = user2;
    }

    public static MatchInfo createMatchInfo(User user1, User user2) {
        return new MatchInfo(LocalDate.now(), user1, user2);
    }

    public void checkPiece(Long userId) {
        if (user1.getId().equals(userId)) {
            user1MatchStatus = UserMatchStatus.CHECKED;
        } else if (user2.getId().equals(userId)) {
            user2MatchStatus = UserMatchStatus.CHECKED;
        } else {
            throw new ApplicationException(MatchErrorCode.INVALID_MATCH_ACCESS);
        }
    }

    public void acceptPiece(Long userId) {
        if (user1.getId().equals(userId)) {
            user1MatchStatus = UserMatchStatus.ACCEPTED;
        } else if (user2.getId().equals(userId)) {
            user2MatchStatus = UserMatchStatus.ACCEPTED;
        } else {
            throw new ApplicationException(MatchErrorCode.INVALID_MATCH_ACCESS);
        }
    }

    public void refusePiece(Long userId) {
        if (user1.getId().equals(userId)) {
            user1MatchStatus = UserMatchStatus.REFUSED;
        } else if (user2.getId().equals(userId)) {
            user2MatchStatus = UserMatchStatus.REFUSED;
        } else {
            throw new ApplicationException(MatchErrorCode.INVALID_MATCH_ACCESS);
        }
    }
}
