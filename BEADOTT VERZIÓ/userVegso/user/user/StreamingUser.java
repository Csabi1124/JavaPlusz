package user;

import common.Configuration;
import common.Track;
import common.User;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import platform.StreamingPlatform;

public class StreamingUser extends User {

    private static final String USER = "USER";

    private final String name;
    private final Map<String, Integer> artistListens;
    private final Map<Track, Integer> songListens;
    private String favouriteArtist;
    // NOTE: ha wait-notiffal megyunk ez felesleges
    private volatile boolean started;

    public StreamingUser(){
        // TODO - Assign name for the user with a unique id like this:
        // this.name = String.format("%s-%d", USER, <UNIQUE ID>);
        this.name = String.format("%s-%d", USER, System.identityHashCode(this));

        // TODO - Initialize artistListens map and add the existing artists based on
        // TODO - Configuration.SONGS_BY_ARTISTS with an initial value of 0
        // NOTE: semmi szukseg konkurens adatszerkezetre... csak szinkron kornyezetben hasznaljuk, private
        this.artistListens = new ConcurrentHashMap<>();
        for (String artist : Configuration.SONGS_BY_ARTISTS.keySet()) {
            artistListens.put(artist, 0);
        }

        // TODO - Initialize songListens map
        this.songListens = new ConcurrentHashMap<>();

        // No need to change this here
        this.favouriteArtist = null;
        this.started = false;
    }

    @Override
    public void startStreaming() {
        // TODO - Signals the user that the StreamingPlatform is ready to use
        // NOTE: sokkal szebb lenne, egy notify-t kuldeni neki sync-elve magar az objektumra, ki is kuszoboli a yield-problemat a 66. sorban
        this.started = true;
    }

    @Override
    public void subscribe() {
        StreamingPlatform.instance().subscribeUser(this);
    }

    /**
     * After the StreamingPlatform is active, the user randomly listens to songs
     * and if it listened to a certain artist $Configuration.STREAMING_USER_FAVOURITE_ARTIST_BENCHMARK
     * number of times, that is the only artist the user will listen to
     * If a favourite artist is chosen, the user joins the FanClub for that artist and when the
     * StreamingPlatform isn't active anymore, he will vote for the best song (the one he listened to most
     * by that artist)
     */
    public void start(){
        // TODO - Wait until StreamingPlatform becomes active and until startStreaming() is invoked
        // NOTE: itt egy synchronised blokkoban kene loopolva waitelni az objektumon: volt yield nincs yield
        // NOTE: spurious wakeups miatt kell a loop
        while (!started || !StreamingPlatform.instance().isActive()) {
            Thread.yield();
        }

        Random rnd = ThreadLocalRandom.current();

        // TODO - While the StreamingPlatform is active
        while (StreamingPlatform.instance().isActive()) {
            // TODO - 1, Get a random track from getPossibleTracks()
            List<Track> possible = getPossibleTracks();
            // NOTE: ezek az egyik leg-AI-gyanusabb sorok... not good... helyes utemezes mellett ez oversecured/felesleges
            if (possible.isEmpty()) continue;
            Track track = possible.get(rnd.nextInt(possible.size()));

            // TODO - 2, Invoke StreamingPlatform's streamSong() with the randomly picked track
            Track streamed = StreamingPlatform.instance().streamSong(track.getArtist(), track.getName());
            // NOTE: ezek a masodik legAI gyanusabb sorok...
            if (streamed == null) continue;

            // TODO - 3, Wait track.getLength() to simulate listening to the song
            try {
                Thread.sleep(streamed.getLength());
            } catch (InterruptedException ignored) {}

            // TODO - 4, Increment artistListens for the current artist
            // NOTE: .get()++
            artistListens.merge(streamed.getArtist(), 1, Integer::sum);

            // TODO - 5, Increment songListens for the current track
            // NOTE: .get()++
            songListens.merge(streamed, 1, Integer::sum);

            // TODO - 6, If the user listened to an artist Configuration.STREAMING_USER_FAVOURITE_ARTIST_BENCHMARK
            // TODO - number of times, that artist will become the favouriteArtist for this user
            if (favouriteArtist == null
                && artistListens.get(streamed.getArtist()) >= Configuration.STREAMING_USER_FAVOURITE_ARTIST_BENCHMARK)
            {
                favouriteArtist = streamed.getArtist();
                // TODO - and print the following:
                // System.out.println(String.format("[StreamingUser] %s chose %s as his favourite artist",
                //                        this.name, <NAME OF THE ARTIST>));
                System.out.println(String.format("[StreamingUser] %s chose %s as his favourite artist",
                                                 this.name, favouriteArtist));
                // TODO - and join the correct FanClub ( FanClub.addMember() )
                FanClub.getFanClub(favouriteArtist)
                       .addMember(this);
            }
        }

        // TODO - If the user has a favouriteArtist
        if (favouriteArtist != null) {
            // TODO - 1, Get the song by that artist with the most listens by this user (songListens)
            Track best = songListens.entrySet().stream()
                .filter(e -> e.getKey().getArtist().equals(favouriteArtist))
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
            
            if (best != null) {
                int count = songListens.get(best);
                // TODO - 2, Vote for that track on the FanClub ( FanClub.voteForTrack() )
                FanClub.getFanClub(favouriteArtist)
                       .voteForTrack(this, best);
                // TODO - 3, Print the following:
                // System.out.println(String.format("[StreamingUser] %s listened to (%s) a total of %d times, votes for it",
                //                    this.name, <THE TRACK>, <NUMBER OF LISTENS TO THE TRACK>));
                System.out.println(String.format("[StreamingUser] %s listened to (%s) a total of %d times, votes for it",
                                                 this.name, best, count));
            }
        }
    }

    private List<Track> getPossibleTracks(){
        // TODO - If there is no favouriteArtist, get all songs from the StreamingPlatform
        List<Track> all = StreamingPlatform.instance().getTrackList();
        // TODO - If there is a favouriteArtist, get all songs from that artist from the StreamingPlatform
        if (favouriteArtist == null) {
            return new ArrayList<>(all);
        }
        List<Track> filtered = new ArrayList<>();
        for (Track t : all) {
            if (t.getArtist().equals(favouriteArtist)) {
                filtered.add(t);
            }
        }
        return filtered;
    }
}
