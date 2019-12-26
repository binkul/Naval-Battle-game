package nbgame.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class IntroForm {
    private static final Button BTN_START = new Button("Start");

    public static void open(String icoPath) {
        Stage window = new Stage();
        Image icon = new Image(icoPath);
        window.getIcons().add(icon);
        window.setScene(setScene());
        window.initStyle(StageStyle.UNDECORATED);
        window.setTitle("Naval Battle");
        window.showAndWait();
    }

    private static Scene setScene() {

        BTN_START.setStyle(
                "    -fx-font-family: Arial;" +
                "    -fx-font-size: 24px;" +
                "    -fx-font-weight: bold;" +
                "    -fx-text-fill: red;");
        BTN_START.setOnAction(startGame);

        StackPane bottomSet = new StackPane();
        bottomSet.setStyle(
                "     -fx-pref-height: 150px;" +
                "     -fx-alignment: center;");
        bottomSet.getChildren().add(BTN_START);

        BorderPane root = new BorderPane();
        root.setStyle(
                "     -fx-background-image: url(/pictures/introBackground.png);" +
                "     -fx-background-size: stretch;");
        root.setBottom(bottomSet);

        root.setPrefSize(1000, 700);

        return new Scene(root);
    }

    private static EventHandler<ActionEvent> startGame = e -> {
        Stage stage = (Stage) BTN_START.getScene().getWindow();
        stage.close();
    };
}
