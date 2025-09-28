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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.javaquizapp.databinding.FragmentMultipleChoiceBinding;

public class MultipleChoiceFragment extends Fragment {
    FragmentMultipleChoiceBinding binding;
    private TextView questionTextView, timerTextView, progressTextView;
    private RadioGroup optionsRadioGroup;
    private RadioButton option1, option2, option3, option4;
    private Button nextButton, hintButton;
    private ImageButton menuButton;
    private ProgressBar timerProgressBar;

    private String[] questions = {
            "Which data type is used to store whole numbers in Java?",
            "What is the correct syntax for declaring a variable in Java?",
            "What does the System.out.println() method do?",
            "Which of the following is NOT a valid Java variable name?",
            "What will be the output of the following code?\nint x = 5;\nx += 3;\nSystem.out.println(x);",
            "How do you correctly create an object in Java?",
            "Which keyword is used to inherit a class in Java?",
            "What is the default value of a boolean variable in Java?",
            "What is the difference between == and .equals() in Java?",
            "What is method overloading in Java?",
            "What is the output of the following code?\nString str = \"Hello\";\nstr.concat(\" World\");\nSystem.out.println(str);",
            "What is the purpose of the final keyword in Java?",
            "Which Java collection allows only unique elements?",
            "How do you create a thread in Java?",
            "What will happen if a Java program runs out of memory?",
            "What is the difference between HashMap and Hashtable in Java?",
            "What is garbage collection in Java?",
            "What does the volatile keyword do in Java?",
            "What is the purpose of the synchronised keyword?",
            "What is the default access modifier for a class in Java?"
    };

    private String[][] options = {
            {"A) float", "B) int", "C) boolean", "D) double"},
            {"A) variableName int;", "B) int variableName;", "C) variableName = int;", "D) declare int variableName;"},
            {"A) Reads input from the user", "B) Prints text to the console", "C) Terminates the program", "D) Saves data to a file"},
            {"A) myVariable", "B) _myVariable", "C) 2ndVariable", "D) myVariable2"},
            {"A) 3", "B) 5", "C) 8", "D) 53"},
            {"A) ClassName object = new ClassName();", "B) object = ClassName();", "C) ClassName object;", "D) new ClassName object();"},
            {"A) implements", "B) extends", "C) super", "D) inherit"},
            {"A) null", "B) true", "C) false", "D) 0"},
            {"A) == compares references, .equals() compares values", "B) .equals() compares references, == compares values", "C) Both do the same thing", "D) None of the above"},
            {"A) Defining multiple methods with the same name but different parameters", "B) Calling a method multiple times", "C) Overriding a method in a subclass", "D) Writing methods with the same name and parameters"},
            {"A) Hello", "B) Hello World", "C) Compilation error", "D) None of the above"},
            {"A) It marks a class as unmodifiable.", "B) It prevents a variable from being changed.", "C) It prevents method overriding.", "D) All of the above."},
            {"A) List", "B) Set", "C) Map", "D) Array"},
            {"A) Implementing the Runnable interface", "B) Extending the Thread class", "C) Using the Executors framework", "D) All of the above"},
            {"A) It will restart automatically.", "B) It will throw an OutOfMemoryError.", "C) The system will allocate more memory.", "D) The program will continue running slowly."},
            {"A) HashMap is synchronised, Hashtable is not", "B) Hashtable allows null keys, HashMap does not", "C) HashMap allows one null key, Hashtable does not allow null keys", "D) There is no difference"},
            {"A) A way to manually delete objects", "B) A process that automatically removes unused objects", "C) A memory management tool for developers", "D) A method for optimizing runtime performance"},
            {"A) Prevents a variable from being changed", "B) Ensures changes to a variable are visible to all threads", "C) Makes a variable constant", "D) Improves memory efficiency"},
            {"A) It ensures that only one thread can execute a method/block at a time", "B) It improves the speed of execution", "C) It prevents a variable from being modified", "D) It stops all other threads from executing"},
            {"A) public", "B) private", "C) protected", "D) package-private"}
    };

    private String[] hints = {
            "Think about the data type that doesn't allow decimal points.",
            "The syntax follows the pattern: type first, then variable name.",
            "This method is commonly used for debugging or displaying output.",
            "Variable names cannot start with a number in Java.",
            "x += 3 is shorthand for x = x + 3.",
            "The 'new' keyword is required to instantiate an object.",
            "The keyword is also used for extending interfaces.",
            "Boolean values can only be true or false.",
            "== checks if two objects are the same instance, while .equals() compares content.",
            "It allows methods to share the same name but differ in parameters.",
            "Strings are immutable—concat() returns a new string rather than modifying the original.",
            "It can be applied to variables, methods, and classes for restriction.",
            "This collection rejects duplicates and is unordered by default.",
            "Java supports multithreading in multiple ways, including interfaces and inheritance.",
            "Java throws an error when it cannot allocate more memory.",
            "One is thread-safe (legacy), the other is not (modern and allows null).",
            "It runs automatically in the background to free memory.",
            "It guarantees visibility across threads but doesn't provide atomicity.",
            "Used to prevent race conditions in multithreading.",
            "If no modifier is specified, the class is accessible only within its package."
    };

    private int[] answers = {1, 1, 1, 2, 2, 0, 1, 2, 0, 0, 0, 3, 1, 3, 1, 2, 1, 1, 0, 3};
    private int currentQuestionIndex = 0;
    private int score = 0;
    private CountDownTimer timer;
    private final long TIME_PER_QUESTION = 15000; //change here for timer seconds per question

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMultipleChoiceBinding.inflate(inflater, container, false);
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
        optionsRadioGroup = view.findViewById(R.id.optionsRadioGroup);
        option1 = view.findViewById(R.id.option1);
        option2 = view.findViewById(R.id.option2);
        option3 = view.findViewById(R.id.option3);
        option4 = view.findViewById(R.id.option4);
        nextButton = view.findViewById(R.id.nextButton);
        hintButton = view.findViewById(R.id.hintButton);
        menuButton = view.findViewById(R.id.menuButton);

        menuButton.setOnClickListener(v -> {
            showExitConfirmationDialog();
        });
        loadQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionsRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity(), "Please select an option", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if answer is correct
                    int selectedAnswerIndex = getSelectedAnswerIndex();
                    if (selectedAnswerIndex == answers[currentQuestionIndex]) {
                        score++;
                        Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "Incorrect!", Toast.LENGTH_SHORT).show();
                    }

                    if (timer != null) {
                        timer.cancel();
                    }

                    currentQuestionIndex++;
                    if (currentQuestionIndex < questions.length) {
                        loadQuestion();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putInt("score", score);
                        bundle.putInt("totalQuestions", questions.length);
                        Navigation.findNavController(v).navigate(R.id.action_navigation_quiz_to_navigation_results, bundle);
                    }
                }
            }
        });

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHint();
            }
        });


    }

    private int getSelectedAnswerIndex() {
        int selectedId = optionsRadioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.option1) return 0;
        if (selectedId == R.id.option2) return 1;
        if (selectedId == R.id.option3) return 2;
        if (selectedId == R.id.option4) return 3;
        return -1; // No answer selected
    }

    private void showExitConfirmationDialog() {
        // if completed all questions, go back to menu directly
        if (currentQuestionIndex >= questions.length - 1) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_quiz_to_navigation_menu);
            return;
        }
        menuButton.setEnabled(false);

        // check if show dialog
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Exit Quiz")
                .setMessage("Are you sure you want to exit? Your progress will not be saved.")
                .setPositiveButton("Exit", (d, which) -> {
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_quiz_to_navigation_menu);
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnDismissListener(d -> {
            menuButton.setEnabled(true);
        });
        dialog.show();
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

    private void loadQuestion() {
        updateProgress();
        questionTextView.setText(questions[currentQuestionIndex]);
        option1.setText(options[currentQuestionIndex][0]);
        option2.setText(options[currentQuestionIndex][1]);
        option3.setText(options[currentQuestionIndex][2]);
        option4.setText(options[currentQuestionIndex][3]);
        optionsRadioGroup.clearCheck();
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
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.length) {
                    loadQuestion();
                } else {
                    // Show results when time runs out
                    Bundle bundle = new Bundle();
                    bundle.putInt("score", score);
                    bundle.putInt("totalQuestions", questions.length);
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_quiz_to_navigation_results, bundle);
                }
            }
        }.start();
    }
}