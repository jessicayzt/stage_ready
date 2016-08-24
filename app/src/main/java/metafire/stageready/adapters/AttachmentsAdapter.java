package metafire.stageready.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;

import metafire.stageready.R;
import metafire.stageready.model.Attachment;
import metafire.stageready.model.SetManager;
import metafire.stageready.model.Song;


/**
 * Created by Jessica on 6/14/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class AttachmentsAdapter extends BaseAdapter implements Serializable {

    private static final long serialVersionUID = -4142388286403545952L;
    private ArrayList<Attachment> attachments;
    private Context context;

    /**
     * Constructs an AttachmentsAdapter with the attachments associated with the current song and
     * the given application context.
     * @param applicationContext the application context
     */

    public AttachmentsAdapter(Context applicationContext){
        Song currentSong = (Song) SetManager.getInstance().getCurrentSet().getCurrentlyModifying();
        attachments = currentSong.getAttachments();
        context = applicationContext;
    }

    /**
     * Returns the size of the list of attachments.
     * @return the size of the attachments list
     */

    @Override
    public int getCount() {
        return attachments.size();
    }

    /**
     * Returns the object (an attachment) at the given position.
     * @param position the position to retrieve the attachment
     * @return the object (an attachment) at the given position
     */

    @Override
    public Object getItem(int position) {
        return attachments.get(position);
    }

    /**
     * Returns the item Id (always 0, as item Id is not used).
     * @param position the position to retrieve the item Id
     * @return always 0
     */

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Returns the view with the given position, view, and ViewGroup.
     * @param position the position to retrieve the view
     * @param convertView the convertView
     * @param parent the parent ViewGroup
     * @return the view given the position, view, and ViewGroup given
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView thumbnail = new ImageView(context);
        View fileNotFoundView;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthThumbnail = (int) Math.floor(width*0.45);
        thumbnail.setLayoutParams(new GridView.LayoutParams(widthThumbnail, widthThumbnail));
        thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (!positionExists(position)) {
            return null;
        }

        else {
            try {
                thumbnail.setImageBitmap(uriToBitmap(Uri.parse(attachments.get(position).getUri())));
                return thumbnail;
            }
            catch (FileNotFoundException e) {
                // thumbnail will be set to file_not_found view below
            }
        }

        File file = new File(Uri.parse(attachments.get(position).getUri()).getPath());
        if (!file.exists()) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            fileNotFoundView =  layoutInflater.inflate(R.layout.file_not_found, null);
            fileNotFoundView.setLayoutParams(new ViewGroup.LayoutParams(widthThumbnail, widthThumbnail)); // height should be the same as the width
            return fileNotFoundView;
        }

        else {
            return thumbnail;
        }
    }

    /**
     * Checks if the given position exists.
     * @param position the position to check
     * @return true if the position exists, false otherwise
     */

    private boolean positionExists(int position) {
        try {
            attachments.get(position);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    private Bitmap uriToBitmap(Uri uri) throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        
        final int REQUIRED_SIZE = 140; // Something tiny is fine, it's only a thumbnail
        // Find the correct scale value
        int width = options.outWidth, height = options.outHeight;
        int scale = 1;
        while (true) {
            if (width / 2 < REQUIRED_SIZE
                    || height / 2 < REQUIRED_SIZE) {
                break;
            }
            width /= 2;
            height /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize
        BitmapFactory.Options newOptions = new BitmapFactory.Options();
        newOptions.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, newOptions);
    }
}
