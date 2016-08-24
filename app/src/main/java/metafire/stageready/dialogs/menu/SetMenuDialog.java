package metafire.stageready.dialogs.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.dialogs.add.SaveSetDialog;
import metafire.stageready.dialogs.menu.sub.ManageSavedSetsDialog;
import metafire.stageready.dialogs.menu.sub.delete_or_clear.NewSetDialog;
import metafire.stageready.model.SetManager;

/**
 * Created by Jessica on 6/27/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class SetMenuDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 6700075258338006377L;

    /**
     * Called when the dialog is first created.
     * @param  savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(R.array.set_menu_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        DialogFragment newSetDialog = new NewSetDialog();
                        newSetDialog.show(getFragmentManager(), "NewSetDialog");
                        break;
                    case 1:
                        if (SetManager.getInstance().getSets().size() <= 6){
                            DialogFragment saveSetDialog = new SaveSetDialog();
                            saveSetDialog.show(getFragmentManager(), "SaveSetDialog");
                        }
                        else {
                            Toast toast = Toast.makeText(getActivity(), "Maximum number of sets reached.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        break;
                    case 2:
                        DialogFragment savedSetsDialog = new ManageSavedSetsDialog();
                        savedSetsDialog.show(getFragmentManager(), "ManageSavedSetsDialog");
                        break;
                }
            }
        });
        final AlertDialog menuDialog =  builder.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);
        menuDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        menuDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return menuDialog;
    }

    /**
     * Called when the dialog is dismissed. Updates the ListView in the set fragment.
     * @param dialog the dialog interface
     */

    @Override
    public void onDismiss(DialogInterface dialog) {
        SetManager.getInstance().getCurrentSet().getSetFragment().getArrayAdapter().notifyDataSetChanged();
        dismiss();
    }
}