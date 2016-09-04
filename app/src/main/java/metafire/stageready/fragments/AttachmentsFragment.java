package metafire.stageready.fragments;

import android.app.DialogFragment;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import metafire.stageready.R;
import metafire.stageready.activities.SongDetailsActivity;
import metafire.stageready.adapters.AttachmentsAdapter;
import metafire.stageready.dialogs.menu.AttachmentMenuDialog;
import metafire.stageready.libs.touch_image_view.TouchImageView;
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Song;

/**
 * Created by Jessica on 6/9/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class AttachmentsFragment extends Fragment implements Serializable {

    private static final long serialVersionUID = 6859054171883211925L;

    /**
     * Called when the fragment's view is first created.
     * @param inflater the layout inflater used to inflate the view
     * @param container the view group used to inflate the view
     * @param savedInstanceState the saved instance state to load the view of the fragment
     * @return the view of the fragment
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_attachments, container, false);

        Button importSheetMusicButton = (Button) rootView.findViewById(R.id.importSheetMusicButton);
        Button closeViewButton = (Button) rootView.findViewById(R.id.closeViewButton);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthButton = (int) Math.floor(width * 0.498);
        int widthColumn = (int) Math.floor(width * 0.5);

        importSheetMusicButton.setWidth(widthButton);
        importSheetMusicButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        closeViewButton.setWidth(widthButton);
        closeViewButton.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);

        SongDetailsActivity songDetailsActivity = (SongDetailsActivity) getActivity();

        if (songDetailsActivity.getMediaPlayer() != null || songDetailsActivity.getMediaRecorder() != null){
            importSheetMusicButton.setClickable(false);
            importSheetMusicButton.setEnabled(false);
        }

        setRetainInstance(true);

        final GridView attachmentsGridView =  (GridView) rootView.findViewById(R.id.attachmentsGridView);

        attachmentsGridView.setAdapter(new AttachmentsAdapter(getActivity()));

        attachmentsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();

                TouchImageView attachmentImageView = (TouchImageView) rootView.findViewById(R.id.attachmentImageView);

                TextView fileNotFoundTextView = (TextView) view.findViewById(R.id.fileNotFoundTextView);
                if (fileNotFoundTextView != null) {
                    Toast toast = Toast.makeText(getActivity(), "File not found.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                else {
                    attachmentImageView.setImageURI(Uri.parse(currentSong.getAttachments().get(position).getUri()));

                    attachmentImageView.setZoom(1);

                    attachmentImageView.setVisibility(View.VISIBLE);

                    Button closeViewButton = (Button) rootView.findViewById(R.id.closeViewButton);
                    closeViewButton.setClickable(true);
                    closeViewButton.setEnabled(true);
                }
            }
        });

        attachmentsGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TouchImageView attachmentImageView = (TouchImageView) rootView.findViewById(R.id.attachmentImageView);

                if (!(attachmentImageView.getVisibility() == View.VISIBLE)) {
                    Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
                    currentSong.setCurrentAttachmentPosition(position);
                    SongDetailsActivity songDetailsActivity = (SongDetailsActivity) getActivity();
                    DialogFragment attachmentDialog = new AttachmentMenuDialog();
                    attachmentDialog.show(songDetailsActivity.getFragmentManager(), "AttachmentMenuDialog");
                }
                return true;
            }
        });

        attachmentsGridView.setColumnWidth(widthColumn);
        return rootView;
    }
}