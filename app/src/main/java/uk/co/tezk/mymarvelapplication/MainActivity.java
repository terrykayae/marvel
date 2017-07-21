package uk.co.tezk.mymarvelapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import uk.co.tezk.mymarvelapplication.view.ComicListFragment;

/**
 * Our main activity that holds the fragments
 */

public class MainActivity extends AppCompatActivity {

    private ComicListFragment mComicListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Only need to inflate the Fragment the first time we're run
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            mComicListFragment = new ComicListFragment();
            transaction.add(R.id.fragmentHolder, new ComicListFragment());
            transaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_set_budget) {
            // Show dialog to get budget

            // Then filter
            mComicListFragment.filterOnPrice(0.00);
            return true;
        } else if (id == R.id.action_clear_budget) {
            mComicListFragment.clearFilter();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
