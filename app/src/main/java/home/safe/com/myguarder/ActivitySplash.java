package home.safe.com.myguarder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by JINNY_ on 2018-01-31.
 */

public class ActivitySplash extends ProGuardian{



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ActivitySplash","onCreate");

        getPermissions();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ActivitySplash","onResume");

        if(getPermissions())
        {
            Intent intent = new Intent(ActivitySplash.this, MainActivity.class);
            startActivity(intent);

            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ActivitySplash","onDestroy");


    }
}
