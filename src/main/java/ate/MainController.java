package ate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;

import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class MainController {

    static class Point{
        double x;
        double y;
    }

    @FXML
    private Label lblMouseCoordinates;
    @FXML
    private Label msgSoundFilesNotLoaded;
    @FXML
    private ComboBox<String> langCombobox;

    @FXML
    private TextField xMultiplierField;
    @FXML
    private TextField yMultiplierField;

    @FXML
    private Label lblMessage;
    @FXML
    ListView<String> vertCoordListView;
    @FXML
    private Pane drawingPane;
    @FXML
    private Group polygonVertices;
    @FXML
    private Group polygonSides;
    @FXML
    private Circle testedPointCircle;
    @FXML
    private HelperLine helperLine;

    @FXML
    private TextField xCoord;
    @FXML
    private TextField yCoord;
    @FXML
    private Button addVertex;

    @FXML
    private Button btnFinishSetting;
    @FXML
    private Button btnNewPolygon;

    @FXML
    private Line xAxis;
    @FXML
    private Line yAxis;
    @FXML
    private Line xAxisArrow1;
    @FXML
    private Line xAxisArrow2;
    @FXML
    private Line yAxisArrow1;
    @FXML
    private Line yAxisArrow2;

    private double xMultiplierValue = 1.0;
    private double yMultiplierValue = 4.0;
    private double originX;
    private double originY;

    private boolean ifTestingPointsNow;
    final static int LIST_ITEM_NOT_SELECTED=-1;
    private static int prevSelectedVertex=LIST_ITEM_NOT_SELECTED;
    private final Point prevVertex = new Point();
    private final ArrayList<Point> vertices = new ArrayList<>();
    private final ObservableList<String> vertCoords = FXCollections.observableArrayList();

    private boolean isPointInPolygon(ArrayList<Point> vertices, Point testedPoint){
        // this algorithm is described in https://wrf.ecse.rpi.edu/Research/Short_Notes/pnpoly.html
        int i, j;
        int nVertices = vertices.size();
        boolean oddIntersections = false;
        for (i = 0, j = nVertices-1; i < nVertices; j = i++) {
            if ( ((vertices.get(i).y > testedPoint.y) != (vertices.get(j).y > testedPoint.y)) &&
                    (testedPoint.x < (vertices.get(j).x-vertices.get(i).x) * (testedPoint.y-vertices.get(i).y) / (vertices.get(j).y-vertices.get(i).y) + vertices.get(i).x) ) {
                oddIntersections = !oddIntersections;
            }
        }
        return oddIntersections;
    }

    @FXML
    private void initialize()
    {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenHeight =  primaryScreenBounds.getHeight();
        double screenWidth =  primaryScreenBounds.getWidth();
        int WINDOW_TITLE_BAR_HEIGHT = 35;
        vertCoordListView.setPrefSize(110, screenHeight - vertCoordListView.getLayoutY() - WINDOW_TITLE_BAR_HEIGHT);
        double drawingPaneWidth = screenWidth - drawingPane.getLayoutX() - 5;
        double drawingPaneHeight = screenHeight - drawingPane.getLayoutY() - WINDOW_TITLE_BAR_HEIGHT;
        drawingPane.setPrefSize( drawingPaneWidth, drawingPaneHeight);

        originX = 20;
        originY = drawingPaneHeight - 20;
        xAxis.setStartX(10);
        xAxis.setStartY(originY);
        xAxis.setEndX(drawingPaneWidth - 20);
        xAxis.setEndY(originY);
        yAxis.setStartX(originX);
        yAxis.setStartY(drawingPaneHeight - 10);
        yAxis.setEndX(originX);
        yAxis.setEndY(10);

        xAxisArrow1.setStartX(xAxis.getEndX() - 10);
        xAxisArrow1.setStartY(xAxis.getEndY() - 5);
        xAxisArrow1.setEndX(xAxis.getEndX());
        xAxisArrow1.setEndY(xAxis.getEndY());
        xAxisArrow2.setStartX(xAxis.getEndX() - 10);
        xAxisArrow2.setStartY(xAxis.getEndY() + 5);
        xAxisArrow2.setEndX(xAxis.getEndX());
        xAxisArrow2.setEndY(xAxis.getEndY());

        yAxisArrow1.setStartX(yAxis.getEndX() - 5);
        yAxisArrow1.setStartY(yAxis.getEndY() + 10);
        yAxisArrow1.setEndX(yAxis.getEndX());
        yAxisArrow1.setEndY(yAxis.getEndY());
        yAxisArrow2.setStartX(yAxis.getEndX() + 5);
        yAxisArrow2.setStartY(yAxis.getEndY() + 10);
        yAxisArrow2.setEndX(yAxis.getEndX());
        yAxisArrow2.setEndY(yAxis.getEndY());

        vertCoordListView.setItems(vertCoords);
        Main.myVoice.setLang("English");
        msgSoundFilesNotLoaded.setVisible(Main.myVoice.skipVoice);
        langCombobox.setPrefHeight(Main.myVoice.skipVoice ? 25 : 50);

        xMultiplierField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,2}([.]\\d{0,1})?")) {
                xMultiplierField.setText(oldValue);
            }
        });
        yMultiplierField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,2}([.]\\d{0,1})?")) {
                yMultiplierField.setText(oldValue);
            }
        });
        xCoord.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,4}")) {
                xCoord.setText(oldValue);
            }
        });
        yCoord.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,4}")) {
                yCoord.setText(oldValue);
            }
        });
    }

    @FXML
    private void langComboboxOnAction(ActionEvent event){
        Main.myVoice.setLang(langCombobox.getValue());
        msgSoundFilesNotLoaded.setVisible(Main.myVoice.skipVoice);
        langCombobox.setPrefHeight(Main.myVoice.skipVoice ? 25 : 50);
    }

    @FXML
    private void vertCoordListViewOnMouseClicked (MouseEvent event)
    {
        int selectedVertIndex = vertCoordListView.getSelectionModel().getSelectedIndex();
        Circle selectedVertex;
        if (selectedVertIndex != LIST_ITEM_NOT_SELECTED) {
            if (selectedVertIndex != prevSelectedVertex) { // if a new item in the list has been selected
                lblMessage.setText("Click on the selected coordinates again to remove the selection");
                selectedVertex = (Circle) polygonVertices.getChildren().get(selectedVertIndex);
                selectedVertex.setFill(Color.ORANGE);
                selectedVertex.setRadius(4.0f);
            }
            if (prevSelectedVertex != LIST_ITEM_NOT_SELECTED) {
                if (selectedVertIndex == prevSelectedVertex) { //if no new vertex has been selected
                    lblMessage.setText("Selection removed");
                }
                selectedVertex = (Circle) polygonVertices.getChildren().get(prevSelectedVertex);
                selectedVertex.setFill(Color.BLACK);
                selectedVertex.setRadius(2.0f);
            }
            prevSelectedVertex = selectedVertIndex;
        }
    }

    @FXML
    private void btnFinishSettingOnAction(ActionEvent event){
        if ( vertices.size()>=3 ) {
            if (!ifTestingPointsNow){
                Line polygonSide = new Line (originX + prevVertex.x / xMultiplierValue, originY - prevVertex.y / yMultiplierValue,
                                            originX + vertices.get(0).x / xMultiplierValue, originY - vertices.get(0).y / yMultiplierValue);
                polygonSide.setFill(Color.BLACK);
                polygonSides.getChildren().add(polygonSide);
                ifTestingPointsNow = true;
                helperLine.setActive(false);
                addVertex.setText("Check point");
                btnFinishSetting.setVisible(false);
            }
            lblMessage.setText("Now you can specify the point to be tested." );
        } else {
            lblMessage.setText("Please set 3 vertices or more." );
        }
    }

    @FXML
    private void butNewPolygonOnAction(ActionEvent event){
        lblMessage.setText("Set the vertices of the polygon with mouse clicks. Use 'Finish setting' button to close the polygon.");
        ifTestingPointsNow = false;
        polygonVertices.getChildren().clear();
        polygonSides.getChildren().clear();
        vertices.clear();
        testedPointCircle.setVisible(false);
        vertCoords.clear();
        helperLine.setActive(false);
        addVertex.setText("Add vertex");
        btnFinishSetting.setVisible(false);
        btnNewPolygon.setVisible(false);
        xMultiplierValue = Double.parseDouble( xMultiplierField.getText() );
        yMultiplierValue = Double.parseDouble( yMultiplierField.getText() );
    }

    @FXML
    private void drawingPaneOnMouseMoveOrDrag(MouseEvent event) {
        double mouseX = (event.getX() - originX) * xMultiplierValue;
        double mouseY = (originY - event.getY()) * yMultiplierValue;
        lblMouseCoordinates.setText((int) mouseX + " x " + (int) mouseY);
        if (!ifTestingPointsNow) {
            helperLine.setEndXY(originX + mouseX / xMultiplierValue, originY - mouseY / yMultiplierValue );
        }
    }

    @FXML
    private void drawingPaneOnMouseExited(MouseEvent event) {
        helperLine.setHidden(true);
    }

    @FXML
    private void drawingPaneOnMouseEntered(MouseEvent event) {
        helperLine.setHidden(false);
    }

    @FXML
    private void butAddVertexOnAction(ActionEvent event){
        AddVertexOrCheckPoint();
    }

    @FXML
    private void xCoordOnAction(ActionEvent event){
        AddVertexOrCheckPoint();
    }

    @FXML
    private void yCoordOnAction(ActionEvent event){
        AddVertexOrCheckPoint();
    }

    private void AddVertexOrCheckPoint() {
        Point clickedPoint = new Point();
        clickedPoint.x = Double.parseDouble( xCoord.getText() );
        clickedPoint.y = Double.parseDouble( yCoord.getText() );
        processClickedPoint(clickedPoint);
    }

    @FXML
    private void drawingPaneOnClick(MouseEvent event) {
        Point clickedPoint = new Point();
        clickedPoint.x = (event.getX() - originX) * xMultiplierValue;
        clickedPoint.y = (originY - event.getY()) * yMultiplierValue;
        processClickedPoint(clickedPoint);
    }

    private void processClickedPoint(Point clickedPoint) {
        String pointCoordinates = (int) clickedPoint.x + " x " + (int) clickedPoint.y;
        if (ifTestingPointsNow){
            Main.myVoice.stop();
            testedPointCircle.setCenterX(originX + clickedPoint.x / xMultiplierValue );
            testedPointCircle.setCenterY(originY - clickedPoint.y / yMultiplierValue );
            testedPointCircle.setVisible(true);
            boolean isInside = isPointInPolygon(vertices, clickedPoint);
            Main.myVoice.play(isInside);
            testedPointCircle.setFill(isInside ? Color.FORESTGREEN : Color.RED);
            lblMessage.setText("The point " + pointCoordinates + " is " + (isInside ? "inside" : "outside") + " the polygon." );
        }
        else { //if setting vertices now
            btnFinishSetting.setVisible(true);
            btnNewPolygon.setVisible(true);
            vertices.add(clickedPoint);
            lblMessage.setText("Added vertex with these coordinates: " + pointCoordinates);
            vertCoords.add(pointCoordinates);
            Circle vertexCircle = new Circle();
            vertexCircle.setCenterX(originX + clickedPoint.x / xMultiplierValue);
            vertexCircle.setCenterY(originY - clickedPoint.y / yMultiplierValue);
            vertexCircle.setRadius(2.0f);
            vertexCircle.setFill(Color.BLACK);
            polygonVertices.getChildren().add(vertexCircle);
            if (vertices.size() > 1) {
                Line polygonSide = new Line(originX + prevVertex.x / xMultiplierValue, originY - prevVertex.y / yMultiplierValue,
                                           originX + clickedPoint.x / xMultiplierValue, originY - clickedPoint.y / yMultiplierValue);
                polygonSide.setFill(Color.BLACK);
                polygonSides.getChildren().add(polygonSide);
            }
            prevVertex.x = clickedPoint.x;
            prevVertex.y = clickedPoint.y;
            helperLine.setStartXY(originX + clickedPoint.x / xMultiplierValue, originY - clickedPoint.y / yMultiplierValue);
            helperLine.setEndXY(originX + clickedPoint.x / xMultiplierValue, originY - clickedPoint.y / yMultiplierValue);
            helperLine.setActive(true);
        }
    }
}
