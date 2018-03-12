package ortino.andrea.set08114.movieseeker;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by Shelob on 22/02/2018.
 * This class adapt the image size with implemented method in the case of the placeholder images,
 * and utilising picasso for the images box coming from the API website.
 */

public class ImageAdapter extends BaseAdapter {
    private Context context;
    //box image paths array
    private ArrayList<String> array;
    private int width;

    public ImageAdapter(Context c, ArrayList<String> paths, int size)
    {
        context = c;
        array = paths;
        width = size;
    }
    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // for each element of the array it calls getView with different index of the array
    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null)
        {
            imageView = new ImageView(context);
        }
        else{
            imageView = (ImageView) convertView;
        }
        // populate the spot with the place holder in case of long loading times
        Drawable drawable = resizeDrawable(context.getResources().getDrawable(R.drawable.placeholder));

        // populate the grid spot with the correct box image and resize it using Picasso
        // the base url is passed + the array with path of the box images
        // load the place holder in case the image does not load
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + array.get(index)).
                resize(width, (int)(width*1.5)).placeholder(drawable).into(imageView);

        return imageView;

    }

    // resize placeholder image box 1 by 1.5
    private Drawable resizeDrawable(Drawable image)
    {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b,width, (int)(width*1.5),false);
        return new BitmapDrawable(context.getResources(),bitmapResized);
    }
}