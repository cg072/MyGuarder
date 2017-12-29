package home.safe.com.myguarder;

import android.content.Context;

import com.safe.home.pgchanger.ProGuardianChanger;
import com.safe.home.pgchanger.ProGuardianDBHelper;

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
        changer.setTargetDBHelper(new MyGuarderDBHelper(context, ProGuardianDBHelper.DB_NAME,null,ProGuardianDBHelper.DB_VERSION,MyGuarderDBHelper.PG_LOCATION));
        changer.setTargetController(new MyGuarderController());
        changer.connectDB(MyGuarderDBHelper.PG_LOCATION);


    }


}
