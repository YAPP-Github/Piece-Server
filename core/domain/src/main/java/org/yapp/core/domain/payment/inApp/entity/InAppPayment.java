package org.yapp.core.domain.payment.inApp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.common.vo.Money;
import org.yapp.core.domain.payment.common.entity.Payment;
import org.yapp.core.domain.payment.common.enums.PaymentStatus;
import org.yapp.core.domain.payment.common.enums.PaymentType;
import org.yapp.core.domain.payment.inApp.enums.InAppStore;
import org.yapp.core.domain.payment.inApp.vo.purchaseCredential.StorePurchaseCredential;
import org.yapp.core.domain.payment.inApp.vo.purchaseId.StorePurchaseId;
import org.yapp.core.domain.user.User;

@Entity
@Table(name = "in_app_payments")
@DiscriminatorValue("IN_APP")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InAppPayment extends Payment {

    @Column(name = "purchase_id", nullable = false, unique = true)
    private String purchaseId;

    @Column(name = "product_uuid", nullable = false)
    private String productUUID;

    @Enumerated(EnumType.STRING)
    @Column(name = "in_app_store", nullable = false)
    private InAppStore inAppStore;

    @Lob
    @Column(name = "purchase_credential", nullable = false)
    private String purchaseCredential;

    @Builder
    public InAppPayment(Long id, Long userId, Money money, PaymentStatus status,
        String purchaseId, InAppStore inAppStore, String purchaseCredential, String productUUID) {

        super(id, User.builder().id(userId).build(), money, PaymentType.IN_APP,
            status);

        this.purchaseId = purchaseId;
        this.inAppStore = inAppStore;
        this.purchaseCredential = purchaseCredential;
        this.productUUID = productUUID;
    }

    public StorePurchaseId getStorePurchaseId() {
        if (this.purchaseId == null) {
            return null;
        }

        if (this.inAppStore == null) {
            return null;
        }

        return this.inAppStore.createPurchaseId(this.purchaseId);
    }

    public StorePurchaseCredential getStorePurchaseCredential() {
        if (this.purchaseCredential == null) {
            throw new IllegalStateException("purchaseCredential is null");
        }

        return this.inAppStore.createPurchaseCredential(this.purchaseCredential);
    }
}