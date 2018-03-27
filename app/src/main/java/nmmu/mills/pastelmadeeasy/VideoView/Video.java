package nmmu.mills.pastelmadeeasy.VideoView;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import nmmu.mills.pastelmadeeasy.Database.DatabaseHelper;
import nmmu.mills.pastelmadeeasy.R;

public class Video extends AppCompatActivity {

    private int conceptID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        final VideoView videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setKeepScreenOn(true);

        Bundle b = getIntent().getExtras();
        conceptID = b.getInt("conceptID");

        DatabaseHelper myDB = new DatabaseHelper(Video.this);
        String video = myDB.getVideo(conceptID);
        myDB.close();

        int videoResource = getResources().getIdentifier(video, "raw", getPackageName());

        String path = "android.resource://" + getPackageName() + "/" + videoResource;
        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        videoView.start();

        final MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaController.show();
                DatabaseHelper myDB = new DatabaseHelper(Video.this);
                myDB.updateComplete(conceptID,"conceptVideoWatched");
                myDB.close();

            }
        });


    }

}
