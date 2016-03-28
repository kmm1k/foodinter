package ee.ttu.foodinter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.client.Firebase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageSingleActivity extends AppCompatActivity {
    @Bind(R.id.single_place_image)ImageView image;
    @Bind(R.id.delete_picture_button)Button button;
    private String foodCardKey;
    private String position = "";
    private Firebase firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_single);
        ButterKnife.bind(this);

        firebase = new Firebase("https://foodinter.firebaseio.com/");

        FoodCard place = getIntent().getParcelableExtra("place");
        foodCardKey = getIntent().getStringExtra("key");
        position = getIntent().getStringExtra("position");
        image.setImageBitmap(stringToBitmap(place.getImage()));
        Log.d("lammas", ""+foodCardKey);
        if (foodCardKey == null) {
            button.setText("Go Back");
        } else {
            button.setText("Delete");
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodCardKey == null) {
                    finish();
                    //firebase.child("FoodUsers").child(FoodConfiguration.USER_ID).child("placeNames").child(position).removeValue();
                }else {
                    firebase.child("FoodCards").child(foodCardKey).removeValue();
                }
                //TODO: remove item from database where user has liked it
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
