package home.safe.com.trans;

import android.content.ContentValues;

import com.safe.home.pgchanger.ProGuardianVO;

import java.io.Serializable;

/**
 * Created by plupin724 on 2018-01-02.
 */

public class RegTransVO extends ProGuardianVO implements Serializable {

    private int rtseq;
    private int rlseq;
    private String rmemo;
    private String rday;
    private String rmid;

    public RegTransVO(int rtseq, int rlseq, String rmemo, String rday, String rmid) {
        this.rtseq = rtseq;
        this.rlseq = rlseq;
        this.rmemo = rmemo;
        this.rday = rday;
        this.rmid = rmid;
    }

    public int getRtseq() {
        return rtseq;
    }

    public void setRtseq(int rtseq) {
        this.rtseq = rtseq;
    }

    public int getRlseq() {
        return rlseq;
    }

    public void setRlseq(int rlseq) {
        this.rlseq = rlseq;
    }

    public String getRmemo() {
        return rmemo;
    }

    public void setRmemo(String rmemo) {
        this.rmemo = rmemo;
    }

    public String getRday() {
        return rday;
    }

    public void setRday(String rday) {
        this.rday = rday;
    }

    public String getRmid() {
        return rmid;
    }

    public void setRmid(String rmid) {
        this.rmid = rmid;
    }

    @Override
    public ContentValues convertDataToContentValues() {
        return null;
    }

    @Override
    public void convertContentValuesToData(ContentValues contentValues) {

    }

    @Override
    public String getDetails() {
        return null;
    }
}
