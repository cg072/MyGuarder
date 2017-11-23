package home.safe.com.myguarder.changer;

import android.content.ContentValues;

/**
 * Created by 김진복 on 2017-11-21.
 * 예제용
 * ProGuardianVO를 상속한 PreTestVO 객체
 * 실제 보여주기 위한 데이터가 있는 객체
 */

public class PreTestVO extends ProGuardianVO {
    String test1 = "";
    String test2 = "";
    String test3 = "";

    public PreTestVO(){
    }

    public PreTestVO(String test1, String test2, String test3) {
        this.test1 = test1;
        this.test2 = test2;
        this.test3 = test3;
    }

    @Override
    public ContentValues convertDataToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PreTestDBHelper.COL1,test1);
        contentValues.put(PreTestDBHelper.COL2,test2);
        contentValues.put(PreTestDBHelper.COL3,test3);
        return contentValues;
    }

    @Override
    public void convertContentValuesToData(ContentValues contentValues) {
        test1 = contentValues.getAsString(PreTestDBHelper.COL1);
        test2 = contentValues.getAsString(PreTestDBHelper.COL2);
        test3 = contentValues.getAsString(PreTestDBHelper.COL3);
    }

    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }

    public String getTest2() {
        return test2;
    }

    public void setTest2(String test2) {
        this.test2 = test2;
    }

    public String getTest3() {
        return test3;
    }

    public void setTest3(String test3) {
        this.test3 = test3;
    }

    public String getDetail() {
        String detail = test1 + "\n" + String.valueOf(test2) + "\n" + test3;

        return detail;
    }

    public String getDetails() {
        String detail = test1 + " / " + String.valueOf(test2) + " / " + test3 + "\n";

        return detail;
    }

}
