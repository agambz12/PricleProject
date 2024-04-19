package com.example.myapplication;

import com.example.myapplication.activities.UserMode;

public class SessionMode {

    private static UserMode userMode = UserMode.REGULAR;

    public static UserMode getUserMode() {
        return userMode;
    }

    public static void setUserMode(UserMode userMode) {
        SessionMode.userMode = userMode;
    }

    public static boolean isGuestMode() {
        return userMode.equals(UserMode.GUEST);
    }
}
