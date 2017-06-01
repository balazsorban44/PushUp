package com.balazsorban.pushup;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements
        Home.OnFragmentInteractionListener,
        Progress.OnFragmentInteractionListener,
        Settings.OnFragmentInteractionListener {


    public DBHandler getDatabase() {
        return this.dbHandler;
    }
    private DBHandler dbHandler;
    private Fragment homeFragment = new Home();
    private Progress progressFragment = new Progress();
    private Settings settingsFragment = new Settings();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home: {
                        commitFragment(homeFragment);
                        return true;
                    }
                    case R.id.navigation_progress: {
                        commitFragment(progressFragment);
                        return true;
                    }
                    case R.id.navigation_settings: {
                        commitFragment(settingsFragment);
                        return true;
                    }
                }
                return false;
            }
        });

        if (findViewById(R.id.fragment_container) != null) {


            if (savedInstanceState != null) {
                return;
            }
            getFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment).commit();


        }

        dbHandler = new DBHandler(this, null, null, 1);


    }
    private void commitFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
