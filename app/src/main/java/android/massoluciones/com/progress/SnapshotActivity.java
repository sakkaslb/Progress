package android.massoluciones.com.progress;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Stefanie on 17/10/2015.
 */
public class SnapshotActivity extends Activity implements View.OnClickListener{

    ImageView img, imgpicked, resultado;
    private Uri mUri;
    Bitmap mPhoto, yourSelectedImage, fotoTomada, fotoSeleccionada;
    Integer height, width;
    Integer bandera=0;
    Button btnPick;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view_instance = findViewById(R.id.layout_snapshot);
        resultado=(ImageView) findViewById(R.id.imgResultado);
        btnPick=(Button) findViewById(R.id.btnSnapshot_Share);
        height=((view_instance.getHeight())/3)*2;
        width=(view_instance.getWidth()/2);
        img=(ImageView) findViewById(R.id.imgSnapshot);
        imgpicked=(ImageView) findViewById(R.id.imgPicked);
        imgpicked.setOnClickListener(this);
        btnPick.setOnClickListener(this);
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File pdfFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/progress");
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
                        fotoTomada = Bitmap.createBitmap(mPhoto, 0, 0,
                                mPhoto.getWidth(), mPhoto.getHeight(),
                                matrix, true);
                        ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                        fotoTomada.compress(Bitmap.CompressFormat.JPEG,0, bmpStream);
                         img.setImageBitmap(fotoTomada);
                        mPhoto.recycle();
                        bandera+=1;
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
                    yourSelectedImage = BitmapFactory.decodeStream(imageStream,null,options);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    fotoSeleccionada = Bitmap.createBitmap(yourSelectedImage, 0, 0,
                            yourSelectedImage.getWidth(), yourSelectedImage.getHeight(),
                            matrix, true);
                    yourSelectedImage.recycle();
                    ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                    fotoSeleccionada.compress(Bitmap.CompressFormat.JPEG,0,bmpStream);
                    imgpicked.setImageBitmap(fotoSeleccionada);
                    bandera+=1;


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
                intent.setAction(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+"/progress");
                intent.setDataAndType(uri, "image/*");
                startActivityForResult(Intent.createChooser(intent, ""), 1);
                break;
            }
            case R.id.btnSnapshot_Share:{
                if (bandera>=2){
                    Bitmap mergedImages = combineImages(fotoTomada, fotoSeleccionada);
                    fotoSeleccionada.recycle();
                    fotoTomada.recycle();
                    img.setVisibility(View.GONE);
                    imgpicked.setVisibility(View.GONE);
                    resultado.setImageBitmap(mergedImages);

                }else{
                    Toast.makeText(this, R.string.snapshot_pickimages,Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
    public Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        int width, height = 0;

        if(c.getWidth() > s.getWidth()) {
            width = c.getWidth();
            height = c.getHeight() + s.getHeight();
        } else {
            width = s.getWidth();
            height = c.getHeight() + s.getHeight();
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, 0f, c.getHeight(), null);
        String tmpImg = String.valueOf(System.currentTimeMillis()) + ".jpg";

    OutputStream os = null;
    try {
      os = new FileOutputStream( Environment.getExternalStorageDirectory().getAbsolutePath()+"/progress/"+ tmpImg);
      cs.compress(Bitmap.CompressFormat.JPEG,100 , os);
    } catch(IOException e) {
      Log.e("combineImages", "problem combining images", e);
    }
        return cs;
    }
}