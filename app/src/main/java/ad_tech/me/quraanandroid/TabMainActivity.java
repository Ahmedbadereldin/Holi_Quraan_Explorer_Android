package ad_tech.me.quraanandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TabMainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //ListView listView;
    private static DatabaseHelper dbHelper;
    static Adapter adapter;
    static Adapter_Parts adapter_Parts;

    //ArrayList<SuraItem> arrayList=new ArrayList<SuraItem>();

    static SharedPreferences sharedPreferences_var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_main);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        sharedPreferences_var = getSharedPreferences("QuraanAndroid", Context.MODE_PRIVATE);

    }

    public void gotoBookMark3_click(View view){
        String book_mark_var = sharedPreferences_var.getString("book_mark", "604");

        Intent resultIntent = new Intent(getApplicationContext(), PagesActivity.class);
        int CurrentBookMark = Integer.valueOf(book_mark_var)+3;
        resultIntent.putExtra("page", String.valueOf(CurrentBookMark));
        //resultIntent.putExtra("page", "604");
        resultIntent.putExtra("IsBookMark", "1");
        resultIntent.putExtra("book_mark_id", "0");
        startActivity(resultIntent);
    }

    public void bt_share_click(View view){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String stringToShare = "Please Visit https://play.google.com/store/apps/details?id=ad_tech.me.quraanandroid";
        shareIntent.putExtra(Intent.EXTRA_TEXT, stringToShare);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();

        //fill_list_view();
        // returns first Fragment item within the pager
        //adapter.getRegisteredFragment(0);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1){
                View rootView_Sura = inflater.inflate(R.layout.fragment_tab_main, container, false);
                ArrayList<SuraItem> arrayList_Sura=new ArrayList<SuraItem>();
                dbHelper=new DatabaseHelper(getContext());
                try {
                    dbHelper.checkAndCopyDatabase();
                    dbHelper.openDatabase();
                }catch (SQLException e){}
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
                                arrayList_Sura.add(item);
                            }while (cursor.moveToNext());
                        }
                    }
                }catch (SQLException e){}
                adapter=new Adapter(getActivity(),R.layout.custom_list_item,arrayList_Sura);
                ListView listView = (ListView)rootView_Sura.findViewById(R.id.ListV);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView Sure_PageNoVar = (TextView)view.findViewById(R.id.Sure_PageNo);

                        //Intent resultIntent = new Intent(getApplicationContext(), PagesActivity.class);
                        Intent resultIntent = new Intent(getActivity().getApplicationContext(), PagesActivity.class);
                        resultIntent.putExtra("page", Sure_PageNoVar.getText());
                        resultIntent.putExtra("IsBookMark", "0");
                        resultIntent.putExtra("book_mark_id", "0");
                        startActivity(resultIntent);
                    }
                });
                return rootView_Sura;
            }
            // Parts =======================================
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 2){
                View rootView_part = inflater.inflate(R.layout.fragment_parts, container, false);
                ArrayList<PartItem> arrayList_Parts=new ArrayList<PartItem>();
                dbHelper=new DatabaseHelper(getContext());
                try {
                    dbHelper.checkAndCopyDatabase();
                    dbHelper.openDatabase();
                }catch (SQLException e){}
                try {
                    Cursor cursor=dbHelper.QueryData("SELECT * FROM PartTbl ORDER BY PartId");
                    if ( cursor !=null){
                        if (cursor.moveToFirst()){
                            do {
                                PartItem item=new PartItem();
                                item.setPartId(cursor.getString(0));
                                item.setPartName(cursor.getString(1));
                                item.setStartPageNo(cursor.getString(2));
                                arrayList_Parts.add(item);
                            }while (cursor.moveToNext());
                        }
                    }
                }catch (SQLException e){}
                adapter_Parts=new Adapter_Parts(getActivity(),R.layout.custom_list_item,arrayList_Parts);
                ListView listView = (ListView)rootView_part.findViewById(R.id.ListV);
                listView.setAdapter(adapter_Parts);
                adapter.notifyDataSetChanged();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView Sure_PageNoVar = (TextView)view.findViewById(R.id.Sure_PageNo);

                        //Intent resultIntent = new Intent(getApplicationContext(), PagesActivity.class);
                        Intent resultIntent = new Intent(getActivity().getApplicationContext(), PagesActivity.class);
                        resultIntent.putExtra("page", Sure_PageNoVar.getText());
                        resultIntent.putExtra("IsBookMark", "0");
                        resultIntent.putExtra("book_mark_id", "0");
                        startActivity(resultIntent);
                    }
                });
                return rootView_part;
            }
            // Book Mark ======================================
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 3){
                View rootView_bookmark = inflater.inflate(R.layout.fragment_book_mark, container, false);
                ArrayList<PartItem> arrayList_Parts=new ArrayList<PartItem>();
                dbHelper=new DatabaseHelper(getContext());
                try {
                    dbHelper.checkAndCopyDatabase();
                    dbHelper.openDatabase();
                }catch (SQLException e){}
                try {
                    Cursor cursor=dbHelper.QueryData("SELECT * FROM book_mark ORDER BY id");
                    if ( cursor !=null){
                        if (cursor.moveToFirst()){
                            do {
                                PartItem item=new PartItem();
                                item.setPartId(cursor.getString(0));
                                //item.setPartName(cursor.getString(1));
                                item.setStartPageNo(cursor.getString(1));
                                arrayList_Parts.add(item);
                            }while (cursor.moveToNext());
                        }
                    }
                }catch (SQLException e){}
                adapter_Parts=new Adapter_Parts(getActivity(),R.layout.custom_list_item,arrayList_Parts);
                ListView listView = (ListView)rootView_bookmark.findViewById(R.id.ListV);
                listView.setAdapter(adapter_Parts);
                adapter.notifyDataSetChanged();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView Sure_PageNoVar = (TextView)view.findViewById(R.id.Sure_PageNo);
                        TextView Sura_id_Var = (TextView)view.findViewById(R.id.Sura_id);

                        //Intent resultIntent = new Intent(getApplicationContext(), PagesActivity.class);
                        Intent resultIntent = new Intent(getActivity().getApplicationContext(), PagesActivity.class);
                        resultIntent.putExtra("page", Sure_PageNoVar.getText());
                        resultIntent.putExtra("IsBookMark", "0");
                        resultIntent.putExtra("book_mark_id", String.valueOf(Sura_id_Var.getText()));
                        startActivity(resultIntent);
                    }
                });
                return rootView_bookmark;
            }

            // Serach ======================================
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 4){
                final View rootView_serach = inflater.inflate(R.layout.fragment_serach, container, false);
                final EditText edittext = (EditText)rootView_serach.findViewById(R.id.txt_serach);


                ArrayList<PartItem> arrayList_Parts=new ArrayList<PartItem>();
                dbHelper=new DatabaseHelper(getContext());
                try {
                    dbHelper.checkAndCopyDatabase();
                    dbHelper.openDatabase();
                }catch (SQLException e){}
                try {
                    Cursor cursor=dbHelper.QueryData("SELECT page_no, ayah_text, soura_id, ayah_no FROM soura_text_page_no " +
                            "WHERE (ayah_text like '%العجل%') " +
                            "ORDER BY id");
                    if ( cursor !=null){
                        if (cursor.moveToFirst()){
                            do {
                                PartItem item=new PartItem();
                                item.setPartId(cursor.getString(2));
                                item.setPartName(cursor.getString(1));
                                item.setStartPageNo(cursor.getString(0));
                                arrayList_Parts.add(item);
                            }while (cursor.moveToNext());
                        }
                    }
                }catch (SQLException e){}
                adapter_Parts=new Adapter_Parts(getActivity(),R.layout.custom_list_item,arrayList_Parts);
                ListView listView = (ListView)rootView_serach.findViewById(R.id.ListV2);
                listView.setAdapter(adapter_Parts);
                adapter.notifyDataSetChanged();

                edittext.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // TODO Auto-generated method stub
                        ArrayList<PartItem> arrayList_Parts=new ArrayList<PartItem>();
                        dbHelper=new DatabaseHelper(getContext());
                        try {
                            dbHelper.checkAndCopyDatabase();
                            dbHelper.openDatabase();
                        }catch (SQLException e){}
                        try {
                            Cursor cursor=dbHelper.QueryData("SELECT page_no, ayah_text, soura_id, ayah_no FROM soura_text_page_no " +
                                    "WHERE (ayah_text like '%" + edittext.getText().toString() + "%') " +
                                    "ORDER BY id");
                            if ( cursor !=null){
                                if (cursor.moveToFirst()){
                                    do {
                                        PartItem item=new PartItem();
                                        item.setPartId(cursor.getString(2));
                                        item.setPartName(cursor.getString(1));
                                        item.setStartPageNo(cursor.getString(0));
                                        arrayList_Parts.add(item);
                                    }while (cursor.moveToNext());
                                }
                            }
                        }catch (SQLException e){}
                        adapter_Parts=new Adapter_Parts(getActivity(),R.layout.custom_list_item,arrayList_Parts);
                        ListView listView = (ListView)rootView_serach.findViewById(R.id.ListV2);
                        listView.setAdapter(adapter_Parts);
                        adapter.notifyDataSetChanged();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            /*
                            TextView Sure_PageNoVar = (TextView)view.findViewById(R.id.Sure_PageNo);
                            TextView Sura_id_Var = (TextView)view.findViewById(R.id.Sura_id);

                            //Intent resultIntent = new Intent(getApplicationContext(), PagesActivity.class);
                            Intent resultIntent = new Intent(getActivity().getApplicationContext(), PagesActivity.class);
                            resultIntent.putExtra("page", Sure_PageNoVar.getText());
                            resultIntent.putExtra("IsBookMark", "0");
                            resultIntent.putExtra("book_mark_id", String.valueOf(Sura_id_Var.getText()));
                            startActivity(resultIntent);
                            */
                            }
                        });
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                    }
                });
                return rootView_serach;
            }

            return null;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.Sura_Index);//"Sura Index";
                    //return "Sura Index";
                case 1:
                    return getResources().getString(R.string.Parts_Index);//"Parts Index";
                    //return "Parts Index";
                case 2:
                    return getResources().getString(R.string.Book_Marks);//"Book Marks";
                    //return "Book Marks";
                case 3:
                    return getResources().getString(R.string.SearchTab);//"Book Marks";
                //return "Book Marks";
            }
            return null;
        }
    }
}
