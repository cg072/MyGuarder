package home.safe.com.myguarder;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by JINNY_ on 2018-01-19.
 */

public class StateChecker {

    SharedPreferences preferences;
    int stateCheck = 0;


    public StateChecker( SharedPreferences preferences)
    {
        this.preferences = preferences;
    }

    public void updateCheck()
    {
        stateCheck = 0;

        if(preferences.getBoolean("ActivityState",false))
            stateCheck += 1;
        if(preferences.getBoolean("MapState", false))
            stateCheck += 1;
        if(preferences.getBoolean("loginState", false))
            stateCheck += 1;
    }

    public void runCheck()
    {
        switch (stateCheck)
        {
            case 0:
                //로그인 안됨
                break;
            case 1:
                //엑티비티 여부
                break;
            case 2:
                // 맵
                break;
            case 3:
                //true
                break;
        }

    }

}
