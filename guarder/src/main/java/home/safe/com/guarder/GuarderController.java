package home.safe.com.guarder;

import android.content.ContentValues;
import android.util.Log;

import com.safe.home.pgchanger.IProGuardianController;
import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hotki on 2017-12-26.
 */

public class GuarderController implements IProGuardianController {

    private ProGuardianDBHelper proGuardianDBHelper;

    // 테스트용
    private ProGuardianDBHelper proGuardianServerHelper;
    // 테스트용

    @Override
    public int insert(ContentValues contentValues) {
        return proGuardianDBHelper.insert(contentValues);
    }

    @Override
    public int update(ContentValues contentValues) {
        return proGuardianDBHelper.update(contentValues);
    }

    @Override
    public int remove(ContentValues contentValues) {
        return proGuardianDBHelper.remove(contentValues);
    }

    @Override
    public List<ContentValues> search(ContentValues contentValues) {
        List<ContentValues> list = new ArrayList<>();

        list = proGuardianDBHelper.search(contentValues);
        return list;
    }

    @Override
    public int insertServer(ContentValues contentValues) {
        return proGuardianServerHelper.insert(contentValues);
    }

    @Override
    public int updateServer(ContentValues contentValues) {
        return proGuardianServerHelper.update(contentValues);
    }

    @Override
    public int removeServer(ContentValues contentValues) {
        return proGuardianServerHelper.remove(contentValues);
    }

    @Override
    public List<ContentValues> searchServer(ContentValues contentValues) {
        Log.v("서버","2");
        return proGuardianServerHelper.search(contentValues);
    }

    @Override
    public void setDBHelper(ProGuardianDBHelper proGuardianDBHelper) {
        this.proGuardianDBHelper = proGuardianDBHelper;

    }

    // 테스트
    public void setHelper(ProGuardianDBHelper proGuardianDBHelper, ProGuardianDBHelper proGuardianServerHelper) {
        this.proGuardianDBHelper = proGuardianDBHelper;

        this.proGuardianServerHelper = proGuardianServerHelper;
    }
    // 테스트

    @Override
    public ProGuardianDBHelper getDBHelper() {
        return this.proGuardianDBHelper;
    }
}
