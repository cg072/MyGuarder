package home.safe.com.myguarder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends ProGuardian implements IProGuardian {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    // 나는야 종하찡
}

