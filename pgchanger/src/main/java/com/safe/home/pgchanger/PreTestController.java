package com.safe.home.pgchanger;

import android.content.ContentValues;

import java.util.List;

/**
 * Created by 김진복 on 2017-11-21.
 * 예제용
 * changer에서 PreTest 객체 관련 Db접근
 */

public class PreTestController implements IProGuardianController {
    private ProGuardianDBHelper proGuardianDBHelper;

    public PreTestController () { }

    @Override
    public int insert(ContentValues contentValues) {
        int res = proGuardianDBHelper.insert(contentValues);
        return res;
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        return proGuardianDBHelper.search(contentValues);
    }

    @Override
    public int insertServer(ContentValues contentValues) {
        return 0;
    }

    @Override
    public int updateServer(ContentValues contentValues) {
        return 0;
    }

    @Override
    public int removeServer(ContentValues contentValues) {
        return 0;
    }

    @Override
    public List<ContentValues> searchServer(ContentValues contentValues) {
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

    @Override
    public ProGuardianDBHelper getDBHelper() {
        return proGuardianDBHelper;
    }
}
