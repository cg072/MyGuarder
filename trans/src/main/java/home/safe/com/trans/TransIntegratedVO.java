package home.safe.com.trans;

import android.content.ContentValues;

import com.safe.home.pgchanger.ProGuardianVO;

/**
 * Created by plupin724 on 2018-01-07.
 */

public class TransIntegratedVO extends ProGuardianVO{

    private int tseq;
    private int tlseq;
    private String tid;
    private String ttype;
    private String tmemo;
    private String tday;

    public TransIntegratedVO(){

    }

    public TransIntegratedVO(int tseq, int tlseq, String tid, String ttype, String tmemo, String tday) {
        this.tseq = tseq;
        this.tlseq = tlseq;
        this.tid = tid;
        this.ttype = ttype;
        this.tmemo = tmemo;
        this.tday = tday;
    }

    public int getTseq() {
        return tseq;
    }

    public void setTseq(int tseq) {
        this.tseq = tseq;
    }

    public int getTlseq() {
        return tlseq;
    }

    public void setTlseq(int tlseq) {
        this.tlseq = tlseq;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTtype() {
        return ttype;
    }

    public void setTtype(String ttype) {
        this.ttype = ttype;
    }

    public String getTmemo() {
        return tmemo;
    }

    public void setTmemo(String tmemo) {
        this.tmemo = tmemo;
    }

    public String getTday() {
        return tday;
    }

    public void setTday(String tday) {
        this.tday = tday;
    }

    //오버라이드 메소드
    @Override
    public ContentValues convertDataToContentValues() {

        ContentValues values = new ContentValues();
        values.put("tseq", getTseq());
        values.put("tlseq", getTlseq());
        values.put("tid", getTid());
        values.put("ttype", getTtype());
        values.put("tmemo", getTmemo());
        values.put("tday", getTday());

        return values;
    }

    @Override
    public void convertContentValuesToData(ContentValues contentValues) {

        setTseq((int)contentValues.get("tseq"));
        setTlseq((int)contentValues.get("tlseq"));
        setTid(contentValues.getAsString("tid"));
        setTtype(contentValues.getAsString("ttype"));
        setTmemo(contentValues.getAsString("tmemo"));
        setTday(contentValues.getAsString("tday"));

    }

    @Override
    public String getDetails() {
        return null;
    }

    @Override
    public String getString() {
        return super.getString();
    }
}
