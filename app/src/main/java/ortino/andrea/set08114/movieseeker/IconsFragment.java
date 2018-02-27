package ortino.andrea.set08114.movieseeker;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;

public  class IconsFragment extends Fragment {
    static GridView gridview;
    static int width;
    static ArrayList<String> boxes;
    static boolean sortByPop;

    public IconsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_icons, container, false);

        WindowManager w1 = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = w1.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        if (MainActivity.TABLET) {
            width = size.x / 4;
        }
        else width = size.x / 2;

        if (getActivity() != null) {
            ArrayList<String> list = new ArrayList<String>();
            ImageAdapter adapter = new ImageAdapter(getActivity(), list, width);
            gridview = (GridView) rootView.findViewById(R.id.gridview);

            gridview.setColumnWidth(width);
            gridview.setAdapter(adapter);
        }

        //listen for presses on grid items
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
            }
        });


        return rootView;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().setTitle("Popular Movies");

        if(isNetworkAvailable())
        {
            gridview.setVisibility(GridView.VISIBLE);
            new ImageLoadTask().execute();
        }
        else{
            TextView textview1 = new TextView(getActivity());
            RelativeLayout layout1 = getActivity().findViewById(R.id.relativeLayout);
            if(layout1.getChildCount()==1)
            {
                layout1.addView(textview1);
            }
            // if not connected grid view is invisible
            gridview.setVisibility(GridView.GONE);
        }
    }

    public boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo !=null &&activeNetworkInfo.isConnected();
    }
    public class ImageLoadTask extends AsyncTask<Void, Void, ArrayList<String>> {

        // String of the movie boxes paths
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            while(true){
                try{
                    boxes = new ArrayList(Arrays.asList(getPathsFromAPI(sortByPop)));
                    return boxes;
                }
                catch(Exception e)
                {
                    continue;
                }
            }

        }
        @Override
        protected void onPostExecute(ArrayList<String>result)
        {
            if(result!=null && getActivity()!=null)
            {
                ImageAdapter adapter = new ImageAdapter(getActivity(),result, width);
                gridview.setAdapter(adapter);

            }
        }
        public String[] getPathsFromAPI(boolean sort)
        {
            String[] array = new String[15];
            for(int i = 0; i<array.length;i++)
            {
                array[i] = ".jpg";
            }
            return array;

        }
    }
}