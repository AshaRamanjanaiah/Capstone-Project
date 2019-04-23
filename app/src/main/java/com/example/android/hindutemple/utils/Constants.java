package com.example.android.hindutemple.utils;

import android.support.design.widget.TextInputLayout;
import android.util.Patterns;

import com.example.android.hindutemple.R;

import java.util.regex.Pattern;

public class Constants {

    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                   // "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    public static boolean validateEmail(TextInputLayout view, String emailInput){

        if(emailInput.isEmpty()){
            view.setError(String.valueOf(R.string.email_field_cant_be_empty));
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            view.setError(String.valueOf(R.string.enter_valid_email));
            return false;
        }else {
            view.setError(null);
            return true;
        }

    }

    public static boolean validatePassword(TextInputLayout view, String passwordInput){

        if(passwordInput.isEmpty()){
            view.setError(String.valueOf(R.string.password_can_not_be_empty));
            return false;
        }else if(!Constants.PASSWORD_PATTERN.matcher(passwordInput).matches()){
            view.setError(String.valueOf(R.string.password_should_contain_6_chars));
            return false;
        }else {
            view.setError(null);
            return true;
        }
    }

    public static boolean validateField(String name, TextInputLayout view, String value) {
        if (value.isEmpty()) {
            view.setError(name + " field can not be empty");
            return false;
        }
            view.setError(null);
            return true;
    }

}
