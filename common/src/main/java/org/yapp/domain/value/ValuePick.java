package org.yapp.domain.value;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "value_pick")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValuePick {

    @Id
    @Column(name = "value_pick_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String category;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private boolean isActive = true;

    @Type(JsonType.class)
    @Column(name = "answers", columnDefinition = "longtext", nullable = false)
    private Map<Integer, Object> answers;
}
