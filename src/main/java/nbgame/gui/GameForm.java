package nbgame.gui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import nbgame.constant.Dimension;
import nbgame.constant.FileAccess;
import nbgame.constant.GameStyle;
import nbgame.engine.PathDriver;
import nbgame.game.Game;
import nbgame.game.Level;

public class GameForm {
    private static final int MARGIN = 40;
    private static final int GAP = 10;

    private final PathDriver pathDriver = new PathDriver();
    private final Game game;
    private final Label lblGamerName = new Label();
    private final Text txtStatus = new Text("Naval Battle Game");
    private final Label lblGamerShots = new Label("0");
    private final Label lblGamerResult = new Label("0");
    private final Label lblCompShots = new Label("0");
    private final Label lblCompResult = new Label("0");

    private final Menu menuArrange = new Menu("Ships placement");
    private final MenuItem menuSettings = new MenuItem("Settings");
    private final MenuItem menuStart = new MenuItem("Start game");
    private final MenuItem menuEnd = new MenuItem("End game");

    private ArrangeForm arrangeForm;

    public GameForm(Game game) {
        this.game = game;
    }

    public void open() {
        Stage window = new Stage();
        Image icon = new Image(pathDriver.getPath(FileAccess.SHIP_ICO_PATH));
        window.getIcons().add(icon);
        window.setScene(setScene());
        window.initStyle(StageStyle.DECORATED);
        window.setTitle("Naval Battle");
        window.show();
    }

    private Scene setScene() {

        // main scene
        BorderPane root  = new BorderPane();
        root.setStyle(GameStyle.GAME_FORM);

        // top
        root.setTop(createTopContainer());

        // left, right and bottom (only size)
        VBox leftBox = new VBox();
        leftBox.setPrefWidth(MARGIN);
        root.setLeft(leftBox);

        VBox rightBox = new VBox();
        rightBox.setPrefWidth(MARGIN);
        root.setRight(rightBox);

        HBox bottomBox = new HBox();
        bottomBox.setPrefHeight(MARGIN);
        root.setBottom(bottomBox);

        // center
        root.setCenter(createCenterContainer());

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> game.getComputerBattleField().enterKeyPressed(e));

        return scene;
    }

    private VBox createTopContainer() {
        VBox vBoxContainer = new VBox();
        vBoxContainer.setStyle(GameStyle.TOP_SET);

        // Menu Level
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioMenuItem radioLow = new RadioMenuItem("Pure random");
        radioLow.setSelected(true);
        radioLow.setOnAction(e -> game.getSettings().setLevelOfDifficulty(Level.LOW));
        radioLow.setToggleGroup(toggleGroup);

        RadioMenuItem radioMedium = new RadioMenuItem("Medium");
        radioMedium.setOnAction(e -> game.getSettings().setLevelOfDifficulty(Level.MEDIUM));
        radioMedium.setToggleGroup(toggleGroup);

        RadioMenuItem radioHigh = new RadioMenuItem("High");
        radioHigh.setOnAction(e -> game.getSettings().setLevelOfDifficulty(Level.HIGH));
        radioHigh.setToggleGroup(toggleGroup);

        RadioMenuItem radioExpert = new RadioMenuItem("Expert");
        radioExpert.setOnAction(e -> game.getSettings().setLevelOfDifficulty(Level.EXPERT));
        radioExpert.setToggleGroup(toggleGroup);

        Menu menuLevel = new Menu("Level");
        menuLevel.getItems().addAll(radioLow, radioMedium, radioHigh, radioExpert);

        // Menu Help
        Menu menuHelp = new Menu("Help");
        MenuItem menuAbout = new MenuItem("About");
        MenuItem menuResult = new MenuItem("Results");
        menuAbout.setOnAction(e -> AboutForm.open(pathDriver.getPath(FileAccess.SHIP_ICO_PATH)));
        menuResult.setOnAction(e -> StatisticForm.open(pathDriver.getPath(FileAccess.SHIP_ICO_PATH), game.getStatistics()));
        menuHelp.getItems().addAll(menuAbout, menuResult);

        // Menu game
        Menu gameMenu = new Menu("Game");
        SeparatorMenuItem sep1 = new SeparatorMenuItem();
        SeparatorMenuItem sep2 = new SeparatorMenuItem();

        MenuItem menuExit = new MenuItem("Exit");
        MenuItem menuRandom = new MenuItem("Random");
        MenuItem menuManual = new MenuItem("Manually");
        menuEnd.setDisable(true);
        menuArrange.getItems().addAll(menuManual, menuRandom);

        gameMenu.getItems().addAll(menuArrange, menuSettings, sep1, menuStart, menuEnd, sep2, menuExit);

        menuStart.setOnAction(e -> game.startNewGame());
        menuEnd.setOnAction(e -> game.breakGame());
        menuRandom.setOnAction(e -> game.gamerShipsRandom());
        menuManual.setOnAction(e -> openArrangeForm());
        menuSettings.setOnAction(e -> new SettingsForm(game).open());
        menuExit.setOnAction(e -> System.exit(0));

        MenuBar menu = new MenuBar();
        menu.setStyle(GameStyle.MAIN_MENU);
        menu.getMenus().addAll(gameMenu, menuLevel, menuHelp);

        txtStatus.setStyle(GameStyle.LBL_STATUS);

        vBoxContainer.getChildren().addAll(menu, txtStatus);

        return vBoxContainer;
    }

    private HBox createCenterContainer() {
        HBox mainContainer = new HBox();
        mainContainer.setStyle(GameStyle.CENTER_POS);

        // GridPane 2 x 3 (row x column)
        GridPane gridPane = new GridPane();
        gridPane.setHgap(80);
        gridPane.setVgap(5);

        Pane userPane = new Pane();
        game.getComputerBattleField().getCanvas().relocate(0,0);
        game.getComputerBattleField().getViewFinder().relocate(Dimension.FIELD_TOP_MARGIN, Dimension.FIELD_LEFT_MARGIN);
        userPane.getChildren().addAll(game.getGamerBattleField().getCanvas(), game.getComputerBattleField().getViewFinder());

        Pane compPane = new Pane();
        game.getGamerBattleField().getCanvas().relocate(0,0);
        game.getGamerBattleField().getViewFinder().relocate(Dimension.FIELD_TOP_MARGIN, Dimension.FIELD_LEFT_MARGIN);
        game.getGamerBattleField().getViewFinder().setMouseTransparent(true);
        compPane.getChildren().addAll(game.getComputerBattleField().getCanvas(), game.getGamerBattleField().getViewFinder());

        gridPane.add(setGamerNameContainer(), 0, 0, 1, 1);
        gridPane.add(setCompNameContainer(), 1, 0, 1, 1);
        gridPane.add(userPane, 0, 1, 1, 1);
        gridPane.add(compPane, 1, 1, 1, 1);
        gridPane.add(setResultContainer(lblGamerShots, lblGamerResult), 0, 2, 1,1);
        gridPane.add(setResultContainer(lblCompShots, lblCompResult), 1, 2, 1,1);

        mainContainer.getChildren().add(gridPane);

        return mainContainer;
    }

    private HBox setGamerNameContainer() {
        HBox nameHBox = new HBox();

        ImageView ico = new ImageView(pathDriver.getPath(FileAccess.USER_ICO_PATH));
        ico.setFitWidth(64);
        ico.setFitHeight(64);

        lblGamerName.setText("None");
        lblGamerName.setGraphic(ico);
        lblGamerName.setStyle(GameStyle.LBL_NAME);

        nameHBox.prefWidth(Dimension.FIELD_WIDTH);
        nameHBox.setStyle(GameStyle.LEFT_POS);
        nameHBox.getChildren().add(lblGamerName);

        return nameHBox;
    }

    private HBox setCompNameContainer() {
        HBox compHBox = new HBox();

        ImageView ico = new ImageView(pathDriver.getPath(FileAccess.COMPUTER_ICO_PATH));
        ico.setFitWidth(64);
        ico.setFitHeight(64);

        Label lblComputerName = new Label("Computer", ico);
        lblComputerName.setStyle(GameStyle.LBL_NAME);

        compHBox.prefWidth(Dimension.FIELD_HEIGHT);
        compHBox.setStyle(GameStyle.LEFT_POS);
        compHBox.getChildren().add(lblComputerName);

        return compHBox;
    }

    private GridPane setResultContainer(Label lblShots, Label lblResult) {
        GridPane resultGrid = createTwoColumnGrid();

        HBox hBox1 = new HBox();
        hBox1.setStyle(GameStyle.SUB_BOTTOM_SET);
        HBox hBox2 = new HBox();
        hBox2.setStyle(GameStyle.SUB_BOTTOM_SET);

        lblShots.setGraphic(new ImageView(new Image(pathDriver.getPath(FileAccess.MISS_ICO_PATH), 32, 32, false, false)));
        lblShots.setStyle(GameStyle.LBL_NAME);
        lblResult.setGraphic(new ImageView(new Image(pathDriver.getPath(FileAccess.EXPLODE_ICO_PATH), 32, 32, false, false)));
        lblResult.setStyle(GameStyle.LBL_NAME);
        hBox1.getChildren().add(lblShots);
        hBox2.getChildren().add(lblResult);

        resultGrid.add(hBox1, 0, 0, 1,1);
        resultGrid.add(hBox2, 1,0,1,1);

        return resultGrid;
    }

    private GridPane createTwoColumnGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(GAP);

        ColumnConstraints colTop1 = new ColumnConstraints();
        ColumnConstraints colTop2 = new ColumnConstraints();
        colTop1.setPrefWidth(248);
        colTop2.setPrefWidth(248);
        RowConstraints rowTop = new RowConstraints();

        grid.getColumnConstraints().addAll(colTop1, colTop2);
        grid.getRowConstraints().addAll(rowTop);

        return grid;
    }

    Label getLblGamerName() {
        return lblGamerName;
    }

    public Text getTxtStatus() {
        return txtStatus;
    }

    public Label getLblGamerShots() {
        return lblGamerShots;
    }

    public Label getLblGamerResult() {
        return lblGamerResult;
    }

    public Label getLblCompShots() {
        return lblCompShots;
    }

    public Label getLblCompResult() {
        return lblCompResult;
    }

    public Menu getMenuArrange() {
        return menuArrange;
    }

    public MenuItem getMenuSettings() {
        return menuSettings;
    }

    public MenuItem getMenuStart() {
        return menuStart;
    }

    public MenuItem getMenuEnd() {
        return menuEnd;
    }

    public void resetResultsLabels() {
        lblCompResult.setText("0");
        lblCompShots.setText("0");
        lblGamerResult.setText("0");
        lblGamerShots.setText("0");
    }

    public ArrangeForm getArrangeForm() {
        return arrangeForm;
    }

    private void openArrangeForm() {
        arrangeForm = new ArrangeForm(game);
        arrangeForm.open();
    }
}
