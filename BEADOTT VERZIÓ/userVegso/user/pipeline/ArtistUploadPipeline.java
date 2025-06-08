package pipeline;

import common.Configuration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Serves as the link between Artist and TrackUploader
 */
public class ArtistUploadPipeline {

    // SINGLETON STUFF BEGIN
    private static ArtistUploadPipeline instance;

    private ArtistUploadPipeline(){
        // TODO - Initialize queue with Configuration.ARTIST_UPLOAD_PIPELINE_CAPACITY
        this.queue = new LinkedBlockingQueue<>(Configuration.ARTIST_UPLOAD_PIPELINE_CAPACITY);
    }

    public static ArtistUploadPipeline instance(){
        if(instance == null)
            instance = new ArtistUploadPipeline();
        return instance;
    }
    // SINGLETON STUFF END

    // TODO - Create a queue for TrackData
    private final BlockingQueue<TrackData> queue;

    /**
     * Add a TrackData to the queue
     * @param trackData TrackData coming from an Artist
     */
    public void submit(final TrackData trackData){
        // TODO - Add trackData to the queue
        try {
            queue.put(trackData);
            // TODO - When it's added, print the following
            System.out.println(String.format("[ArtistUploadPipeline] Received %s", trackData));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Retrieves a TrackData from the queue
     * @return TrackData added to the queue by an artist
     */
    public TrackData get(){
        // TODO - Return a TrackData from the queue
        try {
            // TODO - If under Configuration.ARTIST_UPLOAD_PIPELINE_TIMEOUT_MS there is no new data, return null
            return queue.poll(Configuration.ARTIST_UPLOAD_PIPELINE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

}
