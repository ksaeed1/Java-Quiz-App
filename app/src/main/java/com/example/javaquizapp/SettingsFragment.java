package com.example.javaquizapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.javaquizapp.databinding.FragmentSettingsBinding;

import java.util.Calendar;

public class SettingsFragment extends Fragment {
    FragmentSettingsBinding binding;
    SharedPreferences preferences;
    Calendar reminderCalendar = Calendar.getInstance();
    private android.widget.Toast Toast;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        preferences = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE);

        // DARK MODE
        boolean isDarkMode = preferences.getBoolean("dark_mode", false);
        binding.switchDarkMode.setChecked(isDarkMode);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    binding.switchDarkMode.setEnabled(false);
                    binding.switchDarkMode.post(() -> {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("dark_mode", isChecked);
                        editor.apply();

                        AppCompatDelegate.setDefaultNightMode(
                                isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
                        );
                        binding.switchDarkMode.setEnabled(true);
                    });
                });

        // REMINDER SETUP
        showSavedReminder();

        binding.btnSetReminder.setOnClickListener(v -> showReminderDialog());

        return root;
    }

    private void showReminderDialog() {
        final Calendar current = Calendar.getInstance();

        // Step 1: Date Picker
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            reminderCalendar.set(Calendar.YEAR, year);
            reminderCalendar.set(Calendar.MONTH, month);
            reminderCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Step 2: Time Picker
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view1, hourOfDay, minute) -> {
                reminderCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                reminderCalendar.set(Calendar.MINUTE, minute);
                reminderCalendar.set(Calendar.SECOND, 0);

                if (reminderCalendar.getTimeInMillis() <= System.currentTimeMillis()) {
                    Toast.makeText(requireContext(), "Please select a future time", Toast.LENGTH_SHORT).show();
                }else {
                    // Save to SharedPreferences
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong("reminder_time", reminderCalendar.getTimeInMillis());
                    editor.apply();

                    setReminder(reminderCalendar.getTimeInMillis());
                    showSavedReminder();
                }
            }, current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE), DateFormat.is24HourFormat(getContext()));

            timePickerDialog.show();

        }, current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showSavedReminder() {
        long savedTime = preferences.getLong("reminder_time", -1);
        if (savedTime != -1) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(savedTime);
            String formatted = DateFormat.format("dd-MM-yyyy, hh:mm a", cal).toString();
            binding.tvReminderInfo.setText("Quiz reminder set on: " + formatted);
        } else {
            binding.tvReminderInfo.setText("No reminder set");
        }
    }

    private void setReminder(long timeInMillis) {
        if (!canScheduleExactAlarms()) {
            // Redirect user to settings to allow exact alarms
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
            return;
        }

        Intent intent = new Intent(requireContext(), ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            }
        }
    }

    private boolean canScheduleExactAlarms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
            return alarmManager.canScheduleExactAlarms();
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
