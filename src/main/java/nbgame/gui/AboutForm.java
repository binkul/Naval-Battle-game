package nbgame.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AboutForm {
    private static final String ABOUT = "This is a classic 'Naval Battle' game for single user. \n" +
            "\n" +
            "Before starting the game, please click the Game -> Settings and set: \n" +
            "- Name \n" +
            "- Number of four-mast ships (1-2)\n" +
            "- Number of three-mast ships (1-3)\n" +
            "- Number of two-mast ships (1-4)\n" +
            "- Number of one-mast ships (1-4)\n" +
            "\n" +
            "The game can be started in two ways:\n" +
            "- by automatic placement (Game -> Ships placement -> Random)\n" +
            "- by manual placement (Game -> Ships placement -> Manually)\n" +
            "The computer ships placement is always random!\n" +
            "After placing the ships, the game can be started via Game -> Start game.\n" +
            "Good lock!";
    private static final Button BTN_CLOSE = new Button("Close");

    static void open(String icoPath) {
        Stage window = new Stage();
        window.setScene(setScene());
        window.setTitle("About");
        window.getIcons().add(new Image(icoPath));
        window.show();
    }

    private static Scene setScene() {
        BorderPane root = new BorderPane();

        Label lblTitle = new Label("Game Info");
        lblTitle.setStyle(
                "    -fx-font-family: Arial;" +
                "    -fx-font-weight: bold;" +
                "    -fx-font-size: 18px;" +
                "    -fx-text-alignment: left;" +
                "    -fx-padding: 5 0 10 5;");

        Label lblInfo = new Label(ABOUT);
        lblInfo.setStyle(
                "    -fx-font-family: Arial;" +
                "    -fx-font-weight: normal;" +
                "    -fx-font-size: 14px;" +
                "    -fx-text-alignment: left;" +
                "    -fx-padding: 0 5 0 5;");

        BTN_CLOSE.setStyle(
                "    -fx-font-family: Arial;" +
                "    -fx-font-size: 16px;" +
                "    -fx-font-weight: bold;" +
                "    -fx-text-fill: blue;");
        BTN_CLOSE.setOnAction(e -> {
            Stage stage = (Stage) BTN_CLOSE.getScene().getWindow();
            stage.close();});

        HBox titleBox = new HBox();
        titleBox.setStyle("    -fx-alignment: center;");
        titleBox.getChildren().add(lblTitle);

        HBox infoBox = new HBox();
        infoBox.getChildren().add(lblInfo);

        HBox exitBox = new HBox();
        exitBox.setStyle(
                "    -fx-alignment: center-right;" +
                "    -fx-padding: 15px;");

        exitBox.getChildren().add(BTN_CLOSE);

        root.setTop(titleBox);
        root.setCenter(infoBox);
        root.setBottom(exitBox);

        return new Scene(root);
    }
}
