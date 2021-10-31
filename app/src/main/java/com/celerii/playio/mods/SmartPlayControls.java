package com.celerii.playio.mods;

public class SmartPlayControls {
    String currentSong, currentSongImageURL;
    boolean isPlaying;

    public String getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(String currentSong) {
        this.currentSong = currentSong;
    }

    public String getCurrentSongImageURL() {
        return currentSongImageURL;
    }

    public void setCurrentSongImageURL(String currentSongImageURL) {
        this.currentSongImageURL = currentSongImageURL;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
