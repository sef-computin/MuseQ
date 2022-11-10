package player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

public class Controller {
    
    @FXML
    private ListView<String> songsListView = new ListView<String>();
    @FXML
    private Label currentSongLabel = new Label("...");

    private Media media;
    private MediaPlayer mediaPlayer;
    private int playlistCounter = 0;
    
    private FileChooser filechooser;
    private ObservableList<String> songsLabels = FXCollections.observableArrayList();
    private ArrayList<File> musicFilesList = new ArrayList<File>();

    public void playSong(ActionEvent e){
        if (this.musicFilesList.size() == 0){return ;}
        if (this.mediaPlayer != null) { this.mediaPlayer.dispose(); }
        this.media = new Media(musicFilesList.get(this.playlistCounter).toURI().toString());
        this.mediaPlayer = new MediaPlayer(media);
        this.currentSongLabel.setText(this.songsLabels.get(this.playlistCounter));
        this.mediaPlayer.play();
    }

    public void playNext(ActionEvent e){
        this.playlistCounter = this.playlistCounter == this.musicFilesList.size() - 1 ? 0 : ++this.playlistCounter;
        playSong(e);
    }
    public void playPrev(ActionEvent e){
        this.playlistCounter = this.playlistCounter == 0 ? this.musicFilesList.size() - 1 : --this.playlistCounter;
        playSong(e);
    }
    
    public void loadSongFiles(ActionEvent e){
        this.initializeFileChooserIfNeeded();
        List<File> newSongsList = filechooser.showOpenMultipleDialog(ICalebCom.getPStage());
        if(newSongsList == null) return;
        for(File file : newSongsList){
            System.err.println(file.toString());
            this.musicFilesList.add(file);
            songsLabels.add(file.getName());
            songsListView.setItems(songsLabels);
        }
    }

    public void clearPlaylist(ActionEvent e){
        this.songsLabels.clear();
        this.musicFilesList.clear();
        this.songsListView.setItems(songsLabels);
        this.currentSongLabel.setText("...");
        this.mediaPlayer.stop();
        this.mediaPlayer.dispose();
    }

    private void initializeFileChooserIfNeeded(){
        if (this.filechooser != null) return ;
        this.filechooser = new FileChooser();

        this.filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Music", "*.mp3"));
    }

}
