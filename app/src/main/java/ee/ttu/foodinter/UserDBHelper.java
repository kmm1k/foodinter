package ee.ttu.foodinter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kmm on 15.03.2016.
 */
public class UserDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fooders";
    private static final String TABLE_NAME = "user";
    private static final int DATABASE_VERSION = 1;

    private static final String KEY_ID = "_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_PAGE_ID = "page_id";

    private static final String DELETE_USER_TABLE =
            "DROP TABLE IF EXISTS "+TABLE_NAME;
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE "+TABLE_NAME+" (" +
                    KEY_ID+" INT PRIMARY KEY," +
                    KEY_USERNAME+" TEXT NOT NULL," +
                    KEY_PASSWORD+" TEXT NOT NULL," +
                    KEY_USER_ID+" TEXT NOT NULL," +
                    KEY_PAGE_ID+" INT NOT NULL" +
                    ");";

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_USER_TABLE);
        onCreate(db);
    }

    public void insertUser(String username, String password, String userId, int pageId) {
        Log.d("lammas", "insertUser: "+username+password+userId+pageId);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("user_id", userId);
        values.put("page_id", pageId);
        db.insert(TABLE_NAME, null, values);
    }

    public String[] getUser() {
        SQLiteDatabase db = this.getReadableDatabase();

        String arrData[] = null;
        try {
            Cursor cursor = db.query(
                    TABLE_NAME,
                    new String[] { "*" },
                    null,
                    null, null, null, null, null);

            if(cursor != null)
            {
                Log.d("lammas", ""+cursor.getCount());
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(1); // DeviceID
                    arrData[1] = cursor.getString(2); // EmailID
                    arrData[2] = cursor.getString(3); // Event
                    arrData[3] = cursor.getString(4); // Operator
                }
            }

            cursor.close();
            db.close();
        } catch (Exception e) {

        }

        return arrData;
    }

    public void deleteDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DELETE_USER_TABLE);
        onCreate(db);
    }

    public void updatePageId(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(KEY_PAGE_ID, id);

// Which row to update, based on the ID


        int max = getMaxID();

// Which row to update, based on the ID
        String selection = KEY_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(max) };

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
        //int count = db.update(TABLE_NAME, values, "_id="+id, null);
        Log.d("lammas", "fragmentId: "+id);
        /*String strSQL = "UPDATE "+TABLE_NAME+" SET "+KEY_PAGE_ID+" = "+id+" WHERE "+KEY_ID+" = "+ max;
        db.execSQL(strSQL);*/
        Log.d("lammas", "rows affected: "+count);
    }

    private int getMaxID(){
        int mx=-1;
        try{

            SQLiteDatabase db=this.getReadableDatabase();
            Cursor cursor=db.rawQuery("SELECT max("+KEY_ID+") from  "+TABLE_NAME,new String [] {});
            /*Cursor cursor = db.query(TABLE_NAME, new String[] { "*" },
                    null ,null, null, null, null, null);*/
            cursor.moveToLast();
            if (cursor != null)
                if(cursor.moveToLast())
                {

                    mx= cursor.getInt(0);

                }
            //  cursor.close();

            return mx;
        }
        catch(Exception e){

            return -1;
        }
    }






}
