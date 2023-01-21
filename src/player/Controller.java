package player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javazoom.jl.player.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class Controller {
    
    @FXML
    private ListView<String> songsListView = new ListView<String>();
    @FXML
    private Label currentSongLabel = new Label("...");
    @FXML
    private Button playButton;

    private Media media;
    private MediaPlayer mediaPlayer;
    private int playlistCounter = 0;
    private short playerState = isIDLE;
    
    private FileChooser filechooser;
    private ObservableList<String> songsLabels = FXCollections.observableArrayList();
    private ArrayList<File> musicFilesList = new ArrayList<File>();

    private static final short isIDLE = 0,
                               isPlaying = 1,
                               isPaused = 2;                               

    public void playButtonPushed(ActionEvent e){
        /*
        switch(this.playerState){   // openjfx 1.8
            case isIDLE:
                playSong();
                break;
            case isPlaying:
                this.mediaPlayer.pause();
                this.playerState = isPaused;
                this.playButton.setText("Play");
                break;
            case isPaused:
                this.mediaPlayer.play();
                this.playerState = isPlaying;
                this.playButton.setText("Pause");
                break;
        }
        */   
        switch(this.playerState){   // openjfx 19
            case isIDLE -> {
                playSong();
            }
            case isPlaying -> {
                this.playerState = isPaused;
                this.mediaPlayer.pause();
                this.playButton.setText("Play");
                
            }
            case isPaused -> {
                this.playerState = isPlaying;
                this.mediaPlayer.play();
                this.playButton.setText("Pause");
            }
        }   
    }

    public void playSong(){
        if (this.musicFilesList.size() == 0){return ;}
        if (this.mediaPlayer != null) { this.mediaPlayer.dispose(); }
        this.media = new Media(musicFilesList.get(this.playlistCounter).toURI().toString());
        this.mediaPlayer = new MediaPlayer(media);
        this.currentSongLabel.setText(this.songsLabels.get(this.playlistCounter));
        
        this.playerState = isPlaying;
        this.playButton.setText("Pause");
        this.mediaPlayer.play();
    }

    public void playNext(ActionEvent e){
        this.playlistCounter = this.playlistCounter == this.musicFilesList.size() - 1 ? 0 : ++this.playlistCounter;
        playSong();
    }
    public void playPrev(ActionEvent e){
        this.playlistCounter = this.playlistCounter == 0 ? this.musicFilesList.size() - 1 : --this.playlistCounter;
        playSong();
    }
    
    public void loadSongFiles(ActionEvent e){
        this.initializeFileChooserIfNeeded();
        List<File> newSongsList = filechooser.showOpenMultipleDialog(ICalebCom.getPStage());
        if(newSongsList == null) return;
        for(File file : newSongsList){
            if (file.isDirectory()){ 
                for (File innerfile : file.listFiles()){
                    if (innerfile.getName().substring(innerfile.getName().lastIndexOf('.')) == ".mp3"){
                        System.err.println(file.toString());
                        this.musicFilesList.add(file);
                        songsLabels.add(file.getName());
                        songsListView.setItems(songsLabels);        
                    }
                }
                continue; 
            }
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

        this.playerState = isIDLE;
        this.playButton.setText("Play");
        if(this.mediaPlayer != null) this.mediaPlayer.dispose();
    }

    public void listItemClicked(MouseEvent e){
        //System.err.print("MouseEvent");
        this.playlistCounter = e.getClickCount() == 2 
                            ? this.songsListView.getSelectionModel().getSelectedIndex()
                            : this.playlistCounter;
        if(e.getClickCount() == 2) playSong();
    }

    private void initializeFileChooserIfNeeded(){
        if (this.filechooser != null) return ;
        this.filechooser = new FileChooser();

        this.filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Music", "*.mp3"));
    }
}
