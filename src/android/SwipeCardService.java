package com.print.printer.hardware;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.koolcloud.jni.ClubMsrInterface;


public class SwipeCardService extends Service {

    private static final String ACTION_OPEN_SWIPE_CARD = "com.wanda.pos.hardware.OPEN_SWIPE_CARD";

    private static final String ACTION_CLOSE_SWIPE_CARD = "com.wanda.pos.hardware.CLOSE_SWIPE_CARD";

    // receiver action
    public static final String ACTION_SWIPE_CARD = "com.wanda.pos.hardware.SWIPE_CARD";

    // extra-data
    private static final String EXTRA_CARD_INFO = "card_info";

    private LocalBroadcastManager mBroadcastManager;
    private ExecutorService mExecutor;
    private boolean isRunning;

    @Override
    public void onCreate() {
        super.onCreate();
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (ACTION_OPEN_SWIPE_CARD.equals(action)) {
            if (!isRunning /*&& open()*/) {
                mExecutor.execute(new SwipeCardRunnable());
            }
        } else if (ACTION_CLOSE_SWIPE_CARD.equals(action)) {
            try {
                isRunning = false;
                int result = ClubMsrInterface.cancelPoll();
                if (result >= 0) {
                    ClubMsrInterface.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mExecutor.shutdownNow();
            stopSelf();
        }
        return START_STICKY_COMPATIBILITY;
    }

    private boolean open() {
        try {
            int result = ClubMsrInterface.open();
            if (result < 0) {
                // try close and open
                result = ClubMsrInterface.close();
                if (result >= 0) {
                    result = ClubMsrInterface.open();
                }
                if (result < 0) {
                    ClubMsrInterface.close();
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void open(Context context) {
        Intent intent = new Intent(context, SwipeCardService.class);
        intent.setAction(ACTION_OPEN_SWIPE_CARD);
        context.startService(intent);
    }

    public static String getReceiverData(Intent intent) {
        return intent.getStringExtra(EXTRA_CARD_INFO);
    }

    public static void close(Context context) {
        Intent intent = new Intent(context, SwipeCardService.class);
        intent.setAction(ACTION_CLOSE_SWIPE_CARD);
        context.startService(intent);
    }

    private class SwipeCardRunnable implements Runnable {
        @Override
        public void run() {
            try {
                if(!open()) return;
            } catch (Exception e) {
                return;
            }
            isRunning = true;
            while (isRunning) {
                try {
                    ClubMsrInterface.poll(500);

                    //
                    byte track[] = new byte[255];
                    ClubMsrInterface.getTrackData(1, track, track.length);
                    final String s = new String(track);
                    Intent intent = new Intent(ACTION_SWIPE_CARD);
                    intent.putExtra(EXTRA_CARD_INFO, s);
                    mBroadcastManager.sendBroadcast(intent);
                } catch (Exception e) {
                }
            }
        }
    }
}
