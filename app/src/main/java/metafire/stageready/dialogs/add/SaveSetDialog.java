package metafire.stageready.dialogs.add;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import metafire.stageready.R;
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

public class SaveSetDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = 7849826992898812139L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.save_set_dialog, null);
        builder.setView(view);
        builder.setTitle("Save Set");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // overwritten below
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SaveSetDialog.this.getDialog().cancel();
            }
        });

        final AlertDialog saveSetDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        Button positiveButton = saveSetDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = saveSetDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        saveSetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        saveSetDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        saveSetDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean closeDialog = false;

                EditText setName = (EditText) view.findViewById(R.id.setNameEditText);

                final int defaultColor = setName.getCurrentHintTextColor();
                setName.setHintTextColor(defaultColor);

                String setNameString = setName.getText().toString().trim();

                int redColor = Color.rgb(229, 57, 53);

                if (setNameString.length() == 0) {
                    setName.setHintTextColor(redColor);
                    Toast toast = Toast.makeText(getActivity(), "Insert set name.", Toast.LENGTH_SHORT);
                    toast.show();

                }
                else if (duplicateSetName(setNameString)) {
                    Toast toast = Toast.makeText(getActivity(), "Set name in use.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Set setCopy = (Set) deepClone(SetManager.getInstance().getCurrentSet());
                    if (setCopy != null){
                        setCopy.setName(setNameString);
                        SetManager.getInstance().getSets().add(setCopy);
                    }

                    else {
                        Toast toast = Toast.makeText(getActivity(), "Error saving set.", Toast.LENGTH_SHORT);
                        toast.show();
                    }


                    closeDialog = true;
                }

                if(closeDialog) {
                    saveSetDialog.dismiss();
                }
            }
        });

        return saveSetDialog;
    }

    /**
     * Checks if the set name given already exists in the list of set names associated with the
     * SetManager instance.
     * @param setName the set name to check
     * @return true if the set name already exists, false otherwise
     */

    private static boolean duplicateSetName(String setName){
        boolean duplicate = false;
        for (Set set : SetManager.getInstance().getSets()) {
            if (set.getName() != null) {
                if (set.getName().equals(setName)) {
                    duplicate = true;
                }
            }
        }
        return duplicate;
    }

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