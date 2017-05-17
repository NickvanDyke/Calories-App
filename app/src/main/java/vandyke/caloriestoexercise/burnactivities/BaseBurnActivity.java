package vandyke.caloriestoexercise.burnactivities;

import vandyke.caloriestoexercise.MainActivity;

public abstract class BaseBurnActivity {
    /** the metabolic equivalent of task for this BurnActivity
     *  many MET values are taken from https://sites.google.com/site/compendiumofphysicalactivities/home */
    protected double MET;
    /** the minutes this activity must be done for to burn the desired number of calories */
    protected double requiredMins;
    /** the message displayed for this BurnActivity in listView */
    protected String message;


    /**
     * calculates how many minutes this activity must be performed for, using the appropriate equation,
     * and sets the requiredMins to it
     * @param desiredCaloriesBurned the number of calories to be burned
     */
    public void calcRequiredMins(int desiredCaloriesBurned) {
        requiredMins = desiredCaloriesBurned / (MET * 3.5 * MainActivity.weight / 200);
        System.out.println(MainActivity.weight);
    }

    /** called when units are set
     *
     * @param units the type of units to set to (i.e. imperial or metric)
     */
    public void setUnits(String units) {

    }

    public String toString() {
        return String.format(message, requiredMins);
    }
}