package ate;

import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.List;

class Voice {

    boolean skipVoice = false;
    private MediaPlayer playerInside;
    private MediaPlayer playerOutside;
    private String lang;
    private String extension;
    private static final Alert warningPopup = new Alert(Alert.AlertType.WARNING);

    private String fileSuffix() {
        switch (lang) {
            case "English": return "en";
            case "Russian": return "ru";
            default: return "";
        }
    }

//    public String getLang() {
//        return lang;
//    }

    public void setLang(String newLang) {
        //noinspection StringEquality
        if (lang!= newLang){ //skip loading media if the same media is already loaded
            lang = newLang;
            skipVoice = false; // give it another chance
            loadMedia();
        }
    }

    public void setExtension(List<String> parameterList) {

        boolean ifUseMP3 = parameterList.size()>=1 && parameterList.get(0).equals("mp3");
        boolean ifUseWav = parameterList.size()>=1 && parameterList.get(0).equals("wav");

        warningPopup.setTitle("Sound files alert");
        warningPopup.getDialogPane().setMinWidth(700);
        extension = ifUseMP3 ? "mp3" : "wav";
        if (!ifUseMP3 && !ifUseWav){
            warningPopup.setHeaderText("Extension of sound files not specified");
            warningPopup.setContentText("In order to avoid this message, please start the program with parameter 'wav' or 'mp3', e.g.:\n\n"+
                    "PointInPolygon.jar wav\n"+
                    "PointInPolygon.jar mp3\n\n"+
                    "Proper libraries should be installed in your OS for MP3 support.\n" +
                    "Now WAV files will be used by default.");
            warningPopup.showAndWait();
        }
    }

    private MediaPlayer createPlayer(String name) {
        Media sound = new Media(new File(String.format("%s_%s", name, fileSuffix() + "." + extension)).toURI().toString());
        return new MediaPlayer(sound);
    }

    private void loadMedia() {
        try {
            playerInside = createPlayer("inside");
            playerOutside = createPlayer("outside");
        } catch (Exception e) {
            warningPopup.setHeaderText("Error loading sound files");
            warningPopup.setContentText(e.getMessage());
            warningPopup.showAndWait();
            skipVoice = true;
        }
    }

    void play(boolean isInside) {
        if (!skipVoice) {
            try {
                if (isInside) {
                    playerInside.play();
                } else {
                    playerOutside.play();
                }
            } catch (Exception e) {
                warningPopup.setHeaderText("Error playing sound files");
                warningPopup.setContentText(e.getMessage());
                warningPopup.showAndWait();
                skipVoice = true;
            }
        }
    }

    void stop() {
        if (!skipVoice) {
            playerInside.stop();
            playerOutside.stop();
        }
    }
}