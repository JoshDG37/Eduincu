package com.example.test_cont;

import android.app.Application;
import com.firebase.client.Firebase;

public class Test_cont extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
