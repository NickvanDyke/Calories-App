package vandyke.caloriestoexercise;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // set xml-defined toolbar as the actionbar
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        // enable up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // display SettingsFragment as the main content
        getFragmentManager().beginTransaction().replace(R.id.preferences_frame, new SettingsFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // finish this activity, returning to the previous one
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_actionbar, menu);
        return true;
    }
}
