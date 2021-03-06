package home.safe.com.member;

import android.content.ContentValues;
import android.util.Log;

import com.safe.home.pgchanger.IProGuardianController;
import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.List;
import java.util.Random;

/**
 * Created by hotki on 2017-12-26.
 */

public class MemberController implements IProGuardianController {

    // 테스트용
    ProGuardianDBHelper proGuardianServerHelper;
    // 테스트용

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
        return proGuardianServerHelper.search(contentValues);
    }



    // 비번 생성
    public ContentValues requestPWD() {
        String randomStr = new String();
        Random rnd = new Random();
        for (int i = 0; i < 20; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    randomStr += String.valueOf((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    randomStr += String.valueOf((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    randomStr += String.valueOf((rnd.nextInt(10)));
                    break;
            }
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("pwd", randomStr);

        return contentValues;
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

    @Override
    public ProGuardianDBHelper getDBHelper() {
        return this.proGuardianDBHelper;
    }

}
