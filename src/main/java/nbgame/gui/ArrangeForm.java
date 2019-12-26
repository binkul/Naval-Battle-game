package nbgame.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nbgame.constant.Dimension;
import nbgame.constant.FileAccess;
import nbgame.engine.PathDriver;
import nbgame.game.BattleField;
import nbgame.game.Game;
import nbgame.user.ArrangeKeyboard;

public class ArrangeForm {
    private static final int MARGIN = 40;

    private final Game game;
    private Button btnOk = new Button("Ok");
    private Label lblTitle = new Label();
    private Stage window;

    public ArrangeForm(Game game) {
        this.game = game;
    }

    public void open() {
        PathDriver pathDriver = new PathDriver();
        game.getShipArrangement().initBattleField();

        window = new Stage();
        window.setScene(setScene());
        window.setTitle("Strategy");
        window.getIcons().add(new Image(pathDriver.getPath(FileAccess.SHIP_ICO_PATH)));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setOnCloseRequest(e -> clickCancelButton());

        window.show();
    }

    private void close() {
        window.close();
        window = null;
    }

    private Scene setScene() {
        BorderPane root = new BorderPane();

        lblTitle.setStyle(
                "                -fx-font-family: Arial;" +
                "                -fx-font-weight: bold;" +
                "                -fx-font-size: 24px;" +
                "                -fx-text-alignment: left;" +
                "                -fx-padding: 10 0 10 0;");

        btnOk.setStyle(
                "                -fx-font-family: Arial;" +
                "                -fx-font-size: 16px;" +
                "                -fx-font-weight: bold;" +
                "                -fx-text-fill: black;");
        btnOk.setOnAction(e -> clickOkButton());

        Button btnCancel = new Button("Cancel");
        btnCancel.setStyle(
                "                -fx-font-family: Arial;" +
                "                -fx-font-size: 16px;" +
                "                -fx-font-weight: bold;" +
                "                -fx-text-fill: black;");
        btnCancel.setOnAction(e -> clickCancelButton());

        HBox topBox = new HBox();
        topBox.setStyle("-fx-alignment: center;");
        topBox.getChildren().add(lblTitle);

        HBox leftBox = new HBox();
        leftBox.setPrefWidth(MARGIN);

        HBox rightBox = new HBox();
        rightBox.setPrefWidth(MARGIN);

        HBox bottomBox = new HBox();
        bottomBox.setStyle(
                "                -fx-alignment: center-right;" +
                "                -fx-padding: 15px;");
        bottomBox.setSpacing(20);
        bottomBox.setPrefHeight(MARGIN);
        bottomBox.getChildren().addAll(btnOk, btnCancel);

        Pane centerBox = new Pane();
        BattleField battleField = game.getShipArrangement().getArrangeBattleField();
        battleField.relocate(0,0);
        battleField.getViewFinder().relocate(Dimension.FIELD_TOP_MARGIN, Dimension.FIELD_LEFT_MARGIN);
        if (game.getShipArrangement().getArrangeBattleField().getUserInterface() instanceof ArrangeKeyboard) {
            centerBox.getChildren().addAll(battleField.getCanvas(), battleField.getViewFinder());
        } else {
            centerBox.getChildren().add(battleField.getCanvas());
        }

        root.setTop(topBox);
        root.setLeft(leftBox);
        root.setRight(rightBox);
        root.setBottom(bottomBox);
        root.setCenter(centerBox);

        Scene scene = new Scene(root);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> game.getShipArrangement().getArrangeBattleField().enterKeyPressed(e));

        return scene;
    }

    private void clickOkButton() {
        if (game.getShipArrangement().isUpdate()) {
            game.getShipArrangement().updateGamerShips();
            close();
        } else {
            close();
        }
    }

    private void clickCancelButton() {
        if (game.getShipArrangement().isUpdate()) {
            if (QuestionForm.open("Not saved", "Do You want to discard changes?")) {
                close();
            }
        } else {
            close();
        }
    }

    public Button getBtnOk() {
        return btnOk;
    }

    public Label getLblTitle() {
        return lblTitle;
    }
}
