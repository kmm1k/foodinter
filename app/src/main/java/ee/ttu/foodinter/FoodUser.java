package ee.ttu.foodinter;

import java.util.ArrayList;

/**
 * Created by kmm on 21.03.2016.
 */
public class FoodUser {

    private String uid;
    private String userName;
    private ArrayList<String> placeNames;

    public FoodUser(String userName, String uid, ArrayList placeNames) {
        this.userName = userName;
        this.uid = uid;
        this.placeNames = placeNames;
    }

    private FoodUser() {

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<String> getPlaceNames() {
        return placeNames;
    }

    public void setPlaceNames(ArrayList<String> placeNames) {
        this.placeNames = placeNames;
    }
}
