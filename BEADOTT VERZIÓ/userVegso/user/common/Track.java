package common;

import java.util.Objects;

/**
 * Represents a song with length, song title, artist name and number of streams
 */
public class Track {

    private final int length;
    private final String name;
    private final String artist;
    private int numberOfStreams;

    public Track(int length, String name, String artist) {
        this.length = length;
        this.name = name;
        this.artist = artist;
        this.numberOfStreams = 0;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public int getNumberOfStreams(){
        return this.numberOfStreams;
    }

    public void incrementNumberOfStreams(){
        this.numberOfStreams++;
    }

    @Override
    public String toString() {
        return String.format("%s - %s, %d length, %d number of streams", artist, name, length, numberOfStreams);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return length == track.length && name.equals(track.name) && artist.equals(track.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, name, artist);
    }
}
