package metafire.stageready.dialogs.menu.sub.sub_sub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.dialogs.menu.sub.delete_or_clear.DeleteSavedSetDialog;
import metafire.stageready.dialogs.menu.sub.edit.EditSavedSetDialog;
import metafire.stageready.model.Break;
import metafire.stageready.model.Set;
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Slot;
import metafire.stageready.model.Song;

/**
 * Created by Jessica on 6/27/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class SavedSetDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = -575146930055380119L;
    private int position;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(R.array.saved_set_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Set setCopy = (Set) deepClone(SetManager.getInstance().getSets().get(position));
                        SetManager.getInstance().loadSetFromSavedSet(setCopy);

                        SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().clear();
                        if (setCopy != null) {

                            for (Slot slot : setCopy.getSlots()) {
                                if (slot instanceof Song) {
                                    Song song = (Song) slot;
                                    SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().add(song.getTitle());
                                } else {
                                    Break breakObject = (Break) slot;
                                    SetManager.getInstance().getCurrentSet().getSetFragment().getTitles().add(breakObject.getTitle());
                                }
                            }

                            SetManager.getInstance().getCurrentSet().getSetFragment().getArrayAdapter().notifyDataSetChanged();
                            SetManager.getInstance().getCurrentSet().updateStatsTextView();
                        }
                        else {
                            Toast toast = Toast.makeText(getActivity(), "Error loading set.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        break;
                    case 1:
                        EditSavedSetDialog editSavedSetDialog = new EditSavedSetDialog();
                        editSavedSetDialog.setPosition(position);
                        editSavedSetDialog.show(getFragmentManager(), "EditSavedSetDialog");
                        break;
                    case 2:
                        DeleteSavedSetDialog deleteSetDialog = new DeleteSavedSetDialog();
                        deleteSetDialog.setPosition(position);
                        deleteSetDialog.show(getFragmentManager(), "DeleteSavedSetDialog");
                        break;
                }
            }
        });

        final AlertDialog savedSetDialog = builder.show();

        savedSetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        savedSetDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return savedSetDialog;

    }

    /**
     * Called when the dialog is dismissed, updates the ListView on the set fragment.
     * @param dialog the dialog dismissed
     */

    @Override
    public void onDismiss(DialogInterface dialog) {
        SetManager.getInstance().getCurrentSet().getSetFragment().getArrayAdapter().notifyDataSetChanged();
        dismiss();
    }

    /**
     * Sets the position.
     * @param position the position to set
     */

    public void setPosition(int position){
        this.position = position;
    }

    /**
     * This method makes a "deep clone" of any Java object it is given.
     */

    /**
     * Returns a deep clone of any object it is given.
     * @param object the object to be cloned
     * @return a deep clone of the given object
     */

    private static Object deepClone(Object object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        }
        catch (Exception e) {
            return null;
        }
    }
}