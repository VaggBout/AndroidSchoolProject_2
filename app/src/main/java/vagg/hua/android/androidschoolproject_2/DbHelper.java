package vagg.hua.android.androidschoolproject_2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "LOCATION";
    public static final int DB_VERSION = 1;
    public static final String KEY_ID = "_ID";
    public static final String KEY_TIMESTAMP = "TIMESTAMP";
    public static final String KEY_LON = "LON";
    public static final String KEY_LAT = "LAT";

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+DB_NAME+" ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + KEY_TIMESTAMP + " BIGINT DEFAULT (strftime('%s', 'now')), "
                + KEY_LON + " BIGINT, "
                + KEY_LAT + " BIGINT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
