package com.example.gky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class DetialActivity extends AppCompatActivity {
        ImageButton dtback;
        TextView dtname, dttitle;
        ImageView dtimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detial);

        dttitle = (TextView) findViewById(R.id.dttitle);
        dtback = (ImageButton) findViewById(R.id.dtback);
        dtimg = (ImageView) findViewById(R.id.dtimg);
        dtname = (TextView) findViewById(R.id.dtname);

        dttitle.setEnabled(false);

        dtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetialActivity.this, ListviewArtActivity.class);
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        Image image = (Image) bundle.get("");
        dtname.setText(image.getImgname());
        Glide.with(this).load(image.getImgurl()).into(dtimg);
        dttitle.setText(image.getTitle());
    }
}