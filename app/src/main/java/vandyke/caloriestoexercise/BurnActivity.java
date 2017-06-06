package vandyke.caloriestoexercise;

import vandyke.caloriestoexercise.MainActivity;

public class BurnActivity {
    /** the metabolic equivalent of task for this BurnActivity
     *  many MET values are taken from https://sites.google.com/site/compendiumofphysicalactivities/home */
    public double MET;
    /** the name displayed for this BurnActivity in listView */
    public String name;

    public BurnActivity(String name, double MET) {
        this.name = name;
        this.MET = MET;
    }

    /**
     * calculates how many minutes this activity must be performed for, using the appropriate equation,
     * and sets the requiredMins to it
     * @param desiredCaloriesBurned the number of calories to be burned
     */
    public double calcRequiredMins(double desiredCaloriesBurned) {
        return desiredCaloriesBurned / (MET * 3.5 * MainActivity.weightinKg / 200);
    }

    /**
     * calculates how many calories were burned from performing this activity for the given minutes, and then
     * sets
     * @param minutes the number of minutes the activity was performed for
     */
    public double calcBurnedCalories(double minutes) {
        return minutes * (MET * 3.5 * MainActivity.weightinKg / 200);
    }
}