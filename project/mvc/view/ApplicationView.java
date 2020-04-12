package project.mvc.view;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import project.mvc.controller.ApplicationController;
import project.mvc.model.ApplicationModel;
import project.mvc.view.mainscreen.ErrorBox;
import project.mvc.view.mainscreen.MainView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * In the ApplicationView class all views be accessible. The primaryStage is set up and the necessary set-on-action
 * commands will be set.
 *
 */
public class ApplicationView implements ObserverView {

    private static final String TICTACTOE = "Tic-tac-toe";
    private static final String REVERSI = "Reversi";

    private ApplicationController applicationController;
    private ApplicationModel applicationModel;

    private HashMap<String, GameBoardView> games;

    private Stage primaryStage;

    private Scene mainScene;
    private Scene chooseGameScene;
    private Scene chooseGameModeScene;
    private Scene optionsScene;
    private Scene serverOptionsScene;
    private Scene ticTacToeScene;
    private Scene reversiScene;

    private ScreenView mainView;
    private ScreenView chooseGameView;
    private ScreenView chooseGameModeView;

    private GameBoardView ticTacToeView;
    private GameBoardView reversiView;

    private ScreenBorderPaneView serverOptionsView;
    private ScreenBorderPaneView optionsView;

    private ErrorBox errorBox;

    public ApplicationView(ApplicationController applicationController, ApplicationModel applicationModel){
        this.applicationController = applicationController;
        this.applicationModel = applicationModel;
        games = new HashMap<>();
    }

    public void update(){
        int currentMove = applicationModel.getCurrentMove();
        String game = applicationModel.getCurrentGame();
        int turn = applicationModel.getCurrentTurn();

        if(applicationModel.isOffline()) {
            games.get(game).getGameBoard().setAImove(currentMove);
        }
        else {
            games.get(game).getGameBoard().setMoveForEitherParty(currentMove, turn);

        }

    }

    public void initializeApplicationScreens(Stage primaryStage){
        this.primaryStage = primaryStage;
        primaryStage.setTitle("StartApp screen");
        mainView = new MainView(primaryStage, applicationController);

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            mainView.closeApplication(primaryStage);
        });

        // Declare and initialize the main scene. And the error box.
        mainScene = new Scene(mainView, 900, 600);
        errorBox = new ErrorBox();

        // Declare and initialize the chooseGameScene scene.
        chooseGameScene = mainView.getSceneUnderneath();
        chooseGameView = mainView.getViewUnderneath();

        // Declare and initialize the chooseGameModeScene scene.
        chooseGameModeScene = chooseGameView.getSceneUnderneath();
        chooseGameModeView = chooseGameView.getViewUnderneath();

        // Declare and initialize the Tic-tac-toe scene and view.
        ticTacToeScene = chooseGameModeView.getGameScenes().get(TICTACTOE);
        ticTacToeView = chooseGameModeView.getGameBoardViews().get(TICTACTOE);

        // Declare and initialize the Reversi scene and view.
        reversiScene = chooseGameModeView.getGameScenes().get(REVERSI);
        reversiView = chooseGameModeView.getGameBoardViews().get(REVERSI);

        optionsScene = mainView.getSceneOverhead();
        optionsView = mainView.getBorderPaneViewUnderneath();

        serverOptionsScene = chooseGameModeView.getSceneUnderneath();
        serverOptionsView = chooseGameModeView.getBorderPaneViewUnderneath();

        games.put(TICTACTOE, ticTacToeView);
        games.put(REVERSI, reversiView);

        setOnActionAllButtons();

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void setOnActionAllButtons(){
        setOnActionMainViewButtons();
        setOnActionOptionsViewButtons();
        setOnActionChooseGameViewButtons();
        setOnActionChooseGameModeViewButtons();
        setOnServerOptionsViewButtons();
        setOnActionGameViewButtons();
    }

    public ScreenBorderPaneView getServerOptionsView(){
        return serverOptionsView;
    }

    private void setOnActionMainViewButtons(){
        mainView.getButtons().get("Play").setOnAction(e -> {
            mainView.getWindow().setScene(chooseGameScene);
            mainView.getWindow().setTitle("Choose a game");
        });

        mainView.getButtons().get("Options").setOnAction(e -> {
             mainView.getWindow().setScene(optionsScene);
             mainView.getWindow().setTitle("Options");
        });

        mainView.getButtons().get("Exit").setOnAction(e -> mainView.closeApplication(mainView.getWindow()));
    }

    private void setOnActionOptionsViewButtons() {
        optionsView.getButtons().get("Change details").setOnAction(e -> {
            int port;
            try {
                port = Integer.parseInt(optionsView.getTextFields().get("Port").getText());
            }
            catch(NumberFormatException nm){
                port = 0;
            }
            applicationController.setSettings(optionsView.getTextFields().get("Username").getText().strip(), optionsView.getTextFields().get("IP Address").getText().strip(), port);});

        optionsView.getButtons().get("Go back").setOnAction(e -> {
            optionsView.getWindow().setScene(mainScene);
            optionsView.getWindow().setTitle("Main screen");
        });
    }

    private void setOnActionChooseGameViewButtons(){
        chooseGameView.getButtons().get(TICTACTOE).setOnAction(e -> {
            chooseGameView.getWindow().setScene(chooseGameModeScene);
            chooseGameView.getWindow().setTitle(TICTACTOE);
        });

        chooseGameView.getButtons().get(REVERSI).setOnAction(e -> {
            chooseGameView.getWindow().setScene(chooseGameModeScene);
            chooseGameView.getWindow().setTitle(REVERSI);
        });

        chooseGameView.getButtons().get(" Go back").setOnAction(e -> {
            chooseGameView.getWindow().setScene(mainScene);
            chooseGameView.getWindow().setTitle("StartApp screen");
        });

        errorBox.getConfirm().setOnAction(e -> {
            chooseGameView.getWindow().setScene(chooseGameModeScene);
            if(chooseGameModeView.getWindow().getTitle().equals(TICTACTOE)){
                chooseGameView.getWindow().setTitle(TICTACTOE);
            }
            else if(chooseGameView.getWindow().getTitle().equals(REVERSI)){
                chooseGameView.getWindow().setTitle(REVERSI);
            }
            errorBox.closeWindow();
        });
    }

    private void setOnActionChooseGameModeViewButtons(){
        chooseGameModeView.getButtons().get("Player vs AI").setOnAction(e -> {
            applicationController.setOffline(true);
            if(chooseGameModeView.getWindow().getTitle().equals(TICTACTOE)) {
                ticTacToeView.setMode("Player vs AI");
                applicationController.initializeGame(TICTACTOE);
                ticTacToeView.getGameBoard().setButtons();
                ticTacToeView.setRestartButton(false);
                chooseGameModeView.getWindow().setScene(ticTacToeScene);
                chooseGameModeView.getWindow().setTitle(TICTACTOE + "Player vs AI");
            }
            else{
                reversiView.setMode("Player vs AI");
                applicationController.initializeGame(REVERSI);
                reversiView.getGameBoard().setButtons();
                reversiView.setRestartButton(false);
                chooseGameModeView.getWindow().setScene(reversiScene);
                chooseGameModeView.getWindow().setTitle(REVERSI + "Player vs AI");
            }
        });

        chooseGameModeView.getButtons().get("AI vs Server").setOnAction(e -> {
            applicationController.setOffline(false);
            applicationController.setInLobby(true);
            if(applicationController.connectToServer()) {
                applicationController.fillPlayerSet();
                chooseGameModeView.getWindow().setScene(serverOptionsScene);
                if (chooseGameModeView.getWindow().getTitle().equals(TICTACTOE)) {
                    applicationController.initializeGame(TICTACTOE);
                    chooseGameModeView.getWindow().setTitle("Configure your settings for an online Tic Tac Toe game");
                } else if (chooseGameModeView.getWindow().getTitle().equals(REVERSI)) {
                    applicationController.initializeGame(REVERSI);
                    chooseGameModeView.getWindow().setTitle("Configure your settings for an online Reversi game");
                }
                applicationController.joinLobby();
            }
            else{
                mainView.showErrorWindow(errorBox);
            }
        });

        chooseGameModeView.getButtons().get("Go back").setOnAction(e -> {
            chooseGameModeView.getWindow().setScene(chooseGameScene);
            chooseGameModeView.getWindow().setTitle("Choose a game");
        });
    }

    private void setOnServerOptionsViewButtons(){
        serverOptionsView.getButtons().get("Go back").setOnAction(e -> {
            serverOptionsView.getWindow().setScene(chooseGameModeScene);
            applicationController.setInLobby(false);
            applicationController.disconnectFromServer();
            applicationController.clearActionLabel();
            if(serverOptionsView.getWindow().getTitle().contains(TICTACTOE)) {
                serverOptionsView.getWindow().setTitle(TICTACTOE);
            }
            else if(serverOptionsView.getWindow().getTitle().contains(REVERSI)) {
                serverOptionsView.getWindow().setTitle(REVERSI);
            }
        });

        serverOptionsView.getButtons().get("Refresh list").setOnAction(e -> {
            applicationController.fillPlayerSet();
            applicationController.fillChallengeSet();
        });

        serverOptionsView.getButtons().get("Challenge!").setOnAction(e -> applicationController.challengePlayer(serverOptionsView.getListViews().get("PlayerList").getSelectionModel().getSelectedItem()));

        serverOptionsView.getButtons().get("Accept challenge").setOnAction(e -> {
            if(!serverOptionsView.getListViews().get("ChallengeList").getSelectionModel().isEmpty()) {
                String challenge = serverOptionsView.getListViews().get("ChallengeList").getSelectionModel().getSelectedItem();
                String regex = ". Challenge number is: ";
                String challenger = challenge.substring(0, challenge.indexOf(regex));
                int challengeNr = Integer.parseInt((challenge.substring(challenge.lastIndexOf(": ") + 2)));
                applicationController.acceptChallenge(challenger, challengeNr);
            }
            else{
                serverOptionsView.getEventLabel().setText("You did not choose a challenge,\nor you do not have any challenges at this moment.\nRefresh the list and try again!");
            }
        });

        serverOptionsView.getButtons().get("Join Tournament Lobby").setOnAction(e -> {
            applicationController.setInGame(true);
            if(serverOptionsView.getWindow().getTitle().contains(TICTACTOE)) {
                ticTacToeView.getGameBoard().unSetButtons();
                ticTacToeView.setRestartButton(true);
                serverOptionsView.getWindow().setScene(ticTacToeScene);
                serverOptionsView.getWindow().setTitle(TICTACTOE + " tournament lobby");
            }
            else if(serverOptionsView.getWindow().getTitle().contains(REVERSI)) {
                reversiView.getGameBoard().unSetButtons();
                reversiView.setRestartButton(true);
                serverOptionsView.getWindow().setScene(reversiScene);
                serverOptionsView.getWindow().setTitle(REVERSI + " tournament lobby");
            }
            applicationController.startOnlineGame();
        });

        serverOptionsView.getButtons().get("Subscribe").setOnAction(e -> {
            applicationController.subscribeToGame();
        });
    }

    private void setOnActionGameViewButtons(){
        ticTacToeView.getGameButtons().get("Exit " + TICTACTOE).setOnAction(e -> {
            ticTacToeView.getGameBoard().resetBoard();
            ticTacToeView.getWindow().setScene(chooseGameModeScene);
            ticTacToeView.getWindow().setTitle(TICTACTOE);
            applicationController.setInGame(false);
        });

        ticTacToeView.getGameButtons().get("Restart " + TICTACTOE).setOnAction(e -> ticTacToeView.getGameBoard().gameOver(4));
        reversiView.getGameButtons().get("Restart " + REVERSI).setOnAction(e -> reversiView.getGameBoard().gameOver(4));

        reversiView.getGameButtons().get("Exit " + REVERSI).setOnAction(e -> {
            reversiView.getGameBoard().resetBoard();
            reversiView.getWindow().setScene(chooseGameModeScene);
            reversiView.getWindow().setTitle(REVERSI);
            applicationController.setInGame(false);
        });


    }
}
