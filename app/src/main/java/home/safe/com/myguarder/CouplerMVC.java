package home.safe.com.myguarder;

import android.content.Context;

import com.safe.home.pgchanger.ProGuardianChanger;

/**
 * Created by JINNY_ on 2017-12-27.
 */

public class CouplerMVC extends ProGuardian{

    Context context;
    ProGuardianChanger changer;

    public CouplerMVC(Context context) {
        this.context = context;
    }

    public void setChanger()
    {
        changer = new ProGuardianChanger(context);
        changer.connectDB(MyGuarderDBHelper.PG_LOCATION);


    }


}
