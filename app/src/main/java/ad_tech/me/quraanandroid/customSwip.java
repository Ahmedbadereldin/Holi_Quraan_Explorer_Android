package ad_tech.me.quraanandroid;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Torab on 20-May-16.
 */
public class customSwip extends PagerAdapter {
    //private int [] imageResources ={R.drawable.p0004,R.drawable.p0005,R.drawable.p0006,R.drawable.p0007};
    private int [] imageResources= new int[604];

    private int MainPageCount = imageResources.length; //+ 3; //7;

    private Context ctx;
    private LayoutInflater layoutInflater;

    public customSwip(Context c) {


        /*
        imageResources[0] = c.getResources().getIdentifier("p0004", "drawable", c.getPackageName());
        imageResources[1] = c.getResources().getIdentifier("p0005", "drawable", c.getPackageName());
        imageResources[2] = c.getResources().getIdentifier("p0006", "drawable", c.getPackageName());
        imageResources[3] = c.getResources().getIdentifier("p0007", "drawable", c.getPackageName());
        */

        String f = "";
        int page_no = MainPageCount + 1;
        for(int i=0; i<imageResources.length; i++){
            //f = "p" + String.format("%03d", page_no - 1);// String.valueOf(page_no - 1); //String.format("%03d", page_no - 1);
            f = "p" + arabicToDecimal(String.format("%03d", page_no - 1));
            page_no = page_no - 1;
            imageResources[i] = c.getResources().getIdentifier(f, "drawable", c.getPackageName());
        }

        ctx=c;
    }

    private static final String arabic = "\u06f0\u06f1\u06f2\u06f3\u06f4\u06f5\u06f6\u06f7\u06f8\u06f9";
    private static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }
    @Override
    public int getCount() {

        return imageResources.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        layoutInflater= (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=layoutInflater.inflate(R.layout.activity_custom_swip,container,false);
        ImageView imageView=(ImageView) itemView.findViewById(R.id.swip_image_view);
        //TextView textView=(TextView) itemView.findViewById(R.id.imageCount);
        imageView.setImageResource(imageResources[position]);
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setScaleType(ImageView.ScaleType.FIT_START);
        //textView.setText("Image Counter :"+position);
        container.addView(itemView);
        return itemView;
    }

/*
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
    */

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return  (view==object);
    }


    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

}
