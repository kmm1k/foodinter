package ee.ttu.foodinter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TinderActivity extends Fragment {

    private ArrayList<Bitmap> al;
    //private ArrayAdapter<String> arrayAdapter;
    private ImageAdapterTinder baseAdapter;
    private int i;
    private Firebase firebase;

    @Bind(R.id.frame)
    SwipeFlingAdapterView flingContainer;
    private ArrayList<FoodCard> places;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tinder, container, false);
        ButterKnife.bind(this, view);

        firebase = new Firebase("https://foodinter.firebaseio.com/");
        firebase.child("FoodCards").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<FoodCard> foodCards = new ArrayList<>();

                for (DataSnapshot foodCardSnapshot : dataSnapshot.getChildren()) {
                    FoodCard foodCard = foodCardSnapshot.getValue(FoodCard.class);
                    Log.d("lammas", foodCardSnapshot.getKey());
                    foodCards.add(foodCard);

                    //TODO: remove comments when ready
                    /*if (!FoodConfiguration.USER_ID.equals(foodCard.getUserId())){
                        foodCards.add(foodCard);
                    }*/
                }
                parseFoodCardsIntoPlaces(foodCards);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_camera);
        al = new ArrayList<>();
        i = 0;




        //choose your favorite adapter
        //arrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.item, R.id.helloText, al );
        baseAdapter = new ImageAdapterTinder(getActivity().getApplicationContext(), al);


        //flingContainer.setAdapter(arrayAdapter);
        flingContainer.setAdapter(baseAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                //arrayAdapter.notifyDataSetChanged();
                baseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                //makeToast(TinderActivity.this, "Left!");
                i++;
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                //makeToast(TinderActivity.this, "Right!");
                addPlace(places.get(i).getPlaceName());
                showAlerBox();
                i++;
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                /*Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_camera);
                al.add(bm);
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;*/
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });
        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                //makeToast(TinderActivity.this, "Clicked!");
            }
        });

        return view;
    }

    private void parseFoodCardsIntoPlaces(ArrayList<FoodCard> foodCards) {
        places = new ArrayList<>();
        for (FoodCard foodCard : foodCards) {
            boolean inList = false;
            for (FoodCard placeFoodCard : places) {
                if (foodCard.getPlaceName().equals(placeFoodCard.getPlaceName())) {
                    inList = true;
                }
            }
            //TODO: userfoodcard iteration to check what user has
            if (!inList) {
                places.add(foodCard);
            }
        }
        parseToBitmap(places);
    }

    private void parseToBitmap(ArrayList<FoodCard> places) {
        int pictureCount = places.size();
        Log.d("lammas", "tinder "+pictureCount);
        for (int i = 0; i < pictureCount; i++) {
            //foodPictures[i] = stringToBitmap(places.get(i).getImage());
            al.add(stringToBitmap(places.get(i).getImage()));
            baseAdapter.notifyDataSetChanged();
        }

    }
    private Bitmap stringToBitmap (String imageString) {
        Bitmap imageBitmap = null;
        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
        imageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        //preview.setImageBitmap(imageBitmap);
        return imageBitmap;
    }

    private void addPlace(String placeName) {
        ArrayList<String> placeNames = FoodConfiguration.FOOD_USER.getPlaceNames();
        boolean inList = false;
        for (String place : placeNames) {
            if (place.equals(placeName)) {
                inList = true;
            }
        }
        if (!inList) {
            placeNames.add(placeName);
        }
        FoodConfiguration.FOOD_USER.setPlaceNames(placeNames);
        firebase.child("FoodUsers/"+FoodConfiguration.FOOD_USER.getUid()).setValue(FoodConfiguration.FOOD_USER);
    }


    @OnClick(R.id.right)
    public void right() {
        /**
         * Trigger the right event manually.
         */
        flingContainer.getTopCardListener().selectRight();
    }

    @OnClick(R.id.left)
    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }


    private void showAlerBox() {
        View view = this.getView();
        final String[] items = {"go to restaurant", "continue swiping"};

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Continue swiping?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (items[which]) {
                    case "go to restaurant":

                        break;
                    case "continue swiping":
                        dialog.dismiss();
                        break;
                    default:
                        dialog.dismiss();
                }
            }
        });
        builder.show();
    }


}