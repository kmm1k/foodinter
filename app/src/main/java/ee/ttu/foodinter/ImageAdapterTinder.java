package ee.ttu.foodinter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by kmm on 21.03.2016.
 */
public class ImageAdapterTinder extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<Bitmap> foodPictures;

    public ImageAdapterTinder(Context c) {
        mContext = c;    }

    public ImageAdapterTinder(Context mContext, ArrayList<Bitmap> foodPictures) {
        this.mContext = mContext;
        this.foodPictures = foodPictures;
        layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    public int getCount() {
        return foodPictures.size();
    }

    public Object getItem(int position) {
        return null;    }

    public long getItemId(int position) {
        return 0;    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = layoutInflater.inflate(R.layout.list_item_entry, parent,  false);
        }
        ImageView profilePic = (ImageView)listItem.findViewById(R.id.picture);

        Log.d("lammas", "hai "+foodPictures.get(position));
        profilePic.setImageBitmap(foodPictures.get(position));
        return listItem;
    }
}
