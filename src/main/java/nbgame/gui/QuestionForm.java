package nbgame.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class QuestionForm {
    private static final Button btnCancel = new Button("Cancel");
    private static final Button btnOk = new Button("Ok");
    private static boolean answer = true;

    public static boolean open(String prompt, String question) {
        Stage window = new Stage();
        window.setScene(setScene(question));
        window.setTitle(prompt);
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
        return answer;
    }

    private static Scene setScene(String question) {
        BorderPane root = new BorderPane();

        Label lblTitle = new Label(question);
        lblTitle.setStyle(
                "                -fx-font-family: Arial;" +
                "                -fx-text-fill: black;" +
                "                -fx-font-weight: bold;" +
                "                -fx-font-size: 14px;" +
                "                -fx-text-alignment: center;" +
                "                -fx-padding: 20 40 20 40;");

        btnOk.setStyle(
                "                -fx-font-family: Arial;" +
                "                -fx-font-size: 14px;" +
                "                -fx-font-weight: normal;" +
                "                -fx-text-fill: black;");
        btnOk.setOnAction(e -> {answer = true; close();});

        btnCancel.setStyle(
                "                -fx-font-family: Arial;" +
                "                -fx-font-size: 14px;" +
                "                -fx-font-weight: normal;" +
                "                -fx-text-fill: black;");
        btnCancel.setOnAction(e -> {answer = false; close();});

        HBox topBox = new HBox();
        topBox.setStyle("    -fx-alignment: center;");
        topBox.getChildren().add(lblTitle);

        HBox bottomBox = new HBox();
        bottomBox.setStyle(
                "                -fx-alignment: center-right;" +
                "                -fx-padding: 10px;");
        bottomBox.setSpacing(15);
        bottomBox.getChildren().addAll(btnOk, btnCancel);

        root.setTop(topBox);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root);

        return scene;
    }

    private static void close() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

}
