package home.safe.com.myguarder;

import android.os.Handler;
import android.os.Message;

/**
 *
 * @author 경창현
 * @version 1.0.0
 * @text 서비스에 실행할 스레드
 * @since 2018-01-12 오후 10:47
**/
public class ServiceThread extends Thread{

    Handler handler;
    boolean isRun = true;
//    boolean isExecuteDialog = false;

    public ServiceThread(Handler handler)
    {
        this.handler = handler;
    }

    public void stopForever()
    {
        synchronized (this)
        {
            this.isRun = false;
        }
    }

//    public void executeDialog()
//    {
//        synchronized (this)
//        {
//            this.isExecuteDialog = true;
//        }
//    }

    public void run()
    {
        while (isRun)
        {
            Message message = handler.obtainMessage();
            // 메시지 ID 설정
            message.what = 0;
            // 메시지 내용 설정 (Object)
            message.obj = new String("지킴이 이름");


            handler.sendMessage(message);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
