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
        boolean validate;
        int haut = row - 1;


            if (game.getCurrentPlayer() == CellStatus.O) {

                if (game.getBoard()[haut][col] == CellStatus.X ) {

                    while (game.getBoard()[haut][col] == CellStatus.X ) {

                        haut--;


                    }
                    if (game.getBoard()[haut][col] == CellStatus.O){
                        validate=true;


                    }
                    else {
                        throw new InvalidMoveException("Le coup n'est pas valide");
                    }
                    haut = row - 1;

                    while (validate==true) {

                        while (game.getBoard()[haut][col] == CellStatus.X) {

                            setCell(game,haut,col,CellStatus.O);
                            haut--;

                        }

                        setCell(game,row,col,CellStatus.O);
                        validate=false;
                    }
                }

            }
            //DE HAUT EN BAS en partant des noirs
            if (game.getCurrentPlayer() == CellStatus.X){
                if (game.getBoard()[haut][col] == CellStatus.O) {

                    while (game.getBoard()[haut][col] == CellStatus.O) {


                        haut--;

                    }
                    if (game.getBoard()[haut][col] == CellStatus.X){
                        validate=true;

                    }
                    else {
                        throw new InvalidMoveException("Le coup n'est pas valide");
                    }
                    haut = row - 1;

                    while (validate==true) {

                        while (game.getBoard()[haut][col] == CellStatus.O) {

                            setCell(game,haut,col,CellStatus.X);
                            haut--;

                        }

                        setCell(game,row,col,CellStatus.X);
                        validate=false;
                    }
                }
            }

        //de bas en haut en partant des blancs
        int bas=row+1;
        if (game.getCurrentPlayer() == CellStatus.O) {

            if (game.getBoard()[bas][col] == CellStatus.X ) {

                while (game.getBoard()[bas][col] == CellStatus.X ) {

                    bas++;


                }
                if (game.getBoard()[bas][col] == CellStatus.O){
                    validate=true;


                }
                else {
                    throw new InvalidMoveException("Le coup n'est pas valide");
                }
                bas = row + 1;

                while (validate==true) {

                    while (game.getBoard()[bas][col] == CellStatus.X) {

                        setCell(game,bas,col,CellStatus.O);
                        bas++;

                    }

                    setCell(game,row,col,CellStatus.O);
                    validate=false;
                }
            }

        }
        //DE BAS EN HAUT en partant des noirs
        if (game.getCurrentPlayer() == CellStatus.X){
            if (game.getBoard()[bas][col] == CellStatus.O) {

                while (game.getBoard()[bas][col] == CellStatus.O) {


                    bas++;

                }
                if (game.getBoard()[bas][col] == CellStatus.X){
                    validate=true;

                }
                else {
                    throw new InvalidMoveException("Le coup n'est pas valide");
                }
                bas = row + 1;

                while (validate==true) {

                    while (game.getBoard()[bas][col] == CellStatus.O) {

                        setCell(game,bas,col,CellStatus.X);
                        bas++;

                    }

                    setCell(game,row,col,CellStatus.X);
                    validate=false;
                }
            }
        }

        int droite=col-1;
        //DE GAUCHE A DROITE partant des blancs
        if (game.getCurrentPlayer() == CellStatus.O) {

            if (game.getBoard()[row][droite] == CellStatus.X ) {

                while (game.getBoard()[row][droite] == CellStatus.X ) {

                    droite--;


                }
                if (game.getBoard()[row][droite] == CellStatus.O){
                    validate=true;


                }
                else {
                    throw new InvalidMoveException("Le coup n'est pas valide");
                }
                droite = col -1;

                while (validate==true) {

                    while (game.getBoard()[row][droite] == CellStatus.X) {

                        setCell(game,row,droite,CellStatus.O);
                        droite--;

                    }

                    setCell(game,row,col,CellStatus.O);
                    validate=false;
                }
            }

        }
        //DE GAUCHE A DROITE en partant des noirs
        if (game.getCurrentPlayer() == CellStatus.X){
            if (game.getBoard()[row][droite] == CellStatus.O) {

                while (game.getBoard()[row][droite] == CellStatus.O) {


                    droite--;

                }
                if (game.getBoard()[row][droite] == CellStatus.X){
                    validate=true;

                }
                else {
                    throw new InvalidMoveException("Le coup n'est pas valide");
                }
                droite = col -1 ;

                while (validate==true) {

                    while (game.getBoard()[row][droite] == CellStatus.O) {

                        setCell(game,row,droite,CellStatus.X);
                        droite--;

                    }

                    setCell(game,row,col,CellStatus.X);
                    validate=false;
                }
            }
        }

        int gauche=col+1;
        //DE DROITE A GAUCHE partant des blancs
        if (game.getCurrentPlayer() == CellStatus.O) {

            if (game.getBoard()[row][gauche] == CellStatus.X ) {

                while (game.getBoard()[row][gauche] == CellStatus.X ) {

                    gauche++;


                }
                if (game.getBoard()[row][gauche] == CellStatus.O){
                    validate=true;


                }
                else {
                    throw new InvalidMoveException("Le coup n'est pas valide");
                }
                gauche = col +1;

                while (validate==true) {

                    while (game.getBoard()[row][gauche] == CellStatus.X) {

                        setCell(game,row,gauche,CellStatus.O);
                        gauche++;

                    }

                    setCell(game,row,col,CellStatus.O);
                    validate=false;
                }
            }

        }
        //DE DROITE A GAUCHE en partant des noirs
        if (game.getCurrentPlayer() == CellStatus.X){
            if (game.getBoard()[row][gauche] == CellStatus.O) {

                while (game.getBoard()[row][gauche] == CellStatus.O) {


                    gauche++;

                }
                if (game.getBoard()[row][gauche] == CellStatus.X){
                    validate=true;

                }
                else {
                    throw new InvalidMoveException("Le coup n'est pas valide");
                }
                gauche = col +1 ;

                while (validate==true) {

                    while (game.getBoard()[row][gauche] == CellStatus.O) {

                        setCell(game,row,gauche,CellStatus.X);
                        gauche++;

                    }

                    setCell(game,row,col,CellStatus.X);
                    validate=false;
                }
            }
        }



        //DIAGO HAUT GAUCHE partant des blancs
        if (game.getCurrentPlayer() == CellStatus.O) {

            if (game.getBoard()[haut][gauche] == CellStatus.X ) {

                while (game.getBoard()[haut][gauche] == CellStatus.X ) {

                    gauche++;
                    haut--;


                }
                if (game.getBoard()[haut][gauche] == CellStatus.O){
                    validate=true;


                }
                else {
                    throw new InvalidMoveException("Le coup n'est pas valide");
                }
                gauche = col +1;
                haut =row-1;

                while (validate==true) {

                    while (game.getBoard()[haut][gauche] == CellStatus.X) {

                        setCell(game,haut,gauche,CellStatus.O);
                        gauche++;
                        haut--;

                    }

                    setCell(game,row,col,CellStatus.O);
                    validate=false;
                }
            }

        }
        //DIAGO HAUT GAUCHE en partant des noirs
        if (game.getCurrentPlayer() == CellStatus.X){
            if (game.getBoard()[haut][gauche] == CellStatus.O) {

                while (game.getBoard()[haut][gauche] == CellStatus.O) {


                    gauche++;
                    haut--;

                }
                if (game.getBoard()[haut][gauche] == CellStatus.X){
                    validate=true;

                }
                else {
                    throw new InvalidMoveException("Le coup n'est pas valide");
                }
                gauche = col +1 ;
                haut= row-1;

                while (validate==true) {

                    while (game.getBoard()[haut][gauche] == CellStatus.O) {

                        setCell(game,haut,gauche,CellStatus.X);
                        gauche++;
                        haut--;

                    }

                    setCell(game,row,col,CellStatus.X);
                    validate=false;
                }
            }
        }

        //DIAGO BAS GAUCHE partant des blancs
        if (game.getCurrentPlayer() == CellStatus.O) {

            if (game.getBoard()[bas][gauche] == CellStatus.X ) {

                while (game.getBoard()[bas][gauche] == CellStatus.X ) {

                    gauche++;
                    bas++;


                }
                if (game.getBoard()[bas][gauche] == CellStatus.O){
                    validate=true;


                }
                else {
                    throw new InvalidMoveException("Le coup n'est pas valide");
                }
                gauche = col +1;
                bas =row+1;

                while (validate==true) {

                    while (game.getBoard()[bas][gauche] == CellStatus.X) {

                        setCell(game,bas,gauche,CellStatus.O);
                        gauche++;
                        bas++;

                    }

                    setCell(game,row,col,CellStatus.O);
                    validate=false;
                }
            }

        }
        //DIAGO BAS GAUCHE en partant des noirs
        if (game.getCurrentPlayer() == CellStatus.X){
            if (game.getBoard()[bas][gauche] == CellStatus.O) {

                while (game.getBoard()[bas][gauche] == CellStatus.O) {


                    gauche++;
                    bas++;

                }
                if (game.getBoard()[bas][gauche] == CellStatus.X){
                    validate=true;

                }
                else {
                    throw new InvalidMoveException("Le coup n'est pas valide");
                }
                gauche = col +1 ;
                bas= row+1;

                while (validate==true) {

                    while (game.getBoard()[bas][gauche] == CellStatus.O) {

                        setCell(game,bas,gauche,CellStatus.X);
                        gauche++;
                        bas++;

                    }

                    setCell(game,row,col,CellStatus.X);
                    validate=false;
                }
            }
        }

        //DIAGO HAUT DROITE partant des blancs
        if (game.getCurrentPlayer() == CellStatus.O) {

            if (game.getBoard()[haut][droite] == CellStatus.X ) {

                while (game.getBoard()[haut][droite] == CellStatus.X ) {

                    droite--;
                    haut--;


                }
                if (game.getBoard()[haut][droite] == CellStatus.O){
                    validate=true;


                }
                else {
                    throw new InvalidMoveException("Le coup n'est pas valide");
                }
                droite = col -1;
                haut =row-1;

                while (validate==true) {

                    while (game.getBoard()[haut][droite] == CellStatus.X) {

                        setCell(game,haut,droite,CellStatus.O);
                        droite--;
                        haut--;

                    }

                    setCell(game,row,col,CellStatus.O);
                    validate=false;
                }
            }

        }
        //DIAGO HAUT DROITE en partant des noirs
        if (game.getCurrentPlayer() == CellStatus.X){
            if (game.getBoard()[haut][droite] == CellStatus.O) {

                while (game.getBoard()[haut][droite] == CellStatus.O) {


                    droite--;
                    haut--;

                }
                if (game.getBoard()[haut][droite] == CellStatus.X){
                    validate=true;

                }
                else {
                    throw new InvalidMoveException("Le coup n'est pas valide");
                }
                droite = col -1 ;
                haut= row-1;

                while (validate==true) {

                    while (game.getBoard()[haut][droite] == CellStatus.O) {

                        setCell(game,haut,droite,CellStatus.X);
                        droite--;
                        haut--;

                    }

                    setCell(game,row,col,CellStatus.X);
                    validate=false;
                }
            }
        }

        //DIAGO BAS DROITE partant des blancs
        if (game.getCurrentPlayer() == CellStatus.O) {

            if (game.getBoard()[bas][droite] == CellStatus.X ) {

                while (game.getBoard()[bas][droite] == CellStatus.X ) {

                    droite--;
                    bas++;


                }
                if (game.getBoard()[bas][droite] == CellStatus.O){
                    validate=true;


                }
                else {
                    throw new InvalidMoveException("Le coup n'est pas valide");
                }
                droite = col -1;
                bas =row+1;

                while (validate==true) {

                    while (game.getBoard()[bas][droite] == CellStatus.X) {

                        setCell(game,bas,droite,CellStatus.O);
                        droite--;
                        bas++;

                    }

                    setCell(game,row,col,CellStatus.O);
                    validate=false;
                }
            }

        }
        //DIAGO BAS DROITE en partant des noirs
        if (game.getCurrentPlayer() == CellStatus.X){
            if (game.getBoard()[bas][droite] == CellStatus.O) {

                while (game.getBoard()[bas][droite] == CellStatus.O) {


                    droite--;
                    bas++;

                }
                if (game.getBoard()[bas][droite] == CellStatus.X){
                    validate=true;

                }
                else {
                    throw new InvalidMoveException("Le coup n'est pas valide");
                }
                droite = col +1 ;
                bas= row+1;

                while (validate==true) {

                    while (game.getBoard()[bas][droite] == CellStatus.O) {

                        setCell(game,bas,droite,CellStatus.X);
                        droite--;
                        bas++;

                    }

                    setCell(game,row,col,CellStatus.X);
                    validate=false;
                }
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
