package ad_tech.me.quraanandroid;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duyhoang on 11/30/2015.
 */
public class Adapter extends ArrayAdapter<SuraItem> {
    private Activity activity;
    int id;
    ArrayList<SuraItem> suraItems;
    public Adapter(Activity context, int resource, ArrayList<SuraItem> objects) {
        super(context, resource, objects);
        this.activity=context;
        this.id=resource;
        this.suraItems=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            LayoutInflater inflater=activity.getLayoutInflater();
            convertView=inflater.inflate(id,null);
        }
        SuraItem suraItem=suraItems.get(position);
        TextView sura_id= (TextView) convertView.findViewById(R.id.Sura_id);
        TextView sura_title= (TextView) convertView.findViewById(R.id.Sura_title);
        TextView sure_PageNo= (TextView) convertView.findViewById(R.id.Sure_PageNo);
        TextView sora_location= (TextView) convertView.findViewById(R.id.Sora_location);

        sura_id.setText(suraItem.getSuraID());
        sura_title.setText(suraItem.getSuraName());
        sure_PageNo.setText(suraItem.getPageNo());
        sora_location.setText(suraItem.getPlace());

        return convertView;
    }



}
