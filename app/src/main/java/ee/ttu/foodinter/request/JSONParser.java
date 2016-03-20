package ee.ttu.foodinter.request;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ee.ttu.foodinter.FoodPlace;

public class JSONParser {
    public static List<FoodPlace> parseItemObjects(String content) {
        List<FoodPlace> itemList = new ArrayList<FoodPlace>();

        try {
            JSONObject ar = new JSONObject(content);
            JSONArray jsonGroups = ar.getJSONObject("response").getJSONArray("groups");
            JSONObject jsonItemsObject = (JSONObject) jsonGroups.get(0);
            JSONArray jsonItems = jsonItemsObject.getJSONArray("items");
            Log.d("lammas", jsonItems.toString());
            //Log.d("lammas", ""+jsonItems.length());


            for (int i = 0; i < jsonItems.length(); i++) {
                JSONObject obj = jsonItems.getJSONObject(i);
                FoodPlace item = new FoodPlace();
                JSONObject jsonVenue = obj.getJSONObject("venue");
                Log.d("lammas", jsonVenue.getString("name"));
                try {
                    item.setName(jsonVenue.getString("name"));
                    item.setLocation(jsonVenue.getJSONObject("location").getString("address"));
                    item.setCategory(jsonVenue.getJSONObject("categories").getString("name"));
                    item.setUrl(jsonVenue.getString("url"));
                } catch (Exception e) {

                }


                itemList.add(item);
                //Log.d("lammas", "item tostring: " + item.toString());
            }
            if(itemList == null) {
                Log.d("lammas", "JSON drive object parser on katki");
            }
            else {
                Log.d("lammas", "listi suurus: " + itemList.size());
            }
            return itemList;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> parseItems(String content) {
        List<String> items = new ArrayList<String>();

        try {
            JSONObject ar = new JSONObject(content);

            int i = 0;

            while(!ar.isNull(""+i)) {

                JSONObject obj = ar.getJSONObject("" + i);
                Log.d("lammas", obj.toString());
                items.add(obj.getString("location"));
                i++;
            }

            Log.d("lammas", items.get(0));

            if(items == null) {
                Log.d("lammas", "JSON location parser on katki");
            }
            else {
                Log.d("lammas", "listi suurus: " + items.size());
            }
            return items;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
