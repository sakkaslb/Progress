package progress.massoluciones.com.progress;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class USQLiteHelper extends SQLiteOpenHelper {
    public USQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE CHECKIN( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "foto TEXT, " +
                "fecha TEXT, " +
                "peso INTEGER ) ";
        try{
            db.execSQL(sql);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
