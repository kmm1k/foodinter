package ee.ttu.foodinter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapterMatches extends BaseAdapter {

    private Context mContext;
    private Bitmap[] foodPictures;

    public ImageAdapterMatches(Context c) {
        mContext = c;    }

    public ImageAdapterMatches(Context mContext, Bitmap[] foodPictures) {
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
            imageView.setLayoutParams(new GridView.LayoutParams(300, 256));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(foodPictures[position]);
        return imageView;
    }

}