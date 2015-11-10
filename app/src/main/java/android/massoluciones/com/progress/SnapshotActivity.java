package android.massoluciones.com.progress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.DialogPreference;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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


public class SnapshotActivity extends Activity implements View.OnClickListener{

    ImageView img, imgpicked;
    private Uri mUri;
    Bitmap mPhoto, yourSelectedImage, fotoTomada, fotoSeleccionada;
    Integer height, width;
    Integer bandera=0;
    Button btnPick, btnNew, btnSave;
    View view_instance;
    String mediaPath;
    Double weight=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle b=getIntent().getExtras();
        if (b!=null){
            weight=b.getDouble("peso");
            Log.i("PESO",weight.toString());
        }

        view_instance= findViewById(R.id.layout_snapshot_interno);
        btnPick=(Button) findViewById(R.id.btnSnapshot_Share);
        btnNew=(Button) findViewById(R.id.btnSnapshot_New);
        btnSave=(Button) findViewById(R.id.btnSnapshot_Save);
        img=(ImageView) findViewById(R.id.imgSnapshot);
        imgpicked=(ImageView) findViewById(R.id.imgPicked);
        imgpicked.setOnClickListener(this);
        btnPick.setOnClickListener(this);
        btnNew.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        img.setOnClickListener(this);
        TakePic();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)  {
            case 0:{
                if (resultCode == Activity.RESULT_OK) {
                    getContentResolver().notifyChange(mUri, null);
                    try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;
                        mPhoto = BitmapFactory.decodeFile(mUri.getPath(), options);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        fotoTomada = Bitmap.createBitmap(mPhoto, 0, 0,
                                mPhoto.getWidth(), mPhoto.getHeight(),
                                matrix, true);
                        ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                        fotoTomada.compress(Bitmap.CompressFormat.JPEG,0, bmpStream);
                        img.setImageBitmap(fotoTomada);
                        bandera+=1;
                    } catch (Exception e) {
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
                    /*yourSelectedImage.recycle();*/
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

                if (bandera>=2 && mediaPath!=null && mediaPath.length()>0){
                    String type = "image/*";
                    createInstagramIntent(type, mediaPath);
                }else{
                    Toast.makeText(this, R.string.snapshot_notsaved,Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.btnSnapshot_Save:{
                height=view_instance.getHeight();
                width=view_instance.getWidth();
                if (bandera>=2 &&fotoTomada!=null &&fotoSeleccionada!=null) {
                    mediaPath = combineImages(fotoTomada, fotoSeleccionada, width, height);
                    Toast.makeText(this, R.string.snapshot_saved,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, R.string.snapshot_pickimages,Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.imgSnapshot:{
                TakePic();
                break;
            }
            case R.id.btnSnapshot_New:{
                    AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                    dialog.setTitle(getResources().getText(R.string.app_name));
                    dialog.setMessage(getResources().getText(R.string.snapshot_menu_question));
                    dialog.setCancelable(false);
                    dialog.setPositiveButton(getResources().getText(R.string.action_ok),new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                             if (mPhoto!=null){
                                 mPhoto.recycle();
                             }
                            if (yourSelectedImage!=null){
                                yourSelectedImage.recycle();
                            }
                            if (fotoSeleccionada!=null){
                                fotoSeleccionada.recycle();
                            }
                            if (fotoTomada!=null){
                                fotoTomada.recycle();
                            }
                            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_snapshot_take));
                            imgpicked.setImageDrawable(getResources().getDrawable(R.drawable.ic_snapshot_find));
                          //  bandera=0;
                          //  TakePic();
                            Intent vintent=new Intent().setClass(SnapshotActivity.this,WeightActivity.class);
                            startActivity(vintent);
                            finish();

                        }
                    });
                    dialog.setNegativeButton(getResources().getText(R.string.action_cancel),new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.show();
                    break;
            }
        }
    }
    public void TakePic(){
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
    public String combineImages(Bitmap pc, Bitmap ps, Integer pwidth, Integer pheight) {
        Bitmap cs = null;
        int width, height = 0;
        width = pwidth;
        height = pheight;
        Bitmap c=getResizedBitmap(pc,width/2,(height/7)*6);
        Bitmap s=getResizedBitmap(ps,width/2,(height/7)*6);
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(s, 0f, 0f, null);
        comboImage.drawBitmap(c, s.getWidth(), 0f, null);

        Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
        d.setBounds(0, 0, 50, 50);
        d.draw(comboImage);

        String captionString = "-31 libras";
        String caption1="Jun/15";
        String caption2="Oct/15";
        Typeface plain = Typeface.createFromAsset(getAssets(), "fonts/LeagueSpartan.otf");
        Typeface bold = Typeface.create(plain, Typeface.BOLD);
        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);

        if(captionString != null) {
            paintText.setColor(getResources().getColor(R.color.primary));
            paintText.setTextSize(50);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);
            paintText.setTypeface(bold);
            Rect rectText = new Rect();
            paintText.getTextBounds(captionString, 0, captionString.length(), rectText);
            comboImage.drawText(captionString,
                    160, rectText.height(), paintText);
            }

        if(caption1 != null) {
            paintText.setColor(getResources().getColor(R.color.accent));
            paintText.setTextSize(25);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);
            paintText.setTypeface(bold);
            Rect rectText = new Rect();
            paintText.getTextBounds(caption1, 0, caption1.length(), rectText);
            comboImage.drawText(caption1,
                    70, 500, paintText);
        }

        if(caption2 != null) {
            paintText.setColor(getResources().getColor(R.color.accent));
            paintText.setTextSize(25);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);
            paintText.setTypeface(bold);
            Rect rectText = new Rect();
            paintText.getTextBounds(caption2, 0, caption2.length(), rectText);
            comboImage.drawText(caption2,
                    370, 500, paintText);
        }
        String tmpImg = String.valueOf(System.currentTimeMillis()) + ".jpg";
        String path="";
        OutputStream os = null;
        try {

            os = new FileOutputStream( Environment.getExternalStorageDirectory().getAbsolutePath()+"/progress/"+ tmpImg);
            path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/progress/"+ tmpImg;
            cs.compress(Bitmap.CompressFormat.JPEG,100 , os);
        } catch(IOException e) {
            Log.e("CombineImages", "Problem combining images", e);
        }
        return path;
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);

        /*bm.recycle();*/
        return resizedBitmap;
    }
    public void createInstagramIntent(String type, String mediaPath){

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);
        // Broadcast the Intent.
        startActivity(Intent.createChooser(share,getResources().getString(R.string.snapshot_share)));
    }
}