package com.example.javaquizapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class StartFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);


        Button startButton = view.findViewById(R.id.btn_start_quiz);
        startButton.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_startFragment_to_navigation_home)
        );

        return view;
    }
}
