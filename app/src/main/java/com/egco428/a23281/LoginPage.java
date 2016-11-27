package com.egco428.a23281;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoginPage extends AppCompatActivity {

    private EditText usernameTxt;
    private EditText passwordTxt;
    private Button signinBtn;
    private Button signupBtn;
    private Button cancelBtn;

    private FirebaseDatabase database;
    private DatabaseReference Myreference;
    private ArrayList<Person> personArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.mainbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        database = FirebaseDatabase.getInstance();
        Myreference = database.getReference(databaseconfig.Person_Table);
        personArrayList = new ArrayList<>();
        getUserPass();

        usernameTxt = (EditText)findViewById(R.id.usernameTxt);
        passwordTxt = (EditText)findViewById(R.id.passwordTxt);
        signinBtn = (Button)findViewById(R.id.signinBtn);
        signupBtn = (Button)findViewById(R.id.signupBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkuser();
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupPage.class);
                startActivity(intent);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameTxt.setText("");
                passwordTxt.setText("");
            }
        });
    }

    public void checkuser(){
        boolean correct = false;
        for(int i=0 ; i < personArrayList.size() ; i++){
            if(usernameTxt.getText().toString().equals(personArrayList.get(i).getUsername()) && passwordTxt.getText().toString().equals(personArrayList.get(i).getPassword())) {
                correct = true;
            }
        }
        if(correct){
            Toast.makeText(LoginPage.this, "Login success.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(intent,0);
            usernameTxt.setText("");
            passwordTxt.setText("");
        }
        else{
            Toast.makeText(LoginPage.this, "Login fail.", Toast.LENGTH_SHORT).show();
        }
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
