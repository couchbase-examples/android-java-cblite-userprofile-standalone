package com.couchbase.userprofile.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.couchbase.userprofile.R;
import com.couchbase.userprofile.profile.UserProfileActivity;
import com.couchbase.userprofile.util.DatabaseManager;

public class LoginActivity extends AppCompatActivity {

    EditText usernameInput;
    EditText passwordInput;
    AppCompatImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);

        //makes logging in easier for testing
        imageView = findViewById(R.id.imageViewLogo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameInput.setText("demo@example.com");
                passwordInput.setText("password");
            }
        });

    }

    public void onLoginTapped(View view) {
        if (usernameInput.length() > 0 && passwordInput.length() > 0) {
            DatabaseManager dbMgr = DatabaseManager.getSharedInstance();
            dbMgr.initCouchbaseLite(getApplicationContext());
            dbMgr.openOrCreateDatabaseForUser(getApplicationContext(), usernameInput.getText().toString());

            Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
