package org.yapp.core.domain.value;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

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

    @Column
    private String placeholder;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<String> guides;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean isActive;
}
