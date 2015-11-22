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
import android.util.Log;
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
        try {
            checkIns=new DescargarListadoCheckIn(this).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        grid=(GridView)findViewById(R.id.mtxListadoCheckIns);
        grid.setAdapter(new CustomAdapter(this, "lbs", checkIns)); //SHARED PREFERENCES
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(ListadoCheckinsActivity.this,ListadoCheckinsActivity.class);
                intent.putExtra("numero",i);
                Log.i("LISTADO CHECKINS",String.valueOf(i));
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

    }
}


class DescargarListadoCheckIn extends AsyncTask<Void, Integer, ArrayList<CheckIn>> {
    Context contexto;
    ArrayList<CheckIn> listado;
    ProgressDialog dialog;
    DescargarListadoCheckIn(Context context) {
        this.contexto=context;
    }

    @Override
    protected ArrayList<CheckIn> doInBackground(Void... voids) {
        listado=new ArrayList<CheckIn>();
        USQLiteHelper sql=new USQLiteHelper(contexto,"DBprogress", null,1);
        SQLiteDatabase db=sql.getWritableDatabase();
        String query="SELECT _id, foto, fecha, peso FROM CHECKIN ORDER BY _id;";
        Cursor c=db.rawQuery(query,null);

        while (c.moveToNext()){
            CheckIn check=new CheckIn();
            check.setId(c.getInt(0));
            check.setRuta(c.getString(1));
            check.setFecha(c.getString(2));
            check.setPeso(c.getInt(3));
            listado.add(check);
        }

        c.close();
        db.close();
        return listado;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(contexto);
        dialog.setMessage(contexto.getResources().getString(R.string.photo_wait));
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<CheckIn> listado) {
        super.onPostExecute(listado);
        dialog.dismiss();
    }
}