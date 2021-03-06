package progress.massoluciones.com.progress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.massoluciones.com.progress.R;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
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
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class SnapshotActivity extends Activity implements View.OnClickListener{

    ImageView img, imgpicked;
    private Uri mUri;
    Bitmap mPhoto, yourSelectedImage, fotoTomada, fotoSeleccionada;
    Integer height, width;
    Integer bandera=0;
    Button btnPick, btnNew, btnSave;
    View view_instance;
    String mediaPath, urifotoTomada, urifotoSeleccionada, measure;
    Integer weight=0, difference=0, selectedweight=0;
    CheckIn lastcheckin, prevcheckin;
    ArrayList<CheckIn> listado;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle b=this.getIntent().getExtras();
        if (b!=null){
            weight=b.getInt("peso");
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
        SharedPreferences prefs=getSharedPreferences("progressPrefs", Context.MODE_PRIVATE);
        measure=prefs.getString("medida","lbs");
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

                        //DATABASE FUNCTIONS
                        lastcheckin=new ConsultarCheckIn(this).execute().get();
                        //NO ES LA PRIMERA VEZ QUE TE CHEQUEAS
                        if(lastcheckin.getPeso()>0){
                            new InsertarCheckIn(this,urifotoTomada,weight).execute();
                        }else{
                            new InsertarCheckIn(this,urifotoTomada,weight).execute();
                            Toast.makeText(this,R.string.snapshot_saved,Toast.LENGTH_LONG);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            }
            case 1:{
              if (resultCode==Activity.RESULT_OK){
                   int position=data.getIntExtra("numero",-1);
                    Log.i("SNAPSHOT POSITIION",String.valueOf(position));
                    try {
                        listado=new DescargarListadoCheckIn(this).execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    selectedweight=listado.get(position).getPeso();
                    prevcheckin=listado.get(position);
                    InputStream imageStream = null;
                    Uri selectedImage=Uri.parse ("file:///"+listado.get(position).getRuta());
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize=8;
                        yourSelectedImage = BitmapFactory.decodeStream(imageStream,null,options);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        fotoSeleccionada = Bitmap.createBitmap(yourSelectedImage, 0, 0,
                                yourSelectedImage.getWidth(), yourSelectedImage.getHeight(),
                                matrix, true);
                        ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                        fotoSeleccionada.compress(Bitmap.CompressFormat.JPEG,0,bmpStream);
                        imgpicked.setImageBitmap(fotoSeleccionada);
                        bandera+=1;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this,R.string.snapshot_invalid_image, Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgPicked:{
                Intent vintent=new Intent(this,ListadoCheckinsActivity.class);
                startActivityForResult(vintent,1);
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
                    try {
                        mediaPath = combineImages(fotoTomada, fotoSeleccionada, width, height);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(this, R.string.snapshot_saved,Toast.LENGTH_LONG).show();
                    btnSave.setEnabled(false);
                    imgpicked.setEnabled(false);
                    img.setEnabled(false);
                }
                else {
                    Toast.makeText(this, R.string.snapshot_pickimages,Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.imgSnapshot:{
                TakePic();
                break;
            }
            case R.id.btnSnapshot_New:{
                    urifotoSeleccionada="";
                    urifotoTomada="";
                    btnSave.setEnabled(true);
                    img.setEnabled(true);
                    imgpicked.setEnabled(true);
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
            urifotoTomada=Environment.getExternalStorageDirectory().getAbsolutePath()+"/progress/progress_"+timeStamp+".jpg";
            Log.i("FOTO TOMADA", urifotoTomada);
            mUri = Uri.fromFile(f);
            startActivityForResult(intent,0);
        }
    }
    public String combineImages(Bitmap pc, Bitmap ps, Integer pwidth, Integer pheight) throws ExecutionException, InterruptedException {
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
        difference=weight-selectedweight;
        String captionString =difference+ measure; //SHARED PREFERENCE
        String year=prevcheckin.getFecha().substring(2,4);
        String month=getMonth(Integer.valueOf(prevcheckin.getFecha().toString().substring(4,6)));
        String day=prevcheckin.getFecha().toString().substring(6,8);
        String caption1=day+"/"+month.substring(0,3)+"/"+year;
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        year=timeStamp.substring(2,4);
        month=getMonth(Integer.valueOf(timeStamp.substring(4,6)));
        day=timeStamp.substring(6,8);
        String caption2=day+"/"+month.substring(0,3)+"/"+year;
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
            int x=(comboImage.getWidth() / 2) - (rectText.width() / 2);
            comboImage.drawText(captionString,x, rectText.height(), paintText);//160
            }

        if(caption1 != null) {
            paintText.setColor(getResources().getColor(R.color.accent));
            paintText.setTextSize(25);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);
            paintText.setTypeface(bold);
            Rect rectText = new Rect();
            paintText.getTextBounds(caption1, 0, caption1.length(), rectText);
            int x=(comboImage.getWidth() / 4) - (rectText.width() / 2);
            Log.i("SNAPSHOT TEXTO 1", String.valueOf(x));
            comboImage.drawText(caption1,
                    x, comboImage.getHeight()-200, paintText); //70
        }

        if(caption2 != null) {
            paintText.setColor(getResources().getColor(R.color.accent));
            paintText.setTextSize(25);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);
            paintText.setTypeface(bold);
            Rect rectText = new Rect();
            paintText.getTextBounds(caption2, 0, caption2.length(), rectText);
            int x=((comboImage.getWidth()/4)*3) - (rectText.width() / 2);
            Log.i("SNAPSHOT TEXTO 2", String.valueOf(x));
            comboImage.drawText(caption2,
                    x, comboImage.getHeight()-200, paintText); //370
        }
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String tmpImg ="progress_combined_"+timeStamp + ".jpg";
        String path="";
        OutputStream os = null;
        try {
            File pdfFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/progresscombined");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdirs();
            }
            os = new FileOutputStream( Environment.getExternalStorageDirectory().getAbsolutePath()+"/progresscombined/"+ tmpImg);
            path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/progresscombined/"+ tmpImg;
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
    public String getMonth(int month) {
      //  return "Jan";
      return new DateFormatSymbols().getMonths()[month-1];
    }
}
class ConsultarCheckIn extends AsyncTask<Void, Integer, CheckIn>{
    Context contexto;
    ConsultarCheckIn(Context context) {
        this.contexto=context;
    }

    @Override
    protected CheckIn doInBackground(Void... voids) {
        CheckIn check=new CheckIn();
        USQLiteHelper sql=new USQLiteHelper(contexto,"DBprogress", null,1);
        SQLiteDatabase db=sql.getWritableDatabase();
        String query="SELECT _id, foto, fecha, peso FROM CHECKIN ORDER BY _id DESC LIMIT 1;";
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                check.setId(c.getInt(0));
                check.setRuta(c.getString(1));
                check.setFecha(c.getString(2));
                check.setPeso(c.getInt(3));
            }while (c.moveToNext());

        }
        else {
            check.setId(0);
            check.setPeso(0);
            check.setRuta("");
            check.setFecha("");
        }
        c.close();
        db.close();
        return check;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(CheckIn checkin) {
        super.onPostExecute(checkin);
    }
}

 class InsertarCheckIn extends AsyncTask<Void, Integer, Boolean>
 {
     Context contexto;
     String uri, fecha;
     Integer peso;
     Boolean resultado=false;
     InsertarCheckIn(Context context, String uri, Integer peso) {
         this.contexto=context;
         this.uri=uri;
         this.peso=peso;
     }

     @Override
     protected Boolean doInBackground(Void... voids) {
         Calendar calendar = Calendar.getInstance();
         fecha=new SimpleDateFormat("yyyyMMdd_HHmmss").format(calendar.getTime());
         USQLiteHelper sql=new USQLiteHelper(contexto,"DBprogress", null,1);
         SQLiteDatabase db=sql.getWritableDatabase();
         ContentValues content=new ContentValues();
         content.put("foto",uri);
         content.put("fecha",fecha);
         content.put("peso",peso);
         try{
             db.insert("CHECKIN",null,content);
             resultado=true;
         }catch (Exception e){
             e.printStackTrace();
         } finally {
             db.close();
         }
         return resultado;
     }

     @Override
     protected void onPreExecute() {
         super.onPreExecute();
     }

     @Override
     protected void onPostExecute(Boolean aBoolean) {
         super.onPostExecute(aBoolean);
     }
 }

