package nbgame;

import javafx.application.Application;
import javafx.stage.Stage;
import nbgame.engine.FileDriver;
import nbgame.game.Game;

public class NbGame extends Application {
    Game game = new Game();

    @Override
    public void start(Stage primaryStage) {
        game.initGame();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
