package home.safe.com.member;

import android.content.ContentValues;
import android.util.Log;

import com.safe.home.pgchanger.ProGuardianVO;

import java.io.Serializable;

/**
 * Created by hotki on 2017-11-28.
 */

public class MemberVO extends ProGuardianVO implements Serializable {
    private int mseq = 0;
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
    private String mregday;  // 12개의 변수

    public MemberVO() {}

    public MemberVO(String id, String pwd){
        this.setMid(id);
        this.setMpwd(pwd);
    }

    public MemberVO(String id, String pwd, String name, String phone, String birth, String email, String gender) {
        this.setMid(id);
        this.setMpwd(pwd);
        this.setMphone(phone);
        this.setMname(name);
        this.setMbirth(birth);
        this.setMemail(email);
        this.setMgender(gender);
    }

    public MemberVO(String id, String pwd, String name, String phone, String birth, String email, String gender, String certday) {
        this.setMid(id);
        this.setMpwd(pwd);
        this.setMphone(phone);
        this.setMname(name);
        this.setMbirth(birth);
        this.setMemail(email);
        this.setMgender(gender);
        this.setMcertday(certday);
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
        contentValues.put("mname", mname);
        contentValues.put("mphone", mphone);

        return contentValues;
    }

    @Override
    public ContentValues convertDataToContentValues() {
        ContentValues contentValues = new ContentValues();

        // 체크 메소드 http://mainichibenkyo.tistory.com/339

        /*if(mseq != 0) {
            contentValues.put("mseq", mseq);
        }*/
        if(mname != null) {
            contentValues.put("mname", mname);
        }
        if(mphone != null) {
            contentValues.put("mphone", mphone);
        }
        if(mid != null) {
            contentValues.put("mid", mid);
        }
        if(mpwd != null) {
            contentValues.put("mpwd", mpwd);
        }
        if(mcertday != null) {
            contentValues.put("mcertday", mcertday);
        }
        if(mbirth != null) {
            contentValues.put("mbirth", mbirth);
        }
        if(memail != null) {
            contentValues.put("memail", memail);
        }
        if( mgender != null) {
            contentValues.put("mgender", mgender);
        }
        if(msns != null) {
            contentValues.put("msns", msns);
        }
        if(msnsid != null) {
            contentValues.put("msnsid", msnsid);
        }
        if(mregday != null) {
            contentValues.put("mregday", mregday);
        }

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
