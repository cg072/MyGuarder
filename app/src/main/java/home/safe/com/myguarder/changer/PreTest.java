package home.safe.com.myguarder.changer;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by 김진복 on 2017-11-21.
 * 예제용
 * 안드로이드에서는 db접근이 필요한 액티비티와 동일한 위치
 */

public class PreTest {
    Context context;
    ProGuardianChanger proGuardianChanger;

    public void make() {
        proGuardianChanger = new ProGuardianChanger(context, ProGuardianDBHelper.TABLE_MEMBER);

        ContentValues contentValues = new PreTestVO().convertDataToContentValues();
        int result = proGuardianChanger.insertData(contentValues);

    }
}
