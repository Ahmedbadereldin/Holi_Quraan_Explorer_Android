package ad_tech.me.quraanandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

public class PagesActivity extends AppCompatActivity {

    ViewPager viewPager;
    customSwip  customSwip;

    SharedPreferences sharedPreferences_var;
    int book_mark_id_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages);

        Bundle b = getIntent().getExtras();

        sharedPreferences_var = getSharedPreferences("QuraanAndroid", Context.MODE_PRIVATE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        book_mark_id_test = Integer.valueOf(b.getString("book_mark_id"));

        // hide nav bar -----
        /*
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        */
        // hide nav bar -----




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else
        {
            View decorView = getWindow().getDecorView();
            //Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }



        viewPager=(ViewPager)findViewById(R.id.viewPager);
        customSwip=new customSwip(this);
        viewPager.setAdapter(customSwip);
        //viewPager.setCurrentItem(customSwip.getCount()-1);

        String id = b.getString("page");
        String BookMarkVar1 = b.getString("IsBookMark");
        int v = 0;

        int book_mark_var1 = 0;
        int PageCountVar1 = 0;

        if (BookMarkVar1.equals("0")){
            v = (customSwip.getCount()) - Integer.parseInt(id);
        }
        if (BookMarkVar1.equals("1")){
            book_mark_var1 = Integer.valueOf(sharedPreferences_var.getString("book_mark", "604"));
            //book_mark_var1 = 604;//Integer.valueOf(sharedPreferences_var.getString("book_mark", ""));
            //PageCountVar1 = Integer.valueOf(customSwip.getCount());
            v = book_mark_var1;
        }
        viewPager.setCurrentItem(v);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                SharedPreferences.Editor myEdit = sharedPreferences_var.edit();
                myEdit.putString("book_mark", String.valueOf(position));
                myEdit.commit();

            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.

                SharedPreferences.Editor myEdit = sharedPreferences_var.edit();
                myEdit.putString("book_mark", String.valueOf(position));
                myEdit.commit();
            }
        });

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // hide menu item delete_book_mark when it come from another list other than bookmark list
        MenuItem action_remove_book_mark_var = menu.findItem(R.id.action_remove_book_mark);
        if (book_mark_id_test==0){
            action_remove_book_mark_var.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share_page) {
            //return true;
            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpg");
            int page_num = 604 - viewPager.getCurrentItem();
            String pic_name1 = "p" + String.format("%03d", page_num) + ".png";
            final File photoFile = new File(getFilesDir(), pic_name1);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
            startActivity(Intent.createChooser(shareIntent, "Share image using"));

            /*
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            //Uri uri = Uri.parse("android.resource://ad_tech.me.quraanandroid/"+R.drawable.ic_launcher);
            Uri uri = Uri.parse("android.resource://ad_tech.me.quraanandroid/"+getBaseContext().getResources().getIdentifier("p" + String.format("%03d", viewPager.getCurrentItem()), "drawable", getBaseContext().getPackageName()));//R.drawable.ic_launcher);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello, This is test Sharing");
            startActivity(Intent.createChooser(shareIntent, "Send your image"));
            */
        }

        if (id == R.id.action_add_book_mark){
            DatabaseHelper dbHelper;
            dbHelper=new DatabaseHelper(this);
            try {
                dbHelper.checkAndCopyDatabase();
                dbHelper.openDatabase();
            }catch (SQLException e){}
            try {
                String book_mark_var = sharedPreferences_var.getString("book_mark", "604");
                int CurrentBookMark = 604 - viewPager.getCurrentItem(); //Integer.valueOf(book_mark_var)+3;
                try {
                    dbHelper.ExeSQLData("insert into book_mark (page_no, sura_id) values ("+String.valueOf(CurrentBookMark)+", 0)");
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.AddBookMarkSuccess),
                            Toast.LENGTH_LONG).show();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }catch (SQLException e){}
        }

        if (id == R.id.action_remove_book_mark){
            DatabaseHelper dbHelper;
            dbHelper=new DatabaseHelper(this);
            try {
                dbHelper.checkAndCopyDatabase();
                dbHelper.openDatabase();
            }catch (SQLException e){}
            try {
                String book_mark_var = sharedPreferences_var.getString("book_mark", "604");
                int CurrentBookMark = 604 - viewPager.getCurrentItem(); //Integer.valueOf(book_mark_var)+3;
                try {
                    String gg = "DELETE FROM book_mark where (id = "+String.valueOf(book_mark_id_test)+")";
                    dbHelper.ExeSQLData(gg);
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.DeleteBookMarkSuccess),
                            Toast.LENGTH_LONG).show();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }catch (SQLException e){}
        }
        return super.onOptionsItemSelected(item);
    }


}
