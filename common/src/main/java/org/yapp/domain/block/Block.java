package org.yapp.domain.block;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.domain.BaseEntity;
import org.yapp.domain.user.User;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blocked_contacts",
        uniqueConstraints = {
                @UniqueConstraint(name = "unq_user_phone", columnNames = {"user_id", "phoneNumber"})
        },
        indexes = {
                @Index(name = "idx_phone_number", columnList = "phoneNumber"),
        }
)
public class Block extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String phoneNumber;
}