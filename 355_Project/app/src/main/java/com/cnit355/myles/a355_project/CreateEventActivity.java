package com.cnit355.myles.a355_project;

import android.content.Intent;
import android.support.annotation.MainThread;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class CreateEventActivity extends AppCompatActivity {

    Spinner yearSpinner, daySpinner, monthSpinner;
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    Integer[] days = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
    Integer[] years = {2016, 2017, 2018};
    String eventTitle, eventLocation, eventDescription, eventMonth;
    int eventYear, eventDay;
    Button submitButton, homeButton;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;
    private int eventID = 0;
    int largestKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        setTitle("Create New Event");

        Intent intent = getIntent();
        largestKey = intent.getIntExtra("listViewLength",0);

        yearSpinner = (Spinner)findViewById(R.id.yearSpinner);
        monthSpinner = (Spinner)findViewById(R.id.monthSpinner);
        daySpinner = (Spinner)findViewById(R.id.daySpinner);
        submitButton = (Button) findViewById(R.id.submitButton);
        homeButton = (Button) findViewById(R.id.homeButton);

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, years);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        ArrayAdapter<Integer> dayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

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


            // Add items via the Button and EditText at the bottom of the view.
            final EditText titleEditText = (EditText) findViewById(R.id.eventTitleEditText);
            final Button button = (Button) findViewById(R.id.submitButton);
            final EditText descriptionEditText = (EditText) findViewById(R.id.eventDescriptionEditText);
            final EditText locationEditText = (EditText) findViewById(R.id.locationEditText);

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

            submitButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    eventID = largestKey + 1;
                    eventTitle = String.valueOf(titleEditText.getText());
                    eventDescription = String.valueOf(descriptionEditText.getText());
                    eventLocation = String.valueOf(locationEditText.getText());
                    Event newEvent = new Event(eventTitle, eventDescription, eventLocation, eventMonth, eventYear, eventDay, eventID);

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


        }
    }

    private void loadLogInView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void moveHome(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
