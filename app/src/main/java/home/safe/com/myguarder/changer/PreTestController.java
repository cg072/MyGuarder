package home.safe.com.myguarder.changer;

import android.content.ContentValues;

import java.util.List;

/**
 * Created by 김진복 on 2017-11-21.
 * 예제용
 */

public class PreTestController implements IProGuardianController {
    private ProGuardianDBHelper proGuardianDBHelper;

    public PreTestController () { }

    public PreTestController (ProGuardianDBHelper cDBHelper) {
        this.proGuardianDBHelper = cDBHelper;
    }

    @Override
    public int insert(ContentValues contentValues) {
        proGuardianDBHelper.insert(contentValues);
        return 0;
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        proGuardianDBHelper.search(contentValues);
        return null;
    }

    @Override
    public int update(ContentValues contentValues) {
        proGuardianDBHelper.update(contentValues);
        return 0;
    }

    @Override
    public int remove(ContentValues contentValues) {
        proGuardianDBHelper.remove(contentValues);
        return 0;
    }

    @Override
    public void setDBHelper(ProGuardianDBHelper cDBHelper) {
        this.proGuardianDBHelper = cDBHelper;
    }
}
