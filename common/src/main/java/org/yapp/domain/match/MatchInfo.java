package org.yapp.domain.match;

import org.yapp.domain.user.User;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
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
  private Boolean user1PieceChecked;

  @Column(name = "user_1_accept")
  private Boolean user1Accepted;

  @ManyToOne
  @JoinColumn(name = "user_2")
  private User user2;

  @Column(name = "user_2_piece_checked")
  private Boolean user2PieceChecked;

  @Column(name = "user_2_accept")
  private Boolean user2Accepted;
}
