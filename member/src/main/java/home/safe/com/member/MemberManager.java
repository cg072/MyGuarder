package home.safe.com.member;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safe.home.pgchanger.ProGuardianDBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hotki on 2018-01-09.
 */

public class MemberManager {
    Context context;
    MemberDBHelper dbHelper;
    SQLiteDatabase db;
    MemberController controller;

    // 테스트
    MemberServerHelper serverHelper;
    SQLiteDatabase server;


    public MemberManager(Context context) {
        this.context = context;

        createDB();
        createController();

    }

    public void createController()
    {
        controller = new MemberController();

        //controller.setDBHelper(dbHelper);

        // 테스트
        controller.setHelper(dbHelper, serverHelper);
    }

    public void createDB()
    {
        dbHelper =
                new MemberDBHelper(context, ProGuardianDBHelper.DB_NAME,null,ProGuardianDBHelper.DB_VERSION,MemberDBHelper.PG_MEMBER);
        db = dbHelper.getWritableDatabase();

        //테스트
        serverHelper = new MemberServerHelper(context, ProGuardianDBHelper.DB_NAME, null, ProGuardianDBHelper.DB_VERSION,
                MemberServerHelper.PG_MEMBER);
        server = serverHelper.getWritableDatabase();
        //테스트

    }

    public void closeDB()
    {
        dbHelper.close();
    }

    public int insert(String target, MemberVO memberVO) {
        int check = 0;
        ContentValues sendCV = memberVO.convertDataToContentValues();
        switch (target) {
            case MemberShareWord.TARGET_DB :
                return check = controller.insert(sendCV);
            case MemberShareWord.TARGET_SERVER :
                return check = controller.insertServer(sendCV);
        }
        return check;
    }

    public int delete(String target, MemberVO memberVO) {
        int check = 0;
        ContentValues sendCV = memberVO.convertDataToContentValues();
        switch (target) {
            case MemberShareWord.TARGET_DB :
                return check = controller.remove(sendCV);
            case MemberShareWord.TARGET_SERVER :
                return check = controller.removeServer(sendCV);
        }
        return check;
    }

    public int update(String target, MemberVO memberVO) {
        int check = 0;
        ContentValues sendCV = memberVO.convertDataToContentValues();
        switch (target) {
            case MemberShareWord.TARGET_DB :
                return check = controller.update(sendCV);
            case MemberShareWord.TARGET_SERVER :
                return check = controller.updateServer(sendCV);
        }

        return check;
    }

    public ArrayList<MemberVO> select(String target, String type, MemberVO data) {
        List<ContentValues> resultList = null;
        ContentValues contentValues = data.convertDataToContentValues();
        contentValues.put(MemberShareWord.SELECT_TYPE, type);

        switch (target) {
            case MemberShareWord.TARGET_DB :
                resultList = controller.search(contentValues);
            case MemberShareWord.TARGET_SERVER :
                resultList = controller.searchServer(contentValues);
        }

        MemberVO memberVO;
        ArrayList<MemberVO> memberList = new ArrayList<MemberVO>();

        for (ContentValues cv : resultList) {
            memberVO = new MemberVO();
            memberVO.convertContentValuesToData(cv);
            memberList.add(memberVO);
        }

        return memberList;
    }

    public String requestCode() {
        String requestCode = "";

        ContentValues contentValues = new ContentValues();
        contentValues.put("code","request");

        //requestCode = controller.

        return requestCode;
    }
}
