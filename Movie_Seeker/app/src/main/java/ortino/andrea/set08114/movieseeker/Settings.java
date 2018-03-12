package ortino.andrea.set08114.movieseeker;

import android.preference.ListPreference;
import android.preference.Preference;

import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Settings extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        registerPreferences(findPreference("sortby"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        String value = o.toString();

        if(preference instanceof ListPreference)
        {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);

            if(index>=0)
            {
                preference.setSummary(listPreference.getEntries()[index]);
            }

        }
        else
        {
            preference.setSummary(value);
        }
        return true;
    }

    private void registerPreferences(Preference preference) {

        preference.setOnPreferenceChangeListener(this);

        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.
                getContext()).getString(preference.getKey(),""));

    }
}
