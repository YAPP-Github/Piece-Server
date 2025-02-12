package org.yapp.core.domain.match;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.user.User;

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
  private User user1;

  @Column(name = "user_1_piece_checked")
  private Boolean user1PieceChecked = false;

  @Column(name = "user_1_refused")
  private Boolean user1Refused = false;

  @Column(name = "user_1_accept")
  private Boolean user1Accepted = false;

  @ManyToOne
  @JoinColumn(name = "user_2")
  private User user2;

  @Column(name = "user_2_piece_checked")
  private Boolean user2PieceChecked = false;

  @Column(name = "user_2_refused")
  private Boolean user2Refused = false;

  @Column(name = "user_2_accept")
  private Boolean user2Accepted = false;

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
      user1PieceChecked = true;
    } else {
      user2PieceChecked = true;
    }
  }

  public void acceptPiece(Long userId) {
    if (user1.getId().equals(userId)) {
      user1Accepted = true;
    } else {
      user2Accepted = true;
    }
  }

  public void refusePiece(Long userId) {
    if (user1.getId().equals(userId)) {
      user1Refused = true;
    } else {
      user2Refused = true;
    }
  }
}
