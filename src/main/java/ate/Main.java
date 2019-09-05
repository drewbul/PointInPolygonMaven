package ate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Main extends Application{

    static final Alert warningPopup = new Alert(Alert.AlertType.WARNING);
    static final Voice myVoice = new Voice();

    @Override
    public void start(Stage stage) {
        warningPopup.setTitle("Sound files alert");
        warningPopup.getDialogPane().setMinWidth(700);
        Application.Parameters params = getParameters();
        List<String> parameterList = params.getRaw();
        if (parameterList.size()==4){
            myVoice.loadMedia(parameterList.get(0), parameterList.get(1), parameterList.get(2), parameterList.get(3));
        }
        else {
            myVoice.skipVoice=true;
            warningPopup.setHeaderText("Sound files are not specified");
            warningPopup.setContentText("Voice files should be specified as input parameters, e.g.:\n\n"+
                    "PointInPolygon_JDK8.jar inside_en.wav inside_ru.wav outside_en.wav outside_ru.wav\n\n"+
                    "Both WAV and MP3 are supported, but the proper libraries should be installed in your OS for MP3 support.\r\n" +
                    "Sound playback will be skipped for now.");
            warningPopup.showAndWait();
        }

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