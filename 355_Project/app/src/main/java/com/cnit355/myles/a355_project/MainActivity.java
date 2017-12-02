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

    //init variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;
    private int eventID = 0;

    Spinner yearSpinner, daySpinner, monthSpinner;
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
//    String[] days = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
//    String[] years = {"2016", "2017", "2018"};
    Integer[] days = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
    Integer[] years = {2016, 2017, 2018};
    String eventTitle, eventLocation, eventDescription, eventMonth;
    int eventYear, eventDay;
    int largestKey = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        yearSpinner = (Spinner)findViewById(R.id.yearSpinner);
        monthSpinner = (Spinner)findViewById(R.id.monthSpinner);
        daySpinner = (Spinner)findViewById(R.id.daySpinner);

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, years);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        ArrayAdapter<Integer> dayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner.setAdapter(yearAdapter);
        daySpinner.setAdapter(dayAdapter);
        monthSpinner.setAdapter(monthAdapter);

        yearSpinner.setSelection(0);
        monthSpinner.setSelection(0);
        daySpinner.setSelection(0);

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogInView();
        }
        else {
            mUserId = mFirebaseUser.getUid();

            // Set up ListView
            final ListView listView = (ListView) findViewById(R.id.eventListView);
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
            listView.setAdapter(adapter);

            // Add items via the Button and EditText at the bottom of the view.
            final EditText titleEditText = (EditText) findViewById(R.id.eventTitleEditText);
            final Button button = (Button) findViewById(R.id.addButton);
            final EditText descriptionEditText = (EditText) findViewById(R.id.eventDescriptionEditText);
            final EditText locationEditText = (EditText) findViewById(R.id.eventLocationEditText);

            monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    eventMonth = months[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    eventDay = days[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    eventYear = years[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snap : dataSnapshot.child("Events").getChildren()){

                                if(snap.child(String.valueOf(eventID)) != null){
                                    Log.i("a", String.valueOf(largestKey));
                                    largestKey = eventID+1;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    eventID = largestKey +1;
                    eventTitle = String.valueOf(titleEditText.getText());
                    eventDescription = String.valueOf(descriptionEditText.getText());
                    eventLocation = String.valueOf(locationEditText.getText());
                    Event newEvent = new Event(eventTitle, eventDescription, eventLocation, eventMonth, eventYear, eventDay);

                   // mDatabase.child("Events").push().setValue(newEvent);
                    mDatabase.child("Events").child(String.valueOf(eventID)).setValue(newEvent);

                    titleEditText.setText("");
                    descriptionEditText.setText("");
                    locationEditText.setText("");
                    daySpinner.setSelection(0);
                    yearSpinner.setSelection(0);
                    monthSpinner.setSelection(0);
                }
            });

            ValueEventListener newEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int id = 1;
                    adapter.clear();
                    for (DataSnapshot snap: dataSnapshot.child("Events").getChildren()) {
                        Map<String, String> map = (Map)dataSnapshot.child("Events").child(String.valueOf(id)).getValue();
                        String title = map.get("title");
                        adapter.add(title);
                        id++;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mDatabase.addValueEventListener(newEventListener);

            // Use Firebase to populate the list.
//            mDatabase.addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    ArrayList<String> keys = new ArrayList<String>();
//                    for (DataSnapshot snap: dataSnapshot.getChildren()) {
//                        keys.add(snap.getKey());
//                        Log.i("key", snap.child(keys.get(0)).toString());
//                        Map<String, String> map = (Map)snap.child(keys.get(0)).getValue();
//                        String title = map.get("title");
//                        adapter.add(title);
//                    }
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    adapter.remove((String) dataSnapshot.child("title").getValue());
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
       }

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogInView();
        }
    }

    private void collectEventTitles(Map<String, Object> events){
        ArrayList<String> titles = new ArrayList<>();
        for(Map.Entry<String, Object> entry : events.entrySet()){
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            titles.add((String) singleUser.get("title"));
        }
        Log.i("titles", titles.toString());
    }

    private void loadLogInView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
