package fr.yncrea.cir3.othello.service;

import fr.yncrea.cir3.othello.domain.CellStatus;
import fr.yncrea.cir3.othello.domain.Game;
import fr.yncrea.cir3.othello.domain.GameStatus;
import fr.yncrea.cir3.othello.exception.InvalidMoveException;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private static final int SIZESQUARE = 8;


    /**
     * Create a new game
     *
     * @return a new clean game
     */
    public Game create() {
        Game game = new Game();

        game.setStatus(GameStatus.STARTED);
        game.setCurrentPlayer(CellStatus.X);

        for (int row = 0; row < SIZESQUARE; ++row) {
            for (int col = 0; col < SIZESQUARE; ++col) {
                setCell(game, row, col, CellStatus.EMPTY);
            }
        }
        initialiseCell(game);
        return game;
    }

    /**
     * Play at given row and column for the game
     *
     * @param game
     * @param row
     * @param column
     * @return Updated game (not cloned)
     */
    public Game play(Game game, int row, int column) {
        // check if row is valid

        if (row < 0 || row > SIZESQUARE) {
            throw new InvalidMoveException("Row is not valid");
        }

        // check if column is valid
        if (column < 0 || column > SIZESQUARE) {
            throw new InvalidMoveException("Column is not valid");
        }

        // check if the game is still started
        if (game.getStatus() != GameStatus.STARTED) {
            throw new InvalidMoveException("Game has already ended");
        }

        // check if cell is empty
        if (getCell(game, row, column) != CellStatus.EMPTY) {
            throw new InvalidMoveException("Cell is not empty");
        }

        // ok, so this is a valid move, update board
        coupValid(game,row,column);


        // check if there is a winner
        checkWinner(game);

        // no winner ? then check if this is a draw
        if (game.getWinner() == null) {
            checkDraw(game);
        }

        // if the game has not ended, alternate player
        alternatePlayer(game);

        return game;
    }

    /**
     * Get a cellStatus at the given row and column of the board
     *
     * @param game
     * @param row
     * @param column
     * @return
     */
    private CellStatus getCell(Game game, int row, int column) {
        return game.getBoard()[row][column];
    }

    private void initialiseCell(Game game){
        game.getBoard()[SIZESQUARE/2][SIZESQUARE/2] = CellStatus.O;
        game.getBoard()[SIZESQUARE/2-1][SIZESQUARE/2] = CellStatus.X;
        game.getBoard()[SIZESQUARE/2-1][SIZESQUARE/2-1] = CellStatus.O;
        game.getBoard()[SIZESQUARE/2][SIZESQUARE/2-1] = CellStatus.X;
    }
    /**
     * Get the cellStatus at the given row and column of the board
     * @param game
     * @param row
     * @param column
     * @param value
     */

    private void setCell(Game game, int row, int column, CellStatus value) {

        game.getBoard()[row][column] = value;
    }

    private CellStatus checkWinnerUsingVector(Game game, int fromRow, int fromColumn, int vectorRow, int vectorColumn) {
        int row = fromRow;
        int column = fromColumn;
        CellStatus winner = null;

        while (row >= 0 && row < 8 && column >= 0 && column < 8) {
            CellStatus status = getCell(game, row, column);

            if (winner == null) {
                // init on first iteration
                winner = status;
            } else if (winner != status) {
                // no winner
                return null;
            }

            // apply vector
            row+=vectorRow;
            column+=vectorColumn;
        }

        // empty cell is not a winner
        if (winner == CellStatus.EMPTY) {
            return null;
        }

        return winner;
    }
    private void coupValid(Game game,int row,int col){


        //DE HAUT EN BAS en partant des blancs
        boolean validate=false;
        int haut = row - 1;


            if (game.getCurrentPlayer() == CellStatus.O) {

                if (game.getBoard()[haut][col] == CellStatus.X ) {

                    while (game.getBoard()[haut][col] == CellStatus.X && validate!=true) {
                        if (game.getBoard()[haut][col] == CellStatus.O){
                            validate=true;
                        }
                        haut--;


                    }
                    haut = row - 1;
                    validate=true;
                    while (validate==true) {

                        while (game.getBoard()[haut][col] == CellStatus.X) {

                            setCell(game,haut,col,CellStatus.O);
                            haut--;

                        }

                        setCell(game,row,col,CellStatus.O);
                        validate=false;
                    }
                } else {
                    throw new InvalidMoveException("Le coup n'est pas valide");

                }

            }
            //DE HAUT EN BAS en partant des noirs
            if (game.getCurrentPlayer() == CellStatus.X){
                if (game.getBoard()[haut][col] == CellStatus.O) {

                    while (game.getBoard()[haut][col] == CellStatus.O && validate!=true) {
                        if (game.getBoard()[haut][col] == CellStatus.X){
                            validate=true;

                        }

                        haut--;

                    }
                    haut = row - 1;
                    validate=true;
                    while (validate==true) {

                        while (game.getBoard()[haut][col] == CellStatus.O) {

                            setCell(game,haut,col,CellStatus.X);
                            haut--;

                        }

                        setCell(game,row,col,CellStatus.X);
                        validate=false;
                    }
                } else {
                    throw new InvalidMoveException("Le coup n'est pas valide");

                }
            }


    }
    private CellStatus checkWinnerUsingVector(Game game) {
        // check rows
        for (int row = 0; row < 8; row++) {
            CellStatus winner = checkWinnerUsingVector(game, row, 0, 0, 1);
            if (winner != null) {
                return winner;
            }
        }

        // check column
        for (int col = 0; col < 8; col++) {
            CellStatus winner = checkWinnerUsingVector(game, 0, col, 1, 0);
            if (winner != null) {
                return winner;
            }
        }

        // check first diagonal
        CellStatus winner = checkWinnerUsingVector(game, 0, 0, 1, 1);
        if (winner != null) {
            return winner;
        }

        // check last diagonal
        return checkWinnerUsingVector(game, 2, 0, -1, 1);
    }

    /**
     * Check if there is a winner in the game, and update the status and the winner accordingly
     *
     * @param game
     */
    private void checkWinner(Game game) {
        CellStatus winner = checkWinnerUsingVector(game);
        if (winner != null) {
            game.setWinner(winner);
            game.setStatus(GameStatus.FINISHED);
        }
    }

    /**
     * Check if the game is a draw, and update the game status accordingly
     *
     * @param game
     */
    private void checkDraw(Game game) {
        // count empty cells
        int freeCells = 0;
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                if (getCell(game, row, col) == CellStatus.EMPTY) ++freeCells;
            }
        }

        // this is a draw
        if (freeCells == 0) {
            game.setStatus(GameStatus.DRAW);
        }
    }

    /**
     * Update the game with the next player
     *
     * @param game
     */
    private void alternatePlayer(Game game) {

        if (game.getCurrentPlayer() == CellStatus.O) {
            game.setCurrentPlayer(CellStatus.X);

        } else {
            game.setCurrentPlayer(CellStatus.O);
        }

    }
}
