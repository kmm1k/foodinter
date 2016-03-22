package ee.ttu.foodinter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Firebase firebase;
    private Button registerButton;
    private Button loginButton;
    private TextView emailText;
    private TextView passwordText;
    private TextView errorMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerButton = (Button) findViewById(R.id.registerButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        emailText = (TextView) findViewById(R.id.emailText);
        passwordText = (TextView) findViewById(R.id.passwordText);
        errorMessages = (TextView) findViewById(R.id.errorMssages);


        firebase = new Firebase("https://foodinter.firebaseio.com/");



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logUserIn();
            }
        });


    }
    private void registerUser() {
        Log.d("LogMain", "" + emailText.getText());
        firebase.createUser(""+emailText.getText(), ""+passwordText.getText(), new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                System.out.println("Successfully created user account with uid: " + stringObjectMap.get("uid"));
                Log.d("LogMain", "register user");
                printToast();
                addFoodUser(""+emailText.getText(), stringObjectMap.get("uid").toString());
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                errorMessages.setText(""+firebaseError);
                Log.d("LogMain", "" + firebaseError);
            }
        });
    }

    private void addFoodUser(String userName, String uid) {
        ArrayList<String> places = new ArrayList<>();
        places.add("item");
        FoodUser foodUser = new FoodUser(userName, uid, places);
        firebase.child("FoodUsers/"+uid).setValue(foodUser);
    }

    private void getFoodUser(final String uid) {
        Firebase userSnap = firebase.child("FoodUsers/"+uid);
        userSnap.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FoodConfiguration.FOOD_USER = dataSnapshot.getValue(FoodUser.class);
                FoodConfiguration.USER_ID = uid;
                showLoggedInView(uid);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("LogMain", "" + firebaseError);
            }
        });
    }

    private void showLoggedInView(String uid) {
        Intent intent = new Intent(MainActivity.this, DrawerActivity.class);
        startActivity(intent);
    }

    private void printToast() {
        Toast.makeText(this, "user registered, please log in", Toast.LENGTH_SHORT).show();
    }

    private void logUserIn() {
        firebase.authWithPassword( ""+emailText.getText(), ""+passwordText.getText(), new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                Log.d("LogMain", "log user in");
                getFoodUser(authData.getUid());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                errorMessages.setText(""+firebaseError);
                Log.d("LogMain", "" + firebaseError);
            }
        });
    }

    //firebase.createUser("username", "password", ....)
    //firebase.authWithPassword("username", "password")
}
