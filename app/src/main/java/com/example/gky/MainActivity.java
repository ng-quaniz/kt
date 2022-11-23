package com.example.gky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.slice.SliceItem;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button btsign;
    TextView tvsup;
    EditText etu,etp;
    boolean etpVisible;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btsign = (Button) findViewById(R.id.btsignin);
        tvsup = (TextView) findViewById(R.id.tvsup);
        etu = (EditText) findViewById(R.id.etu);
        etp = (EditText) findViewById(R.id.etp);
        mAuth = FirebaseAuth.getInstance();
        progressDialo = new ProgressDialog(this);


        tvsup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        btsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        etp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right=2;
                if(motionEvent.getAction() == MotionEvent. ACTION_UP) {
                    if(motionEvent.getRawX()>=etp.getRight()-etp.getCompoundDrawables()[Right].getBounds().width())
                    {
                        int selection = etp.getSelectionEnd();
                        if (etpVisible) {
                            etp.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.hpw,0);
                            etp.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            etpVisible = false;
                        } else {
                            etp.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.pw,0);
                            etp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            etpVisible = true;
                        }
                            etp.setSelection(selection);
                            return true;
                        }
                    }

                            return false;
            }
        });
    }




    private void userLogin(){
        String email = etu.getText().toString().trim();
        String pass = etp.getText().toString().trim();

        if(email.isEmpty()){
            etu.setError("Email is require!");
            etu.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etu.setError("Please provide valid email!");
            etu.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            etp.setError("Password is require!");
            etp.requestFocus();
            return;
        }

        if(pass.length() < 6 ) {
            etp.setError("Min password length should be 6 characters!");
            etp.requestFocus();
            return;
        }

        progressDialo.show();
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialo.dismiss();
                    Intent intent = new Intent(MainActivity.this, ListviewArtActivity.class);
                    startActivity(intent);
                }else{
                    progressDialo.dismiss();
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}