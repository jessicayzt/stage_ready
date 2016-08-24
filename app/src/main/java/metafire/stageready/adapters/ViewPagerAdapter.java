package metafire.stageready.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jessica on 5/24/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class ViewPagerAdapter extends FragmentPagerAdapter implements Serializable {

    private static final long serialVersionUID = -490951702683964666L;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tabTitles = new ArrayList<>();

    /**
     * Constructs a ViewPagerAdapter with the given FragmentManager
     * @param fragmentManager the fragmentManager with which to construct the adapter
     */

    public ViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    /**
     * Adds a fragment to the adapter.
     * @param fragment the fragment to add
     * @param title the title of the fragment to add
     */

    public void addFragment(Fragment fragment, String title){
        this.fragments.add(fragment);
        this.tabTitles.add(title);
    }

    /**
     * Returns the fragment with the given position.
     * @param position the position with which to return the fragment
     * @return the fragment at the given position
     */

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /**
     * Returns the number of fragments.
     * @return the number of fragments
     */

    @Override
    public int getCount() {
        return fragments.size();
    }

    /**
     * Returns the title at the given position.
     * @param position the position with which to return the title
     * @return the title at the given position as a CharsSequence
     */

    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles.get(position);
    }
}
