import common.Configuration;
import common.MockTrackUploader;
import common.MockUsers;
import pipeline.Artist;
import pipeline.TrackUploader;
import platform.StreamingPlatform;
import user.FanClub;
import user.StreamingUser;

public class Main {

    public static void main(String[] args) {

        // Part 1 - No need to mock anything

        // TODO - Create Artists based on the artists in Configuration.SONGS_BY_ARTISTS
        for (String name : Configuration.SONGS_BY_ARTISTS.keySet()) {
            Artist artist = new Artist(name);
            // TODO - Invoke begin() for each Artist on a new thread (important that this happens
            // TODO - before TrackUploader's start() is invoked)
            new Thread(() -> artist.begin()).start();
        }

        // TODO - Create Configuration.TRACK_UPLOADER_QUANTITY number of TrackUploader
        for (int i = 0; i < Configuration.TRACK_UPLOADER_QUANTITY; i++) {
            TrackUploader uploader = new TrackUploader();
            // TODO - Invoke start() for each TrackUploader on a new thread
            new Thread(() -> uploader.start()).start();
        }

        // Part 2 - Mock Track Uploader and Mock Users
        // TODO - Invoke StreamingPlatform's start() on a new thread
        new Thread(() -> StreamingPlatform.instance().start()).start();
        MockTrackUploader.mockSongs();
        MockUsers.mockUsers();

        // Part 3 - FanClubs and StreamingUsers
        // TODO - Create a new FanClub for each artist
        for (String artistName : Configuration.SONGS_BY_ARTISTS.keySet()) {
            FanClub.getFanClub(artistName);
        }
        // TODO - Create Configuration.STREAMING_USER_NUMBER_OF_USERS number of users
        // TODO - Invoke subscribe() on the created user
        // TODO - Invoke start() for the user on a new thread
        for (int i = 0; i < Configuration.STREAMING_USER_NUMBER_OF_USERS; i++) {
            StreamingUser user = new StreamingUser();
            user.subscribe();
            new Thread(() -> user.start()).start();
        }

        // NOTE: Ennek itt mar nincs ertelme... a subscribe-ok elott leteznie kell es a 33. sorban meg van hivva
        new Thread(() -> StreamingPlatform.instance().start()).start();
        // NOTE: ja es errol beszeltem, hogy mar tele kell legyen a subscriber listajanak, kulonben mire activ lesz(feltoltodtek a zenek) meg nem lesz min vegigiteraljon

        //StreamingPlatform.instance().mockStart();
    }
}
