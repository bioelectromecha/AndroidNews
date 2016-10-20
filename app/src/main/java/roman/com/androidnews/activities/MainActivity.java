package roman.com.androidnews.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import roman.com.androidnews.R;
import roman.com.androidnews.fragments.NewsFragment;

/**
 * the main acitivty for the
 */
public class MainActivity extends AppCompatActivity {


    // a key to get the fragment id from savedInstanceState
    private static final String KEY_FRAGMENT = "FRAGMENT";
    //holds the current fragment
    private Fragment mForegroundFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // if a fragment does not exist
            mForegroundFragment = new NewsFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, mForegroundFragment);
            fragmentTransaction.commit();
        } else {
            //get the fragment if it already exists
            mForegroundFragment = getSupportFragmentManager().findFragmentById(savedInstanceState.getInt(KEY_FRAGMENT));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the current fragment id - so it can be reused when activity is recreated
        outState.putInt(KEY_FRAGMENT, mForegroundFragment.getId());
    }
}
