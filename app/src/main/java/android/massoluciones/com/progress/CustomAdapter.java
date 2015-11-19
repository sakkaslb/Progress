package android.massoluciones.com.progress;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;


public class CustomAdapter extends BaseAdapter {

    public ArrayList<CheckIn> checkins;
    Context context;
    String medida;
    ArrayList<CheckIn> listado=new ArrayList<CheckIn>();
    private static LayoutInflater inflater = null;
    public CustomAdapter(Context contexto, String pmedida, ArrayList<CheckIn> checkins) {
        this.context=contexto;
        this.medida=pmedida;
        this.listado=checkins;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return listado.size();
    }

    @Override
    public Object getItem(int i) {
        return listado.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class Holder{
        TextView peso, archivo, fecha,medida;
        ImageView img;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder=new Holder();
        View rowView;
        rowView=inflater.inflate(R.layout.activity_fotocheckin,null);
        if (view!=null){
            holder.archivo=(TextView) rowView.findViewById(R.id.txtFotoRuta);
            holder.peso=(TextView) rowView.findViewById(R.id.txtFotoPeso);
            holder.medida=(TextView) rowView.findViewById(R.id.txtFotoPesoMedida);
            holder.fecha=(TextView) rowView.findViewById(R.id.txtFotoFecha);
            holder.img=(ImageView) rowView.findViewById(R.id.imgFotoCheckin);

            holder.fecha.setText(listado.get(i).getFecha().toString());
            holder.peso.setText(listado.get(i).getPeso().toString());
            holder.archivo.setText(listado.get(i).getRuta().toString());
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
            imageLoader.displayImage("file:///" + listado.get(i).getRuta(), holder.img, options);
        }
        return rowView;
    }

}
