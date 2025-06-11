package user;

import common.Track;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import platform.StreamingPlatform;

public class FanClub {

    private static final List<FanClub> fanClubs = new ArrayList<>();

    // NOTE: hasznos az a synchronized
    public static synchronized FanClub getFanClub(final String artist) {
        // TODO - Get the appropriate FanClub from fanClubs
        for (FanClub fc : fanClubs) {
            if (fc.artist.equals(artist)) {
                return fc;
            }
        }
        return new FanClub(artist);
    }

    private final String artist;
    private final Map<Track, Integer> mostStreamedSongs;
    private final List<StreamingUser> members;
    // TODO - You can change the type for this if you wanna
    private final AtomicInteger numberOfVotes;

    public FanClub(final String artist) {
        this.artist = artist;
        // TODO - Initialize mostStreamedSongs and fill it with all the songs from the artist
        // TODO - with an initial value of 0
        // NOTE: sima hashmap? / concurrent hashmap?
        this.mostStreamedSongs = new LinkedHashMap<>();
        // NOTE: feltetelezi, hogy a tracklist teljes, pedig meg az se biztos, hogy az osszes feltoltodott
        // NOTE: mainben jobb lenne, ha engednenk, hogy a fanok hozzak letre az elso fanclubokat: nem kell kulon, a getFanClub() megoldja
        // NOTE: de persze az utasitast is kovethetjuk es csinalhatjuk a configurations alapjan is
        // NOTE: akkor egy nested loopban addig kerni a trakListtol a szamot, amig nem talaljuk meg a cimet
        List<Track> all = StreamingPlatform.instance().getTrackList();
        for (Track t : all) {
            if (t.getArtist().equals(artist)) {
                this.mostStreamedSongs.put(t, 0);
            }
        }
        // TODO - Initialize members list
        this.members = Collections.synchronizedList(new ArrayList<>());
        this.numberOfVotes = new AtomicInteger(0);

        fanClubs.add(this);
    }

    /**
     * Adds a member to the fan club
     * @param user fan club member candidate
     */
    public void addMember(final StreamingUser user) {
        // TODO - Add the user to the members list and print the following:
        // System.out.println(String.format("[FanClub] %s fan club has %d members", this.artist, <NUMBER OF MEMBERS>));
        members.add(user);
        System.out.printf("[FanClub] %s fan club has %d members%n", this.artist, members.size());
        // TODO - Start waitForVote for the user on a new thread
        new Thread(() -> waitForVote(user)).start();
    }

    /**
     * Lets a user vote for their favourite track
     * @param user voter
     * @param track the track user votes for
     */
    public void voteForTrack(final StreamingUser user, final Track track) {
        // TODO - Increment vote on the appropriate song (mostStreamedSongs)
        synchronized (user) {
            mostStreamedSongs.merge(track, 1, Integer::sum);
            numberOfVotes.incrementAndGet();
            user.notify();
        }
        // TODO - Increment numberOfVotes
    }

    // TODO - waitForVote should terminate after a vote is received
    private void waitForVote(final StreamingUser user) {
        // TODO - Wait until the user invokes voteForTrack()
        synchronized (user) {
            while (numberOfVotes.get() < members.size()) {
                try {
                    user.wait();
                } catch (InterruptedException ignored) {
                }
            }
            closeVote();
        }
    }

    private void closeVote() {
        // TODO - If the numberOfVotes equals the number of members:
        if (numberOfVotes.get() != members.size()) return;
        // TODO - 1, Create a StringBuilder like this:
        // StringBuilder sb = new StringBuilder("*******************************")
        StringBuilder sb = new StringBuilder("*******************************");
        // TODO - 2, Append the artist like this to the StringBuilder:
        // sb.append(String.format("\n[FanClub] %s fan club's votes:", artist))
        sb.append(String.format("\n[FanClub] %s fan club's votes:", artist));
        // TODO - 3, Add songs from mostStreamedSongs in an ascending order (0 -> max)
        // TODO - to the StringBuilder like this:
        // sb.append(String.format("\n[FanClub] %s - %d votes", e.getKey(), e.getValue())
        mostStreamedSongs.entrySet().stream()
            .sorted(Comparator.comparingInt(Map.Entry::getValue))
            .forEach(e -> sb.append(
                String.format("\n[FanClub] %s - %d votes", e.getKey(), e.getValue())
            ));
        // TODO - 4, Append some delimiter to the StringBuilder like this and print it:
        sb.append("\n*******************************");
        System.out.println(sb);
    }
}
