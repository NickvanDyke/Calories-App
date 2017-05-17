package vandyke.caloriestoexercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import vandyke.caloriestoexercise.burnactivities.BaseBurnActivity;
import vandyke.caloriestoexercise.burnactivities.Running;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<BaseBurnActivity> burnActivities;

    public static String units;
    public static int weight;

    ArrayAdapter adapter;
    EditText calorieEntry;

    SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        units = prefs.getString("units", "imperial");
        weight = Integer.parseInt(prefs.getString("weight", "150"));

        // set the toolbar as this activity's actionbar
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        // create and add BaseBurnActivity
        burnActivities = new ArrayList<>();
        burnActivities.add(new Running());

        // set up listView stuff
        ListView listView = (ListView)findViewById(R.id.burnActivitiesList);
        adapter = new ArrayAdapter<>(this, R.layout.activity_listview_item, burnActivities);
        listView.setAdapter(adapter);

        // update listView stuff when the entered number changes
        calorieEntry = (EditText)findViewById(R.id.calorieEntry);
        calorieEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateRequiredMins();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // listen for changes to preferences
        listener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                        switch (key) {
                            case "units":
                                units = prefs.getString("units", "imperial");
                                updateUnits();
                                break;
                            case "weight":
                                weight = Integer.parseInt(prefs.getString("weight", "150"));
                                updateRequiredMins();
                                break;
                        }
                    }
                };
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    public void updateRequiredMins() {
        int numCals = 0;
        String cals = calorieEntry.getText().toString();
        if (!cals.equals(""))
            numCals = Integer.parseInt(cals);
        for (BaseBurnActivity activity : burnActivities)
            activity.calcRequiredMins(numCals);
        adapter.notifyDataSetChanged();
    }

    public void updateUnits() {
        for (BaseBurnActivity burn : burnActivities)
            burn.setUnits(units);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // launch settings activity
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actionbar, menu);
        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(listener);
    }
}
