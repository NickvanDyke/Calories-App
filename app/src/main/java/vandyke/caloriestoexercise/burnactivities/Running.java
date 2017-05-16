package vandyke.caloriestoexercise.burnactivities;

import vandyke.caloriestoexercise.SettingsActivity;

import static vandyke.caloriestoexercise.SettingsActivity.UNITS.IMPERIAL;

public class Running extends BaseBurnActivity {

    public Running() {
        MET = 9.8;
        setMessage();
    }

    public void notifyUnitsChange() {
        setMessage();
    }

    public void setMessage() {
        message = SettingsActivity.units == IMPERIAL ? "Run at 6 MPH (10min/mile) for %.2f minutes" : "Run at 9.656 KPH (6.213min/mile) for %.2f minutes";
    }
}
