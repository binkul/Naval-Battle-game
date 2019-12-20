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
    private static final Button btnStart = new Button("Start");

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

        btnStart.setStyle(
                "    -fx-font-family: Arial;" +
                "    -fx-font-size: 24px;" +
                "    -fx-font-weight: bold;" +
                "    -fx-text-fill: red;");
        btnStart.setOnAction(startGame);

        StackPane bottomSet = new StackPane();
        bottomSet.setStyle(
                "     -fx-pref-height: 150px;" +
                "     -fx-alignment: center;");
        bottomSet.getChildren().add(btnStart);

        BorderPane root = new BorderPane();
        root.setStyle(
                "     -fx-background-image: url(/pictures/introBackground.png);" +
                "     -fx-background-size: stretch;");
        root.setBottom(bottomSet);

        root.setPrefSize(1000, 700);

        Scene scene = new Scene(root);

        return scene;
    }

    private static EventHandler<ActionEvent> startGame = e -> {
        Stage stage = (Stage) btnStart.getScene().getWindow();
        stage.close();
    };
}
