package vandyke.caloriestoexercise;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BurnActivityAdapter extends ArrayAdapter<BurnActivity> {

    private MainActivity mainActivity;
    private int layoutResourceId;
    private ArrayList<BurnActivity> data;

    private String currentSearch;

    public BurnActivityAdapter(Context context, int layoutResourceId, ArrayList<BurnActivity> data) {
        super(context, layoutResourceId, data);
        this.mainActivity = (MainActivity) context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        currentSearch = "";
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        BurnActivityHolder holder;
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = mainActivity.getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new BurnActivityHolder();
            holder.activityName = (TextView) row.findViewById(R.id.activityName);
            holder.activityValue = (TextView) row.findViewById(R.id.activityValue);

            row.setTag(holder);
        } else {
            holder = (BurnActivityHolder) row.getTag();
        }

        BurnActivity burnActivity = data.get(position);
        holder.activityName.setText(burnActivity.name);
        if (mainActivity.enterCalories) {
            double minutes = burnActivity.calcRequiredMins(mainActivity.weightinKg, mainActivity.entryFieldValue);
            int minutesPart = (int) minutes;
            holder.activityValue.setText(String.format("%dm %02ds", minutesPart, (int) (60 * (minutes - minutesPart))));
        } else {
            holder.activityValue.setText(String.format("%.1f", burnActivity.calcBurnedCalories(mainActivity.weightinKg, mainActivity.entryFieldValue)));
        }

        return row;
    }

    public void filter(String search) {
        if (search.length() < currentSearch.length())
            data = new ArrayList<>(mainActivity.getListData());
        if (search.equals(""))
            notifyDataSetChanged();
        else
            for (int i = 0; i < data.size(); i++)
                if (!data.get(i).name.contains(search)) {
                    data.remove(i);
                    i--;
                }
        currentSearch = search;
        notifyDataSetChanged();
    }

    public void filter() {
        filter(currentSearch);
    }

    public ArrayList<BurnActivity> getData() {
        return data;
    }

    public void setData(ArrayList<BurnActivity> data) {
        this.data = new ArrayList<>(data);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    static class BurnActivityHolder {
        TextView activityName;
        TextView activityValue;
    }
}
