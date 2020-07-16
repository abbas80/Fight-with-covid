package com.example.covid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText phone_no,otp;
    private String opt_recieved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone_no=(EditText)findViewById(R.id.phone_et);
        otp=(EditText)findViewById(R.id.opt_et);
        
        Button bt=(Button) findViewById(R.id.verify_btn);

        Button send_otp=(Button) findViewById(R.id.send_otp);
        Button bt_reg=(Button) findViewById(R.id.bt_reg);

        firebaseAuth=FirebaseAuth.getInstance();

        FirebaseApp.initializeApp(MainActivity.this);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendopt();
            }
        });
        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kl=phone_no.getText().toString().trim().substring(3);
                List<Long> l1=new ArrayList<>();
                l1.add((long) 0);
                user user=new user(Long.parseLong(kl),l1);
                FirebaseDatabase.getInstance().getReference().child("User").child(kl).setValue(user);
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent =new Intent(MainActivity.this,home_screen.class);
                String kl=phone_no.getText().toString().trim().substring(3);
                intent.putExtra("userid",kl);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();*/
                 verify_coded_sent();
               // startActivity(new Intent(MainActivity.this,home_screen.class));
          //      startActivity(new Intent(MainActivity.this,track.class));
            }
        });
    }

    private void verify_coded_sent() {
        String code=otp.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(opt_recieved,code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent =new Intent(MainActivity.this,home_screen.class);
                            String kl=phone_no.getText().toString().trim().substring(3);
                            intent.putExtra("userid",kl);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            // ...
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                Toast.makeText(MainActivity.this,"Error in sending OTP",Toast.LENGTH_SHORT).show();
                                // The verification code entered was invalid
                            }
                           }
                    }
                });
    }
    private void sendopt() {
        String phoneno=phone_no.getText().toString().trim();
        if(phoneno.length()==0)
        {
            return;
        }
        else if(phoneno.length()<10)
        {
            phone_no.setError("Please enter valid number");
            phone_no.requestFocus();
            return;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneno,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            opt_recieved=s;
            super.onCodeSent(s, forceResendingToken);
        }
    };

    @Override
    protected void onStart() {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent intent =new Intent(MainActivity.this,home_screen.class);
            intent.putExtra("userid",phone_no.getText().toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        super.onStart();
    }
}
