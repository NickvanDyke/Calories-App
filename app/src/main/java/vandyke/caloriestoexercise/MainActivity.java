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
import android.widget.*;
import org.json.JSONException;
import org.json.JSONObject;
import vandyke.caloriestoexercise.burnactivities.BurnActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private HashMap<String, ArrayList<BurnActivity>> categoriesMap;

    public static String units;
    public static double weight;
    public static double weightinKg;

    BurnActivityAdapter listAdapter;
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
            categoriesMap = new HashMap<>();
            json = new JSONObject(loadJSONFromRaw(R.raw.burn_activites));
            Iterator<String> categoryIter = json.keys();
            while (categoryIter.hasNext()) {
                ArrayList<BurnActivity> category = new ArrayList<>();
                String categoryName = categoryIter.next();
                JSONObject categoryJSON = json.getJSONObject(categoryName);
                Iterator<String> nameIter = categoryJSON.keys();
                while (nameIter.hasNext()) {
                    String name = nameIter.next();
                    category.add(new BurnActivity(name, categoryJSON.getInt(name)));
                }
                categoriesMap.put(categoryName, category);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // set up listView stuff
        ListView listView = (ListView)findViewById(R.id.burnActivitiesList);
        listAdapter = new BurnActivityAdapter(this, R.layout.activity_listview_item, categoriesMap.values().iterator().next());
        listView.setAdapter(listAdapter);

        // set up spinner stuff
        Spinner categorySpinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.categories_entries, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listAdapter.clear();
                listAdapter.addAll(categoriesMap.get(((TextView)view).getText().toString().toLowerCase().replace("&", "and")));
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                                setWeightInKg();
                                updateRequiredMins();
                                break;
                            case "weight":
                                weight = Double.parseDouble(prefs.getString("weight", "200"));
                                setWeightInKg();
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
        for (BurnActivity activity : listAdapter.getData())
            activity.calcRequiredMins(numCals);
        listAdapter.notifyDataSetChanged();
    }

    public void setWeightInKg() {
        weightinKg = units.equals("imperial") ? weight * 0.45359237 : weight;

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
