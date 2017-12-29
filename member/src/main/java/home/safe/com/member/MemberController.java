package home.safe.com.member;

import android.content.ContentValues;

import com.safe.home.pgchanger.IProGuardianController;
import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.List;

/**
 * Created by hotki on 2017-12-26.
 */

public class MemberController implements IProGuardianController {

    ProGuardianDBHelper proGuardianDBHelper;

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
    public void setDBHelper(ProGuardianDBHelper proGuardianDBHelper) {
        this.proGuardianDBHelper = proGuardianDBHelper;
    }

    @Override
    public ProGuardianDBHelper getDBHelper() {
        return this.proGuardianDBHelper;
    }
}
