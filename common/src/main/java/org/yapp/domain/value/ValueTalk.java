package org.yapp.domain.value;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean isActive;
}
