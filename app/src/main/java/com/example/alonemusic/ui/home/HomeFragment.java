package com.example.alonemusic.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.activity.LoginActivity;
import com.example.alonemusic.R;

public class HomeFragment extends Fragment {

    Button logout;
    TextView toolbarTitle;
    GlobalApplication app;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        app = (GlobalApplication)getActivity().getApplication();
        logout = view.findViewById(R.id.button_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.setLogoutFlag(true);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Home");

        return view;
    }
}