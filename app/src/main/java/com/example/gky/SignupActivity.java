package com.example.gky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton supback;
    Button cracc;
    EditText edname, edmail, edphone, edpass;
    boolean edpassVisible;
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        supback = (ImageButton) findViewById(R.id.supback);
        edname = (EditText) findViewById(R.id.edname);
        edmail = (EditText) findViewById(R.id.edmail);
        edphone = (EditText) findViewById(R.id.edphone);
        edpass = (EditText) findViewById(R.id.edpass);
        mAuth = FirebaseAuth.getInstance();
        cracc = (Button) findViewById(R.id.cracc);
        cracc.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);


        supback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        edpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right=2;
                if(motionEvent.getAction() == MotionEvent. ACTION_UP) {
                    if(motionEvent.getRawX()>=edpass.getRight()-edpass.getCompoundDrawables()[Right].getBounds().width())
                    {
                        int selection = edpass.getSelectionEnd();
                        if (edpassVisible) {
                            edpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.hpw,0);
                            edpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            edpassVisible = false;
                        } else {
                            edpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.pw,0);
                            edpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            edpassVisible = true;
                        }
                        edpass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


    }

    @Override
    public void onClick(View view) {
        cracc();
    }

    private void cracc() {
        String email = edmail.getText().toString().trim();
        String name = edname.getText().toString().trim();
        String phone = edphone.getText().toString().trim();
        String pass = edpass.getText().toString().trim();

        if(name.isEmpty()){
            edname.setError("Name is require!");
            edname.requestFocus();
            return;
        }

        if(email.isEmpty()){
            edmail.setError("Email is require!");
            edmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edmail.setError("Please provide valid email!");
            edmail.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            edphone.setError("Phone number is require!");
            edphone.requestFocus();
            return;
        }

        if(!Patterns.PHONE.matcher(phone).matches()){
            edphone.setError("Please provide valid Phone number!");
            edphone.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            edpass.setError("Password is require!");
            edpass.requestFocus();
            return;
        }
        if(pass.length() < 6 ) {
            edpass.setError("Min password length should be 6 characters!");
            edpass.requestFocus();
            return;
        }
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.show();
                        if(task.isSuccessful()){
                            User user = new User(name, phone, email);
                            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignupActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignupActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(SignupActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                });
}


}
