package ortino.andrea.set08114.movieseeker;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Shelob on 15/02/2018.
 *  Main Activity managed by fragments
 */

public class MainActivity extends AppCompatActivity {

    public static boolean TABLET = false;

    public boolean isTablet(Context context)
    {
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)==Configuration.SCREENLAYOUT_SIZE_LARGE);
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)==4);
        return(xlarge||large);
    }

    // On creation we check if there is already a saved instance, if it is null it is replaced by a new instance of the fragment.
    // We use replace for not creating unnecessary instances of the fragment.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TABLET= isTablet(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new MovieFragment())
                    .commit();
        }

    }

    // Inflating the menu and handle its item's selection. On interactions, it opens a new activity
    // Settings.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,Settings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}