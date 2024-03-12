package com.example.demoproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import com.example.demoproject.sqlite.DBManager;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private TextView loginError;
    private DBManager dbManager;
    private String oldUserName;
    private String oldPassword;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbManager = new DBManager(this);
        dbManager.open();

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);
        registerButton = (Button) findViewById(R.id.register);
        loginError = (TextView) findViewById(R.id.loginerror);

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (getIntent().getBooleanExtra("íslogout", false)) {
            editor.clear();
            editor.commit();
        }
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkingFormat(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        oldUserName = sharedPreferences.getString("username", "");
        oldPassword = sharedPreferences.getString("password", "");

        if (!oldPassword.isEmpty() && !oldUserName.isEmpty()) {
            usernameEditText.setText(oldUserName);
            passwordEditText.setText(oldPassword);
            login(oldUserName, oldPassword);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        editor.putString("username", usernameEditText.getText().toString());
        editor.putString("password", passwordEditText.getText().toString());

        editor.commit();
    }

    private boolean checkingFormat(String userName, String password) {
        boolean check = true;
        if (userName.isEmpty()) {
            usernameEditText.setError("Username must not be empty");
            check = false;
        }
        if (password.length() < 8) {
            passwordEditText.setError("Password at least 8 characters");
            check = false;
        }
        loginButton.setEnabled(check);
        return check;
    }
    private void login(String userName, String password) {
        boolean success = checkingFormat(userName, password);
        if (success) {
            if (dbManager.checkingLogin(userName, password)) {
                updateUiWithUser(userName);
            } else {
                loginError.setText("Incorrect username or password");
            }
        }
    }

    private void updateUiWithUser(String userName) {
        String welcome = "Welcome " + userName;
        // TODO : initiate successful logged in experience
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("userID", dbManager.getUserID(userName));
        startActivity(intent);
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }
}