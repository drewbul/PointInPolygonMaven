package ate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Main extends Application{

    static final Voice myVoice = new Voice();

    @Override
    public void start(Stage stage) {

        Application.Parameters params = getParameters();
        List<String> parameterList = params.getRaw();
        myVoice.setExtension(parameterList);

        try {
            Group root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.rgb(200,200,200));
            stage.setScene(scene);
            stage.setTitle("Check if point is inside polygon");
            stage.setMaximized(true);
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
            stage.show();
        } catch (IOException loadException) {
            loadException.printStackTrace();
            System.out.println("Error loading FXML scene: " + loadException);
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}