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
    private Bitmap[] foodPictures;

    public ImageAdapter(Context c) {
        mContext = c;    }

    public ImageAdapter(Context mContext, Bitmap[] foodPictures) {
        this.mContext = mContext;
        this.foodPictures = foodPictures;
    }

    public int getCount() {
        return foodPictures.length;
    }

    public Object getItem(int position) {
        return null;    }

    public long getItemId(int position) {
        return 0;    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(foodPictures[position]);
        return imageView;
    }

}