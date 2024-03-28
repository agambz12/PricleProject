package com.example.myapplication.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.AlertDialogUtils;
import com.example.myapplication.DataBaseManager;
import com.example.myapplication.R;
import com.example.myapplication.models.Location;
import com.example.myapplication.models.RecycleBin;
import com.example.myapplication.models.RecycleBinType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class CreateRecycleBinActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextInputEditText addressET;
    private Location location = new Location();

    private static final int REQUEST_CODE = 3;

    private Bitmap imageBitmap;
    private ImageView image;
    private String imageUrl;
    private StorageReference storageRef;
    private RecycleBinType selectedType = RecycleBinType.PAPER;
    private RecycleBinType[] types = RecycleBinType.values();
    private String uid;


    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                    imageBitmap = photo;
                    image.setImageBitmap(photo);
                }
            }
    );

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        Uri uri = result.getData().getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        imageBitmap = bitmap;
                        image.setImageBitmap(bitmap);
                    } catch (Exception e) {
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recycle_bin_activty);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        Button cameraBT = findViewById(R.id.camera);
        Button galleryBT = findViewById(R.id.gallery);
        Button saveBT = findViewById(R.id.save);
        image = findViewById(R.id.image);
        addressET = findViewById(R.id.address);
        addressET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateRecycleBinActivity.this, FindMyLocationActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        cameraBT.setOnClickListener(v -> {
            openCamera();
        });
        galleryBT.setOnClickListener(v -> {
            openPhotoGallery();
        });
        saveBT.setOnClickListener(view -> {
                    if (addressET.getText() == null ||  addressET.getText().toString().equals("")) {
                        AlertDialogUtils.showAlertDialog(this, getString(R.string.error), getString(R.string.no_location));
                    } else {
                        saveRecycleBin();
                    }
                }
        );

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, types);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        spinner.setOnItemSelectedListener(this);


    }

    private void saveRecycleBin() {
        uploadImageToStorage(new Runnable() {
            @Override
            public void run() {
                saveRecycleBinToDataBase();
            }
        });
    }

    private void saveRecycleBinToDataBase() {
        RecycleBin recycleBin = new RecycleBin(uid, imageUrl, location, selectedType);
        DataBaseManager.createRecycleBin(recycleBin, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateRecycleBinActivity.this, R.string.create_successfully, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    AlertDialogUtils.showAlertDialog(CreateRecycleBinActivity.this, getString(R.string.error), task.getException().getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            Address address = (Address) data.getParcelableExtra("address");
            addressET.setText(address.getAddressLine(0));
            location.setAddress(addressET.getText().toString());
            location.latitude = address.getLatitude();
            location.longitude = address.getLongitude();

        }
    }

    private void uploadImageToStorage(Runnable onDone) {
        // the image name should be the user uuid - uniqe
        uid = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child("images/" + uid + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();
        // Upload file to Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(imageData);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                imageUrl = uri.toString();
                onDone.run();
            });
        }).addOnFailureListener(e -> {
            // Handle failed upload
            Log.e("TAG", "Upload failed: " + e.getMessage());
        });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraActivityResultLauncher.launch(takePictureIntent);
    }

    private void openPhotoGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        galleryActivityResultLauncher.launch(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedType = types[adapterView.getSelectedItemPosition()];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}