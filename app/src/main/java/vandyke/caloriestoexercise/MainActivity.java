package vandyke.caloriestoexercise;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.support.v7.widget.SearchView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    private HashMap<String, ArrayList<BurnActivity>> categoriesMap;

    public static String units;
    public static float weight;
    public static float weightinKg;

    public static float entryFieldValue;
    public static boolean enterCalories;

    private BurnActivityAdapter listAdapter;
    private EditText entryField;
    private ArrayList<BurnActivity> allActivites;

    public static SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterCalories = true;

        // load preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        units = prefs.getString("units", "Kilograms");
        weight = prefs.getFloat("weight", 200);
        setWeightInKg();

        // set the toolbar as this activity's actionbar
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        // parse the JSON and add BurnActivities
        try {
            categoriesMap = new HashMap<>();
            JSONObject json = new JSONObject(loadJSONFromRaw(R.raw.burn_activites));
            Iterator<String> categoryIter = json.keys();
            while (categoryIter.hasNext()) {
                ArrayList<BurnActivity> category = new ArrayList<>();
                String categoryName = categoryIter.next();
                JSONObject categoryJSON = json.getJSONObject(categoryName);
                Iterator<String> nameIter = categoryJSON.keys();
                while (nameIter.hasNext()) {
                    String name = nameIter.next();
                    category.add(new BurnActivity(name, categoryJSON.getDouble(name)));
                }
                categoriesMap.put(categoryName, category);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // set up listView stuff
        ListView listView = (ListView)findViewById(R.id.burnActivitiesList);
        listAdapter = new BurnActivityAdapter(this, R.layout.burnactivity_listview_item, categoriesMap.get("bicycling"));
        listView.setAdapter(listAdapter);

        // set up spinner stuff
        Spinner categorySpinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.categories_entries, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("hi");
                if (view == null) {
                    System.out.println("VIEW WAS NULL");
                    return; // TODO: not sure why view is sometimes null upon restart... try to figure out I guess. But this fixes crashes for now
                }
                String selected = ((TextView)view).getText().toString().toLowerCase().replace("&", "and");
                if (selected.equals("all")) {
                    if (allActivites == null) { // TODO: maybe optimize this more, idk
                        allActivites = new ArrayList<>();
                        for (ArrayList<BurnActivity> list : categoriesMap.values())
                            allActivites.addAll(list);
                        Collections.sort(allActivites);
                    }
                    listAdapter.setData(allActivites);
                } else
                    listAdapter.setData(categoriesMap.get(selected));
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // update listView stuff when the entered number changes
        entryField = (EditText)findViewById(R.id.entryField);
        entryField.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cals = entryField.getText().toString();
                entryFieldValue = cals.equals("") ? 0 : Float.parseFloat(cals);
                listAdapter.notifyDataSetChanged();
            }

            public void afterTextChanged(Editable s) {

            }
        });
        entryField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        // change text and list when button is pressed to change from entering calories to minutes
        ToggleButton button = (ToggleButton)findViewById(R.id.toggleButton);
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    entryField.setHint("minutes");
                    ((TextView)findViewById(R.id.rightLabel)).setText("Calories");
                    enterCalories = false;
                } else {
                    entryField.setHint("calories");
                    ((TextView)findViewById(R.id.rightLabel)).setText("Time");
                    enterCalories = true;
                }
                listAdapter.notifyDataSetChanged();
            }
        });

        // listen for changes to preferences
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                        switch (key) {
                            case "units":
                                units = prefs.getString("units", "Kilograms");
                                setWeightInKg();
                                listAdapter.notifyDataSetChanged();
                                break;
                            case "weight":
                                weight = prefs.getFloat("weight", 200);
                                setWeightInKg();
                                listAdapter.notifyDataSetChanged();
                                break;
                        }
                    }
                };
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    public void setWeightInKg() {
        weightinKg = units.equals("Kilograms") ? weight * 0.45359237f : weight;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                WeightDialog.createAndShow(getSupportFragmentManager());
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the actionbar with the stuff in the xml file for it
        getMenuInflater().inflate(R.menu.main_actionbar, menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            public boolean onClose() {
                return false;
            }
        });
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
