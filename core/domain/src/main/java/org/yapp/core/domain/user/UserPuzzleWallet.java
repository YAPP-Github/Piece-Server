package org.yapp.core.domain.user;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.yapp.core.domain.common.vo.Puzzle;

@Entity
@Table(name = "puzzle_wallet")
public class UserPuzzleWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Puzzle puzzle;

    public UserPuzzleWallet() {
        this.puzzle = Puzzle.of(0);
    }

    @Version
    private Long version;

    public Long getPuzzleCount() {
        return this.puzzle.getCount();
    }

    public void addPuzzle(Puzzle puzzle) {
        this.puzzle = this.puzzle.add(puzzle);
    }

    public void subtractPuzzle(Puzzle puzzle) {
        this.puzzle = this.puzzle.subtract(puzzle);
    }
}
