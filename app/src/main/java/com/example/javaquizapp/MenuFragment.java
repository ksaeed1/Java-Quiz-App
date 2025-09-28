package com.example.javaquizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class MenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        //Find buttons
        ImageButton backButton = view.findViewById(R.id.backButton);
        Button multipleChoiceButton = view.findViewById(R.id.multipleChoiceButton);
        Button trueOrFalseButton = view.findViewById(R.id.trueOrFalseButton);

        backButton.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        //Button for mcq
        multipleChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load the QuizFragment
                Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_navigation_quiz);
            }
        });

        //Button for true or false
        trueOrFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load the TrueOrFalse
                Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_navigation_true_or_false);
            }
        });

        return view;
    }
}