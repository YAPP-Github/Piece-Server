package org.yapp.core.domain.promotion;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.BaseEntity;
import org.yapp.core.domain.common.vo.Money;
import org.yapp.core.domain.common.vo.Puzzle;

@Entity
@Table(name = "promotion_cash_products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromotionCashProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_cash_product_id")
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private Money price;

    @Column(name = "discount_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountRate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "promotion_target", nullable = false)
    @Enumerated(EnumType.STRING)
    private PromotionTarget promotionTarget;

    @Embedded
    @AttributeOverride(name = "count", column = @Column(name = "reward_puzzle_count"))
    private Puzzle rewardPuzzle;

    @Column(name = "card_image_url")
    private String cardImageUrl;

    private LocalDate startDate;

    private LocalDate endDate;

    @PrePersist
    private void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
