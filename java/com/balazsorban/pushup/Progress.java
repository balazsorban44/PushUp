package com.balazsorban.pushup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Progress extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageButton deletePushUpsBtn;
    private TextView pushUps;

    public Progress() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        deletePushUpsBtn = (ImageButton) view.findViewById(R.id.deletePushUpsBtn);
        deletePushUpsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                deletePushUps(view);
            }
        });
        pushUps = (TextView) view.findViewById(R.id.pushUpsString);
        showPushUps(pushUps);
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
        if (((MainActivity) getActivity()).getDatabase().databaseToString().equals("You have no saved data.")){
            deletePushUpsBtn.setEnabled(false);
        } else deletePushUpsBtn.setEnabled(true);
    }

    public interface OnFragmentInteractionListener {
    }

    public void showPushUps(View v) {
        String result = ((MainActivity) getActivity()).getDatabase().databaseToString();
        if (result.length() != 0){
            pushUps.setText(((MainActivity) getActivity()).getDatabase().databaseToString());
        }
        else pushUps.setText("You have no saved data.");
    }

    public void deletePushUps(View v) {
        ((MainActivity) getActivity()).getDatabase().emptyDatabase();
        deletePushUpsBtn.setEnabled(false);
        showPushUps(pushUps);
        Toast.makeText(getContext(),
                "Your pushups were deleted.", Toast.LENGTH_SHORT).show();
    }
}