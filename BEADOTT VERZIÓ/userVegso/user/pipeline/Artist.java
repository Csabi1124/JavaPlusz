package pipeline;

import common.Configuration;
import platform.StreamingPlatform;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents an Artist that can upload songs to the StreamingPlatform
 */
public class Artist {

    private static final int UPLOAD_TIME_MS = 100;

    private final String name;

    public Artist(final String name){
        this.name = name;
    }

    /**
     * Artists first register on the StreamingPlatform then create TrackData
     * for each of their songs and submit it to ArtistUploadPipeline
     */
    public void begin(){
        // TODO - Get number of songs for the artist based on Configuration.SONGS_BY_ARTISTS
        List<String> songs = Configuration.SONGS_BY_ARTISTS.get(name);
        int numberOfSongs = songs.size();
        // TODO - Invoke StreamingPlatform's registerArtist (registerOnPlatform)
        registerOnPlatform(numberOfSongs);

        for (int i = 0; i < numberOfSongs; i++) {
            // TODO - Create a TrackData object with the relevant information from the artist's songs
            String song = songs.get(i);
            // TODO - use generateTrackLength to generate the length of a song
            int length = generateTrackLength();
            TrackData td = new TrackData(numberOfSongs, i + 1, length, song, name);
            // TODO - Invoke ArtistUploadPipeline's submit with the created TrackData
            ArtistUploadPipeline.instance().submit(td);
            // TODO - Wait UPLOAD_TIME_MS millisecs between each submit
            try {
                Thread.sleep(UPLOAD_TIME_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void registerOnPlatform(final int numberOfSongs){
        // TODO - Invoke StreamingPlatform's registerArtist
        StreamingPlatform.instance().registerArtist(name, numberOfSongs);
    }

    private int generateTrackLength(){
        // TODO - Generate a random number between Configuration.MIN_TRACK_DURATION_MS
        int min = Configuration.MIN_TRACK_DURATION_MS;
        // TODO - and Configuration.MAX_TRACK_DURATION_MS
        int max = Configuration.MAX_TRACK_DURATION_MS;
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
