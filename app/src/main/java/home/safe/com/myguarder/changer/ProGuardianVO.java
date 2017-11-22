package home.safe.com.myguarder.changer;

import android.content.ContentValues;

import java.io.Serializable;


/**
 * Created by 김진복 on 2017-11-21.
 * VO 객체 부모
 * VO 객체 만들때 상속
 */

abstract public class ProGuardianVO implements Serializable {
    abstract public ContentValues convertDataToContentValues();
    abstract public void convertContentValuesToData(ContentValues contentValues);
    public String getString() { return  null;}

}
