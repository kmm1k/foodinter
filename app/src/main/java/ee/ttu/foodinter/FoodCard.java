package ee.ttu.foodinter;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by kmm on 16.03.2016.
 */
public class FoodCard {
    private String userId;
    private String image; //Base64
    private String placeName = "";
    private String placeInfo = "";

    public FoodCard(String userId, Bitmap imageBitmap, String description, String name) {
        this.userId = userId;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        this.image = imageBase64;
        placeInfo = description;
        placeName = name;
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
}
