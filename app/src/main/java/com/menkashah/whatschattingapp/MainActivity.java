package com.menkashah.whatschattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.menkashah.whatschattingapp.Adapters.FragmentAdapters;
import com.menkashah.whatschattingapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mainBinding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        auth= FirebaseAuth.getInstance();
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("message");
//        mRef.setValue("Hello world");

//        usetablayout and inside it used viewpager
        mainBinding.viewPager.setAdapter(new FragmentAdapters(getSupportFragmentManager()));
        mainBinding.tabLayout.setupWithViewPager(mainBinding.viewPager);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             String value = snapshot.getValue(String.class);
                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Intent i3 = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(i3);
                break;
            case R.id.logout:
                auth.signOut();
                Intent i = new Intent(MainActivity.this, SignIn.class);
                startActivity(i);
                break;

            case R.id.groupchat:
                Intent ii = new Intent(MainActivity.this, GroupChat.class);
                startActivity(ii);
                break;

        }
        return true;
    }
}