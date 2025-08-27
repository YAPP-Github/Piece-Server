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
@NoArgsConstructor
@Getter
public class FreeInstantMatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "matched_time")
    private LocalDateTime matchedTime;

    public FreeInstantMatchHistory(Long userId, LocalDateTime matchedTime) {
        this.userId = userId;
        this.matchedTime = matchedTime;
    }
}
