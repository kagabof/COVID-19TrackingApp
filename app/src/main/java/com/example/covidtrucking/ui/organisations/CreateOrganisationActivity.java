package com.example.covidtrucking.ui.organisations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.covidtrucking.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class CreateOrganisationActivity extends AppCompatActivity {
    static final int PICK_IMAGE_REQUEST = 1;
    EditText nameInput, descriptionInput, locationInput;
    Button addOrgButton, addLogoButton, findLogButton;
    ImageView mImageView;
    ProgressBar progressBar;
    Uri mImageUri;
    String downloadLogoUrl;
    String qrCodeUrl;

    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_organisation);

        nameInput = findViewById(R.id.org_name_input);
        descriptionInput = findViewById(R.id.org_description_input);
        locationInput = findViewById(R.id.org_location_input);
        addOrgButton = findViewById(R.id.btd_add_org);
        addLogoButton = findViewById(R.id.btn_add_logo);
        progressBar = findViewById(R.id.progress_bar);
        mImageView = findViewById(R.id.img_view);
        findLogButton = findViewById(R.id.btn_find_logo);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Organisations");

        findLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


        addLogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });

        addOrgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nameInput.getText().toString();
                final String description = descriptionInput.getText().toString();
                final String orgLocation = locationInput.getText().toString();

                if(name.isEmpty()){
                    nameInput.setError("Enter name!");
                } else if(description.isEmpty()) {
                    descriptionInput.setError("Enter description!");
                } else if(orgLocation.isEmpty()){
                    locationInput.setError("Enter Location!");
                } else {
                    final String org_id = UUID.randomUUID().toString();
                    UploadTask uploadTask = uploadQRCodeImage(org_id);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    qrCodeUrl = uri.toString();
//                                    url[0] = uri.toString();
//                                    System.out.println(">>>>>>>>>......>>>>>>>1...rl[0]:"+url[0]);
                                    if(uri.toString() != null){
                                        final OrganisationHelperClass newOrg = new OrganisationHelperClass(org_id, name, description, orgLocation, downloadLogoUrl, qrCodeUrl);
                                        mDatabaseRef.child(String.valueOf(System.currentTimeMillis())).setValue(newOrg)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            System.out.println(">>>>>>>>>......>>>>>>>2.."+qrCodeUrl);

                                                            mImageView.setImageResource(R.drawable.ic_baseline_image_24);
                                                            nameInput.setText("");
                                                            descriptionInput.setText("");
                                                            locationInput.setText("");
                                                            Toast.makeText(CreateOrganisationActivity.this, "Organisation created successfully!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(CreateOrganisationActivity.this, "Organisation failed to be created!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(CreateOrganisationActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

//                    System.out.println(">>>>>>>>>......>>>>>>>1..."+qrUrl);
//                    if(qrUrl != null){
//                        final OrganisationHelperClass newOrg = new OrganisationHelperClass(org_id, name, description, orgLocation, downloadLogoUrl, qrCodeUrl);
//                        mDatabaseRef.child(String.valueOf(System.currentTimeMillis())).setValue(newOrg)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if(task.isSuccessful()){
//                                            System.out.println(">>>>>>>>>......>>>>>>>2.."+qrCodeUrl);
//
//                                            mImageView.setImageResource(R.drawable.ic_baseline_image_24);
//                                            nameInput.setText("");
//                                            descriptionInput.setText("");
//                                            locationInput.setText("");
//                                            Toast.makeText(CreateOrganisationActivity.this, "Organisation created successfully!", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toast.makeText(CreateOrganisationActivity.this, "Organisation failed to be created!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                    } else {
//                        Toast.makeText(CreateOrganisationActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
//                    }

                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {
        if(mImageUri !=null) {
            final StorageReference fileReference = mStorageRef.child(String.valueOf(System.currentTimeMillis()) +"."+ getFileExtension(mImageUri));
            fileReference.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(0);
                            }
                        }, 5000);
                        Toast.makeText(CreateOrganisationActivity.this, "Logo image uploaded!", Toast.LENGTH_SHORT).show();
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadLogoUrl = uri.toString();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateOrganisationActivity.this, "Image failed to upload!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                        progressBar.setProgress((int) progress);
                    }
                });
        } else {
            Toast.makeText(this, "Please select an image logo!", Toast.LENGTH_SHORT).show();
        }
    }

    private UploadTask uploadQRCodeImage(String orgName) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        final String[] url = {null};

        try{
            final StorageReference fileReference = mStorageRef.child(String.valueOf(System.currentTimeMillis()) +"."+ getFileExtension(mImageUri));
            BitMatrix bitMatrix = multiFormatWriter.encode(
                    System.currentTimeMillis()+"_"+orgName,
                    BarcodeFormat.QR_CODE,
                    200,
                    200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            final StorageReference mStorage = FirebaseStorage.getInstance().getReference("QRCodeImages");
            UploadTask uploadTask = mStorage.child(UUID.randomUUID().toString()).putBytes(data);

//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            qrCodeUrl = uri.toString();
//                            url[0] = uri.toString();
//                            System.out.println(">>>>>>>>>......>>>>>>>1...rl[0]:"+url[0]);
//                        }
//                    });
//                }
//            });
            return uploadTask;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

}