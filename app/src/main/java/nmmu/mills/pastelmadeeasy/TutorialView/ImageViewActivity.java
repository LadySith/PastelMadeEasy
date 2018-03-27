package nmmu.mills.pastelmadeeasy.TutorialView;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import nmmu.mills.pastelmadeeasy.R;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageViewActivity extends AppCompatActivity {
    ImageView imageView;
    PhotoViewAttacher attacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        Bundle b = getIntent().getExtras();
        int id = b.getInt("imageID");

        imageView = (ImageView) findViewById(R.id.image_view);
        Drawable bitmap = getResources().getDrawable(id);
        imageView.setImageDrawable(bitmap);
        attacher = new PhotoViewAttacher(imageView);

    }

}
