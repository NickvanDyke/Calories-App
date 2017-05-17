package vandyke.caloriestoexercise;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import static vandyke.caloriestoexercise.SettingsActivity.UNITS.IMPERIAL;

public class SettingsActivity extends AppCompatActivity {
    public enum UNITS {
        IMPERIAL, METRIC
    }
    public static UNITS units = IMPERIAL;
    public static int weight = 200;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        // enable up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
