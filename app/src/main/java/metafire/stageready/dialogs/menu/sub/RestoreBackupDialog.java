package metafire.stageready.dialogs.menu.sub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.model.Schedule;
import metafire.stageready.model.SetManager;

/**
 * Created by Jessica on 7/10/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class RestoreBackupDialog extends DialogFragment implements Serializable{

    private static final long serialVersionUID = -1516803218979010464L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Restore backup from external storage? All current app data will be overwritten! Application will restart.");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "Stage Ready";
                    FileInputStream fileInputStreamSetManager = new FileInputStream(path + "/SetManagerBackup.ser");
                    ObjectInputStream objectInputStreamSetManager = new ObjectInputStream(fileInputStreamSetManager);
                    SetManager setManager = (SetManager) objectInputStreamSetManager.readObject();
                    objectInputStreamSetManager.close();
                    SetManager.getInstance().loadSetManager(setManager);
                    SetManager.saveToFile(getActivity());
                    fileInputStreamSetManager.close();

                    FileInputStream fileInputStreamSchedule = new FileInputStream(path + "/ScheduleBackup.ser");
                    ObjectInputStream objectInputStreamSchedule = new ObjectInputStream(fileInputStreamSchedule);
                    Schedule schedule = (Schedule) objectInputStreamSchedule.readObject();
                    objectInputStreamSchedule.close();
                    Schedule.getInstance().loadSchedule(schedule);
                    Schedule.saveToFile(getActivity());
                    fileInputStreamSchedule.close();
                    Intent i = getActivity().getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    Toast toast = Toast.makeText(getActivity(), "Backup restore successful.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                catch (FileNotFoundException e) {
                    Toast toast = Toast.makeText(getActivity(), "Backup restore failed. No backup found in external storage.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                catch (IOException | ClassNotFoundException e) {
                    Toast toast = Toast.makeText(getActivity(), "Backup restore failed.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                RestoreBackupDialog.this.getDialog().cancel();
            }
        });

        final AlertDialog restoreBackupFromExternalStorageDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        Button positiveButton = restoreBackupFromExternalStorageDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = restoreBackupFromExternalStorageDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        restoreBackupFromExternalStorageDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        restoreBackupFromExternalStorageDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return restoreBackupFromExternalStorageDialog;
    }
}