package fr.yncrea.cir3.othello.service;

import fr.yncrea.cir3.othello.domain.CellStatus;
import fr.yncrea.cir3.othello.domain.Game;
import fr.yncrea.cir3.othello.domain.GameStatus;
import fr.yncrea.cir3.othello.exception.InvalidMoveException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tic Tac Toe Engine")
public class GameServiceTest {
    private final GameService service = new GameService();

    @Test
    @DisplayName("First player should be X")
    public void createFirstPlayerIsX() {
        Game game = service.create();
        assertThat(game.getCurrentPlayer()).isEqualTo(CellStatus.X);
    }

    @Test
    @DisplayName("All new game cells should be EMPTY")
    public void createEmptyCells() {
        CellStatus[][] board = service.create().getBoard();

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                assertThat(board[row][col]).isEqualTo(CellStatus.EMPTY);
            }
        }
    }

    @Test
    @DisplayName("New game status should be started")
    public void createStartedStatus() {
        Game game = service.create();
        assertThat(game.getStatus()).isEqualTo(GameStatus.STARTED);
    }

    @Test
    @DisplayName("Cannot play out of boundary")
    public void playOutOfBoundary() {
        Game game = service.create();
        Throwable thrown =  assertThrows(InvalidMoveException.class, () -> service.play(game, -1, 0));
        assertThat(thrown.getMessage()).isEqualTo("Row is not valid");

        thrown =  assertThrows(InvalidMoveException.class, () -> service.play(game, 3, 0));
        assertThat(thrown.getMessage()).isEqualTo("Row is not valid");

        thrown =  assertThrows(InvalidMoveException.class, () -> service.play(game, 0, -1));
        assertThat(thrown.getMessage()).isEqualTo("Column is not valid");

        thrown =  assertThrows(InvalidMoveException.class, () -> service.play(game, 0, 3));
        assertThat(thrown.getMessage()).isEqualTo("Column is not valid");
    }

    @Test
    @DisplayName("Cannot play on non empty cell")
    public void playCellNotEmpty() {
        Game game = service.create();

        service.play(game, 0, 0);

        Throwable thrown =  assertThrows(InvalidMoveException.class, () -> service.play(game, 0, 0));
        assertThat(thrown.getMessage()).isEqualTo("Cell is not empty");
    }

    @Test
    @DisplayName("Cannot play on non started game")
    public void playNotStartedGame() {
        Game game = service.create();
        game.setStatus(GameStatus.DRAW);

        Throwable thrown =  assertThrows(InvalidMoveException.class, () -> service.play(game, 0, 0));
        assertThat(thrown.getMessage()).isEqualTo("Game has already ended");
    }

    @Test
    @DisplayName("Players should alternate")
    public void playPlayerAlternate() {
        Game game = service.create();
        assertThat(game.getCurrentPlayer()).isEqualTo(CellStatus.X);

        service.play(game, 0, 0);
        assertThat(game.getCurrentPlayer()).isEqualTo(CellStatus.O);

        service.play(game, 1, 0);
        assertThat(game.getCurrentPlayer()).isEqualTo(CellStatus.X);
    }

    @Test
    @DisplayName("Detect a winning row")
    public void playWinningRow() {
        Game game = service.create();

        service.play(game, 0, 0);
        service.play(game, 1, 0);

        service.play(game, 0, 1);
        service.play(game, 1, 1);

        assertThat(game.getStatus()).isEqualTo(GameStatus.STARTED);
        service.play(game, 0, 2);

        assertThat(game.getStatus()).isEqualTo(GameStatus.FINISHED);
        assertThat(game.getWinner()).isEqualTo(CellStatus.X);
    }

    @Test
    @DisplayName("Detect a winning column")
    public void playWinningColumn() {
        Game game = service.create();

        service.play(game, 0, 0);
        service.play(game, 0, 1);

        service.play(game, 1, 0);
        service.play(game, 1, 1);

        service.play(game, 0, 2); // let O wins

        assertThat(game.getStatus()).isEqualTo(GameStatus.STARTED);
        service.play(game, 2, 1);

        assertThat(game.getStatus()).isEqualTo(GameStatus.FINISHED);
        assertThat(game.getWinner()).isEqualTo(CellStatus.O);
    }

    @Test
    @DisplayName("Detect a winning diagonal")
    public void playWinningDiagonal() {
        Game game = service.create();

        service.play(game, 0, 0);
        service.play(game, 0, 1);

        service.play(game, 1, 1);
        service.play(game, 0, 2);

        assertThat(game.getStatus()).isEqualTo(GameStatus.STARTED);
        service.play(game, 2, 2);

        assertThat(game.getStatus()).isEqualTo(GameStatus.FINISHED);
        assertThat(game.getWinner()).isEqualTo(CellStatus.X);
    }

    @Test
    @DisplayName("Detect a draw")
    public void playDraw() {
        Game game = service.create();

        service.play(game, 0, 1);
        service.play(game, 0, 0);

        service.play(game, 1, 0);
        service.play(game, 0, 2);

        service.play(game, 1, 1);
        service.play(game, 1, 2);

        service.play(game, 2, 0);
        service.play(game, 2, 1);

        service.play(game, 2, 2);

        assertThat(game.getStatus()).isEqualTo(GameStatus.DRAW);
        assertThat(game.getWinner()).isEqualTo(null);
    }
}
