package ortino.andrea.set08114.movieseeker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.graphics.Color.RED;

/**
 * A placeholder fragment containing a simple view.
 */

public class MoviesDetailsFragment extends Fragment {

    public static String about;
    public static String vote;
    public static String date;
    public static String title;
    public static String box;
    public static boolean favorite;
    public static Button btn;

    public MoviesDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies_details, container, false);

        Intent intent = getActivity().getIntent();
        getActivity().setTitle("Movie Info");

        if(intent !=null && intent.hasExtra("about"))
        {
            about = intent.getStringExtra("about");
            TextView textView = (TextView) rootView.findViewById(R.id.about);
            textView.setText(about);
        }

        if(intent !=null && intent.hasExtra("title"))
        {
            title = intent.getStringExtra("title");
            TextView textView = (TextView) rootView.findViewById(R.id.movieTitle);
            textView.setText(title);
        }

        if(intent !=null && intent.hasExtra("vote"))
        {
            vote = intent.getStringExtra("vote");
            TextView textView = (TextView) rootView.findViewById(R.id.vote);
            textView.setText(vote);
        }
        if(intent !=null && intent.hasExtra("date"))
        {
            date = intent.getStringExtra("date");
            TextView textView = (TextView) rootView.findViewById(R.id.date);
            textView.setText("\nReleased: " + date);

        }
        if(intent !=null && intent.hasExtra("box"))
        {
            box = intent.getStringExtra("box");
            ImageView imageView = (ImageView) rootView.findViewById(R.id.boxImage);

            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + box).resize(
                    MovieFragment.width, (int)(MovieFragment.width*1.5)).into(imageView);

        }

        btn = (Button)rootView.findViewById(R.id.btnFav);

        if(intent !=null && intent.hasExtra("favorite"))
        {
            favorite = intent.getBooleanExtra("favorite", false);
            if(!favorite)
            {
                btn.setText("FAVORITE");
                btn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            }
            else
            {
                btn.setText("REMOVE");
                btn.getBackground().setColorFilter(RED, PorterDuff.Mode.MULTIPLY);
            }
        }

        return rootView;
    }
}

