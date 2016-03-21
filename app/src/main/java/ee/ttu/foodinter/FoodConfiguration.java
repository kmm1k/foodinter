package ee.ttu.foodinter;

/**
 * Created by kmm on 20.03.2016.
 */
public class FoodConfiguration {
    public static String SERVER_ADDRESS = "https://foodinter.firebaseio.com/";
    public static String USER_ID = "b0cd8383-c5c3-4b31-948f-062963ac0043";
    public static final String FOURSQUARE_LINK = "https://api.foursquare.com/v2/venues/explore";
    public static final String FOURSQUARE_USER_ID = "WX1K0WOBFIIAXGKP4VIMWO052Y1FSKEHL05SH1IIPKGRFUWK";
    public static final String FOURSQUARE_USER_SECRET = "CJTH1DZRBJGNC1VSVVDQGLIWEVLDLYG1UL5GOTECKZ3HZULY";
    public static final String FOURSQUARE_BUILT_LINK = FOURSQUARE_LINK
            +"?client_id="+FOURSQUARE_USER_ID
            +"&client_secret="+FOURSQUARE_USER_SECRET;
    public static FoodUser FOOD_USER;
}
