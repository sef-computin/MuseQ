package player;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicHandler {
    
    private static Media media;
    private static MediaPlayer player;

    public static void prepareMedia(String path){
        MusicHandler.media = new Media(new File(path).toURI().toString());
        MusicHandler.player = new MediaPlayer(MusicHandler.media);
    }

    public static void musicPlay(){
        assert(media != null);
        player.play();
    }
}
