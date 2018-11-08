package com.example.user.treasurehunter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Random;

public class UserCreate extends AppCompatActivity
{
    EditText etUsername;
    EditText etPassword1;
    EditText etPassword2;
    String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword1 = (EditText)findViewById(R.id.etPassword);
        etPassword2 = (EditText)findViewById(R.id.etPassword2);
        etPassword1.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        etPassword2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
    }

    public void generateAccountClicked(View view)
    {
        IOwrite writer = new IOwrite();
        IOread reader = new IOread();
        boolean generated = false;
        ArrayList<String> existingIDs = reader.existingIDs("users", this);
        userID = "";
        while (!generated)
        {
            Random rand = new Random();
            for(int j = 0; j <= 9; j++)
            {
                userID += String.valueOf(rand.nextInt(9));
            }
            generated = true;
            for(int i = 0; i < existingIDs.size(); i++)
            {
                if(existingIDs.get(i).equals(userID))
                {
                    generated = false;
                }
            }
        }
        boolean userUnique = true;
        if (!existingIDs.get(0).equals(""))
        {
            for(int i = 0; i < existingIDs.size(); i++)
            {
                if(etUsername.getText().toString().equals(reader.retrieveUser(existingIDs.get(i), this).getUserName()))
                {
                    userUnique = false;
                }
            }
        }
        if(userUnique)
        {
            if(!etPassword1.getText().toString().equals(etPassword2.getText().toString()) && userUnique)
            {
                etPassword2.setError("Passwords don't match");
            }
            else if(etUsername.getText().toString().equals("") && userUnique)
            {
                etUsername.setError("Username cant be blank");
            }
            else if(etPassword1.getText().toString().equals("") && userUnique)
            {
                etPassword1.setError("Password cant be blank");
            }
            else {

                User user = new User(userID, etUsername.getText().toString(), etPassword1.getText().toString());
                writer.writeUser(user, this);
                Intent pinIntent = new Intent(this, LogInScreen.class);
                startActivity(pinIntent);
            }
        }
        else{
            etUsername.setError("Username already exists");
        }
    }

    /**
     * Method that allows the user to close the App.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            finish();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
