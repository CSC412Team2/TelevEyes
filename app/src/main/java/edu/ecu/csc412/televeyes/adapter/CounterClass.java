package edu.ecu.csc412.televeyes.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.format.DateUtils;
import android.widget.TextView;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import edu.ecu.csc412.televeyes.model.Show;
import edu.ecu.csc412.televeyes.util.Util;

/**
 * Created by jilli on 11/3/2016.
 */
public class CounterClass extends CountDownTimer {
    private TextView textView;
    private Show show;
    private Context context;

    public CounterClass(long millisInFuture, long countDownInterval, TextView textView, Show show, Context context) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.show = show;
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {

        int second = (int) (millisUntilFinished / 1000) % 60;
        int minute = (int) (millisUntilFinished / (1000 * 60)) % 60;
        int hour = (int) (millisUntilFinished / (1000 * 60 * 60)) % 24;
        int day = (int) (millisUntilFinished / (1000 * 60 * 60 * 24));

        String cdClock = String.format("%d days %02d:%02d:%02d", day, hour, minute, second);

        long milliFuture;

        String showTime = show.getSchedule().getShowTime();

        textView.setText(showTime);

        TimeZone timezoneone = TimeZone.getDefault();
        Date n = new Date();
        long milliNow = n.getTime();

        SimpleDateFormat format = null;

        format = new SimpleDateFormat("k:mm", Locale.getDefault());

        Date d = Util.getNextOccurenceOfDay(Util.getDayFromString(showTime));

        long milliShow = d.getTime();
        milliFuture = milliShow - milliNow;

        boolean nowInDst = timezoneone.inDaylightTime(n);
        boolean showInDst = timezoneone.inDaylightTime(d);

        if (showInDst == false && nowInDst == true) {
            milliFuture -= (1000 * 60 * 60);
        }
        if (nowInDst == false && showInDst == true) {
            milliFuture += (1000 * 60 * 60);
        }

        if (milliFuture < 1) {
            textView.setText("TBD");
            cancel();
        }

        System.out.print(cdClock);
        textView.setText(cdClock);
    }



    @Override
    public void onFinish() {


    }
}



