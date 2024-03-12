package com.example.demoproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.demoproject.sqlite.DBManager;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private Button backButton;
    private DBManager dbManager;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbManager = new DBManager(this);
        dbManager.open();

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.register);
        backButton = (Button) findViewById(R.id.back);

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
                    register(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private boolean checkingFormat(String userName, String password) {
        boolean check = true;
        if (userName.isEmpty()) {
            usernameEditText.setError("Username must not be empty");
            check = false;
        } else if (dbManager.UserNameAlreadyExists(userName)) {
            usernameEditText.setError("Username already exists");
            check = false;
        }
        if (password.length() < 8) {
            passwordEditText.setError("Password at least 8 characters");
            check = false;
        }
        return check;
    }
    private void register(String userName, String password) {
        boolean success = checkingFormat(userName, password);
        if (success) {
            dbManager.insert(userName, password);
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }
    }

}