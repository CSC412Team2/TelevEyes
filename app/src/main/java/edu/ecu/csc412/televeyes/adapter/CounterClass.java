package edu.ecu.csc412.televeyes.adapter;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import edu.ecu.csc412.televeyes.model.Show;

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
            String cdClock = String.format("%02d:%02d:%02d:%02d", day, hour, minute, second);

            long milliFuture;
            String showDate = mValues.get(position).airdate;
            String showTime = mValues.get(position).airtime;

            textViewTime.setText(showTime);

            TimeZone timezoneone = TimeZone.getDefault();
            Date n = new Date();
            long milliNow = n.getTime();

            SimpleDateFormat format = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
            } else {
                format = new SimpleDateFormat("yyyy-MM-dd k:mm z", context.getResources().getConfiguration().locale);
            }
            Date d = null;
            try {
                d = format.parse(showTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long milliShow = d.getTime();
            milliFuture = milliShow - milliNow;

            boolean nowInDst = timezoneone.inDaylightTime(n);
            boolean showInDst = timezoneone.inDaylightTime(d);

            if (showInDst==false && nowInDst==true) {
                milliFuture -= (1000*60*60);
            }
            if (nowInDst==false && showInDst==true){
                milliFuture += (1000*60*60);
            }

            if(milliFuture < 1){
                textView.setText("TBD");
                cancel();
            }

            int second = (int) (millisUntilFinished / 1000) % 60;
            int minute = (int)(millisUntilFinished / (1000 * 60)) % 60;
            int hour = (int)(millisUntilFinished / (1000 * 60 * 60)) %24;
            int day = (int) (millisUntilFinished/ (1000 * 60 * 60 * 24));

            System.out.print(cdClock);
            textView.setText(cdClock);
        }
        @Override
        public void onFinish() {


        }
}



