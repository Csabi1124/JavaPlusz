package platform;

import common.Configuration;
import common.Track;
import common.User;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StreamingPlatform {

    // SINGLETON STUFF BEGIN
    private static StreamingPlatform instance;

    private StreamingPlatform(){
        // TODO - Initialize required attributes
        // TODO - Might wanna change trackList implementation for Part 2
        this.trackList = new CopyOnWriteArrayList<>();
        this.songsPerArtist = new ConcurrentHashMap<>();
        this.topFiveSongs = new CopyOnWriteArrayList<>();
        this.subscribers = new CopyOnWriteArrayList<>();
        this.isActive = false;
        // TODO - Create counter for maxNumberOfSongs
        this.maxNumberOfSongs = new AtomicInteger(0);
        // TODO - Create counter for uploadedSongs
        this.uploadedSongs = new AtomicInteger(0);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public static synchronized StreamingPlatform instance(){
        if(instance == null)
            instance = new StreamingPlatform();
        return instance;
    }
    // SINGLETON STUFF END

    private final List<Track> trackList;
    private final Map<String, Integer> songsPerArtist;
    private final List<Track> topFiveSongs;
    private final List<User> subscribers;

    // TODO - Type can be changed or can remain the same, however you wanna do it for Part 2
    private volatile boolean isActive;
    // TODO - Create counter for maxNumberOfSongs
    private final AtomicInteger maxNumberOfSongs;
    // TODO - Create counter for uploadedSongs
    private final AtomicInteger uploadedSongs;

    private final ScheduledExecutorService scheduler;

    /**
     * Registers an artist to the platform
     * Records the number of songs per artist in the songPerArtist map
     * Increases maxNumberOfSongs with the newly registered amount
     * @param artistName
     * @param numberOfSongs
     */
    public void registerArtist(final String artistName, final int numberOfSongs){
        // TODO - Print the following:
        // System.out.println(String.format("[StreamingPlatform] %s registered %d songs", artistName, numberOfSongs));
        if (songsPerArtist.containsKey(artistName)) {
            // TODO - If the artist is already registered do nothing and print:
            // System.out.println(String.format("[StreamingPlatform] ERROR - %s already registered!", artistName));
            System.out.println(String.format("[StreamingPlatform] ERROR - %s already registered!", artistName));
            return;
        }
        songsPerArtist.put(artistName, numberOfSongs);
        System.out.println(String.format("[StreamingPlatform] %s registered %d songs", artistName, numberOfSongs));
        // TODO - Increase maxNumberOfSongs with numberOfSongs
        maxNumberOfSongs.addAndGet(numberOfSongs);
    }

    public void upload(final Track track){
        // TODO - Print the following:
        // System.out.println(String.format("[StreamingPlatform] Adding (%s) to track list", track));
        synchronized (trackList) {
            if (trackList.contains(track)) {
                // TODO - If the trackList already contains this track do nothing and print:
                // System.out.println(String.format("[StreamingPlatform] ERROR - %s already exists!", track));
                System.out.println(String.format("[StreamingPlatform] ERROR - %s already exists!", track));
                return;
            }
            System.out.println(String.format("[StreamingPlatform] Adding (%s) to track list", track));
            // TODO - Add the track to the trackList
            trackList.add(track);
            // TODO - Increment uploadedSongs
            uploadedSongs.incrementAndGet();
        }
    }

    /**
     * Subscribes a user to the StreamingPlatform
     * @param user
     */
    public void subscribeUser(final User user){
        this.subscribers.add(user);
    }

    /**
     * Streams a song to the user
     * @param artist Name of the artist
     * @param songName Name of the song to be streamed
     * @return The song
     */
    public Track streamSong(final String artist, final String songName){
        // TODO - If the StreamingPlatform is not active, do nothing (isActive)
        if (!isActive) return null;
        // TODO - Otherwise
        // TODO - 1, Get the right track from trackList
        Track found = null;
        synchronized (trackList) {
            for (Track t : trackList) {
                if (t.getArtist().equals(artist) && t.getName().equals(songName)) {
                    found = t;
                    break;
                }
            }
        }
        // TODO - 2, If there is no corresponding track, return null
        if (found == null) return null;
        // TODO - 3, If there is such a song, increment number of streams for the track
        found.incrementNumberOfStreams();
        // TODO - 4, Update the topFiveSongs list if this song surpassed a song in
        // TODO - number of streams compared to the previous contents of the list
        updateTopFiveSongs();
        // TODO - 5, Return the correct song (or null if there was no such song)
        return found;
    }

    /**
     * Starts the StreamingPlatform - use this if you do Part 2 of the assignment
     * The StreamingPlatform waits until the number of uploadedSongs match maxNumberOfSongs,
     * set isActive to true, then invoke startStreaming() for the users
     * After $Configuration.STREAMING_PLATFORM_SIMULATION_DURATION set isActive to false
     */
    public void start(){
        // TODO - Wait until uploadedSongs equals maxNumberOfSongs
        while (uploadedSongs.get() < maxNumberOfSongs.get()) {
            try {
                // TODO - You can use Configuration.STREAMING_PLATFORM_STARTUP_WAIT_TIME to wait between each check
                Thread.sleep(Configuration.STREAMING_PLATFORM_STARTUP_WAIT_TIME);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        updateTopFiveSongs();

        // TODO - Print the following after uploadedSongs equals maxNumberOfSongs:
        // System.out.println("[StreamingPlatform] All tracks are ready, Streaming Platform is online");
        System.out.println("[StreamingPlatform] All tracks are ready, Streaming Platform is online");
        this.isActive = true;
        for (final User u : subscribers) {
            new Thread(() -> u.startStreaming()).start();
        }

        // TODO - Invoke printTopFiveSongs every 1000 milliseconds
        scheduler.scheduleAtFixedRate(this::printTopFiveSongs, 1, 1, TimeUnit.SECONDS);
        // TODO - Wait Configuration.STREAMING_PLATFORM_SIMULATION_DURATION
        try {
            Thread.sleep(Configuration.STREAMING_PLATFORM_SIMULATION_DURATION);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        scheduler.shutdownNow();
        this.isActive = false;
        // TODO - Print the following:
        // System.out.println("[StreamingPlatform] Streaming Platform is shutting down");
        System.out.println("[StreamingPlatform] Streaming Platform is shutting down");
    }

    /**
     * Mocks the functionality of StreamingPlatform for Part 3
     */
    public void mockStart(){
        this.isActive = true;
        for (final User u : subscribers) {
            new Thread(() -> u.startStreaming()).start();
        }
        try {
            Thread.sleep(Configuration.STREAMING_PLATFORM_SIMULATION_DURATION);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.isActive = false;
    }

    public List<Track> getTrackList(){
        return this.trackList;
    }

    public boolean isActive(){
        return isActive;
    }

    /**
     * Prints the top five songs in order
     */
    private void printTopFiveSongs(){
        StringBuilder sb = new StringBuilder("[StreamingPlatform] Current Top 5 songs:");
        // TODO - Append the following line to the StringBuilder for each of the top five songs
        // sb.append(String.format("\n[StreamingPlatform] %d. %s", <RANKING ON TOP5>, <THE TRACK>));
        for (int i = 0; i < topFiveSongs.size(); i++) {
            sb.append(String.format("\n[StreamingPlatform] %d. %s", i + 1, topFiveSongs.get(i)));
        }
        sb.append("\n[StreamingPlatform] *****************************************");
        System.out.println(sb);
    }

    private void updateTopFiveSongs(){
        // TODO - Update topFiveSongs with the 5 most streamed songs (getNumberOfStreams)
        // TODO - Rankings should be in order (most streamed is first, second second, etc)
        List<Track> sorted;
        synchronized (trackList) {
            sorted = new ArrayList<>(trackList);
        }
        sorted.sort(Comparator.comparingInt(Track::getNumberOfStreams).reversed());

        synchronized (topFiveSongs) {
            topFiveSongs.clear();
            for (int i = 0; i < Math.min(5, sorted.size()); i++) {
                topFiveSongs.add(sorted.get(i));
            }
        }
    }
}
