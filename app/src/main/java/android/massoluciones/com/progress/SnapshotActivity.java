package android.massoluciones.com.progress;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Stefanie on 17/10/2015.
 */
public class SnapshotActivity extends Activity {

    ImageView img;
    private Uri mUri;
    Bitmap mPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        img=(ImageView) findViewById(R.id.imgSnapshot);
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File pdfFolder = new File(Environment.getExternalStorageDirectory()+"/progressapp");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File f = new File(pdfFolder,  "progress_"+timeStamp+".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        mUri = Uri.fromFile(f);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:{
                if (resultCode == Activity.RESULT_OK) {
                    getContentResolver().notifyChange(mUri, null);
                    ContentResolver cr = getContentResolver();

                    try {

                        mPhoto = MediaStore.Images.Media.getBitmap(cr, mUri);
                        img.setImageBitmap(mPhoto);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            }
        }

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