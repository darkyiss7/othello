package fr.yncrea.cir3.othello.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Game {
    private static final int SIZESQUARE = 8;
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Enumerated(EnumType.STRING)
    private CellStatus winner;

    @Enumerated(EnumType.STRING)
    private CellStatus currentPlayer;

    @Lob
    private CellStatus[][] board;

    public Game() {
        board = new CellStatus[SIZESQUARE][SIZESQUARE];
    }
}
