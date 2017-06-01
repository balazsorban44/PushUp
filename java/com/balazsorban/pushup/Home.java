package com.balazsorban.pushup;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.util.Date;

public class Home extends Fragment{

    private OnFragmentInteractionListener mListener;
    private long timeStart;
    private Button currentPushUpBtn, donePushUpBtn, cancelPushUpBtn;
    private Chronometer elapsed;
    private FloatingActionButton activityBtn;
    private int currPushUps = 0;
    private DBHandler dbHandler;

    public Home() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        elapsed = (Chronometer) view.findViewById(R.id.chronometer);
        activityBtn = (FloatingActionButton) view.findViewById(R.id.activityBtn);
        currentPushUpBtn = (Button) view.findViewById(R.id.currentPushUpBtn);
        donePushUpBtn = (Button) view.findViewById(R.id.donePushUpBtn);
        cancelPushUpBtn = (Button) view.findViewById(R.id.cancelPushUpBtn);
        setToStart();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        currentPushUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                updateCurrentPushUp(view);
            }
        });
        donePushUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                doneCurrentPushUp(view);
            }
        });
        cancelPushUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                cancelCurrentPushUp(view);
            }
        });
        currentPushUpBtn.setText(String.format("%s", getCurrPushUps()));
    }

    public interface OnFragmentInteractionListener {
        DBHandler getDatabase();
    }

    void setToStart(){
        activityBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                activityBtn.setImageResource(R.drawable.ic_pause_black_24dp);
                startActivity(view);
                Toast.makeText(getContext(),
                        "Session started.", Toast.LENGTH_SHORT).show();
                activityBtn.setPadding(30,30,30,30);
            }
        });
    }

    void setToPause(){
        activityBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                activityBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                pauseActivity(view);
                Toast.makeText(getContext(),
                        "Session paused.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    int getCurrPushUps(){
        return currPushUps;
    }
    void setCurrPushUps(int i) {
        if (i == 0){
            this.currPushUps = 0;
        }
        else this.currPushUps += 1;
    }

    void startActivity(View v){
        if (currPushUps == 0){
            elapsed.start();
            timeStart = new Date().getTime();
        }
        setToPause();
    }
    void pauseActivity(View v){
        elapsed.stop();
        setToStart();
    }
    void updateCurrentPushUp(View v) {

        setCurrPushUps(1);
        currentPushUpBtn.setText(String.format("%s", getCurrPushUps()));
    }

    void cancelCurrentPushUp(View v) {
        setCurrPushUps(0);
        currentPushUpBtn.setText(String.format("%s", getCurrPushUps()));

        setToStart();
        Toast.makeText(getContext(),
                "Session was cancelled.", Toast.LENGTH_SHORT).show();
    }

    public void doneCurrentPushUp(View v) {
        int currPushUps = getCurrPushUps();
        long finished = new Date().getTime();
        elapsed.stop();
        long duration = (finished - timeStart)/1000;
        if (duration > 43200){
            duration = 0;
        }
        Sessions session = new Sessions(currPushUps, finished, duration);
        ((MainActivity) getActivity()).getDatabase().addSession(session);
        setCurrPushUps(0);
        currentPushUpBtn.setText(Integer.toString(getCurrPushUps()));
        activityBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        setToStart();
        Toast.makeText(getContext(),
                "Session saved.", Toast.LENGTH_SHORT).show();
    }
}
