package com.example.javaquizapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.javaquizapp.databinding.FragmentMultipleChoiceBinding;
import com.example.javaquizapp.databinding.FragmentTrueOrFalseBinding;

public class TrueOrFalseFragment extends Fragment {
    FragmentTrueOrFalseBinding binding;
    private TextView questionTextView, timerTextView, progressTextView;
    private Button trueButton, falseButton, nextButton, hintButton;
    private ImageButton menuButton;
    private ProgressBar timerProgressBar;

    private String[] questions = {
            "The `main` method in Java must always be declared as 'public static int main(String[] args)'.",
            "Java is a statically typed programming language.",
            "Identifiers in Java can start with a number.",
            "The `System.out.println` method is used for printing output to the console in Java.",
            "Java supports multiple inheritance using classes.",
            "An abstract class in Java can have both abstract and concrete methods.",
            "A constructor in Java can be `private`.",
            "Interfaces in Java can have instance variables.",
            "A subclass in Java can override a `final` method from its superclass.",
            "In Java, primitive data types are passed by value.",
            "A Java array can hold multiple data types.",
            "Garbage collection in Java is manually controlled by the programmer.",
            "A `static` variable belongs to an instance of a class, not the class itself.",
            "The `String` class in Java is immutable.",
            "A `try` block in Java must always be followed by a `catch` block."
    };

    private boolean[] answers = {
            false,  // Question 1
            true,   // Question 2
            false,  // Question 3
            true,   // Question 4
            false,  // Question 5
            true,   // Question 6
            true,   // Question 7
            false,  // Question 8
            false,  // Question 9
            true,   // Question 10
            false,  // Question 11
            false,  // Question 12
            false,  // Question 13
            true,   // Question 14
            false   // Question 15
    };

    private String[] hints = {
            "The main method should return void, not int.",
            "Java requires explicit type declarations at compile time.",
            "Java identifiers must start with a letter, underscore or dollar sign.",
            "This is the standard output method in Java.",
            "Java only supports single inheritance for classes, but multiple inheritance for interfaces.",
            "Abstract classes can have implemented methods alongside abstract ones.",
            "Private constructors are used in singleton patterns.",
            "Interfaces can only have constants (public static final variables).",
            "Final methods cannot be overridden by subclasses.",
            "Primitives are passed by value, objects by reference value.",
            "Java arrays can only hold one type of element.",
            "Garbage collection is automatic in Java's JVM.",
            "Static variables belong to the class, not instances.",
            "String objects cannot be modified after creation.",
            "A try block can be followed by catch, finally, or both."
    };

    private int currentQuestionIndex = 0;
    private int score = 0;
    private CountDownTimer timer;
    private final long TIME_PER_QUESTION = 15000;
    private boolean answerSelected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrueOrFalseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressTextView = view.findViewById(R.id.progressTextView);
        questionTextView = view.findViewById(R.id.questionTextView);
        timerTextView = view.findViewById(R.id.timerTextView);
        timerProgressBar = view.findViewById(R.id.timerProgressBar);
        trueButton = view.findViewById(R.id.trueButton);
        falseButton = view.findViewById(R.id.falseButton);
        nextButton = view.findViewById(R.id.nextButton);
        hintButton = view.findViewById(R.id.hintButton);

        loadQuestion();

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answerSelected) {
                    Toast.makeText(getActivity(), "Please select an option", Toast.LENGTH_SHORT).show();
                } else {
                    moveToNextQuestion(); }
            }
        });

        nextButton.setVisibility(View.VISIBLE);

        menuButton = view.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> {
                    showExitConfirmationDialog();
        });

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHint();
            }
        });

    }

    private void checkAnswer(boolean userAnswer) {
        if (answerSelected) return; // Prevent multiple answers

        trueButton.setEnabled(false);
        falseButton.setEnabled(false);
        answerSelected = true;
        boolean correctAnswer = answers[currentQuestionIndex];

        if (userAnswer == correctAnswer) {
            score++;
            Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Incorrect!", Toast.LENGTH_SHORT).show();
        }

    }

    private void moveToNextQuestion() {
        if (timer != null) {
            timer.cancel();
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < questions.length) {
            loadQuestion();
        } else {
            // Show results fragment with score
            Bundle bundle = new Bundle();
            bundle.putInt("score", score);
            bundle.putInt("totalQuestions", questions.length);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_true_or_false_to_navigation_results, bundle);
        }
    }

    private void showHint() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < hints.length) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Hint");
            builder.setMessage(hints[currentQuestionIndex]);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else {
            Toast.makeText(getActivity(), "No hint available", Toast.LENGTH_SHORT).show();
        }
    }

    private void showExitConfirmationDialog() {
        // if completed all questions, go back to menu directly
        if (currentQuestionIndex >= questions.length - 1) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_true_or_false_to_navigation_menu);
            return;
        }
        menuButton.setEnabled(false);

        // check if show dialog
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Exit Quiz")
                .setMessage("Are you sure you want to exit? Your progress will not be saved.")
                .setPositiveButton("Exit", (d, which) -> {
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_true_or_false_to_navigation_menu);
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnDismissListener(d -> {
            menuButton.setEnabled(true);
        });
                dialog.show();
    }

    private void loadQuestion() {
        answerSelected = false;
        trueButton.setEnabled(true);
        falseButton.setEnabled(true);

        updateProgress();
        questionTextView.setText(questions[currentQuestionIndex]);

        // Reset button states
        trueButton.setBackgroundColor(getResources().getColor(R.color.default_button));
        falseButton.setBackgroundColor(getResources().getColor(R.color.default_button));

        startTimer();
    }

    private void updateProgress() {
        int totalQuestions = questions.length;
        int currentQuestionNumber = currentQuestionIndex + 1;
        String progressText = "Question " + currentQuestionNumber + " of " + totalQuestions;
        progressTextView.setText(progressText);
    }

    private void startTimer() {
        timer = new CountDownTimer(TIME_PER_QUESTION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) (millisUntilFinished / 1000);
                timerTextView.setText(String.valueOf(progress));
                timerProgressBar.setProgress(progress * 10);
            }

            @Override
            public void onFinish() {
                timerTextView.setText("0");
                if (!answerSelected) {
                    // Highlight correct answer when time runs out
                    checkAnswer(!answers[currentQuestionIndex]); // This will show the correct answer
                }
                // Auto-proceed after a short delay
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                moveToNextQuestion();
                            }
                        },
                        1500);
            }
        }.start();
    }
}