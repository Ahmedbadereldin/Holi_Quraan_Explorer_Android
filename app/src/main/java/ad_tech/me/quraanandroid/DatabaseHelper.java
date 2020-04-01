package ad_tech.me.quraanandroid;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by duyhoang on 11/30/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_Path = "/data/data/ad_tech.me.quraanandroid/";
    private static String DB_Name = "quraan_db.sqlite";
    private static String soura_text_page_no = "soura_text_page_no.sql";
    private SQLiteDatabase myDatabase;
    private final Context myContext;
    public static final int Version = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_Name, null, Version);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            myDatabase.execSQL("create table IF NOT EXISTS book_mark (id INTEGER primary key, page_no TEXT, sura_id INT)");
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_Path + DB_Name;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {

        }
        if (checkDB != null) checkDB.close();
        return checkDB != null ? true : false;
    }
    private void copyDatabase() throws IOException{
        InputStream myInput=myContext.getAssets().open(DB_Name);
        String outFileName=DB_Path+DB_Name;
        OutputStream myOutput=new FileOutputStream(outFileName);
        byte[] buffer=new byte[1024];
        int length;
        while((length =myInput.read(buffer))>0){
            myOutput.write(buffer,0,length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
    public void openDatabase(){
        String myPath=DB_Path+DB_Name;
        myDatabase=SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
        myDatabase.execSQL("create table IF NOT EXISTS book_mark (id INTEGER primary key, page_no TEXT, sura_id INT)");
        //myDatabase.execSQL("Delete table IF EXISTS soura_text_page_no");

        try{
            myDatabase.execSQL("SELECT * FROM soura_text_page_no where (page_no = 1)");
        }catch (Exception ex){
            try {
                executeDBScripts(soura_text_page_no);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean executeDBScripts(String aSQLScriptFilePath) throws IOException,SQLException {
        boolean isScriptExecuted = false;
        try {

            InputStream insertsStream = myContext.getAssets().open(aSQLScriptFilePath);
            BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = insertReader.readLine()) != null) {
                sb.append(str + "\n ");
            }
            insertReader.close();
            myDatabase.execSQL(sb.toString());
            /*
            try {
                AssetManager mngr = myContext.getAssets();
                //mngr.
                BufferedReader br = new BufferedReader(new FileReader(mngr.open(aSQLScriptFilePath)));
                //BufferedReader br = new BufferedReader(new FileReader(String.valueOf(mngr.open(aSQLScriptFilePath))));
                if (!br.ready()) {
                    throw new IOException();
                }
                String str;
                StringBuffer sb = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
                br.close();
                myDatabase.execSQL(sb.toString());
            } catch (IOException e) {
                System.out.println(e);
            }
            */
            /*
            InputStream is = myContext.getAssets().open(aSQLScriptFilePath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);
            myDatabase.execSQL(text);
            isScriptExecuted = true;
            */
            /*
            BufferedReader in = new BufferedReader(new FileReader(aSQLScriptFilePath));
            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = in.readLine()) != null) {
                sb.append(str + "\n ");
            }
            in.close();
            myDatabase.execSQL(sb.toString());
            isScriptExecuted = true;
            */

        } catch (Exception e) {
            System.err.println("Failed to Execute" + aSQLScriptFilePath +". The error is"+ e.getMessage());
        }
        return isScriptExecuted;
    }
    /*
    public void openDatabase() throws SQLException{
        String myPath=DB_Path+DB_Name;
        myDatabase=SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
    }
    */
    public void ExeSQLData(String sql) throws SQLException{
        myDatabase.execSQL(sql);
    }


    public Cursor QueryData(String query){
        return myDatabase.rawQuery(query,null);
    }

    /*
    public Cursor QueryData(String query) throws SQLException{
        return myDatabase.rawQuery(query,null);
    }
    */

    @Override
    public synchronized void close() {
        if (myDatabase !=null)
            myDatabase.close();
        super.close();
    }
    public void checkAndCopyDatabase(){
        boolean dbExist=checkDatabase();
        if (dbExist) {
            Log.d("TAG","Database already exist");
        }else{
            this.getReadableDatabase();
            try {
                copyDatabase();
            }catch (IOException e){
                Log.d("TAG","Error copying database");
            }
        }
    }
}
