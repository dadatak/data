package com.prilaga.data.utils;

import android.os.CountDownTimer;

/**
 * Created by macbook on 23.09.15.
 */
public final class DownTimerUtil {

    long millisInFuture;
    long countDownInterval;

    public DownTimerUtil(long millisInFuture) {
        init(millisInFuture, 1000);
    }

    public DownTimerUtil(long millisInFuture, long countDownInterval) {
        init(millisInFuture, countDownInterval);
    }

    private void init(long millisInFuture, long countDownInterval) {
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
        if (countDownTimer != null)
            countDownTimer.cancel();
        countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {

            public void onTick(long millisUntilFinished) {

                if (downTimerListener != null)
                    downTimerListener.onTick(millisUntilFinished);
            }

            public void onFinish() {
//            mTextField.setText("done!");
            }
        };
    }

    private CountDownTimer countDownTimer;

    public void cancel() {
        countDownTimer.cancel();
    }

    public void start() {
        countDownTimer.start();
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    /*
             LISTENER
    */

    private DownTimerListener downTimerListener;

    public void setDownTimerListener(DownTimerListener downTimerListener) {
        this.downTimerListener = downTimerListener;
    }

    public interface DownTimerListener {
        void onTick(long time);
    }

}
