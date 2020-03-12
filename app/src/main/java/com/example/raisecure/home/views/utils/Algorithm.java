package com.example.raisecure.home.views.utils;

import com.example.raisecure.home.views.Const;

public class Algorithm {

    public static boolean checkPinRequirement(String pin) {
        return pin.length() >= Const.MIN_PIN_LENGTH;
    }
}
