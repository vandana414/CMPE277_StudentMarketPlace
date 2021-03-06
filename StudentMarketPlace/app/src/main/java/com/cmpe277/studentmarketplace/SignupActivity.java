package com.cmpe277.studentmarketplace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    EditText email_input, pwd_input, fname_input, lname_input, strAddress, phone;
    Button signup_btn;
    TextView login_link;
    Database db;
    SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fname_input = (EditText) findViewById(R.id.input_fname);
        lname_input = (EditText) findViewById(R.id.input_lname);
        strAddress = (EditText) findViewById(R.id.input_addr);
        phone = (EditText) findViewById(R.id.input_phone);
        email_input = (EditText) findViewById(R.id.input_email);
        pwd_input = (EditText) findViewById(R.id.input_password);
        signup_btn = (Button) findViewById(R.id.btn_signup);
        login_link = (TextView) findViewById(R.id.link_login);
        db = new Database(this);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signup() {

        if (!validate()) {
            onSignupFailed("SignUp Failed");
            return;
        }

        signup_btn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = fname_input.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String email = email_input.getText().toString();
                        String password = pwd_input.getText().toString();
                        String address = strAddress.getText().toString();
                        String phoneNum = phone.getText().toString();
                        String fname = fname_input.getText().toString();
                        String lname = lname_input.getText().toString();
                        DbResult result = db.insertNewUser(email, password, address, phoneNum,fname,lname);
                        if (result.getStatus())
                            onSignupSuccess();
                        else
                            onSignupFailed(result.getMessage());
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        signup_btn.setEnabled(true);
        Bundle b = new Bundle();
        b.putString("email", email_input.getText().toString());
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtras(b);
        setResult(RESULT_OK, i);
        finish();
    }

    public void onSignupFailed(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();

        signup_btn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String fname = fname_input.getText().toString();
        String lname = lname_input.getText().toString();
        String email = email_input.getText().toString();
        String password = pwd_input.getText().toString();

        if (fname.isEmpty() || fname.length() < 3) {
            fname_input.setError("at least 3 characters");
            valid = false;
        } else {
            fname_input.setError(null);
        }

        if (lname.isEmpty() || fname.length() < 3) {
            lname_input.setError("at least 3 characters");
            valid = false;
        } else {
            lname_input.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_input.setError("enter a valid email address");
            valid = false;
        } else {
            email_input.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            pwd_input.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            pwd_input.setError(null);
        }

        return valid;
    }
}
