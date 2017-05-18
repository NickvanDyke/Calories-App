package vandyke.caloriestoexercise.burnactivities;

import vandyke.caloriestoexercise.MainActivity;

public class BurnActivity {
    /** the metabolic equivalent of task for this BurnActivity
     *  many MET values are taken from https://sites.google.com/site/compendiumofphysicalactivities/home */
    protected double MET;
    /** the minutes this activity must be done for to burn the desired number of calories */
    protected double requiredMins;
    /** the displayString displayed for this BurnActivity in listView */
    protected String displayString;
    protected String displayStringMetric;

    public BurnActivity(double MET, String displayString, String displayStringMetric) {
        this.MET = MET;
        this.displayString = displayString;
        this.displayStringMetric = displayStringMetric;
    }

    /**
     * calculates how many minutes this activity must be performed for, using the appropriate equation,
     * and sets the requiredMins to it. Note that weight should be in kg
     * @param desiredCaloriesBurned the number of calories to be burned
     */
    public void calcRequiredMins(int desiredCaloriesBurned) {
        requiredMins = desiredCaloriesBurned / (MET * 3.5 * MainActivity.weightinKg / 200);
    }

    /** called when units are set
     *
     * @param units the type of units to set to (i.e. imperial or metric)
     */
    public void setUnits(String units) {

    }

    public String toString() {
        return String.format(MainActivity.units.equals("imperial") ? displayString : displayStringMetric, requiredMins);
    }
}