package ortino.andrea.set08114.movieseeker;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MoviesDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.containerDetails, new MoviesDetailsFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void favorite(View view) {
        Button btn = (Button) findViewById(R.id.btnFav);
        if (btn.getText().equals("FAVORITE")) {
            //code to store movie data in database
            btn.setText("REMOVE");
            btn.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

            ContentValues values = new ContentValues();
            values.put(StoreDB.NAME,MoviesDetailsFragment.box);
            values.put(StoreDB.ABOUT,MoviesDetailsFragment.about);
            values.put(StoreDB.VOTE,MoviesDetailsFragment.vote);
            values.put(StoreDB.DATE,MoviesDetailsFragment.date);
            values.put(StoreDB.TITLE,MoviesDetailsFragment.title);

            getContentResolver().insert(StoreDB.CONTENT_URI, values);

        } else {
            btn.setText("FAVORITE");
            btn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            getContentResolver().delete(Uri.parse("content://ortino.andrea.set08114.movieseeker.provider.Movies/movies"),
                    "title=?",new String[]{MoviesDetailsFragment.title});
        }
    }
}
