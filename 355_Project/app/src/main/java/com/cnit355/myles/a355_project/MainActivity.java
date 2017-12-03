package com.cnit355.myles.a355_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    ArrayAdapter<String> adapter;
    private static final int request_code = 5;
    TextView tvTitle, tvDescription, tvDate, tvLocation, tvEventDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Events");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final ListView listView = (ListView) findViewById(R.id.eventListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvEventDetails = (TextView) findViewById(R.id.tvEventDetails);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final int tempPosition = position;

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, String> map = (Map) dataSnapshot.child("Events").child(String.valueOf(tempPosition + 1)).getValue();
                        Log.i("title", map.get("title"));

                        String tempTitle = map.get("title");
                        String tempDescription = map.get("description");
                        String tempDay = map.get("day");
                        String tempMonth = map.get("month");
                        String tempYear = map.get("year");
                        String tempLocation = map.get("location");

                        tvTitle.setText("Title: " + tempTitle);
                        tvDescription.setText("Description: " + tempDescription);
                        tvDate.setText("Date: " + tempMonth + "/" + tempDay + "/" + tempYear);
                        tvLocation.setText("Location: " + tempLocation);
                        tvEventDetails.setText("Event Details For: " + tempTitle);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

        });

        ValueEventListener newEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int id = 1;
                for (DataSnapshot snap : dataSnapshot.child("Events").getChildren()) {
                    Map<String, String> map = (Map) dataSnapshot.child("Events").child(String.valueOf(id)).getValue();
                    String title = map.get("title");
                    adapter.add(title);
                    id++;
                    Log.i("key", snap.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(newEventListener);
    }

    public void createEvent(View v){
        Intent intent = new Intent(this, CreateEventActivity.class);
        intent.putExtra("listViewLength", adapter.getCount());
        startActivityForResult(intent, request_code);
    }
}
