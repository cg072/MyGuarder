package home.safe.com.trans;

import android.content.ContentValues;

import com.safe.home.pgchanger.ProGuardianVO;

import java.io.Serializable;

/**
 * Created by plupin724 on 2017-12-27.
 */

public class TransVO extends ProGuardianVO implements Serializable{

    private int tseq;
    private String ttype;
    private String tname;


    public TransVO(int tseq, String ttype, String tname) {
        this.tseq = tseq;
        this.ttype = ttype;
        this.tname = tname;
    }

    public int getTseq() {
        return tseq;
    }

    public void setTseq(int tseq) {
        this.tseq = tseq;
    }

    public String getTtype() {
        return ttype;
    }

    public void setTtype(String ttype) {
        this.ttype = ttype;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    //오버라이드 메소드////
    @Override
    public ContentValues convertDataToContentValues() {
        ContentValues contentValues = new ContentValues();

        return contentValues;
    }

    @Override
    public void convertContentValuesToData(ContentValues contentValues) {

    }

    @Override
    public String getDetails() {
        return null;
    }
}
