package com.balazsorban.pushup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Date;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout home, progress, settings;
    private Button doneBtn, currentBtn, cancelBtn, showPushUpsBtn, deletePushUpsBtn;
    private TextView pushUps;
    private DBHandler dbHandler;
    private int currPushUps = 0;
    private boolean done = true;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home: {
                    home.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                    settings.setVisibility(View.GONE);
                    return true;
                }
                case R.id.navigation_progress: {
                    home.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    settings.setVisibility(View.GONE);
                    showPushUps(showPushUpsBtn);
                    return true;
                }
                case R.id.navigation_settings: {
                    home.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    settings.setVisibility(View.VISIBLE);
                    return true;
                }
            }
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        dbHandler = new DBHandler(this, null, null, 1);

        doneBtn = (Button) findViewById(R.id.donePushUpBtn);
        currentBtn = (Button) findViewById(R.id.currentPushUpBtn);
        cancelBtn = (Button) findViewById(R.id.cancelPushUpBtn);

        showPushUpsBtn = (Button) findViewById(R.id.showPushUpsBtn);
        deletePushUpsBtn = (Button) findViewById(R.id.deletePushUpsBtn);
        pushUps = (TextView) findViewById(R.id.pushUpsString);

        //cancel = (Button) findViewById(R.id.cancelSessionBtn);
        home = (ConstraintLayout) findViewById(R.id.home);
        progress = (ConstraintLayout) findViewById(R.id.progress);
        settings = (ConstraintLayout) findViewById(R.id.settings);
        home.setVisibility(View.VISIBLE);

    }


    public void updateCurrentPushUp(View v) {
        currPushUps += 1;
        currentBtn.setText(String.format("%s", currPushUps));
    }

    public void cancelCurrentPushUp(View v) {
        currPushUps = 0;
        currentBtn.setText(String.format("%s", currPushUps));
    }

    public void doneCurrentPushUp(View v) {
        long timeStamp = new Date().getTime();
        Sessions session = new Sessions(currPushUps, timeStamp);
        dbHandler.addSession(session);
        Toast.makeText(getApplicationContext(),
                "Today's results were saved.", Toast.LENGTH_SHORT).show();
    }

    public void showPushUps(View v) {
        pushUps.setText(dbHandler.databaseToString());
    }

    public void deletePushUps(View view) {
        dbHandler.emptyDatabase();
        pushUps.setText(dbHandler.databaseToString());
        Toast.makeText(getApplicationContext(),
                "Your pushups were deleted.", Toast.LENGTH_SHORT).show();
    }

}
