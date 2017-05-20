package vandyke.caloriestoexercise;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.w3c.dom.Text;
import vandyke.caloriestoexercise.burnactivities.BurnActivity;

import java.util.ArrayList;

public class BurnActivityAdapter extends ArrayAdapter<BurnActivity> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<BurnActivity> data;

    public BurnActivityAdapter(Context context, int layoutResourceId, ArrayList<BurnActivity> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        BurnActivityHolder holder;
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new BurnActivityHolder();
            holder.activityName = (TextView)row.findViewById(R.id.activityName);
            holder.activityMinutes = (TextView)row.findViewById(R.id.activityMinutes);

            row.setTag(holder);
        } else {
            holder = (BurnActivityHolder)row.getTag();
        }

        BurnActivity burnActivity = data.get(position);
        holder.activityName.setText(burnActivity.name);
        if (MainActivity.enterCalories) {
            double minutes = burnActivity.calcRequiredMins(MainActivity.entryFieldValue);
            int minutesPart = (int) minutes;
            holder.activityMinutes.setText(String.format("%dm %ds", minutesPart, (int) (60 * (minutes - minutesPart))));
        } else {
            holder.activityMinutes.setText(String.format("%.1f", burnActivity.calcBurnedCalories(MainActivity.entryFieldValue)));
        }

        return row;
    }

    public ArrayList<BurnActivity> getData() {
        return data;
    }

    static class BurnActivityHolder {
        TextView activityName;
        TextView activityMinutes;
    }
}
