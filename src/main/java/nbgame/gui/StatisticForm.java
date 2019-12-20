package nbgame.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import nbgame.game.Statistic;

import java.util.List;

public class StatisticForm {

    public static void open(String icoPath, List<Statistic> statistics) {
        Stage window = new Stage();
        window.setScene(setScene(statistics));
        window.setTitle("About");
        window.getIcons().add(new Image(icoPath));
        window.show();
    }

    private static Scene setScene(List<Statistic> statistics) {
        BorderPane root = new BorderPane();

        Label lblTitle = new Label("Game Statistic");
        lblTitle.setStyle(
                "    -fx-font-family: Arial;" +
                        "    -fx-font-weight: bold;" +
                        "    -fx-font-size: 18px;" +
                        "    -fx-text-alignment: left;" +
                        "    -fx-padding: 5 0 10 5;");

        ListView<String> listView = new ListView<>();
        if (statistics.size() == 0) listView.getItems().add("No results file.");
        for (Statistic statistic : statistics) {
            listView.getItems().add(statistic.toString());
        }

        listView.setStyle(
                "    -fx-pref-width: 500pt;" +
                "    -fx-font-family: Arial;" +
                        "    -fx-font-weight: bold;" +
                        "    -fx-font-size: 14px;" +
                        "    -fx-text-alignment: left;" +
                        "    -fx-padding: 0 5 0 5;");

        Button btnClose = new Button("Close");
        btnClose.setStyle(
                "    -fx-font-family: Arial;" +
                        "    -fx-font-size: 16px;" +
                        "    -fx-font-weight: bold;" +
                        "    -fx-text-fill: blue;");
        btnClose.setOnAction(e -> {
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();});

        HBox titleBox = new HBox();
        titleBox.setStyle("    -fx-alignment: center;");
        titleBox.getChildren().add(lblTitle);

        AnchorPane infoBox = new AnchorPane(); // HBox infoBox = new HBox();
        AnchorPane.setLeftAnchor(listView, 20d);
        AnchorPane.setRightAnchor(listView, 20d);
        AnchorPane.setBottomAnchor(listView, 20d);
        AnchorPane.setTopAnchor(listView, 20d);

//        infoBox.setStyle(
//                "    -fx-alignment: center;" +
//                "    -fx-pref-width: 550pt;");
        infoBox.getChildren().add(listView);

        HBox exitBox = new HBox();
        exitBox.setStyle(
                "    -fx-alignment: center-right;" +
                        "    -fx-padding: 15px;");

        exitBox.getChildren().add(btnClose);

        root.setTop(titleBox);
        root.setCenter(infoBox);
        root.setBottom(exitBox);

        Scene scene = new Scene(root);

        return scene;
    }
}
