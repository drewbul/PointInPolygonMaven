package ate;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

class Voice {

    private MediaPlayer playerInside_en;
    boolean skipVoice = false;
    private MediaPlayer playerOutside_en;
    private MediaPlayer playerInside_ru;
    private MediaPlayer playerOutside_ru;

    void loadMedia(String fnInside_en, String fnInside_ru, String fnOutside_en, String fnOutside_ru) {
        try {
            Media soundInside_en = new Media(new File(fnInside_en).toURI().toString());
            Media soundInside_ru = new Media(new File(fnInside_ru).toURI().toString());
            Media soundOutside_en = new Media(new File(fnOutside_en).toURI().toString());
            Media soundOutside_ru = new Media(new File(fnOutside_ru).toURI().toString());
            playerInside_en = new MediaPlayer(soundInside_en);
            playerOutside_en = new MediaPlayer(soundOutside_en);
            playerInside_ru = new MediaPlayer(soundInside_ru);
            playerOutside_ru = new MediaPlayer(soundOutside_ru);
        } catch (Exception e) {
            Main.warningPopup.setHeaderText("Error loading sound files");
            Main.warningPopup.setContentText(e.getMessage());
            Main.warningPopup.showAndWait();
            skipVoice = true;
        }
    }

    void stop() {
        if (!skipVoice) {
            playerInside_en.stop();
            playerOutside_en.stop();
            playerInside_ru.stop();
            playerOutside_ru.stop();
        }
    }

    void play(boolean isInside, String lang) {
        if (!skipVoice) {
            try {
                if (isInside && lang.equals("English")) playerInside_en.play();
                if (isInside && lang.equals("Russian")) playerInside_ru.play();
                if (!isInside && lang.equals("English")) playerOutside_en.play();
                if (!isInside && lang.equals("Russian")) playerOutside_ru.play();
            } catch (Exception e) {
                Main.warningPopup.setHeaderText("Error playing sound files");
                Main.warningPopup.setContentText(e.getMessage());
                Main.warningPopup.showAndWait();
                skipVoice = true;
            }
        }
    }
}