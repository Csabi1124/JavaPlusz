package pipeline;

/**
 * Represents a song created by an Artist
 */
public class TrackData {

    private final int numberOfTracksOnAlbum;
    private final int trackNumber;
    private final int length;
    private final String name;
    private final String artist;

    public TrackData(int numberOfTracksOnAlbum, int trackNumber, int length, String name, String artist) {
        this.numberOfTracksOnAlbum = numberOfTracksOnAlbum;
        this.trackNumber = trackNumber;
        this.length = length;
        this.name = name;
        this.artist = artist;
    }

    public int getNumberOfTracksOnAlbum() {
        return numberOfTracksOnAlbum;
    }

    public int getTrackNumber() {
        return trackNumber;
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

    @Override
    public String toString() {
        return String.format("(%d/%d) %s - %s, %d length", trackNumber, numberOfTracksOnAlbum,
                artist, name, length);
    }
}
