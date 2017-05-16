package vandyke.caloriestoexercise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
    }
}
