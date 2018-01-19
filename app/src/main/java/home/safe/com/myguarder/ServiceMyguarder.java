package home.safe.com.myguarder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 *
 * @author 경창현
 * @version 1.0.0
 * @text 서비스
 * @since 2018-01-12 오후 10:47
**/
public class ServiceMyguarder extends Service {

    ServiceMyguarderThread thread;

    public ServiceMyguarder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        HandlerServiceMyguarder handler = new HandlerServiceMyguarder();
        thread = new ServiceMyguarderThread(handler);
        thread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        thread.stopForever();
        thread = null;  //쓰레기 값을 넣어줘야 빨리 회수한다.
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d("ServiceMyguarder","onTaskRemoved");
    }

    /**
     *
     * @author 경창현
     * @version 1.0.0
     * @text 서비스 핸들러
     * @since 2018-01-12 오후 9:09
    **/
    class HandlerServiceMyguarder extends Handler
    {

        @Override
        public void handleMessage(Message msg) {

            Notification.Builder noti = new Notification.Builder(ServiceMyguarder.this);
            noti.setSmallIcon(R.drawable.ic_settings_black_24dp);
            noti.setTicker("티커입니다.");
            noti.setContentTitle("타이틀");
            noti.setContentText("누구누구님이 위치정보를 요청하였습니다.");

            NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1234,noti.build());

            Intent intent = new Intent(ServiceMyguarder.this, MainActivity.class);
            intent.putExtra("service","지킴이이름");
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(ServiceMyguarder.this, 1234,  intent, PendingIntent.FLAG_ONE_SHOT);
            noti.setContentIntent(pendingIntent);

//            Intent intent = new Intent(ServiceMyguarder.this, ActivityPopupLocationRequest.class);
//            intent.putExtra("service","지킴이이름");
//            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//            PendingIntent pendingIntent =
//                    PendingIntent.getActivity(ServiceMyguarder.this, 0,  intent, PendingIntent.FLAG_UPDATE_CURRENT);

            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

        }
    }
}
