package com.polyjoule.ylebourlout.apriou.polygame;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.polyjoule.ylebourlout.apriou.polygame.Accueil.SETS;
import static com.polyjoule.ylebourlout.apriou.polygame.Accueil.databaseReference;
import static com.polyjoule.ylebourlout.apriou.polygame.Accueil.userInfo;

/**
 * Created by Alexis on 20/07/2017.
 */

public class Registration extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPseudo;
    private Button buttonSignup;

    private TextView contactus;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;


    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if (firebaseAuth.getCurrentUser() != null) {
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), Profil.class));
        }

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPseudo = (EditText) findViewById(R.id.editTextPseudo);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
        contactus = (TextView) findViewById(R.id.contactus);

        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        contactus.setOnClickListener(this);
    }

    private void registerUser() {

        //getting email and password from edit texts
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String pseudo = editTextPseudo.getText().toString().trim();

        //checking if email and passwords are empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(pseudo)) {
            Toast.makeText(this, "Please enter pseudo", Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            userInfo.setPseudo(pseudo);
                            userInfo.setEmail(email);
                            final FirebaseUser usr = firebaseAuth.getCurrentUser();
                            databaseReference.child("users").child(usr.getUid()).setValue(userInfo);

                            SharedPreferences settings = getSharedPreferences(SETS, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("email",email);
                            editor.putString("pseudo", pseudo);
                            editor.putInt("highScore",-1);
                            editor.commit();




                            finish();
                            startActivity(new Intent(getApplicationContext(), Profil.class));
                        } else {
                            //display some message here
                            Toast.makeText(Registration.this, "Registration Error", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

        if (view == buttonSignup) {
            registerUser();
        }

        if (view == textViewSignin) {
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, Login.class));
        }

        if(view == contactus){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            //, Uri.fromParts(
            //"mailto","polyjoule@univ-nantes.fr", null));
            //emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[Application mobile]");
            //mailIntent.putExtra(Intent.EXTRA_EMAIL, "polyjoule@univ-nantes.fr");

            emailIntent.setData(Uri.parse("mailto:polyjoule@univ-nantes.fr"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, "polyjoule@univ-nantes.fr");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[Application mobile]");

            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(emailIntent);
                //startActivity(Intent.createChooser(emailIntent, "Contact us"));

            } else {
                Log.d("emailIntent","null");
            }
            //startActivity(emailIntent);

            //startActivity(Intent.createChooser(emailIntent, "Contact us"));
            //emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            //startActivity(Intent.createChooser(emailIntent, "Send email..."));

        }
    }

    @Override
    public void onBackPressed() {
        Intent retourMenuIntent = new Intent(Registration.this, Accueil.class);

        startActivity(retourMenuIntent);
    }
}
