package metafire.stageready.data_structs;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jessica on 7/7/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class CircularArrayList<String> extends ArrayList<String> implements Serializable {

    private static final long serialVersionUID = 7292912809840320761L;

    /**
     * Creates a circular array list by overwriting the get method with this implementation.
     * @param index the index to get the string
     */

    @Override
    public String get(int index) {
        if(index < 0)
            index = size() + index;
        if (index > size() - 1){
            index = index - size();
        }

        return super.get(index);
    }
}
