package org.yapp.domain.profile;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.domain.value.ValueTalk;

@Entity
@Table(name = "profile_value_talk")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileValueTalk {
    @Id
    @Column(name = "profile_value_talk_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "value_item_id", nullable = false)
    private ValueTalk valueTalk;

    @Column
    private String summary;

    @Column(length = 300)
    private String answer;
}