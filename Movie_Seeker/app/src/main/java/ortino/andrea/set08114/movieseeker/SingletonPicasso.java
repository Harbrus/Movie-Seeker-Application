package ortino.andrea.set08114.movieseeker;

import android.app.Application;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by Shelob on 02/03/2018.
 * Picasso save the images it loads automatically as a single instance.
 */

public class SingletonPicasso extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Picasso save images on the disk cache automatically
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        Picasso.setSingletonInstance(built);
    }
}