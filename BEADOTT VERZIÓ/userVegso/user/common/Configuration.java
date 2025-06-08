package common;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Configuration {

    // Configs related to Tracks
    public static final int MAX_TRACK_DURATION_MS = 100;
    public static final int MIN_TRACK_DURATION_MS = 50;

    // Configs related to Artists
    public static final int ARTIST_UPLOAD_PIPELINE_CAPACITY = 5;
    public static final int ARTIST_UPLOAD_PIPELINE_TIMEOUT_MS = 2000;

    // Configs related to TrackUploader
    public static final int TRACK_CONVERSION_DURATION_MULTIPLIER = 5;
    public static final int TRACK_UPLOADER_QUANTITY = 5;

    // Configs related to StreamingPlatform
    public static final int STREAMING_PLATFORM_STARTUP_WAIT_TIME = 10;
    public static final int STREAMING_PLATFORM_SIMULATION_DURATION = 15000;

    // Configs related to StreamingUser
    public static final int STREAMING_USER_NUMBER_OF_USERS = 1000;
    public static final int STREAMING_USER_FAVOURITE_ARTIST_BENCHMARK = 50;

    // Map for artists and their songs
    // Disclaimer: If you change this, it serves as an official statement that you don't
    // have a good taste in music, but sure, go ahead
    public static Map<String, List<String>> SONGS_BY_ARTISTS = Collections.unmodifiableMap(Map.of(
            "Krubi", List.of("JEGHIDEG", "LEJTO", "FELEJTO", "CSEKK", "DINAMIT", "PETOFI", "PUSZI", "SAPIENS",
                    "KUTYA", "COPFOCSKA"),
            "Kendrick Lamar", List.of("Wesley's Theory", "For Free?", "King Kunta", "Institutionalized",
                    "These Walls", "u", "Alright", "For Sale?", "Momma", "Hood Politics", "How Much A Dollar Cost",
                    "Complexion", "The Blacker The Berry", "You Ain't Gotta Lie", "i", "Mortal Man"),
            "Eminem", List.of("Kill You", "Stan", "Who Knew", "The Way I Am", "The Real Slim Shady", "Remember Me?",
                    "I'm Back", "Marshall Mathers", "Drug Ballad", "Amityville", "B!tch Please II", "Kim",
                    "Under The Influence", "Criminal"),
            "Linkin Park", List.of("Papercut", "One Step Closer", "With You", "Points of Authority", "Crawling",
                    "Runaway", "By Myself", "In The End", "A Place for My Head", "Forgotten", "Cure for the Itch",
                    "Pushing Me Away", "My December", "High Voltage")
    ));
}
