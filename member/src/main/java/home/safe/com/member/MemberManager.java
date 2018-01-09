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

    final static String SELECT_TYPE = "type";

    public MemberManager(Context context) {
        this.context = context;

        createDB();
        createController();
    }

    public void createController()
    {
        controller = new MemberController();
        controller.setDBHelper(dbHelper);
    }

    public void createDB()
    {
        dbHelper =
                new MemberDBHelper(context, ProGuardianDBHelper.DB_NAME,null,ProGuardianDBHelper.DB_VERSION,MemberDBHelper.PG_GUARDER);
        db = dbHelper.getWritableDatabase();

    }

    public void closeDB()
    {
        dbHelper.close();
    }

    public int insert(MemberVO memberVO) {
        int check = 0;
        ContentValues sendCV = memberVO.convertDataToContentValues();
        check = controller.insert(sendCV);

        return check;
    }

    public int delete(MemberVO memberVO) {
        int check = 0;
        ContentValues sendCV = memberVO.convertDataToContentValues();
        check = controller.remove(sendCV);

        return check;
    }

    public int update(MemberVO memberVO) {
        int check = 0;
        ContentValues sendCV = memberVO.convertDataToContentValues();
        check = controller.update(sendCV);

        return check;
    }

    public ArrayList<MemberVO> select(String type, MemberVO data) {
        Log.v("가더","디비 보내기 진입");
        List<ContentValues> resultList;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SELECT_TYPE, type);
        resultList = controller.search(contentValues);

        MemberVO memberVO;
        ArrayList<MemberVO> memberList = new ArrayList<MemberVO>();

        for (ContentValues cv : resultList) {
            memberVO = new MemberVO();
            memberVO.convertContentValuesToData(cv);
            memberList.add(memberVO);
        }

        return memberList;
    }
}
