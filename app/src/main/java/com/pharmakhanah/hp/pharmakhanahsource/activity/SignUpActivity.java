package com.pharmakhanah.hp.pharmakhanahsource.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
//import com.facebook.AccessToken;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pharmakhanah.hp.pharmakhanahsource.R;
import com.pharmakhanah.hp.pharmakhanahsource.model.UserInformation;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    Button btnSignUp, btnTwitterLogin, btnGmailLogin, btnFacebookLogin;
    EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    MaterialBetterSpinner citySpinner;
    ArrayAdapter<String> adapterSpinner;
    ArrayList englishGov;
    ArrayList arabicGov;
    private ProgressBar progressBar;
//    com.facebook.login.LoginManager fbLoginManager;
//    CallbackManager callbackManager;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    Spinner spinner;
    String at = "@";
    String email;
    String password;
    String confirmPassword;
    String staticName = "";
    String avatar = "";
    String phone = "";
    String city = "";
    public static String uId = "";
    boolean b = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, Main2Activity.class));
            finish();
        }
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("UsersInformation");
        initView();
        progressBar = (ProgressBar) findViewById(R.id.progressbar_sign_up);
        englishGov = new ArrayList();
        arabicGov = new ArrayList();
        handleSpinner();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    progressBar.setVisibility(View.VISIBLE);
                    email = editTextEmail.getText().toString();
                    password = editTextPassword.getText().toString();
                    confirmPassword = editTextConfirmPassword.getText().toString();
                    if (TextUtils.isEmpty(email)) {
                        progressBar.setVisibility(View.INVISIBLE); //To Hide ProgressBar
                        editTextEmail.setError(" enter your Email");
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        progressBar.setVisibility(View.INVISIBLE); //To Hide ProgressBar
                        editTextPassword.setError("enter your password");
                        return;
                    }
                    if (password.length() < 6) {
                        progressBar.setVisibility(View.INVISIBLE); //To Hide ProgressBar
                        editTextPassword.setError("password should be 6 chars or more");
                        return;
                    }
                    if (!b) {
                        progressBar.setVisibility(View.INVISIBLE); //To Hide ProgressBar
                        Snackbar.make(v, "select your  country ", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        return;
                    }


                    if (validate(email, password, confirmPassword)) {

                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            uId = firebaseAuth.getCurrentUser().getUid();
                                            staticName = email.substring(email.indexOf(email.charAt(0)), email.indexOf(at));
                                            final UserInformation userInformation = new UserInformation(staticName, email, avatar, city, phone, uId);
                                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                databaseReference.child("Users").child(uId).setValue(userInformation)
                                                                        .addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Toast.makeText(SignUpActivity.this, "Verification sent to :" + firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                                                                startActivity(new Intent(SignUpActivity.this, Main2Activity.class));
                                                                                finish();

                                                                            }
                                                                        });
                                                            } else {
                                                                Toast.makeText(SignUpActivity.this, "false to sent Verification  ", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            try {
                                                throw task.getException();
                                            } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                editTextEmail.setError("malformed-wrong email");
                                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                                progressBar.setVisibility(View.INVISIBLE); //To Hide ProgressBar
                                                editTextEmail.setError("really exist email");
                                            } catch (Exception e) {
                                            }
                                        }
                                    }
                                });


                    } else {
                        progressBar.setVisibility(View.INVISIBLE); //To Hide ProgressBar
                        Toast.makeText(SignUpActivity.this, "Invalid email or not match password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "check internet connection", Toast.LENGTH_SHORT).show();

                }
            }
        });
//        handleFacebookLogin();
//        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isOnline()) {
//                    fbLoginManager.logInWithReadPermissions(SignUpActivity.this, Arrays.asList("email", "public_profile", "user_birthday"));
//
//                } else {
//                    Toast.makeText(SignUpActivity.this, "check internet connection", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        editTextEmail = findViewById(R.id.edit_email);
        editTextPassword = findViewById(R.id.edit_password);
        editTextConfirmPassword = findViewById(R.id.edit_confirm_password);
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnFacebookLogin = findViewById(R.id.fb_login);
        btnGmailLogin = findViewById(R.id.gmail_login);
        btnTwitterLogin = findViewById(R.id.twitter_login);
        citySpinner = findViewById(R.id.city_spinner);

    }

    private boolean validate(String emailStr, String password, String repeatPassword) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return password.length() > 0 && repeatPassword.equals(password) && matcher.find();
    }

//    private void handleFacebookAccessToken(AccessToken accessToken) {
//        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(), R.string.firebase_error_login, Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }

    public void handleSpinner() {
        if (Locale.getDefault().getLanguage().equals("en")) {
            String[] arr1 = getResources().getStringArray(R.array.englishGov);
            for (String anArr1 : arr1) {
                city = anArr1;
                if (city.length() > 0 && !englishGov.contains(city)) {
                    englishGov.add(city);
                }
            }
            city = "";
            Collections.sort(englishGov, String.CASE_INSENSITIVE_ORDER);
            adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, englishGov);
            citySpinner.setAdapter(adapterSpinner);

        } else {
            String[] arr2 = getResources().getStringArray(R.array.arabicGov);
            for (int x = 0; x < arr2.length; x++) {
                city = arr2[x];
                if (city.length() > 0 && !arabicGov.contains(city)) {
                    arabicGov.add(city);
                }
            }
            Collections.sort(arabicGov, String.CASE_INSENSITIVE_ORDER);
            adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arabicGov);
            citySpinner.setAdapter(adapterSpinner);
        }
        citySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(SignUpActivity.this, "selected", Toast.LENGTH_SHORT).show();
                city=parent.getItemAtPosition(position).toString();
                b = true;
            }
        });

    }

//    public void handleFacebookLogin() {
//        fbLoginManager = com.facebook.login.LoginManager.getInstance();
//        callbackManager = CallbackManager.Factory.create();
//        fbLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                handleFacebookAccessToken(loginResult.getAccessToken());
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
//    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}



