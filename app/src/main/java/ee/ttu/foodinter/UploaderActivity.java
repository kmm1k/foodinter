package ee.ttu.foodinter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kmm on 20.03.2016.
 */
public class UploaderActivity extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private ImageView preview;
    private Firebase firebase;
    private int RESULT_OK = -1;
    @Bind(R.id.upload_image) Button button1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_uploader, container, false);
        ButterKnife.bind(this, view);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(view);
            }
        });


        preview = (ImageView) view.findViewById(R.id.imageView);
        firebase = new Firebase(FoodConfiguration.SERVER_ADDRESS);
        firebase.child("foodCards").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FoodCard lastFoodCard = null;
                for (DataSnapshot foodCardSnapshot: dataSnapshot.getChildren()) {
                    FoodCard foodCard = foodCardSnapshot.getValue(FoodCard.class);
                    showImage(foodCard);
                    lastFoodCard = foodCard;
                    if (lastFoodCard != null) {
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return view;
    }

    private void showImage (FoodCard foodCard) {
        Bitmap imageBitmap = null;
        String encodedImage = foodCard.getImage();
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        imageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        preview.setImageBitmap(imageBitmap);
    }

    private void uploadPhoto(Bitmap image) {
        FoodCard foodCard = new FoodCard(FoodConfiguration.USER_ID, image);
        firebase.child("FoodCards").push().setValue(foodCard);
    }

    private void showListOfRestos() {

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
                        startActivityForResult(Intent.createChooser(chooseImageIntent, "Select file"), SELECT_FILE);
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
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                //preview.setImageBitmap(image);
                uploadPhoto(image);
            } else if (requestCode == SELECT_FILE) {
                Uri selectImageUri = data.getData();
                String imagePath = getRealPathFromUri(selectImageUri);
                Bitmap image = BitmapFactory.decodeFile(imagePath);
                //preview.setImageBitmap(image);
                uploadPhoto(image);
            }
        }
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

}
