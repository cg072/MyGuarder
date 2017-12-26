package home.safe.com.member;

import android.content.ContentValues;

import com.safe.home.pgchanger.ProGuardianVO;

import java.io.Serializable;

/**
 * Created by hotki on 2017-11-28.
 */

public class MemberVO extends ProGuardianVO implements Serializable {
    private int mseq;
    private String mname;
    private String mphone;
    private String mid;
    private String mpwd;
    private String mcertday;
    private String mbirth;
    private String memail;
    private String mgender;
    private String msns;
    private String msnsid;
    private String mregday;
    private String mnickname;

    public int getMseq() {
        return mseq;
    }

    public void setMseq(int mseq) {
        this.mseq = mseq;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMphone() {
        return mphone;
    }

    public void setMphone(String mphone) {
        this.mphone = mphone;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMpwd() {
        return mpwd;
    }

    public void setMpwd(String mpwd) {
        this.mpwd = mpwd;
    }

    public String getMcertday() {
        return mcertday;
    }

    public void setMcertday(String mcertday) {
        this.mcertday = mcertday;
    }

    public String getMbirth() {
        return mbirth;
    }

    public void setMbirth(String mbirth) {
        this.mbirth = mbirth;
    }

    public String getMemail() {
        return memail;
    }

    public void setMemail(String memail) {
        this.memail = memail;
    }

    public String getMgender() {
        return mgender;
    }

    public void setMgender(String mgender) {
        this.mgender = mgender;
    }

    public String getMsns() {
        return msns;
    }

    public void setMsns(String msns) {
        this.msns = msns;
    }

    public String getMsnsid() {
        return msnsid;
    }

    public void setMsnsid(String msnsid) {
        this.msnsid = msnsid;
    }

    public String getMregday() {
        return mregday;
    }

    public void setMregday(String mregday) {
        this.mregday = mregday;
    }

    public String getMnickname() {
        return mnickname;
    }

    public void setMnickname(String mnickname) {
        this.mnickname = mnickname;
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
