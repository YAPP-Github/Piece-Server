package org.yapp.core.domain.match;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ManualMatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_1_id")
    private Long user1Id;

    @Column(name = "user_2_id")
    private Long user2Id;

    @Column(name = "match_date_time")
    private LocalDateTime dateTime;

    @Column(name = "is_matched")
    private Boolean isMatched;

    public ManualMatchHistory(Long user1Id, Long user2Id, LocalDateTime dateTime,
        Boolean isMatched) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.dateTime = dateTime;
        this.isMatched = isMatched;
    }

    public void checkMatched() {
        this.isMatched = true;
    }
}
