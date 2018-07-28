package com.pharmakhanah.hp.pharmakhanahsource.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BlurMaskFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
//import com.pharmacy.hp.pharmakhanah.R;
//import com.facebook.AccessToken;
//import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.pharmakhanah.hp.pharmakhanahsource.R;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    Button btnRegister, btnLogin;
    EditText editTextEmail, editTextPassword;
    String email = "", password = "";
    CheckBox checkBoxShowPassword, checkBoxRememberMe;
    TextView textViewResetPassword;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBarLogin;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this,Main2Activity.class));
            finish();
        }
//        FacebookSdk.sdkInitialize(this.getApplicationContext());
//        if (AccessToken.getCurrentAccessToken() != null) {
//            goLoginScreen();
//        }

    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        firebaseAuth = FirebaseAuth.getInstance();

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        boolean saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin) {
            editTextEmail.setText(loginPreferences.getString("email", ""));
            editTextPassword.setText(loginPreferences.getString("password", ""));
            checkBoxRememberMe.setChecked(true);
        }
        checkBoxRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("email", editTextEmail.getText().toString());
                    loginPrefsEditor.putString("password", editTextPassword.getText().toString());
                    loginPrefsEditor.apply();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.apply();
                }
            }
        });

        checkBoxShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    //hide password
                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
//        textViewResetPassword.setBackgroundResource(R.drawable.test_image);

        textViewResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (isOnline()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setCancelable(false).setTitle("Reset Password")
                            .setMessage("We just need your registered Email Id to sent you password reset instructions.");
                    final EditText input = new EditText(LoginActivity.this);
                    input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    builder.setView(input);
                    builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            String mail = input.getText().toString();
                            if (TextUtils.isEmpty(mail)) {
                                Toast.makeText(LoginActivity.this, " Enter your registered email id", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (!isValidEmail(mail)) {
                                Toast.makeText(LoginActivity.this, " Invalid Email", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            firebaseAuth.sendPasswordResetEmail(mail)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this, " We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(LoginActivity.this, " Failed to send reset email!", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                    });

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();

                } else {
                    Toast.makeText(LoginActivity.this, "check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (isOnline()) {

                    progressBarLogin.setVisibility(View.VISIBLE);
                    email = editTextEmail.getText().toString();
                    password = editTextPassword.getText().toString();
                    if (TextUtils.isEmpty(email)) {
                        progressBarLogin.setVisibility(View.INVISIBLE);
                        editTextEmail.setError(" enter your email");
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        progressBarLogin.setVisibility(View.INVISIBLE);
                        editTextPassword.setError(" enter your password ");
                        return;
                    }
                    if (!isValidEmail(email)) {
                        progressBarLogin.setVisibility(View.INVISIBLE);
                        editTextEmail.setError("Invalid Email");
                    }

                    if (!isValidPassword(password)) {
                        progressBarLogin.setVisibility(View.INVISIBLE);
                        editTextPassword.setError("Invalid Password  ");
                    }
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBarLogin.setVisibility(View.INVISIBLE);
                                Snackbar.make(view, "Successfully", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                                finish();
                            } else {
                                progressBarLogin.setVisibility(View.INVISIBLE);
                                Snackbar.make(view, "Wrong password or e-mail", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "check internet connection", Toast.LENGTH_SHORT).show();

                }
            }
        });
        applyBlurMaskFilter(textViewResetPassword, BlurMaskFilter.Blur.SOLID);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));

            }
        });


    }

    protected void applyBlurMaskFilter(TextView tv, BlurMaskFilter.Blur style) {
        float radius = tv.getTextSize() / 10;
        BlurMaskFilter filter = new BlurMaskFilter(radius, style);
        tv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        tv.getPaint().setMaskFilter(filter);
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, Main2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    void initView() {
        progressBarLogin = findViewById(R.id.progressbar_login);
        editTextEmail = findViewById(R.id.edit_email);
        editTextPassword = findViewById(R.id.edit_password);
        textViewResetPassword = findViewById(R.id.txt_reset_passowrd);
        checkBoxShowPassword = findViewById(R.id.check_show_password);
        checkBoxRememberMe = findViewById(R.id.check_remember_me);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        return false;
    }

}
