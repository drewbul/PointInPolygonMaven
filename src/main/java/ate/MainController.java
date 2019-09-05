package ate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
//    @FXML
//    private Button btnFinishSetting;
//    @FXML
//    private Button butNewPolygon;

    private boolean ifTestingPointsNow;
    private static int prevSelectedVertex=-1;
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
        drawingPane.setPrefSize(screenWidth - drawingPane.getLayoutX() - 5, screenHeight - drawingPane.getLayoutY() - WINDOW_TITLE_BAR_HEIGHT);
        vertCoordListView.setItems(vertCoords);
        msgSoundFilesNotLoaded.setVisible(Main.myVoice.skipVoice);
        langCombobox.setVisible(!Main.myVoice.skipVoice);
    }

    @FXML
    private void vertCoordListViewOnMouseClicked (MouseEvent event)
    {
        int selectedVertIndex = vertCoordListView.getSelectionModel().getSelectedIndex();
        Circle selectedVertex;
        if (selectedVertIndex!=-1) {    // if the list is not empty
            if (selectedVertIndex != prevSelectedVertex) { // if a new item in the list has been selected
                lblMessage.setText("Click on the selected coordinates again to remove the selection");
                selectedVertex = (Circle) polygonVertices.getChildren().get(selectedVertIndex);
                selectedVertex.setFill(Color.ORANGE);
                selectedVertex.setRadius(4.0f);
            }
            if (prevSelectedVertex != -1) { //skip deselecting if -1
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
                    Line polygonSide = new Line (prevVertex.x, prevVertex.y, vertices.get(0).x, vertices.get(0).y);
                    polygonSide.setFill(Color.BLACK);
                    polygonSides.getChildren().add(polygonSide);
                    ifTestingPointsNow = true;
                    helperLine.setActive(false);
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
    }

    @FXML
    private void drawingPaneOnMouseMoveOrDrag(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        lblMouseCoordinates.setText((int) mouseX + " x " + (int) mouseY);
        if (!ifTestingPointsNow) {
            helperLine.setEndXY(mouseX, mouseY);
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
    private void drawingPaneOnClick(MouseEvent event) {
        Point clickedPoint = new Point();
        clickedPoint.x = event.getX();
        clickedPoint.y = event.getY();
        String pointCoordinates = (int) clickedPoint.x + " x " + (int) clickedPoint.y;
        if (ifTestingPointsNow){
            Main.myVoice.stop();
            testedPointCircle.setCenterX(clickedPoint.x);
            testedPointCircle.setCenterY(clickedPoint.y);
            testedPointCircle.setVisible(true);
            boolean isInside = isPointInPolygon(vertices, clickedPoint);
            String curLang = langCombobox.getValue();
            Main.myVoice.play(isInside, curLang);
            testedPointCircle.setFill(isInside ? Color.FORESTGREEN : Color.RED);
            lblMessage.setText("The point " + pointCoordinates + " is " + (isInside ? "inside" : "outside") + " the polygon." );
        }
        else { //if setting vertices now
            vertices.add(clickedPoint);
            lblMessage.setText("Added vertex with these coordinates: " + pointCoordinates);
            vertCoords.add(pointCoordinates);
            Circle vertexCircle = new Circle();
            vertexCircle.setCenterX(clickedPoint.x);
            vertexCircle.setCenterY(clickedPoint.y);
            vertexCircle.setRadius(2.0f);
            vertexCircle.setFill(Color.BLACK);
            polygonVertices.getChildren().add(vertexCircle);
            if (vertices.size() > 1) {
                Line polygonSide = new Line(prevVertex.x, prevVertex.y, clickedPoint.x, clickedPoint.y);
                polygonSide.setFill(Color.BLACK);
                polygonSides.getChildren().add(polygonSide);
            }
            prevVertex.x = clickedPoint.x;
            prevVertex.y = clickedPoint.y;
            helperLine.setStartXY(clickedPoint.x, clickedPoint.y);
            helperLine.setEndXY(clickedPoint.x, clickedPoint.y);
            helperLine.setActive(true);
        }
    }
}