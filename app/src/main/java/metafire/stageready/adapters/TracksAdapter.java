package metafire.stageready.adapters;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.List;

import metafire.stageready.activities.SongDetailsActivity;
import metafire.stageready.dialogs.menu.TrackMenuDialog;
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Song;

/**
 * Created by Jessica on 6/17/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class TracksAdapter extends ArrayAdapter<String> implements Serializable {

    private static final long serialVersionUID = 5798838767562695672L;
    private SongDetailsActivity songDetailsActivity;

    /**
     * Creates a TracksAdapter with given context, resource, text view resource Id, and list of strings
     * @param context the context to construct the object
     * @param objects the list of strings with which to construct the adapter
     */

    public TracksAdapter(Context context, List<String> objects) {
        super(context, metafire.stageready.R.layout.list_title, metafire.stageready.R.id.title, objects);
        this.songDetailsActivity = (SongDetailsActivity) context;
    }

    /**
     * Returns the view with the given position, view, and ViewGroup.
     * @param position the position to retrieve the view
     * @param convertView the convertView
     * @param parent the parent ViewGroup
     * @return the view given the position, view, and ViewGroup given
     */

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);
        int trackColor = Color.rgb(245, 245, 245);

        final int selectedColor = Color.argb(115, 178, 235, 242);
        final Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();

        view.setBackgroundColor(trackColor);
        if (position == currentSong.getCurrentTrackPosition()) {
            view.setBackgroundColor(selectedColor);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songDetailsActivity.onClickTrack(position);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (songDetailsActivity.getMediaPlayer() == null || (songDetailsActivity.getMediaPlayer() != null && !songDetailsActivity.getMediaPlayer().isPlaying()) ) {
                    Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
                    currentSong.setCurrentTrackPosition(position);
                    notifyDataSetChanged();
                    DialogFragment trackDetailsDialog = new TrackMenuDialog();
                    trackDetailsDialog.show(songDetailsActivity.getFragmentManager(), "TrackMenuDialog");
                }
                return true;
            }
        });

        return view;
    }
}
