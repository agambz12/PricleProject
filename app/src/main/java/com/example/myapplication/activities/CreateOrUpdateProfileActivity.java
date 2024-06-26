package com.example.myapplication.activities;

import static android.provider.MediaStore.Images.Media.getBitmap;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.AlertDialogUtils;
import com.example.myapplication.DataBaseManager;
import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class CreateOrUpdateProfileActivity extends AppCompatActivity {
    
    public static final String CREATE_PROFILE = "CREATE_PROFILE";
    private Bitmap imageBitmap;
    private ImageView profileImg;
    private EditText ETname;
    private EditText ETlastName;
    private EditText ETbirth;
    private EditText ETphoneNumber;
    private EditText ETemail;
    private EditText ETuserName;
    private Button createBT;
    private StorageReference storageRef;
    private FirebaseAuth auth;
    private String imageDownloadUrl;
    private TextView title;
    private User currentUser;
    private ProgressDialog progressDialog;

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                    imageBitmap = photo;
                    profileImg.setImageBitmap(photo);
                }
            }
    );

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        Uri uri = result.getData().getData();
                        Bitmap bitmap = getBitmap(this.getContentResolver(), uri);
                        imageBitmap = bitmap;
                        profileImg.setImageBitmap(bitmap);
                    } catch (Exception e) {
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        currentUser = (User) getIntent().getSerializableExtra("user");
        auth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference();
        profileImg = findViewById(R.id.profile_image);
        Button cameraBT = findViewById(R.id.camera);
        Button galleryBT = findViewById(R.id.gallery);
        ETemail=findViewById(R.id.email);
        ETemail.setText(auth.getCurrentUser().getEmail());
        ETname=findViewById(R.id.name);
        ETlastName=findViewById(R.id.last_name);
        ETphoneNumber=findViewById(R.id.pNumber);
        title = findViewById(R.id.title);

        cameraBT.setOnClickListener(v-> {
            openCamera();
        });

        galleryBT.setOnClickListener(v-> {
            openPhotoGallery();
        });

        createBT = findViewById(R.id.create);
        createBT.setOnClickListener(v-> createProfile());
        if (currentUser != null) {
            updateUserFields();
        }


    }

    private void updateUserFields() {
        ETemail.setText(currentUser.getEmail());
        ETname.setText(currentUser.getFirstName());
        ETlastName.setText(currentUser.getLastName());
        ETphoneNumber.setText(currentUser.getPhone());
        createBT.setText(getString(R.string.save));
        Glide.with(this).load(currentUser.getImage()).centerCrop()
                .into(profileImg);
        title.setText(getString(R.string.update_your_account_details));
    }

    private void createProfile() {
        boolean isValidInputs = true;
        if (ETname.getText().toString().isEmpty()) {
            ETname.setError(getString(R.string.please_enter_your_name));
            isValidInputs = false;
        }
        if (ETphoneNumber.getText().toString().isEmpty()) {
            ETphoneNumber.setError(getString(R.string.please_enter_your_phone));
            isValidInputs = false;
        }
        if (ETlastName.getText().toString().isEmpty()) {
            ETlastName.setError(getString(R.string.please_enter_your_last_name));
            isValidInputs = false;
        }

        if (isValidInputs) {
            saveUser();
        }
    }

    private void saveUser() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.please_wait), true, false);
        uploadImageToStorage(new Runnable() {
            @Override
            public void run() {
                User user = new User(ETname.getText().toString(), ETlastName.getText().toString(),
                        ETphoneNumber.getText().toString(), imageDownloadUrl, auth.getCurrentUser().getEmail(), auth.getCurrentUser().getUid());
                if (currentUser != null) {
                    user.setMyCreatedOrders(currentUser.getMyCreatedOrders());
                    user.setMyOrdersPickUp(currentUser.getMyOrdersPickUp());
                }
                DataBaseManager.createUser(user, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            int title = currentUser == null ? R.string.create_successfully : R.string.profile_is_updated_successfully;
                            Toast.makeText(CreateOrUpdateProfileActivity.this, title, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateOrUpdateProfileActivity.this, HomeScreenActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            AlertDialogUtils.showAlertDialog(CreateOrUpdateProfileActivity.this, getString(R.string.error), task.getException().getMessage());
                        }
                    }
                });
            }
        });

    }

    private void uploadImageToStorage(Runnable onDone) {
        if (imageBitmap == null) {// its update
            imageDownloadUrl = currentUser.getImage();
            onDone.run();
        } else {
            StorageReference imageRef = storageRef.child("images/" + auth.getCurrentUser().getUid() + ".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();
            // Upload file to Firebase Storage
            UploadTask uploadTask = imageRef.putBytes(imageData);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageDownloadUrl = uri.toString();
                    onDone.run();
                });
            }).addOnFailureListener(e -> {
                // Handle failed upload
                Log.e("TAG", "Upload failed: " + e.getMessage());
            });
        }



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
}