package vandyke.caloriestoexercise;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;

public class WeightDialog extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity mainActivity = (MainActivity)getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_weight, null);
        final EditText weightField = (EditText)view.findViewById(R.id.weightField);
        weightField.setText(Float.toString(mainActivity.weight));
        final Spinner unitsSpinner = (Spinner)view.findViewById(R.id.unitsSpinner);
        builder.setTitle("Weight")
                .setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor editor = mainActivity.prefs.edit();
                        String weightEntry = weightField.getText().toString();
                        editor.putFloat("weight", weightEntry.equals("") ? 0 : Float.parseFloat(weightEntry));
                        editor.putString("units", (String)unitsSpinner.getSelectedItem());
                        editor.apply();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog dialog = builder.create();
        if (weightField.requestFocus())
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return dialog;
    }

    public static void createAndShow(FragmentManager fragmentManager) {
        new WeightDialog().show(fragmentManager, "weight entry dialog");
    }
}
