package ee.ttu.foodinter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ee.ttu.foodinter.request.HttpManager;
import ee.ttu.foodinter.request.JSONParser;
import ee.ttu.foodinter.request.RequestPackage;

/**
 * Created by kmm on 20.03.2016.
 */
public class UploaderActivity extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private ImageView preview;
    private Firebase firebase;
    @Bind(R.id.upload_image) Button button1;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String mLatitudeText;
    private String mLongitudeText;
    private String strJson = "";
    Bitmap image;
    TextView userInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    //Toast.makeText(getActivity(), "Back press", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_uploader, container, false);
        userInfo = (TextView) view.findViewById(R.id.user_info);
        ButterKnife.bind(this, view);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(view);
            }
        });


        preview = (ImageView) view.findViewById(R.id.imageView);
        firebase = new Firebase(FoodConfiguration.SERVER_ADDRESS);
        final GridView gridview = (GridView) view.findViewById(R.id.gridview);
        userInfo.setText(FoodConfiguration.FOOD_USER.getUserName());


        firebase.child("FoodCards").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<FoodCard> foodCards = new ArrayList<>();
                final ArrayList<String> foodCardKeys = new ArrayList<>();

                for (DataSnapshot foodCardSnapshot: dataSnapshot.getChildren()) {
                    FoodCard foodCard = foodCardSnapshot.getValue(FoodCard.class);
                    Log.d("lammas", foodCardSnapshot.getKey());
                    if (FoodConfiguration.USER_ID.equals(foodCard.getUserId())){
                        foodCardKeys.add(foodCardSnapshot.getKey());
                        foodCards.add(foodCard);
                    }
                }
                int pictureCount = foodCards.size();
                Log.d("lammas", ""+pictureCount);
                Bitmap[] foodPictures = new Bitmap[pictureCount];
                for (int i = 0; i < pictureCount; i++) {
                    foodPictures[i] = stringToBitmap(foodCards.get(i).getImage());
                }
                gridview.setAdapter(new ImageAdapter(view.getContext(), foodPictures));
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        Intent intent = new Intent(getContext(), ImageSingleActivity.class);
                        intent.putExtra("place",  foodCards.get(position));
                        intent.putExtra("key",  foodCardKeys.get(position));
                        startActivity(intent);
                        //deleteImageDialog(foodCardKeys.get(position), foodCards.get(position));
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("lammas", "failed to get info from db");
            }
        });

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(view.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        return view;
    }

    private void deleteImageDialog(final String foodCardKey, final FoodCard foodCard) {
        final String[] items = {"delete food", "cancel"};

        ImageView image = new ImageView(getActivity());
        image.setImageBitmap(stringToBitmap(foodCard.getImage()));
        image.setMinimumHeight(200);
        image.setMinimumWidth(200);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(foodCard.getPlaceName());
        builder.setMessage(foodCard.getPlaceInfo())
                .setPositiveButton(items[1], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(items[0], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firebase.child("FoodCards").child(foodCardKey).removeValue();
                    }
                }).
        setView(image);
        // Create the AlertDialog object and return it
        builder.show();


    }
    private void deleteCard() {

    }

    private Bitmap stringToBitmap (String imageString) {
        Bitmap imageBitmap = null;
        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
        imageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        //preview.setImageBitmap(imageBitmap);
        return imageBitmap;
    }

    public void chooseImage(View view) {
        //Toast.makeText(this, "choose image", Toast.LENGTH_SHORT).show();
        final String[] items = {"take photo", "choose from library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (items[which]) {
                    case "take photo":
                        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePhotoIntent, REQUEST_CAMERA);
                        break;
                    case "choose from library":
                        Intent chooseImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        chooseImageIntent.setType("image/*");
                        getActivity().startActivityForResult(Intent.createChooser(chooseImageIntent, "Select file"), SELECT_FILE);
                        break;
                    default:
                        dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                image = (Bitmap) data.getExtras().get("data");
                //preview.setImageBitmap(image);
                addImageToDatabaseWithInfo();
            } else if (requestCode == SELECT_FILE) {
                Uri selectImageUri = data.getData();
                String imagePath = getRealPathFromUri(selectImageUri);
                Log.d("lammas", ""+imagePath);
                image = BitmapFactory.decodeFile(imagePath);
                //preview.setImageBitmap(image);
                Log.d("lammas", ""+image.getByteCount());
                //addImageToDatabaseWithInfo();
            }
        }
    }

    private void addImageToDatabaseWithInfo() {
        getLocation();
        try {
            getNearbyPlaces();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showListOfPlaces(final List<FoodPlace> places) {
        final String noPlaceString = "This place does not exist here.";
        int size = places.size();
        final String[] placeNames = new String[size +1];

        for (int i = 0; i < size; i++) {
            placeNames[i] = places.get(i).getName();
        }
        placeNames[size]= noPlaceString;

        View view = getView();
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Choose place where you are");

        builder.setItems(placeNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("lammas", ""+placeNames[which]);
                String description = "";
                if (placeNames[which].equals(noPlaceString)) {
                    getPlaceDescription();
                } else {
                    FoodPlace place =  places.get(which);
                    description = "location: "+place.getLocation()+" cat: "
                            +place.getCategory()+" url: "
                            +place.getUrl()+"";
                    uploadPhoto(description, place.getName());
                }
            }
        });
        builder.show();
    }

    private void getPlaceDescription() {
        final String[] name = {""};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Insert name of the place.");

        final EditText nameInput = new EditText(getActivity());
        nameInput.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(nameInput);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name[0] = nameInput.getText().toString();
                uploadPhoto("", name[0]);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void uploadPhoto(String description, String name) {
        FoodCard foodCard = new FoodCard(FoodConfiguration.USER_ID, image, description, name);
        firebase.child("FoodCards").push().setValue(foodCard);
    }

    private void getLocation() {
        onStart();
        onStop();
    }

    private void getNearbyPlaces() throws IOException {

        RequestPackage rp = new RequestPackage();
        rp.setParam("client_id", "WX1K0WOBFIIAXGKP4VIMWO052Y1FSKEHL05SH1IIPKGRFUWK");
        rp.setParam("client_secret", "CJTH1DZRBJGNC1VSVVDQGLIWEVLDLYG1UL5GOTECKZ3HZULY");
        rp.setParam("ll", ""+mLatitudeText+","+mLongitudeText);
        rp.setParam("v", "20130815");
        rp.setParam("section", "food");
        rp.setParam("sortByDistance", "1");
        rp.setUri("https://api.foursquare.com/v2/venues/explore");
        Task task = new Task();
        task.execute(rp);
    }


    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private String getRealPathFromUri(Uri contentUri) {
        String selectedImagePath = null;
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            selectedImagePath = cursor.getString(column_index);
        }
        if (cursor != null) {
            cursor.close();
        }
        return selectedImagePath;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getView().getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getView().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
            Log.d("lammas", "lat: "+mLastLocation.getLatitude()+" lon: "+mLastLocation.getLongitude());
        }
        //Toast.makeText(this, "info: "+mLatitudeText +mLongitudeText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class Task extends AsyncTask<RequestPackage, String, String> {

        @Override
        protected String doInBackground(RequestPackage... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            List<FoodPlace> places = null;
            if(result == null) {
                Log.d("lammas", "somthing went wrong, got no result");
                Toast.makeText(getActivity(), "info: did not get any places", Toast.LENGTH_SHORT).show();
            } else {
                //Log.d("lammas", result);
                places = JSONParser.parseItemObjects(result);
            }
            showListOfPlaces(places);
        }

    }
}
