package progress.massoluciones.com.progress;

import android.app.Activity;
import android.content.Context;
import android.massoluciones.com.progress.R;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.coverflow.CoverFlow;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MemoryActivity extends Activity {
    ArrayList<CheckIn> listado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            listado=new DescargarListadoCheckIn(this).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        CoverFlow coverFlow;
        coverFlow =  (CoverFlow) findViewById(R.id.coverflow);
        coverFlow.setAdapter(new ImageAdapter(this,listado));
        coverFlow.setSpacing(-25);
        coverFlow.setSelection(4, true);
        coverFlow.setAnimationDuration(1000);

    }

    @Override
    protected void onStop() {
        super.onStop();
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
class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<CheckIn> mImageIds;
    private ImageView[] mImages;

    public ImageAdapter(Context c, ArrayList<CheckIn> plistado) {
        mContext = c;
        mImageIds=plistado;
        mImages = new ImageView[mImageIds.size()];

    }

    public int getCount() {
        return mImageIds.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //Use this code if you want to load from resources
        ImageView i = new ImageView(mContext);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300)).build();//for rounded corners

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(true)
                .showImageOnLoading(R.drawable.ic_launcher).build();

        //download and display image from url
        imageLoader.displayImage("file:///" +mImageIds.get(position).getRuta(),i, options);

        return i;

    }
    /** Returns the size (0.0f to 1.0f) of the views
     * depending on the 'offset' to the center. */
    public float getScale(boolean focused, int offset) {
        /* Formula: 1 / (2 ^ offset) */
        return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset)));
    }
}