package org.yapp.core.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_delete_reason")
public class UserDeleteReason extends BaseEntity {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "reason")
    private String reason;
}
