package project.mvc.view.gameboard;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.HBox;
import project.mvc.controller.ApplicationController;
import project.mvc.view.GameBoard;

public class TicTacToeBoard extends GameBoard {

    private int turn = 1;
    private int counter;

    public TicTacToeBoard(double buttonHeight, double buttonWidth, GridPane layout, HBox topBar, ApplicationController controller) {
        super(3, 3, buttonHeight, buttonWidth, layout, topBar, controller);
    }

    public void setButtons(){
        Button[] tiles = super.getTiles();
        for(Button tile : tiles){
            tile.setOnAction(e -> {
                int id = Integer.parseInt(tile.getId());
                if(super.getController().playerHisTurn(id)) {
                    super.setGameStats();
                    updateTicTacToeBoard();
                    gameOver(super.getController().getGameOver());
                }
            });
        }
    }

    private void updateTicTacToeBoard(){
        int[] board = getController().getBoard();
        super.getController().getGameBoardLogic().printBoard();
        int counter = 0;
        for(int piece : board){
            Button b = super.getTiles()[counter];
            if(piece == 1){
                Image image = new Image(getClass().getResourceAsStream("../../web/ttt-black-circle.png"), super.getGameButtonWidth() - 20, super.getGameButtonHeight() - 20, false, false);
                ImageView imageView = new ImageView(image);
                if(!super.getController().getOffline()) {
                    Platform.runLater(() -> {
                        b.setGraphic(imageView);
                    });
                }
                else {
                    b.setGraphic(imageView);
                }
            }
            else if(piece == 2){
                Image image = new Image(getClass().getResourceAsStream("../../web/ttt-black-times.png"), super.getGameButtonWidth() - 20, super.getGameButtonHeight() - 20, false, false);
                ImageView imageView = new ImageView(image);
                if(!super.getController().getOffline()) {
                    Platform.runLater(() -> {
                        b.setGraphic(imageView);
                    });
                }
                else {
                    b.setGraphic(imageView);
                }
            }
            counter++;
        }
    }

    public void setAImove(int move){
        updateTicTacToeBoard();
        /*if(move >= 0)
            setMove( 2, super.getTiles()[move]);*/
    }

    public void setMoveForEitherParty(int move, int turn){
        updateTicTacToeBoard();

    }

    public void resetBoard(){
        super.getController().resetGame();
        Button[] tiles = super.getTiles();
        for (Button button : tiles) {
            button.setGraphic(null);
        }
    }

}