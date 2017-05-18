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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vandyke.caloriestoexercise.burnactivities.BurnActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private ArrayList<BurnActivity> burnActivities;

    public static String units;
    public static double weight;
    public static double weightinKg;

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
        weight = Double.parseDouble(prefs.getString("weight", "200"));
        weightinKg = units.equals("imperial") ? weight * 0.45359237 : weight;

        // set the toolbar as this activity's actionbar
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        // parse the JSON and add BurnActivities
        JSONObject json = null;
        try {
            json = new JSONObject(loadJSONFromRaw(R.raw.burn_activites));
            burnActivities = new ArrayList<>();
            Iterator<String> iter = json.keys();
            while (iter.hasNext()) {
                JSONObject burnActivity = json.getJSONObject(iter.next());
                burnActivities.add(new BurnActivity(burnActivity.getInt("MET"), burnActivity.getString("displayString"), burnActivity.getString("displayStringMetric")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(json);


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
                                if (weight != weightinKg) // units were previously set to imperial and are now metric
                                    weightinKg = weight;
                                else // units were previously metric and are now imperial
                                    weightinKg = weight * 0.45359237;
                                updateUnits();
                                break;
                            case "weight":
                                weight = Double.parseDouble(prefs.getString("weight", "200"));
                                weightinKg = units.equals("imperial") ? weight * 0.45359237 : weight;
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
        for (BurnActivity activity : burnActivities)
            activity.calcRequiredMins(numCals);
        adapter.notifyDataSetChanged();
    }

    public void updateUnits() {
        for (BurnActivity burn : burnActivities)
            burn.setUnits(units);
        updateRequiredMins();
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

    public String loadJSONFromRaw(int resourceId) {
        String json;
        try {
            InputStream inputStream = getResources().openRawResource(resourceId);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
