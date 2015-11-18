package android.massoluciones.com.progress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListadoCheckinsActivity extends Activity {
    GridView grid;
    ArrayList<CheckIn> checkIns=new ArrayList<CheckIn>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_listadocheckins);
        grid=(GridView)findViewById(R.id.mtxListadoCheckIns);
        grid.setAdapter(new CustomAdapter(this, "lbs")); //SHARED PREFERENCES
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),SnapshotActivity.class);
                intent.putExtra("id",i);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

    }
}
