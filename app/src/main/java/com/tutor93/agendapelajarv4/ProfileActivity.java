package com.tutor93.agendapelajarv4;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutor93.agendapelajarv4.models.User;

import java.util.ArrayList;

/**
 * Created by indra on 13/08/2016.
 */
public class ProfileActivity extends BaseActivity {

    static final String TAG = "Profile Activity";

    private EditText username_ed;
    private EditText email_ed;
    private EditText user_ed;

    private DatabaseReference mDatabase;
    private ArrayList<String> keysArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        username_ed = (EditText) findViewById(R.id.profile_username);
        email_ed = (EditText) findViewById(R.id.profile_email);
        user_ed = (EditText) findViewById(R.id.profile_uid);

        mDatabase.addValueEventListener(valueEventListener);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            User user = dataSnapshot.child("users").child(getUid()).getValue(User.class);
            String username = user.username;
            String email = user.email;

            username_ed.setText(username);
            email_ed.setText(email);
            user_ed.setText(getUid());

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}




