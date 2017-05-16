package vandyke.caloriestoexercise.burnactivities;

public class Running extends BaseBurnActivity {

    public Running() {
        MET = 9.8;
    }

    @Override
    public String toString() {
        return String.format("Run at 6 mph for %.2f minutes", requiredMins);
    }
}
