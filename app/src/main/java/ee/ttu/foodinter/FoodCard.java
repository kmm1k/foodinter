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
    private String restoName; //Base64
    private String restoInfo; //Base64

    public FoodCard() {

    }
    public FoodCard(String userId, Bitmap imageBitmap) {
        this.userId = userId;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        this.image = imageBase64;
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

    public String getRestoName() {
        return restoName;
    }

    public void setRestoName(String restoName) {
        this.restoName = restoName;
    }

    public String getRestoInfo() {
        return restoInfo;
    }

    public void setRestoInfo(String restoInfo) {
        this.restoInfo = restoInfo;
    }
}
