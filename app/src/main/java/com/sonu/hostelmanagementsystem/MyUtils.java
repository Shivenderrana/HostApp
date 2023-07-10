package com.sonu.hostelmanagementsystem;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

final class MyUtils {
    static void hideKeyBoard(Activity activity){
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if(view == null){
            view = new View(activity);
        }
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
