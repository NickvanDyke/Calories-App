package vandyke.caloriestoexercise.burnactivities;

import vandyke.caloriestoexercise.MainActivity;

public class Running extends BaseBurnActivity {

    public Running() {
        MET = 9.8;
        setMessage();
    }

    public void setUnits(String units) {
        setMessage();
    }

    public void setMessage() {
        message = MainActivity.units.equals("imperial") ? "Run at 6 MPH (10min/mile) for %.2f minutes" : "Run at 9.656 KPH (6.213min/mile) for %.2f minutes";
    }
}
