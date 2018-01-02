package home.safe.com.trans;

import android.content.ContentValues;

import com.safe.home.pgchanger.ProGuardianVO;

import java.io.Serializable;

/**
 * Created by plupin724 on 2017-12-27.
 */

public class TransVO extends ProGuardianVO implements Serializable{

    //이동수단에 대한 변수
    public int teseq; //순번
    public String ttype; //종류
    public String tname; //명칭(메모)
    //등록한 메모에 대한 변수(새로 추가함)
    public String ttime;

    //이동수단 등록에 대한 변수
    public int rtseq;
    public int rlseq;
    //새로추가한 등록 종류
    public String rtype;
    public String rmemo;
    public String rday;
    public String rmid;

    //생성자
    public TransVO(int teseq, String ttype, String tname, String ttime, int rtseq, int rlseq, String rtype, String rmemo, String rday, String rmid) {
        this.teseq = teseq;
        this.ttype = ttype;
        this.tname = tname;
        this.ttime = ttime;
        this.rtseq = rtseq;
        this.rlseq = rlseq;
        this.rtype = rtype;
        this.rmemo = rmemo;
        this.rday = rday;
        this.rmid = rmid;
    }

    //getter and setter


    public int getTeseq() {
        return teseq;
    }

    public void setTeseq(int teseq) {
        this.teseq = teseq;
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

    public String getTtime() {
        return ttime;
    }

    public void setTtime(String ttime) {
        this.ttime = ttime;
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

    public String getRtype() {
        return rtype;
    }

    public void setRtype(String rtype) {
        this.rtype = rtype;
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

    //오버라이드 메소드////
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
