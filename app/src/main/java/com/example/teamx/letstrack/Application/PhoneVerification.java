package com.example.teamx.letstrack.Application;

import android.telephony.SmsManager;
import android.util.Log;

import java.util.Random;

/**
 * Created by Aranyak on 08-Nov-17.
 */

public class PhoneVerification {

    private String phone;

    private String code;

    public String getPhone() {
        return phone;
    }

    public PhoneVerification(String phone) {

        this.phone = phone;
        Random rn = new Random();

        code = Integer.toString(100000 + rn.nextInt(100000));
    }

    public void setCode(String code) {
        this.code = code;
    }


    public void sendVerificationtext() {


        String message = "Your verification code is " + code + ".";
        Log.d("", "The code generated is " + code);
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(phone, null, message, null, null);
    }

    public String getCode() {
        return code;
    }

    public boolean verifyCode(String c) {

        if (c.equals(code))
            return true;
        else
            return false;
    }
}
