package common;

import platform.StreamingPlatform;

import java.util.concurrent.ThreadLocalRandom;

public class MockTrackUploader {

    /**
     * Creates songs without needing to implement the pipeline section
     */
    public static void mockSongs(){
        Configuration.SONGS_BY_ARTISTS.entrySet().forEach(e -> {
            StreamingPlatform.instance().registerArtist(e.getKey(), e.getValue().size());
            e.getValue().forEach(song -> {
                Track track = new Track(generateSongDuration(), song, e.getKey());
                StreamingPlatform.instance().upload(track);
            });
        });
    }

    /**
     * Randomly generate duration for a song
     * @return random duration
     */
    private static int generateSongDuration(){
        return ThreadLocalRandom.current().nextInt(
                Configuration.MIN_TRACK_DURATION_MS, Configuration.MAX_TRACK_DURATION_MS);
    }
}
