package Project.MVC.View.MainScreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ChooseGameModeView extends VBox {

    public ChooseGameModeView(Stage window) {
        setSpacing(10);
        setPadding(new Insets(20,20,20,20));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: #1da1f2;");

        Label chooseGameTitle = new Label("Choose a mode");
        chooseGameTitle.setTextFill(Color.WHITE);
        chooseGameTitle.setFont(new Font("Arial", 30));
        Button pva = new Button("Player vs AI");
        Button pvs = new Button("Player vs Server");
        Button back = new Button("Go back");

        pva.setMinWidth(150);
        pvs.setMinWidth(150);
        back.setMinWidth(150);


        pva.setOnAction(e -> {
            // Hier moet iets komen zodat de speler naar tictactoescene word gebracht met pva
            TicTacToeView ticTacToeView = new TicTacToeView(window, "pva");
            Scene TicTacToeScene = new Scene(ticTacToeView, 900, 600);

            window.setScene(TicTacToeScene);
            window.setTitle("Tic Tac Toe Player vs AI");
        });

        pvs.setOnAction(e -> {
            // Hier moet iets komen zodat de speler naar tictactoescene word gebracht met pvs
            TicTacToeView ticTacToeView = new TicTacToeView(window, "pvs");
            Scene TicTacToeScene = new Scene(ticTacToeView, 900, 600);

            window.setScene(TicTacToeScene);
            window.setTitle("Tic Tac Toe Player vs Server");
        });

        back.setOnAction(e -> {
            System.out.println("Moet terug naar ChooseGameView");
        });

        getChildren().addAll(chooseGameTitle, pva, pvs, back);
    }
}