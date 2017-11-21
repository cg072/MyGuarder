package home.safe.com.myguarder.changer;

import android.content.ContentValues;

/**
 * Created by 김진복 on 2017-11-21.
 * 모든 VO 부모객체
 * 상속 받아서 만들면 됨
 */

abstract public class ProGuardianVO {
    abstract public ContentValues convertDataToContentValues();
    abstract public void convertContentValuesToData(ContentValues contentValues);
    public String getString() { return  null;}


}
