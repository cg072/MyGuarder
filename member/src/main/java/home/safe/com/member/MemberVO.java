package home.safe.com.member;

import android.content.ContentValues;

import com.safe.home.pgchanger.ProGuardianVO;

import java.io.Serializable;

/**
 * Created by hotki on 2017-11-28.
 */

public class MemberVO extends ProGuardianVO implements Serializable {
    private int mseq;
    private String mname = "ff";
    private String mphone = "ff";
    private String mid = "ff";
    private String mpwd = "ff";
    private String mcertday = "ff";
    private String mbirth = "ff";
    private String memail = "ff";
    private String mgender = "ff";
    private String msns = "ff";
    private String msnsid = "ff";
    private String mregday = "ff";

    public MemberVO() {}

    public MemberVO(String id, String pwd){
        this.setMid(id);
        this.setMpwd(pwd);
    }

    public MemberVO(String gender, String name, String email, String birth, String sns) {
        this.setMid(email);
        this.setMgender(gender);
        this.setMname(name);
        this.setMemail(email);
        this.setMbirth(birth);
        this.setMsns(sns);
        this.setMsnsid(email);
    }

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

    public ContentValues convertDataToContentValuesSendDB() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("mseq", getMseq());
        contentValues.put("mname", getMname());
        contentValues.put("mphone", getMphone());

        return contentValues;
    }

    @Override
    public ContentValues convertDataToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("mseq", getMseq());
        contentValues.put("mname", getMname());
        contentValues.put("mphone", getMphone());
        contentValues.put("mid", getMid());
        contentValues.put("mpwd", getMpwd());
        contentValues.put("mcertday", getMcertday());
        contentValues.put("mbirth", getMbirth());
        contentValues.put("memail", getMemail());
        contentValues.put("mgender", getMgender());
        contentValues.put("msns", getMsns());
        contentValues.put("msnsid", getMsnsid());
        contentValues.put("mregday", getMregday());

        return contentValues;
    }

    @Override
    public void convertContentValuesToData(ContentValues contentValues) {
        this.setMseq(contentValues.getAsInteger("mseq"));
        this.setMname(contentValues.getAsString("mname"));
        this.setMphone(contentValues.getAsString("mphone"));
        this.setMid(contentValues.getAsString("mid"));
        this.setMpwd(contentValues.getAsString("mpwd"));
        this.setMcertday(contentValues.getAsString("mcertday"));
        this.setMbirth(contentValues.getAsString("mbirth"));
        this.setMemail(contentValues.getAsString("memail"));
        this.setMgender(contentValues.getAsString("mgender"));
        this.setMsns(contentValues.getAsString("msns"));
        this.setMsnsid(contentValues.getAsString("msnsid"));
        this.setMregday(contentValues.getAsString("mregday"));
    }

    @Override
    public String getDetails() {
        return null;
    }
}
