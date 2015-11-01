package android.massoluciones.com.progress;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Stefanie on 17/10/2015.
 */
public class SnapshotActivity extends Activity implements View.OnClickListener{

    ImageView img, imgpicked;
    private Uri mUri;
    Bitmap mPhoto;
    Integer height, width;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view_instance = findViewById(R.id.layout_snapshot);
        height=((view_instance.getHeight())/3)*2;
        width=(view_instance.getWidth()/2);
        img=(ImageView) findViewById(R.id.imgSnapshot);
        imgpicked=(ImageView) findViewById(R.id.imgPicked);
        imgpicked.setOnClickListener(this);
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
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


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)  {
            case 0:{
                if (resultCode == Activity.RESULT_OK) {
                    getContentResolver().notifyChange(mUri, null);
                    ContentResolver cr = getContentResolver();

                    try {

                        mPhoto = MediaStore.Images.Media.getBitmap(cr, mUri);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        Bitmap rotated = Bitmap.createBitmap(mPhoto, 0, 0,
                                mPhoto.getWidth(), mPhoto.getHeight(),
                                matrix, true);
                        ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                        rotated.compress(Bitmap.CompressFormat.JPEG,0, bmpStream);
                        mPhoto.recycle();
                        img.setImageBitmap(rotated);




                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            }
            case 1:{
                if (resultCode==Activity.RESULT_OK){
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize=8;
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream,null,options);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap rotated = Bitmap.createBitmap(yourSelectedImage, 0, 0,
                            yourSelectedImage.getWidth(), yourSelectedImage.getHeight(),
                            matrix, true);
                    yourSelectedImage.recycle();
                    ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                    rotated.compress(Bitmap.CompressFormat.JPEG,0,bmpStream);
                    imgpicked.setImageBitmap(rotated);


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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgPicked:{
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, ""), 1);
                break;
            }
        }
    }
}