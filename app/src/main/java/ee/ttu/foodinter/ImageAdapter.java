package ee.ttu.foodinter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private Bitmap[]mis_fotos;

    public ImageAdapter(Context c) {
        mContext = c;    }

    public ImageAdapter(Context mContext, Bitmap[] mis_fotos) {
        this.mContext = mContext;
        this.mis_fotos = mis_fotos;
    }

    public int getCount() {
        return mis_fotos.length;
    }

    public Object getItem(int position) {
        return null;    }

    public long getItemId(int position) {
        return 0;    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(mis_fotos[position]);
        return imageView;
    }

}