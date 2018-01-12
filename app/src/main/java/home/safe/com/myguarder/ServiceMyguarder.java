package home.safe.com.myguarder;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 *
 * @author 경창현
 * @version 1.0.0
 * @text 서비스
 * @since 2018-01-12 오후 10:47
**/
public class ServiceMyguarder extends Service {

    ServiceThread thread;

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
        thread = new ServiceThread(handler);
        thread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        thread.stopForever();
        thread = null;  //쓰레기 값을 넣어줘야 빨리 회수한다.
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
            Intent intent = new Intent(ServiceMyguarder.this, ActivityPopupLocationRequest.class);
            intent.putExtra("service","지킴이이름");
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(ServiceMyguarder.this, 0,  intent, PendingIntent.FLAG_UPDATE_CURRENT);

            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

        }
    }
}
