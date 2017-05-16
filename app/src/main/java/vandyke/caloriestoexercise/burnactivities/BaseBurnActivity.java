package vandyke.caloriestoexercise.burnactivities;

import vandyke.caloriestoexercise.MainActivity;

public abstract class BaseBurnActivity {
    /** the metabolic equivalent of task for this BurnActivity
     *  many MET values are taken from https://sites.google.com/site/compendiumofphysicalactivities/home
     */
    protected double MET;
    protected double requiredMins; // the minutes this activity must be done for to burn the desired number of cals

    public void update(int desiredCaloriesBurned) {
        requiredMins = desiredCaloriesBurned / (MET * 3.5 * MainActivity.weight / 200);
    }

    public abstract String toString();
}