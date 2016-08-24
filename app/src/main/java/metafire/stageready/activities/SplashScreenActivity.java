package metafire.stageready.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

/**
 * Created by Jessica on 6/17/2016.
 */

/**
 * @author  Jessica Yang <jessicayzt@hotmail.com>
 * @version 1.0
 * @since   1.0
 */

public class SplashScreenActivity extends AppCompatActivity implements Serializable {

    private static final long serialVersionUID = -5034508389277714215L;

    /**
     * Called when the activity is first created. Automatically forwards user to main activity once
     * it has finished loading.
     * @param savedInstanceState the saved instance state
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
