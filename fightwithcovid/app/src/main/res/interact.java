package com.example.covid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class interact extends AppCompatActivity {
    private ListView listView;
    private String userid;
    private List<Long> interactee_list=new ArrayList<>();
    private List<String> interactee_list_string=new ArrayList<>();

    private com.example.covid.user user=new com.example.covid.user();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact);
        listView=(ListView)findViewById(R.id.interacted_listview);
        Intent intent = getIntent();
        userid=intent.getStringExtra("userid");
        Toast.makeText(com.example.covid.interact.this,"user id="+userid,Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference().child("User").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user=dataSnapshot.getValue(com.example.covid.user.class);
                Toast.makeText(com.example.covid.interact.this,"user id="+user.getInteracted_user(),Toast.LENGTH_SHORT).show();

                interactee_list=user.getInteracted_user();

                Toast.makeText(com.example.covid.interact.this,"user id="+interactee_list,Toast.LENGTH_SHORT).show();
                update_listview(interactee_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //interactee_list.add(Long.parseLong("8299897152"));
        //interactee_list.add(Long.parseLong("8299897193"));

        for(int i=0;i<interactee_list.size();i++)
        {
          //  interactee_list_string.add((i+1)+":"+interactee_list.get(i));
        }
       // interactee_list_string.add("8299897153");
      }

    private void update_listview(List<Long> interactee_list1) {
        if((interactee_list1.get(0)+"").length()<10)
        {
            interactee_list1.remove(0);
        }
        for(int i=0;i<interactee_list1.size();i++)
        {
            interactee_list_string.add((i+1)+":"+interactee_list1.get(i));
        }


        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,interactee_list_string);

        listView.setAdapter(arrayAdapter);


    }

}
