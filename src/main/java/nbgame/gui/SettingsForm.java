package nbgame.gui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nbgame.constant.FileAccess;
import nbgame.constant.SettingStyle;
import nbgame.engine.PathDriver;
import nbgame.game.Game;

public class SettingsForm {
    private static final int MARGIN = 20;
    private static final Button BTN_OK = new Button("Ok");
    private static final ToggleGroup GROUP_FOUR = new ToggleGroup();
    private static final ToggleGroup GROUP_THREE = new ToggleGroup();
    private static final ToggleGroup GROUP_TWO = new ToggleGroup();
    private static final ToggleGroup GROUP_ONE = new ToggleGroup();
    private static final TextField TXT_NAME = new TextField();

    private final Game game;
    private Stage window;
    private boolean update = false;
    private int fourMastCount;
    private int threeMastCount;
    private int twoMastCount;
    private int oneMastCount;
    private String name;

    public SettingsForm(Game game) {
        this.game = game;
        this.fourMastCount = game.getSettings().getFourMastCount();
        this.threeMastCount = game.getSettings().getThreeMastCount();
        this.twoMastCount = game.getSettings().getTwoMastCount();
        this.oneMastCount = game.getSettings().getOneMastCount();
        this.name = game.getSettings().getName();
    }

    void open() {
        PathDriver pathDriver = new PathDriver();
        update = false;
        window = new Stage();
        window.setScene(setScene());
        window.setTitle("Settings");
        window.getIcons().add(new Image(pathDriver.getPath(FileAccess.SHIP_ICO_PATH)));
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
    }

    private void close() {
        window.close();
        window = null;
    }

    private Scene setScene() {
        BorderPane root = new BorderPane();

        Label lblTitle = new Label("Settings");
        lblTitle.setStyle(SettingStyle.LBL_TITLE);

        BTN_OK.setStyle(SettingStyle.BTN_STYLE);
        BTN_OK.setOnAction(e -> clickOkButton());

        Button btnCancel = new Button("Cancel");
        btnCancel.setStyle(SettingStyle.BTN_STYLE);
        btnCancel.setOnAction(e -> clickCancelButton());

        HBox topBox = new HBox();
        topBox.setStyle(SettingStyle.CENTER_POS);
        topBox.getChildren().add(lblTitle);

        HBox leftBox = new HBox();
        leftBox.setPrefWidth(MARGIN);

        HBox rightBox = new HBox();
        rightBox.setPrefWidth(MARGIN);

        HBox bottomBox = new HBox();
        bottomBox.setStyle(SettingStyle.BOTTOM_POS);
        bottomBox.setSpacing(20);
        bottomBox.getChildren().addAll(BTN_OK, btnCancel);

        root.setTop(topBox);
        root.setLeft(leftBox);
        root.setRight(rightBox);
        root.setBottom(bottomBox);
        root.setCenter(centerContainer());

        return new Scene(root);
    }

    private GridPane centerContainer() {
        GridPane gridPane = new GridPane();

        // 2x1 (row x column)
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        Label lblName = new Label("Name");
        lblName.setStyle(SettingStyle.LBL_NAME);

        TXT_NAME.setStyle(SettingStyle.TXT_NAME);
        TXT_NAME.setText(name);
        TXT_NAME.setPrefColumnCount(20);
        TXT_NAME.textProperty().addListener(e -> {
            name = TXT_NAME.getText();
            update = true;
        });

        HBox textBox = new HBox();
        textBox.setStyle(SettingStyle.NAME_SET);
        textBox.getChildren().addAll(lblName, TXT_NAME);

        HBox radioBox = new HBox();
        radioBox.setStyle(SettingStyle.NAME_SET);
        radioBox.getChildren().addAll(setFourMastSheep(), setThreeMastSheep(), setTwoMastSheep(), setOneMastSheep());

        gridPane.add(textBox, 0, 0, 1, 1);
        gridPane.add(radioBox, 0, 1, 1,1);

        return gridPane;
    }

    private VBox setFourMastSheep() {
        VBox rdBox = new VBox();
        rdBox.setStyle(SettingStyle.RADIO_SET);

        RadioButton rb1 = new RadioButton("1 - four-mast ship");
        rb1.setStyle(SettingStyle.BTN_RADIO);
        rb1.setUserData("1");
        rb1.setToggleGroup(GROUP_FOUR);

        RadioButton rb2 = new RadioButton("2 - four-mast ship");
        rb2.setStyle(SettingStyle.BTN_RADIO);
        rb2.setUserData("2");
        rb2.setToggleGroup(GROUP_FOUR);

        if (fourMastCount == 1) {
            rb1.setSelected(true);
        } else {
            rb2.setSelected(true);
        }

        GROUP_FOUR.selectedToggleProperty().addListener(e -> {
            if (GROUP_FOUR.getSelectedToggle().getUserData() != null) {
                fourMastCount = Integer.parseInt(GROUP_FOUR.getSelectedToggle().getUserData().toString());
                update = true;
            }
        });

        rdBox.getChildren().addAll(rb1, rb2);

        return rdBox;
    }

    private VBox setThreeMastSheep() {
        VBox rdBox = new VBox();
        rdBox.setStyle(SettingStyle.RADIO_SET);

        RadioButton rb1 = new RadioButton("1 - three-mast ship");
        rb1.setStyle(SettingStyle.BTN_RADIO);
        rb1.setUserData("1");
        rb1.setToggleGroup(GROUP_THREE);

        RadioButton rb2 = new RadioButton("2 - three-mast ship");
        rb2.setStyle(SettingStyle.BTN_RADIO);
        rb2.setUserData("2");
        rb2.setToggleGroup(GROUP_THREE);

        RadioButton rb3 = new RadioButton("3 - three-mast ship");
        rb3.setStyle(SettingStyle.BTN_RADIO);
        rb3.setUserData("3");
        rb3.setToggleGroup(GROUP_THREE);

        if (threeMastCount == 1) {
            rb1.setSelected(true);
        } else if (threeMastCount == 2) {
            rb2.setSelected(true);
        } else {
            rb3.setSelected(true);
        }

        GROUP_THREE.selectedToggleProperty().addListener(e -> {
            if (GROUP_THREE.getSelectedToggle().getUserData() != null) {
                threeMastCount = Integer.parseInt(GROUP_THREE.getSelectedToggle().getUserData().toString());
                update = true;
            }
        });

        rdBox.getChildren().addAll(rb1, rb2, rb3);

        return rdBox;
    }

    private VBox setTwoMastSheep() {
        VBox rdBox = new VBox();
        rdBox.setStyle(SettingStyle.RADIO_SET);

        RadioButton rb1 = new RadioButton("1 - two-mast ship");
        rb1.setStyle(SettingStyle.BTN_RADIO);
        rb1.setUserData("1");
        rb1.setToggleGroup(GROUP_TWO);

        RadioButton rb2 = new RadioButton("2 - two-mast ship");
        rb2.setStyle(SettingStyle.BTN_RADIO);
        rb2.setUserData("2");
        rb2.setToggleGroup(GROUP_TWO);

        RadioButton rb3 = new RadioButton("3 - two-mast ship");
        rb3.setStyle(SettingStyle.BTN_RADIO);
        rb3.setUserData("3");
        rb3.setToggleGroup(GROUP_TWO);

        RadioButton rb4 = new RadioButton("4 - two-mast ship");
        rb4.setStyle(SettingStyle.BTN_RADIO);
        rb4.setUserData("4");
        rb4.setToggleGroup(GROUP_TWO);

        if (twoMastCount == 1) {
            rb1.setSelected(true);
        } else if (twoMastCount == 2) {
            rb2.setSelected(true);
        } else if (twoMastCount == 3) {
            rb3.setSelected(true);
        } else {
            rb4.setSelected(true);
        }

        GROUP_TWO.selectedToggleProperty().addListener(e -> {
            if (GROUP_TWO.getSelectedToggle().getUserData() != null) {
                twoMastCount = Integer.parseInt(GROUP_TWO.getSelectedToggle().getUserData().toString());
                update = true;
            }
        });

        rdBox.getChildren().addAll(rb1, rb2, rb3, rb4);

        return rdBox;
    }

    private VBox setOneMastSheep() {
        VBox rdBox = new VBox();
        rdBox.setStyle(SettingStyle.RADIO_SET);

        RadioButton rb1 = new RadioButton("1 - one-mast ship");
        rb1.setStyle(SettingStyle.BTN_RADIO);
        rb1.setUserData("1");
        rb1.setToggleGroup(GROUP_ONE);

        RadioButton rb2 = new RadioButton("2 - one-mast ship");
        rb2.setStyle(SettingStyle.BTN_RADIO);
        rb2.setUserData("2");
        rb2.setToggleGroup(GROUP_ONE);

        RadioButton rb3 = new RadioButton("3 - one-mast ship");
        rb3.setStyle(SettingStyle.BTN_RADIO);
        rb3.setUserData("3");
        rb3.setToggleGroup(GROUP_ONE);

        RadioButton rb4 = new RadioButton("4 - one-mast ship");
        rb4.setStyle(SettingStyle.BTN_RADIO);
        rb4.setUserData("4");
        rb4.setToggleGroup(GROUP_ONE);

        if (oneMastCount == 1) {
            rb1.setSelected(true);
        } else if (oneMastCount == 2) {
            rb2.setSelected(true);
        } else if (oneMastCount == 3) {
            rb3.setSelected(true);
        } else {
            rb4.setSelected(true);
        }

        GROUP_ONE.selectedToggleProperty().addListener(e -> {
            if (GROUP_ONE.getSelectedToggle().getUserData() != null) {
                oneMastCount = Integer.parseInt(GROUP_ONE.getSelectedToggle().getUserData().toString());
                update = true;
            }
        });

        rdBox.getChildren().addAll(rb1, rb2, rb3, rb4);

        return rdBox;
    }

    private void clickOkButton() {
        if (update) {
            game.getSettings().setName(name);
            game.getGameForm().getLblGamerName().setText(name);
            game.getSettings().setFourMastCount(fourMastCount);
            game.getSettings().setThreeMastCount(threeMastCount);
            game.getSettings().setTwoMastCount(twoMastCount);
            game.getSettings().setOneMastCount(oneMastCount);
            game.updateSettings();
        }
        close();
    }

    private void clickCancelButton() {
        if (update) {
            if (QuestionForm.open("Not saved", "Do You want to discard changes?")) {
                close();
            }
        } else {
            close();
        }
    }

}
