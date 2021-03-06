package com.example.user.treasurehunter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.user.treasurehunter.LogInScreen.currentActiveUser;

/**
 * @author Zach Curll, Matthew Finnegan, Alexander Kulpin, Dominic Marandino, Brandon Ostasewski, Paul Sigloch
 * @version Sprint 2
 */
public class MainActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener
{
    private DrawerLayout nDrawerLayout;
    private NavigationView nView1;
    private NavigationView nView2;
    private ActionBarDrawerToggle nToggle;
    private ArrayList<String> groupSpinner = new ArrayList<>();
    private ArrayList<String> idSpinner = new ArrayList<>();
    private String selected;
    static final int REQUEST_LOCATION = 1;
    public static String currentLayout = "";
    public static String currentLayoutID = "";

    // INCLUDE DOCUMENTATION Explain what is done on create***********************************
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IOread reader = new IOread();

        // Instantiate the drawer layout functions
        nDrawerLayout = findViewById(R.id.drawerLayout);
        nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, R.string.open, R.string.close);
        nDrawerLayout.addDrawerListener(nToggle);
        nToggle.syncState();

        // Set the navigation views
        nView1 = findViewById(R.id.nav_view);
        nView2 = findViewById(R.id.nav_view2);

        // Grant permission to view location
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }

        // Set primary color
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));

        // Set up spinner with an array of groups
        Spinner idselector = findViewById(R.id.spinner2);
        idselector.setOnItemSelectedListener(this);
        for(int i = 0; i < currentActiveUser.getAssociatedGroupID().size() - 1; i++)
        {
            if(!currentActiveUser.getAssociatedGroupID().get(i).equals("null") &&
                        currentActiveUser.getAssociatedGroupID().get(i).charAt(0) != '%')
            {
                idSpinner.add(currentActiveUser.getAssociatedGroupID().get(i));
                Group group = reader.retrieveGroup(currentActiveUser.getAssociatedGroupID().get(i), this);
                groupSpinner.add(group.getGroupName());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                groupSpinner
        );
        idselector.setAdapter(adapter);

        // On entering, select the spinner with the current selected layout
        for(int i = 0; i < groupSpinner.size() - 1; i++)
        {
            if(idselector.getSelectedItem().equals(currentLayout))
            {
                int selectedPosition = idselector.getSelectedItemPosition();
                idselector.setSelection(selectedPosition, false);
                currentLayoutID = idSpinner.get(i);
            }
            else{
                int selectedPosition = idselector.getSelectedItemPosition() + 1;
                idselector.setSelection(selectedPosition, false);
            }
        }
        System.out.println(currentLayout);
    }

    // INCLUDE DOCUMENTATION*****************************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(nToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // INCLUDE DOCUMENTATION*****************************************************
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String item = parent.getItemAtPosition(position).toString();
        currentLayout = item;
        currentLayoutID = idSpinner.get(position);
        System.out.println(currentLayoutID);
        selected = item;
        Toast.makeText(parent.getContext(), "Current Layout: " + item, Toast.LENGTH_LONG).show();
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void onNothingSelected(AdapterView<?> arg0)
    {
        currentLayout = "";
        currentLayoutID = "";
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void pinsClicked(MenuItem menuItem)
    {
        Intent pinIntent = new Intent(this, PinActivity.class);
        startActivity(pinIntent);
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void drawRight(View view)
    {
        nDrawerLayout.openDrawer(nView2);
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void drawLeft(View view)
    {
        nDrawerLayout.openDrawer(nView1);
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void pinsViewClicked(MenuItem menuItem)
    {
        Intent pinIntent = new Intent(this, PinView.class);
        startActivity(pinIntent);
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void testClicked(MenuItem menuItem)
    {
        Intent locIntent = new Intent(this, IOtester.class);
        startActivity(locIntent);
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void auditClicked(MenuItem menuItem)
    {
        if(!currentLayout.equals(""))
        {
            Intent pinIntent = new Intent(this, UserAuditLog.class);
            startActivity(pinIntent);
        }
        else{
            Toast.makeText(this, "No group Selected", Toast.LENGTH_LONG).show();
        }
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void auditUserClicked(MenuItem menuItem)
    {
        Intent pinIntent = new Intent(this, UserAuditLog.class);
        currentLayoutID = "personal";
        currentLayout = "Personal";
        startActivity(pinIntent);
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void logoutClicked(MenuItem menuItem)
    {
        currentActiveUser = null;
        currentLayout = "";
        Intent locIntent = new Intent(this, LogInScreen.class);
        startActivity(locIntent);
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void accountClicked(MenuItem menuItem)
    {
        Intent locIntent = new Intent(this, UserAccountManager.class);
        startActivity(locIntent);
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void settingsClicked(MenuItem menuItem)
    {
        Intent locIntent = new Intent(this, UserSettings.class);
        startActivity(locIntent);
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void groupsClicked(MenuItem menuItem)
    {
    Intent locIntent = new Intent(this, GroupCreator.class);
    startActivity(locIntent);
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void testClick(View v)
    {
        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void groupViewClicked(MenuItem menuItem)
    {
        if(!currentLayoutID.equals(""))
        {
            Intent locIntent = new Intent(this, GroupView.class);
            startActivity(locIntent);
        }
        else{
            Toast.makeText(this, "No group Selected", Toast.LENGTH_LONG).show();
        }
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void groupInvitesClicked(MenuItem menuItem)
    {
        Intent locIntent = new Intent(this, GroupInvites.class);
        startActivity(locIntent);
    }

    // INCLUDE DOCUMENTATION*****************************************************
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_LOCATION:
                break;
        }
    }

    /**
     * Method that allows the user to close the App.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            //finish();
            //System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void clickScavengerHunt(View v)
    {
        if(!currentLayout.equals(""))
        {
            Intent yourIntent = new Intent(this, YourPins.class);
            yourIntent.putExtra("class", "Scavenger Hunt Pin");
            startActivity(yourIntent);
        }
        else{
            Toast.makeText(this, "No group Selected", Toast.LENGTH_LONG).show();
        }
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void clickShipwreck(View v)
    {
        if(!currentLayout.equals(""))
        {
            Intent yourIntent = new Intent(this, YourPins.class);
            yourIntent.putExtra("class", "Shipwreck Pin");
            startActivity(yourIntent);
        }
        else{
            Toast.makeText(this, "No group Selected", Toast.LENGTH_LONG).show();
        }
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void clickTreasure(View v)
    {
        if (!currentLayout.equals(""))
        {
            Intent yourIntent = new Intent(this, YourPins.class);
            yourIntent.putExtra("class", "Treasure Pin");
            startActivity(yourIntent);
        }
        else{
            Toast.makeText(this, "No group Selected", Toast.LENGTH_LONG).show();
        }
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void clickForestFire(View v)
    {
        if (!currentLayout.equals(""))
        {
            Intent yourIntent = new Intent(this, YourPins.class);
            yourIntent.putExtra("class", "Forest Fire Pin");
            startActivity(yourIntent);
        }
        else{
            Toast.makeText(this, "No group Selected", Toast.LENGTH_LONG).show();
        }
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void clickHunting(View v)
    {
        if (!currentLayout.equals(""))
        {
            Intent yourIntent = new Intent(this, YourPins.class);
            yourIntent.putExtra("class", "Hunting Pin");
            startActivity(yourIntent);
        }
        else{
            Toast.makeText(this, "No group Selected", Toast.LENGTH_LONG).show();
        }
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void clickSurvivor(View v)
    {
        if (!currentLayout.equals(""))
        {
            Intent yourIntent = new Intent(this, YourPins.class);
            yourIntent.putExtra("class", "Survivor Pin");
            startActivity(yourIntent);
        }
        else{
            Toast.makeText(this, "No group Selected", Toast.LENGTH_LONG).show();
        }
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void clickWhale(View v)
    {
        if (!currentLayout.equals(""))
        {
            Intent yourIntent = new Intent(this, YourPins.class);
            yourIntent.putExtra("class", "Whale Pin");
            startActivity(yourIntent);
        }
        else{
            Toast.makeText(this, "No group Selected", Toast.LENGTH_LONG).show();
        }
    }

    // INCLUDE DOCUMENTATION*****************************************************
    public void clickCustom(View v)
    {
        if (!currentLayout.equals(""))
        {
            Intent yourIntent = new Intent(this, YourPins.class);
            yourIntent.putExtra("class", "Custom Pin");
            startActivity(yourIntent);
        }
        else{
            Toast.makeText(this, "No group Selected", Toast.LENGTH_LONG).show();
        }
    }

    public void clickMap(MenuItem menuItem)
    {
        Intent pinIntent = new Intent(this, Map.class);
        currentLayoutID = "personal";
        currentLayout = "Personal";
        startActivity(pinIntent);
    }
}