package com.egco428.a23281;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SignupPage extends AppCompatActivity implements SensorEventListener {

    private EditText regisuser;
    private EditText regispass;
    private EditText latitudeTxt;
    private EditText longitudeTxt;
    private Button addBtn;
    private Button randomBtn;

    private FirebaseDatabase database;
    private DatabaseReference Myreference;
    private ArrayList<Person> personArrayList;

    private SensorManager sensorManager;
    private long lastUpdate;
    int n = 0;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.mainbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        regisuser = (EditText)findViewById(R.id.regisUser);
        regispass = (EditText)findViewById(R.id.regisPass);
        latitudeTxt = (EditText)findViewById(R.id.latiTxt);
        longitudeTxt = (EditText)findViewById(R.id.longiTxt);
        randomBtn = (Button) findViewById(R.id.randomBtn);
        addBtn = (Button)findViewById(R.id.addBtn);

        database = FirebaseDatabase.getInstance();
        Myreference = database.getReference(databaseconfig.Person_Table);
        personArrayList = new ArrayList<>();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (regisuser.getText().toString().equals("") || regispass.getText().toString().equals("") || latitudeTxt.getText().toString().equals("") || longitudeTxt.getText().toString().equals("")){
                    Toast.makeText(SignupPage.this, "Failed to add user, Please enter your Blank fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean same = false;
                    for (i=0; i<personArrayList.size(); i++){
                        if (regisuser.getText().toString().equals(personArrayList.get(i).getUsername())){
                            same = true;
                        }
                    }
                    if (same){
                        Toast.makeText(SignupPage.this, "Same Username!, Please try again.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SignupPage.this, "Sign-up success!!.", Toast.LENGTH_SHORT).show();

                        String user = regisuser.getText().toString();
                        String pass = regispass.getText().toString();

                        String latitude = latitudeTxt.getText().toString();
                        double lat = Double.parseDouble(latitude);

                        String longitude = longitudeTxt.getText().toString();
                        double lon = Double.parseDouble(longitude);

                        Person objData = new Person(user, pass, lat, lon);
                        Myreference.push().setValue(objData);
                        finish();
                    }
                }
            }
        });

        randomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomLatLong();
            }
        });

        getUserPass();
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event){
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();
        if (accelationSquareRoot > 2 ) {
            if (actualTime - lastUpdate < 700) {
                return;
            }
            lastUpdate = actualTime;
            n++;

            if(n == 2) {
                randomLatLong();
                n = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
        //this method for will able error with adding implements SensorEventListener
    }

    @Override
    public void onResume(){
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void randomLatLong(){
        double minLat = -85.000000;
        double maxLat = 85.000000;
        double latitude = minLat + Math.random() * ((maxLat - minLat) + 1);

        double minLong = -179.999989;
        double maxLong = 179.999989;
        double longitude = minLong + Math.random() * ((maxLong - minLong) + 1);

        DecimalFormat decform = new DecimalFormat("#.######");
        latitudeTxt.setText(decform.format(latitude));
        longitudeTxt.setText(decform.format(longitude));
    }

    public void getUserPass(){
        Myreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Person dataFirebase = dataSnapshot.getValue(Person.class);
                personArrayList.add(dataFirebase);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
