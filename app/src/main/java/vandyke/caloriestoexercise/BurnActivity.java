package vandyke.caloriestoexercise;

public class BurnActivity implements Comparable {
    /** the name displayed for this BurnActivity in listView */
    public String name;
    /** the metabolic equivalent of task for this BurnActivity
     *  many MET values are taken from https://sites.google.com/site/compendiumofphysicalactivities/home */
    public double MET;

    public BurnActivity(String name, double MET) {
        this.name = name;
        this.MET = MET;
    }

    /**
     * calculates how many minutes this activity must be performed for, using the appropriate equation
     * @param weightInKg weight in Kilograms
     * @param desiredCaloriesBurned the number of calories to be burned
     */
    public double calcRequiredMins(float weightInKg, float desiredCaloriesBurned) {
        return desiredCaloriesBurned / (MET * 3.5 * weightInKg / 200);
    }

    /**
     * calculates how many calories were burned from performing this activity for the given minutes
     * @param weightInKg weight in Kilograms
     * @param minutes the number of minutes the activity was performed for
     */
    public double calcBurnedCalories(float weightInKg, float minutes) {
        return minutes * (MET * 3.5 * weightInKg / 200);
    }

    public int compareTo(Object o) {
        return name.compareTo(((BurnActivity)o).name);
    }
}