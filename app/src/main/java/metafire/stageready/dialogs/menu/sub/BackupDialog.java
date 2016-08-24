package metafire.stageready.dialogs.menu.sub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

public class BackupDialog extends DialogFragment implements Serializable {

    private static final long serialVersionUID = -6938743752510555117L;

    /**
     * Called when the dialog is first created.
     * @param savedInstanceState the saved instance state
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Backup to external storage? Most recent backup will be overwritten!");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (canWriteExternalStorage()) {
                    String filePath = Environment.getExternalStorageDirectory().getPath();

                    File file = new File(filePath, "Stage Ready");

                    if (!file.exists()){
                        if (!file.mkdirs()){
                            Toast toast = Toast.makeText(getActivity(), "Error creating directory.", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                    }

                    File setManagerBackupFile = new File(file.getAbsolutePath(), "SetManagerBackup.ser");
                    File scheduleBackupFile = new File(file.getAbsolutePath(), "ScheduleBackup.ser");

                    try {
                        FileOutputStream fileOutputStreamSetManager = new FileOutputStream(setManagerBackupFile);
                        ObjectOutputStream objectOutputStreamSetManager = new ObjectOutputStream(fileOutputStreamSetManager);
                        objectOutputStreamSetManager.writeObject(SetManager.getInstance());
                        objectOutputStreamSetManager.close();
                        fileOutputStreamSetManager.close();

                        FileOutputStream fileOutputStreamSchedule = new FileOutputStream(scheduleBackupFile);
                        ObjectOutputStream objectOutputStreamSchedule = new ObjectOutputStream(fileOutputStreamSchedule);
                        objectOutputStreamSchedule.writeObject(Schedule.getInstance());
                        objectOutputStreamSchedule.close();
                        fileOutputStreamSchedule.close();

                        Toast toast = Toast.makeText(getActivity(), "Backup successful.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    catch (IOException e) {
                        Toast toast = Toast.makeText(getActivity(), "Backup failed.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else{
                    Toast toast = Toast.makeText(getActivity(), "Backup failed. Cannot write to external storage.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BackupDialog.this.getDialog().cancel();
            }
        });

        final AlertDialog backupToExternalStorageDialog = builder.show();

        int buttonTextColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);

        Button positiveButton = backupToExternalStorageDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(buttonTextColor);

        Button negativeButton = backupToExternalStorageDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(buttonTextColor);
        backupToExternalStorageDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDialog = (int) Math.floor(width*0.85);

        backupToExternalStorageDialog.getWindow().setLayout(widthDialog, WindowManager.LayoutParams.WRAP_CONTENT);

        return backupToExternalStorageDialog;
    }

    /**
     * Checks if the external storage is present with read and write permissions.
     * @return true if can write to external storage, false otherwise
     */

    private static boolean canWriteExternalStorage() {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
    }
}