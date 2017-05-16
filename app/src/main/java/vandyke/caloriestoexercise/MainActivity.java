package vandyke.caloriestoexercise;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create and add BaseBurnActivity
        burnActivities = new ArrayList<>();
        burnActivities.add(new Running());

        // set up listView stuff
        final ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_listview_item, burnActivities);
        final ListView listView = (ListView)findViewById(R.id.burnActivitiesList);
        listView.setAdapter(adapter);

        // update listView stuff when the entered number changes
        final EditText calorieEntry = (EditText)findViewById(R.id.calorieEntry);
        calorieEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int numCals;
                String cals = calorieEntry.getText().toString();
                if (cals.equals(""))
                    numCals = 0;
                else
                    numCals = Integer.parseInt(cals);
                for (BaseBurnActivity activity : burnActivities)
                    activity.calcRequiredMins(numCals);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    public void openSettings(View view) {

    }
}
