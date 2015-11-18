package android.massoluciones.com.progress;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class CustomAdapter extends BaseAdapter {

    public ArrayList<CheckIn> checkins;
    Context context;
    String medida;
    private static LayoutInflater inflater = null;
    public CustomAdapter(Context contexto, String pmedida) {
        this.context=contexto;
        this.medida=pmedida;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            checkins=new DescargarListadoCheckIn(context).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return checkins.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class Holder{
        TextView peso, archivo, fecha,medida;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        Holder holder=new Holder();
        View rowView;
        rowView=inflater.inflate(R.layout.activity_fotocheckin,null);
        holder.archivo=(TextView) rowView.findViewById(R.id.txtFotoRuta);
        holder.peso=(TextView) rowView.findViewById(R.id.txtFotoPeso);
        holder.medida=(TextView) rowView.findViewById(R.id.txtFotoPesoMedida);
        holder.fecha=(TextView) rowView.findViewById(R.id.txtFotoFecha);
        holder.img=(ImageView) rowView.findViewById(R.id.imgFotoCheckin);
        holder.fecha.setText(checkins.get(position).getFecha().toString());
        holder.peso.setText(checkins.get(position).getPeso().toString());
        holder.archivo.setText(checkins.get(position).getRuta().toString());
        holder.medida.setText(medida);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .showImageOnLoading(R.drawable.ic_launcher).build();

        //download and display image from url
        imageLoader.displayImage("file:///"+checkins.get(position).getRuta(),holder.img, options);
        return rowView;
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
        CheckIn check=new CheckIn();
        if(c.moveToFirst()){
            do
            {
                check.setId(c.getInt(0));
                check.setRuta(c.getString(1));
                check.setFecha(c.getString(2));
                check.setPeso(c.getInt(3));
                listado.add(check);

            }while (c.moveToNext());

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