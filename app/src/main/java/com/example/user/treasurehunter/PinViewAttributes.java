package com.example.user.treasurehunter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.Serializable;

public class PinViewAttributes extends AppCompatActivity implements Serializable
    {
        public PinDS pin;
        public TextView tvColor;

        EditText text;
        TableRow degreeRow;
        TableRow speedRow;
        Button goBackButton, placePinButton;
        TextView tvBanner;
        String passedID;

        /**
         * Method displays a screen to the user so they can Create a pin.
         * Assigns the entered data into the correct fields.
         */
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pin_view_attributes);
            pin = (PinDS) getIntent().getSerializableExtra("pin");
            degreeRow = findViewById(R.id.Row10);
            speedRow = findViewById(R.id.Row11);
            goBackButton = findViewById(R.id.goBack);
            placePinButton = findViewById(R.id.button16);
            tvColor = findViewById(R.id.tvcolor);
            tvColor.setTextColor(pin.getDefaultColor());
            tvColor.setText(pin.getColor());
            tvBanner = findViewById(R.id.tvBanner);
            tvBanner.setBackgroundColor(pin.getDefaultColor());
            tvBanner.setText("   " + pin.getPinName());
            //goBackButton.setBackgroundColor(pin.getDefaultColor());
            //placePinButton.setBackgroundColor(pin.getDefaultColor());


            //Hides the degree and speed rows from displaying if the pin is not Moveable.
            degreeRow.setVisibility(View.GONE);
            speedRow.setVisibility(View.GONE);
            if(pin instanceof PinMoveable)
            {
                PinMoveable movPin = (PinMoveable) pin;
                degreeRow.setVisibility(View.VISIBLE);
                speedRow.setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.etDegree)).setText("" + movPin.getDegree());
                ((TextView)findViewById(R.id.etSpeed)).setText("" + movPin.getSpeed());
            }

            ((TextView)findViewById(R.id.publisher)).setText(pin.getPublisher());
            ((TextView)findViewById(R.id.pinName)).setText(pin.getPinTitle());
            ((TextView)findViewById(R.id.description)).setText(pin.getDescription());
            ((TextView)findViewById(R.id.radius)).setText(pin.getRadius());
            ((TextView)findViewById(R.id.etLocationLat)).setText("" + pin.getLatitude());
            ((TextView)findViewById(R.id.etLocationLong)).setText("" + pin.getLongitude());
            ((TextView)findViewById(R.id.etAltitude)).setText("" + pin.getAltitude());
            ((TextView)findViewById(R.id.etTime)).setText(pin.getTime());
            ((TextView)findViewById(R.id.etDate)).setText(pin.getDate());


            passedID = (String) getIntent().getSerializableExtra("id");
        }

        public void backClicked(View view)
        {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        }

        /**
         * Method that allows the user to move back to the MainActivity screen.
         */
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event)
        {
            if (keyCode == KeyEvent.KEYCODE_BACK )
            {
                Intent mainIntent = new Intent(this, PinActivity.class);
                startActivity(mainIntent);
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
}
