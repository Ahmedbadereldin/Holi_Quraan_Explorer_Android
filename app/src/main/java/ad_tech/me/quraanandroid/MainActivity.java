package ad_tech.me.quraanandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    private DatabaseHelper dbHelper;
    Adapter adapter;
    ArrayList<SuraItem> arrayList=new ArrayList<SuraItem>();

    SharedPreferences sharedPreferences_var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        sharedPreferences_var = getSharedPreferences("QuraanAndroid", Context.MODE_PRIVATE);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String book_mark_var = sharedPreferences_var.getString("book_mark", "");

                Intent resultIntent = new Intent(getApplicationContext(), PagesActivity.class);
                int CurrentBookMark = Integer.valueOf(book_mark_var)+3;
                resultIntent.putExtra("page", String.valueOf(CurrentBookMark));
                resultIntent.putExtra("IsBookMark", "1");
                startActivity(resultIntent);
            }
        });


        //loadDatabase
        dbHelper=new DatabaseHelper(this);
        try {
            dbHelper.checkAndCopyDatabase();
            dbHelper.openDatabase();
        }catch (SQLException e){

        }
        try {
            Cursor cursor=dbHelper.QueryData("select * from SuraTbl order by SuraID");
            if ( cursor !=null){
                if (cursor.moveToFirst()){
                    do {
                        SuraItem item=new SuraItem();
                        item.setSuraID(cursor.getString(0));
                        item.setSuraName(cursor.getString(1));
                        item.setPageNo(cursor.getString(3));
                        item.setPlace(cursor.getString(6));
                        arrayList.add(item);
                    }while (cursor.moveToNext());
                }
            }
        }catch (SQLException e){}
        adapter=new Adapter(this,R.layout.custom_list_item,arrayList);
        listView= (ListView) findViewById(R.id.SuraListView);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView Sure_PageNoVar = (TextView)view.findViewById(R.id.Sure_PageNo);

                Intent resultIntent = new Intent(getApplicationContext(), PagesActivity.class);
                resultIntent.putExtra("page", Sure_PageNoVar.getText());
                resultIntent.putExtra("IsBookMark", "0");
                startActivity(resultIntent);
            }
        });
    }

    public void gotoBookMark3_click(View view){
        String book_mark_var = sharedPreferences_var.getString("book_mark", "604");

        Intent resultIntent = new Intent(getApplicationContext(), PagesActivity.class);
        int CurrentBookMark = Integer.valueOf(book_mark_var)+3;
        resultIntent.putExtra("page", String.valueOf(CurrentBookMark));
        //resultIntent.putExtra("page", "604");
        resultIntent.putExtra("IsBookMark", "1");
        startActivity(resultIntent);
    }

    public void bt_share_click(View view){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String stringToShare = "Please Visit https://play.google.com/store/apps/details?id=ad_tech.me.quraanandroid";
        shareIntent.putExtra(Intent.EXTRA_TEXT, stringToShare);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //return true;
            //Intent intent = new Intent(this, PagesActivity.class);
            //startActivity(intent);


            String book_mark_var = sharedPreferences_var.getString("book_mark", "");

            Intent resultIntent = new Intent(getApplicationContext(), PagesActivity.class);
            int CurrentBookMark = Integer.valueOf(book_mark_var)+3;
            resultIntent.putExtra("page", String.valueOf(CurrentBookMark));
            resultIntent.putExtra("IsBookMark", "1");
            startActivity(resultIntent);
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
