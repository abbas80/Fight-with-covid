package com.example.fightwithcovid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class home_screen extends AppCompatActivity {
    private  String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Button track=(Button)findViewById(R.id.track_btn);
        Button does=(Button)findViewById(R.id.does_btn);
        Button current=(Button)findViewById(R.id.current_status_btn);
        Button symptoms=(Button)findViewById(R.id.symptons_btn_id);
        Button interact=(Button)findViewById(R.id.interact_id);
        Button logout=(Button)findViewById(R.id.logout);

        Intent intent1 = getIntent();
        userid=intent1.getStringExtra("userid");
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(home_screen.this,track.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });
        does.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(home_screen.this,does.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });
        symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(home_screen.this,symptoms.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(home_screen.this,current.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });
        interact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(home_screen.this,interact.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent =new Intent(home_screen.this,MainActivity.class);
                //intent.putExtra("userid",phone_no.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
