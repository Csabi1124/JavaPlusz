package common;

import platform.StreamingPlatform;

import java.util.concurrent.ThreadLocalRandom;

public class MockUser extends User{

    /**
     * Mocks Users for the StreamingPlatform
     */
    @Override
    public void startStreaming() {
        new Thread(() -> {
            for (int i = 0; i < 15000; i++) {
                Track track = StreamingPlatform.instance().getTrackList().get(
                        ThreadLocalRandom.current().nextInt(StreamingPlatform.instance().getTrackList().size()));
                StreamingPlatform.instance().streamSong(track.getArtist(), track.getName());
            }
        }).start();
    }

    @Override
    public void subscribe() {
        StreamingPlatform.instance().subscribeUser(this);
    }
}
