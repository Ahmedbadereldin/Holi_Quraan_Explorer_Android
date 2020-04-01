package ad_tech.me.quraanandroid;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adel_2 on 2016-10-22.
 */

public class Adapter_Parts extends ArrayAdapter<PartItem> {
    private Activity activity;
    int id;
    ArrayList<PartItem> partItems;
    public Adapter_Parts(Activity context, int resource, ArrayList<PartItem> objects) {
        super(context, resource, objects);
        this.activity=context;
        this.id=resource;
        this.partItems=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            LayoutInflater inflater=activity.getLayoutInflater();
            convertView=inflater.inflate(id,null);
        }
        PartItem partItem=partItems.get(position);
        TextView sura_id= (TextView) convertView.findViewById(R.id.Sura_id);
        TextView sura_title= (TextView) convertView.findViewById(R.id.Sura_title);
        TextView sure_PageNo= (TextView) convertView.findViewById(R.id.Sure_PageNo);
        //TextView sora_location= (TextView) convertView.findViewById(R.id.Sora_location);

        sura_id.setText(partItem.getPartId());
        sura_title.setText(partItem.getPartName());
        sure_PageNo.setText(partItem.getStartPageNo());
        //sora_location.setText(partItem.getPlace());

        return convertView;
    }
}
