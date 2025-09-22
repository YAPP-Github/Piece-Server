package org.yapp.core.domain.common.vo;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Currency;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public final class Money {

    private static final Pattern CURRENCY_PATTERN_ISO4217 = Pattern.compile("[A-Z]{3}");

    private BigDecimal amount;
    private Currency currency;

    public static <T> Money sum(Collection<T> items, Function<T, Money> mapper) {
        return items.stream()
            .map(mapper)
            .reduce(Money::add)
            .orElseGet(() -> {
                String defaultCurrency = items.stream()
                    .map(mapper)
                    .findFirst()
                    .map(m -> m.getCurrency().getCurrencyCode())
                    .orElse("KRW");
                return Money.of(BigDecimal.ZERO, defaultCurrency);
            });
    }

    public static Money of(BigDecimal amount, String currencyCode) {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(currencyCode, "currency");

        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Amount must be ≥ 0");
        }
        if (!CURRENCY_PATTERN_ISO4217.matcher(currencyCode).matches()) {
            throw new IllegalArgumentException("Currency must be ISO-4217");
        }

        return new Money(amount.stripTrailingZeros(),
            Currency.getInstance(currencyCode));
    }

    public Money applyFixedDiscount(Money discountAmount) {
        requireSameCurrency(discountAmount);
        BigDecimal discounted = this.amount.subtract(discountAmount.getAmount());
        if (discounted.signum() < 0) {
            return new Money(BigDecimal.ZERO, this.currency);
        }
        return new Money(discounted, this.currency);
    }

    public Money applyPercentageDiscount(BigDecimal percentage) {
        Objects.requireNonNull(percentage, "percentage");

        if (percentage.signum() < 0 || percentage.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100.");
        }

        BigDecimal discountFactor = BigDecimal.ONE.subtract(
            percentage.divide(BigDecimal.valueOf(100), MathContext.DECIMAL128));

        BigDecimal discounted = this.amount
            .multiply(discountFactor)
            .setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_UP)
            .stripTrailingZeros();

        if (currency.getDefaultFractionDigits() < 0) {
            throw new IllegalStateException("Unsupported currency for scaling: " + currency);
        }

        return new Money(discounted, this.currency);
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        var result = amount.subtract(other.amount);
        if (result.signum() < 0) {
            throw new IllegalArgumentException("Negative money");
        }
        return new Money(result, currency);
    }

    private void requireSameCurrency(Money m) {
        if (!currency.equals(m.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Money m)) {
            return false;
        }
        return amount.compareTo(m.amount) == 0
            && currency.equals(m.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currency);
    }
}
