package com.example.grocerynotifier.converters;

import android.util.Patterns;

import androidx.room.Room;

import com.example.grocerynotifier.daos.AppDatabase;
import com.example.grocerynotifier.daos.UserDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class ValidatorUtils {

    public static String validateEmail(String email, UserDao userDao) {
        if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email address.");
        }

        if (userDao.getUserByEmail(email) != null) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        return email;
    }

    public static String validatePhone(String phone) {
        if (phone != null && Pattern.matches("^\\d{10}$", phone)) {
            return phone;
        } else {
            throw new IllegalArgumentException("Invalid phone number. Must be 10 digits.");
        }
    }

    public static String validateDate(String date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return date;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use dd-MM-yyyy.");
        }
    }

    public static String validatePassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null.");
        }
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        if (Pattern.matches(passwordPattern, password)) {
            return password;
        } else {
            throw new IllegalArgumentException("Password must contain at least 8 characters, including 1 digit, 1 uppercase letter, 1 special character.");
        }
    }

    public static String validateConfirmPassword(String password, String confirmPassword) {
        if (password != null && password.equals(confirmPassword)) {
            return confirmPassword;
        } else {
            throw new IllegalArgumentException("Passwords do not match.");
        }
    }
}
