package progress.massoluciones.com.progress;

import android.app.Activity;
import android.massoluciones.com.progress.R;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

/**
 * Created by Stefanie on 17/10/2015.
 */
public class ProgressActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }
}
