package home.safe.com.myguarder.changer;

import android.content.ContentValues;

/**
 * Created by 김진복 on 2017-11-21.
 * 예제용
 */

public class PreTestVO extends ProGuardianVO {
    String test1;
    String test2;
    String test3;

    @Override
    public ContentValues convertDataToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("test1",test1);
        contentValues.put("test2",test2);
        contentValues.put("test3",test3);
        return contentValues;
    }

    @Override
    public void convertContentValuesToData(ContentValues contentValues) {
        test1 = contentValues.getAsString("test1");
        test2 = contentValues.getAsString("test2");
        test3 = contentValues.getAsString("test3");
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


}
