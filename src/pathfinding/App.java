/*
 * SV: Nguyễn Minh Mẫn
 * MSSV: 1512318
 * Đồ án môn cơ sở trí tuệ nhân
 * minhman133@outlook.com
 */
package pathfinding;

import javafx.scene.control.Button;
import javafx.geometry.Insets;
import pathfinding.searcher.Map;
import pathfinding.searcher.Searcher;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import pathfinding.searcher.Graphics;

// Lớp này định nghĩa giao diện ứng dụng
public class App extends Application {

    static Searcher searcher;
    private Map mMap;
    private final int mHeight;
    private final int mWidth;
    private State mState;

    private enum State {
        BLANK, MAPONLY, SEARCHED, BLOCKED
    }

    public App() {
        mHeight = 600;
        mWidth = 800;
        searcher = null;
        mMap = null;
    }

    private void drawMap(double W, double H) {
        Graphics.scale = (float) mHeight / (float) mMap.getHeight();
        Graphics.gc.setFill(Color.WHITE);
        Graphics.gc.fillRect(0, 0, W, H);
        for (int i = 0; i < mMap.numObj(); i++) {
            Graphics.drawPolygon(mMap.getObject(i), Color.CORNFLOWERBLUE, 1);
        }
        Graphics.drawStartPoint(mMap.getStart());
        Graphics.drawGoalPoint(mMap.getGoal());
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        Canvas canvas = new Canvas(1920, 1080);
        Graphics.gc = canvas.getGraphicsContext2D();

        mState = State.BLANK;

        DropShadow ds = new DropShadow();
        ds.setOffsetY(0.5);
        ds.setOffsetX(0.5);
        ds.setColor(Color.GRAY);

        HBox hbox = new HBox();
        hbox.prefWidthProperty().bind(primaryStage.widthProperty());
        hbox.setPadding(new Insets(10, 8, 10, 8));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #f5f5f5;");
        hbox.setEffect(ds);

        Label statusLb = new Label();
        statusLb.setPadding(new Insets(5, 5, 5, 5));
        statusLb.setText("Euclidean distance: NULL");

        Label distanceLb = new Label();
        distanceLb.setPadding(new Insets(5, 5, 5, 5));
        distanceLb.setText("Path distance: NULL");

        Button searchBtn = new Button("Search");
        searchBtn.setPrefSize(80, 20);
        searchBtn.setDisable(true);
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (mState == State.MAPONLY && mState != State.BLOCKED) {
                    searcher.clear();
                    System.out.println("Searching...");
                    searcher.run();
                    System.out.println("Done.");
                    distanceLb.setText("Path distance: " + String.valueOf(searcher.getDistance()));
                    mState = State.SEARCHED;
                }
            }
        }
        );

        Button drawBtn = new Button("Update");
        drawBtn.setPrefSize(80, 20);
        drawBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e
            ) {
                mMap = null;
                searcher = null;
                mMap = new Map("input.txt");
                searcher = new Searcher(mMap, mMap.getStart(), mMap.getGoal());
                drawMap(canvas.getWidth(), canvas.getHeight());
                distanceLb.setText("Path distance: NULL");
                if (mMap.getEuclide() != -1) {
                    statusLb.setText("Euclidean distance: " + String.valueOf(mMap.getEuclide()));
                    mState = mState.MAPONLY;
                    searchBtn.setDisable(false);
                } else {
                    statusLb.setText("Euclidean distance: NULL");
                    mState = mState.BLOCKED;
                    searchBtn.setDisable(true);
                    messageBox("Searching failed",
                            "Cannot find any path from Start to Goal.",
                            "Check input.txt file and try again.");
                }
            }
        }
        );

        final ToggleGroup group = new ToggleGroup();

        RadioButton astar = new RadioButton();
        astar.setText("A*");
        astar.setToggleGroup(group);
        astar.setPadding(new Insets(5, 5, 5, 5));
        astar.setSelected(true);

        RadioButton ucs = new RadioButton();
        ucs.setText("Greedy");
        ucs.setToggleGroup(group);
        ucs.setPadding(new Insets(5, 5, 5, 5));

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    if (mState == State.SEARCHED) {
                        searchBtn.setDisable(true);
                        mState = State.MAPONLY;
                    }
                    if (astar.isSelected()) {
                        Searcher.algorithm = 0;
                    } else {
                        Searcher.algorithm = 1;
                    }
                }
            }
        });

        hbox.getChildren().addAll(drawBtn, searchBtn, astar, ucs, statusLb, distanceLb);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(canvas);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);

        grid.add(hbox, 0, 0);
        grid.add(scrollPane, 0, 1);

        Scene scene = new Scene(grid, mWidth, mHeight, Color.WHITE);
        primaryStage.setTitle("AI Project - 1512318");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void messageBox(String title, String head, String text) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(text);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
