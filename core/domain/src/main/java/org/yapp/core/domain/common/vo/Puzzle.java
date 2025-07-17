package org.yapp.core.domain.common.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.Getter;

@Embeddable
@Getter
public final class Puzzle {

    @Column(name = "puzzle_count", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private final long count;

    protected Puzzle() {
        this.count = 0;
    }

    private Puzzle(long count) {
        if (count < 0) {
            throw new IllegalArgumentException("Puzzle count cannot be negative.");
        }
        this.count = count;
    }

    public static Puzzle of(long count) {
        return new Puzzle(count);
    }

    public Puzzle add(Puzzle other) {
        Objects.requireNonNull(other);
        return new Puzzle(this.count + other.count);
    }

    public Puzzle subtract(Puzzle other) {
        Objects.requireNonNull(other);
        if (this.count < other.count) {
            throw new IllegalArgumentException("Cannot subtract more puzzles than available.");
        }
        return new Puzzle(this.count - other.count);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Puzzle puzzle = (Puzzle) o;
        return count == puzzle.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }
}