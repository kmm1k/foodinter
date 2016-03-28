package ee.ttu.foodinter;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by kmm on 16.03.2016.
 */
public class FoodCard implements Parcelable {
    private String userId;
    private String image; //Base64
    private String placeName;
    private String placeInfo;

    public FoodCard(String userId, Bitmap imageBitmap, String description, String name) {
        this.userId = userId;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        this.image = imageBase64;
        this.placeInfo = description;
        this.placeName = name;
    }

    public FoodCard() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceInfo() {
        return placeInfo;
    }

    public void setPlaceInfo(String placeInfo) {
        this.placeInfo = placeInfo;
    }

    protected FoodCard(Parcel in) {
        userId = in.readString();
        image = in.readString();
        placeName = in.readString();
        placeInfo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(image);
        dest.writeString(placeName);
        dest.writeString(placeInfo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FoodCard> CREATOR = new Parcelable.Creator<FoodCard>() {
        @Override
        public FoodCard createFromParcel(Parcel in) {
            return new FoodCard(in);
        }

        @Override
        public FoodCard[] newArray(int size) {
            return new FoodCard[size];
        }
    };
}