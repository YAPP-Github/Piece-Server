package org.yapp.core.domain.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.value.ValuePick;

@Entity
@Table(name = "profile_value_pick")
@Getter
@NoArgsConstructor
public class ProfileValuePick {

    @Id
    @Column(name = "profile_value_pick_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "value_pick_id", nullable = false)
    private ValuePick valuePick;

    @Column
    private Integer selectedAnswer;

    @Builder
    public ProfileValuePick(Profile profile, ValuePick valuePick, Integer selectedAnswer) {
        this.profile = profile;
        this.valuePick = valuePick;
        this.selectedAnswer = selectedAnswer;
    }

    public void updatedSelectedAnswer(Integer newSelectedAnswer) {
        if (newSelectedAnswer == null || newSelectedAnswer <= 0) {
            throw new IllegalArgumentException("선택된 항목은 1 이상이어야 합니다.");
        }

        this.selectedAnswer = newSelectedAnswer;
    }
}
