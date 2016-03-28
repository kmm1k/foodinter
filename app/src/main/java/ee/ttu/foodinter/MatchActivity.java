package ee.ttu.foodinter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class MatchActivity extends AppCompatActivity {
    private Firebase firebase;
    private String placeName;
    TextView placeNameText;
    TextView placeInfoText;
    Button deleteMatchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        placeNameText = (TextView) this.findViewById(R.id.place_name);
        placeInfoText = (TextView) this.findViewById(R.id.place_info);
        deleteMatchButton = (Button) this.findViewById(R.id.delete_match);

        placeName = getIntent().getStringExtra("name");

        firebase = new Firebase(FoodConfiguration.SERVER_ADDRESS);
        final GridView gridview = (GridView) this.findViewById(R.id.gridview);
        firebase.child("FoodCards").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<FoodCard> foodCards = new ArrayList<>();

                for (DataSnapshot foodCardSnapshot: dataSnapshot.getChildren()) {
                    FoodCard foodCard = foodCardSnapshot.getValue(FoodCard.class);
                    Log.d("lammas", foodCardSnapshot.getKey());
                    if (placeName.equals(foodCard.getPlaceName())){
                        foodCards.add(foodCard);
                    }

                }
                placeNameText.setText(foodCards.get(0).getPlaceName());
                placeInfoText.setText(foodCards.get(0).getPlaceInfo());
                int pictureCount = foodCards.size();
                Log.d("lammas", ""+pictureCount);
                Bitmap[] foodPictures = new Bitmap[pictureCount];
                for (int i = 0; i < pictureCount; i++) {
                    foodPictures[i] = stringToBitmap(foodCards.get(i).getImage());
                }
                gridview.setAdapter(new ImageAdapterMatches(getBaseContext(), foodPictures));
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        Intent intent = new Intent(getBaseContext(), ImageSingleActivity.class);
                        intent.putExtra("place",  foodCards.get(position));
                        intent.putExtra("position",  position);
                        startActivity(intent);
                    }
                });
                boolean matchIsSet = false;
                for (String placeName: FoodConfiguration.FOOD_USER.getPlaceNames()) {
                    if (placeName.equals(foodCards.get(0).getPlaceName())) {
                        matchIsSet = true;
                    }
                }
                if (matchIsSet) {
                    deleteMatchButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = 0;
                            for (String placeName: FoodConfiguration.FOOD_USER.getPlaceNames()) {
                                if (placeName.equals(foodCards.get(0).getPlaceName())) {
                                    break;
                                }
                                position++;
                            }
                            ArrayList<String> placeNames = FoodConfiguration.FOOD_USER.getPlaceNames();
                            placeNames.remove(position);
                            firebase.child("FoodUsers").child(FoodConfiguration.USER_ID).child("placeNames").setValue(placeNames);

                        }
                    });
                } else {
                    deleteMatchButton.setText("Go Back");
                    deleteMatchButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("lammas", "failed to get info from db");
            }

        });
    }

    private Bitmap stringToBitmap (String imageString) {
        Bitmap imageBitmap = null;
        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
        imageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        //preview.setImageBitmap(imageBitmap);
        return imageBitmap;
    }
}
