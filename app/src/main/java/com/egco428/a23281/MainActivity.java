package com.egco428.a23281;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference Myreference;
    private ArrayList<Person> personArrayList;
    private ArrayAdapter customAdapter;

    Person personloca;

    public static final String Mlati = "Latitude";
    public static final String Mlong = "Longitude";
    public static final String Muser = "Username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.mainbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        Myreference = database.getReference(databaseconfig.Person_Table);
        personArrayList = new ArrayList<>();
        getUserPass();

        customAdapter = new CustomAdapter(this, personArrayList);
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, LocationPage.class);
                personloca = personArrayList.get(i);
                intent.putExtra(Mlati, personloca.getLatitude());
                intent.putExtra(Mlong, personloca.getLongitude());
                intent.putExtra(Muser, personloca.getUsername());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("LOGOUT");
            dialog.setIcon(R.drawable.ic_exit);
            dialog.setCancelable(true);
            dialog.setMessage("Do you want to logout?");

            dialog.setPositiveButton("Yes", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            dialog.setNegativeButton("No", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            dialog.show();  ;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getUserPass(){
        Myreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Person dataFirebase = dataSnapshot.getValue(Person.class); //get value from database in to Data form
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
