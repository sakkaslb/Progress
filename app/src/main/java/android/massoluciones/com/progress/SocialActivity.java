package android.massoluciones.com.progress;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Created by Stefanie on 17/10/2015.
 */
public class SocialActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
