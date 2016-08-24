package metafire.stageready.dialogs.menu.sub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.Serializable;
import java.util.ArrayList;

import metafire.stageready.dialogs.menu.sub.sub_sub.SavedSetDialog;
import metafire.stageready.model.Set;
import metafire.stageready.model.SetManager;

/**
 * Created by Jessica on 6/27/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class ManageSavedSetsDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 5953601023735505700L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        ArrayList<String> setNames = new ArrayList<>();
        for (Set set : SetManager.getInstance().getSets()) {
            if (set.getName() != null) {
                setNames.add(set.getTotalTime() + " - " + set.getName());
            }
        }

        CharSequence[] setNamesArray = setNames.toArray(new CharSequence[setNames.size()]);

        if (setNamesArray.length == 0){
            builder.setMessage("No saved sets.");

        }
        else {

            builder.setItems(setNamesArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SavedSetDialog savedSetDialog = new SavedSetDialog();
                    savedSetDialog.setPosition(which);
                    savedSetDialog.show(getFragmentManager(), "SavedSetDialog");
                }

            });
        }

        final AlertDialog manageSavedSetsDialog = builder.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width * 0.85);
        manageSavedSetsDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        manageSavedSetsDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return manageSavedSetsDialog;
    }

    /**
     * Called when the dialog is stopped. Updates the set fragment's ListView.
     */

    @Override
    public void onDismiss(DialogInterface dialog) {
        SetManager.getInstance().getCurrentSet().getSetFragment().getArrayAdapter().notifyDataSetChanged();
        dismiss();
    }
}