package org.yapp.domain.value;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "value_talk")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValueTalk {
    @Id
    @Column(name = "value_talk_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String subTitle;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean isActive;
}
