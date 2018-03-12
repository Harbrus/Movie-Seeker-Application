package ortino.andrea.set08114.movieseeker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Shelob LAST EDIT 05/03/2018.
 * Movie Fragment where all the main methods take place
 */

public class MovieFragment extends Fragment {

    private static final String BASIC_LINK = "http://api.themoviedb.org/3/discover/movie?sort_by=";
    private static final String API_KEY = "feddb54cb55ee598eb384037bcec31c1";
    static GridView gView;
    static ArrayList<String> boxes;
    static int width;
    static boolean sortbyPop = true;
    static boolean sortByVote;
    static boolean sortByFavourites;
    static PreferenceChangeListener favListener;
    static ArrayList<String> aboutList;
    static ArrayList<String> titlesList;
    static ArrayList<String> datesList;
    static ArrayList<String> votesList;
    static ArrayList<String> idsList;
    static ArrayList<Boolean> favouritesList;
    static SharedPreferences favs;
    static ArrayList<String> favBoxes = new ArrayList<String>();
    static ArrayList<String> aboutListF;
    static ArrayList<String> titlesListF;
    static ArrayList<String> datesListF;
    static ArrayList<String> votesListF;

    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point dimension = new Point();
        display.getSize(dimension);

        if (MainActivity.TABLET) {
            width = dimension.x / 5;
        } else width = dimension.x / 3;

        if (getActivity() != null) {
            ArrayList<String> array = new ArrayList<String>();
            ImageAdapter adapter = new ImageAdapter(getActivity(), array, width);
            gView = (GridView) rootView.findViewById(R.id.gridview);

            gView.setColumnWidth(width);
            gView.setAdapter(adapter);
        }
        //listen to item pressed on  the gridview
        gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!sortByFavourites)
                {
                    favouritesList = registerFavorites();

                     Intent intent2 = new Intent(getActivity(), MoviesDetails.class).
                             putExtra("about", aboutList.get(position)).
                             putExtra("box", boxes.get(position)).
                             putExtra("title", titlesList.get(position)).
                             putExtra("date", datesList.get(position)).
                             putExtra("vote", votesList.get(position)).
                             putExtra("favorite", favouritesList.get(position));

                    startActivity(intent2);
                }
                 else
                {
                    Intent intent2 = new Intent(getActivity(), MoviesDetails.class).
                            putExtra("about", aboutListF.get(position)).
                            putExtra("box", favBoxes.get(position)).
                            putExtra("title", titlesListF.get(position)).
                            putExtra("date", datesListF.get(position)).
                            putExtra("vote", votesListF.get(position)).
                            putExtra("favorite", favouritesList.get(position));

                    startActivity(intent2);
                }
            }
        });

        return rootView;
    }

    // even if not in this activity it will be called when prefs change and updates
    private class PreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            gView.setAdapter(null);
            onStart();
        }
    }

    public ArrayList<Boolean> registerFavorites()
    {
        ArrayList<Boolean> data = new ArrayList<>();

        // set everything to false
        for(int i =0; i<titlesList.size();i++)
        {
            data.add(false);
        }

        // check the normal titles list and if they are favorites set the arraylist elements to true
        // In this way even if it is not sortByFav true, it will consider favorite titles and
        // let the user favorite/remove them.

        for(String favoritedMovies: titlesListF)
        {
            for(int y = 0; y<titlesList.size(); y++)
            {
                if(favoritedMovies.equals(titlesList.get(y)))
                {
                    data.set(y,true);
                }
            }
        }

        return data;
    }

    @Override
    public void onStart() {
        super.onStart();
        favs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        favListener = new PreferenceChangeListener();
        favs.registerOnSharedPreferenceChangeListener(favListener);

        if (favs.getString("sortby", "popularity").equals("popularity")) {
            getActivity().setTitle("Popular Movies");
            sortbyPop = true;
            sortByFavourites = false;
            sortByVote = false;
        } else if (favs.getString("sortby", "popularity").equals("vote")) {
            getActivity().setTitle("Highest Voted Movies");
            sortbyPop = false;
            sortByFavourites = false;
            sortByVote = true;
        } else if (favs.getString("sortby", "popularity").equals("favourites")) {
            getActivity().setTitle("Favourited Movies");
            sortbyPop = false;
            sortByFavourites = true;
            sortByVote = false;
        }
        TextView textView = new TextView(getActivity());
        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.linearLayout);

        loadFavoritesMovies();

        // check if the user has any favourites movies and display textview for no favs
        // otherwise grid layout will be visible with favourite movies
        if (sortByFavourites) {
            if (favBoxes.size() == 0) {
                textView.setText("Nothing to show.");
                if (layout.getChildCount() == 1)
                    layout.addView(textView);
                gView.setVisibility(GridView.GONE);
            } else {
                gView.setVisibility(GridView.VISIBLE);
                layout.removeView(textView);
            }
            if (favBoxes != null && getActivity() != null) {
                ImageAdapter adapter = new ImageAdapter(getActivity(), favBoxes, width);
                gView.setAdapter(adapter);
            }
        }

        // if not from favourites movies we fetch normally
        else {
            gView.setVisibility(GridView.VISIBLE);
            layout.removeView(textView);
        }

        if (isNetworkAvailable()) {
            new ImageLoadTask().execute();
        } else {
            TextView textView2 = new TextView(getActivity());
            LinearLayout layout1 = (LinearLayout) getActivity().findViewById(R.id.linearLayout);
            textView2.setText("No Internet Connection");
            if (layout1.getChildCount() == 1) {
                layout1.addView(textView2);
            }
            gView.setVisibility(GridView.GONE);
        }
    }

    public void loadFavoritesMovies()
    {
        String URL = "content://ortino.andrea.set08114.movieseeker.provider.Movies/movies";
        Uri movies = Uri.parse(URL);
        Cursor c = getActivity().getContentResolver().query(movies,null,null,null,"title");
        favBoxes = new ArrayList<String>();
        titlesListF = new ArrayList<String>();
        datesListF = new ArrayList<String>();
        aboutListF = new ArrayList<String>();
        favouritesList = new ArrayList<Boolean>();
        votesListF = new ArrayList<String>();
        if(c==null) return;

        // retrieve data from the cursor of the database
        while(c.moveToNext())
        {
            aboutListF.add(c.getString(c.getColumnIndex(StoreDB.ABOUT)));
            favBoxes.add(c.getString(c.getColumnIndex(StoreDB.NAME)));
            titlesListF.add(c.getString(c.getColumnIndex(StoreDB.TITLE)));
            votesListF.add(c.getString(c.getColumnIndex(StoreDB.VOTE)));
            datesListF.add(c.getString(c.getColumnIndex(StoreDB.DATE)));
            favouritesList.add(true);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, ArrayList<String>> {

        //Array holding strings of boxes paths (max 15 paths)
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            while (true) {
                try {
                    boxes = new ArrayList(Arrays.asList(getPathsFromAPI()));
                    return boxes;

                } catch (Exception e) {
                    continue;
                }
            }

        }


        // here we uses the return of ImageLoadTask to set the grid view through the imageAdapter
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result != null && getActivity() != null)
            {
                ImageAdapter adapter = new ImageAdapter(getActivity(), result, width);
                gView.setAdapter(adapter);

            }
        }

        public String[] getPathsFromAPI() throws JSONException, IOException {
            while (true)
            {
                HttpURLConnection urlConnection = null;
                BufferedReader bufferedReader = null;
                String JSONResult;

                    String urlString = null;
                    if (sortbyPop) {
                        urlString = BASIC_LINK + "popularity.desc&api_key=" + API_KEY;

                    } else if (sortByVote) {
                        urlString = BASIC_LINK + "vote_average.desc&vote_count.gte=300&api_key=" + API_KEY;
                    }

                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read the input stream into a String
                InputStream inputStream = null;
                inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null)
                {
                        return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0)
                {
                    return null;
                }

                JSONResult = buffer.toString();

                aboutList = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResult, "overview")));
                titlesList = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResult, "original_title")));
                votesList = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResult, "vote_average")));
                datesList = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResult, "release_date")));
                idsList = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResult, "id")));

                return getPathsFromJSON(JSONResult);

                }
            }
        }


    public String[] getStringsFromJSON(String JSONStringParam, String search)  throws JSONException
    {
        JSONObject JSONString = new JSONObject(JSONStringParam);

        JSONArray moviesArray = JSONString.getJSONArray("results");
        String[] result = new String[moviesArray.length()];

        for(int i = 0; i<moviesArray.length();i++)
        {
            JSONObject movie = moviesArray.getJSONObject(i);
            if(search.equals("vote_average"))
            {
                Double number = movie.getDouble("vote_average");
                String vote = Double.toString(number)+"/10";
                result[i]=vote;
            }
            else {
                String data = movie.getString(search);
                result[i] = data;
            }
        }
        return result;
    }
    // We parse the string containing the json formatted string into an Json object and get the "result" from the api
    // where movies are stored, extracting in the loop each poster paths
    public String[] getPathsFromJSON(String JSONStringParam) throws JSONException{

        JSONObject JSONString = new JSONObject(JSONStringParam);

        JSONArray moviesArray = JSONString.getJSONArray("results");
        String[] result = new String[moviesArray.length()];

        for(int i = 0; i<moviesArray.length();i++)
        {
            JSONObject movie = moviesArray.getJSONObject(i);
            String moviePath = movie.getString("poster_path");
            result[i] = moviePath;
        }
        return result;
    }
}
