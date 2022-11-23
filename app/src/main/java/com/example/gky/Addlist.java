package com.example.gky;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;

public class Addlist extends AppCompatActivity {
    EditText edname, edtitle;
    ImageView edimg;
    Button add;
    ImageButton back;
    int Request_Code = 2;
    Uri pathuri;
    ProgressDialog progressDialog;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlist);

        edname = (EditText) findViewById(R.id.edname);
        edimg = (ImageView) findViewById(R.id.edimg);
        edtitle = (EditText) findViewById(R.id.edtitle);
        back = (ImageButton) findViewById(R.id.back);
        add = (Button) findViewById(R.id.add);
        storageReference = FirebaseStorage.getInstance().getReference("art");
        databaseReference = FirebaseDatabase.getInstance().getReference("art");
        progressDialog = new ProgressDialog(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Addlist.this, ListviewArtActivity.class);
                startActivity(intent);
            }
        });

        edimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Request_Code);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Upload();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Request_Code && resultCode==RESULT_OK && data !=null && data.getData() !=null){
            pathuri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), pathuri); edimg.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String GetFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void Upload() {
        progressDialog.show();
        if (pathuri !=null){

            final StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension (pathuri));
            storageReference1.putFile(pathuri).addOnSuccessListener(new OnSuccessListener<UploadTask. TaskSnapshot>() {
                        @Override public void onSuccess (UploadTask. TaskSnapshot taskSnapshot) {
                            String ImgName = edname.getText().toString().trim();
                            String Title = edtitle.getText().toString().trim();
                            progressDialog.dismiss();
                            Image image = new Image(ImgName, taskSnapshot.getUploadSessionUri().toString(), Title);
                            String ImageUploadId = databaseReference.push().getKey();
                            databaseReference.child(ImageUploadId).setValue(image);
                                Toast.makeText(Addlist.this, "Upload Success..", Toast.LENGTH_SHORT).show();
                                edname.setText("");
                            edtitle.setText("");


                        }

            });

        }

    }

}