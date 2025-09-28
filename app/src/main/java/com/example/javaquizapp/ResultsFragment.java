package com.example.javaquizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class ResultsFragment extends Fragment {

    private int score;
    private int totalQuestions;

    public ResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            score = getArguments().getInt("score");
            totalQuestions = getArguments().getInt("totalQuestions");
        }

        TextView scoreTextView = view.findViewById(R.id.scoreTextView);
        Button menuButton = view.findViewById(R.id.menuButton);

        int percentage = (int) (((double) score / totalQuestions) * 100);

        String resultText = String.format("Your Score: %d/%d\n(%d%%)", score, totalQuestions, percentage);
        scoreTextView.setText(resultText);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.navigation_home);
            }
        });

    }
}