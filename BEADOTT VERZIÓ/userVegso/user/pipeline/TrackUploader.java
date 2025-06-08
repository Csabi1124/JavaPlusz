package pipeline;

import common.Track;
import common.Configuration;
import platform.StreamingPlatform;

/**
 * Gets TrackData from the ArtistUploadPipeline, converts it to Track
 * and uploads it to the StreamingPlatform
 */
public class TrackUploader {

    /**
     * Begin getting TrackData from the ArtistUploadPipeline, converting it
     * and uploading to StreamingPlatform
     */
    public void start(){
        // TODO - Begin getting TrackData from ArtistUploadPipeline
        TrackData td = ArtistUploadPipeline.instance().get();

        // TODO - if the received TrackData is null, the uploader is done uploading
        if (td == null) return;

        // TODO - otherwise keep going until there is TrackData coming in
        while (td != null) {
            // TODO - Use convert to create Track from TrackData
            Track t = convert(td);

            // TODO - Use StreamingPlatform's upload for the newly created Track
            StreamingPlatform.instance().upload(t);

            td = ArtistUploadPipeline.instance().get();
        }
    }

    private Track convert(final TrackData trackData){
        // TODO - Wait trackData.getLength() * Configuration.TRACK_CONVERSION_DURATION_MULTIPLIER
        try {
            Thread.sleep((long) trackData.getLength() * Configuration.TRACK_CONVERSION_DURATION_MULTIPLIER);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // TODO - to simulate conversion process

        // TODO - Print the following:
        // System.out.println("[TrackUploader] Finished converting " + trackData);
        System.out.println("[TrackUploader] Finished converting " + trackData);

        // TODO - Create a Track based on information from TrackData

        // TODO - Return with the newly created Track
        return new Track(trackData.getLength(), trackData.getName(), trackData.getArtist());
    }
}
