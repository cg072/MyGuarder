package home.safe.com.guarder;

import android.content.ContentValues;

import com.safe.home.pgchanger.PreTestVO;
import com.safe.home.pgchanger.ProGuardianVO;

import java.io.Serializable;

/**
 * Created by hotki on 2017-11-28.
 */

public class GuarderVO extends ProGuardianVO implements Serializable {

    final static private String SEQ = "gseq";
    final static private String NAME = "gmcname";
    final static private String PHONE = "gmcphone";
    final static private String USE = "gstate";
    final static private String MID = "gmid";
    final static private String MCID = "gmcid";
    final static private String REGDAY = "gregday";

    private int gseq;
    private int gstate;
    private String gmcname;
    private String gmcphone;
    private String gmid;
    private String gmcid;
    private String gregday;

    public GuarderVO() {}
    public GuarderVO(String name, String phone, int use) {
        this.gmcname = name;
        this.gmcphone = phone;
        this.gstate = use;
    }
    public GuarderVO(String name, String phone) {
        this.gmcname = name;
        this.gmcphone = phone;
    }
    public GuarderVO(int use) {
        this.gstate = use;
    }
    public GuarderVO(String gmcid, int gstate) {
        this.gmcid = gmcid;
        this.gstate = gstate;
    }

    public String getGmcname() {
        return gmcname;
    }

    public void setGmcname(String gmcname) {
        this.gmcname = gmcname;
    }

    public String getGmcphone() {
        return gmcphone;
    }

    public void setGmcphone(String gmcphone) {
        this.gmcphone = gmcphone;
    }

    public int getGseq() {
        return gseq;
    }

    public void setGseq(int gseq) {
        this.gseq = gseq;
    }

    public String getGmid() {
        return gmid;
    }

    public void setGmid(String gmid) {
        this.gmid = gmid;
    }

    public String getGmcid() {
        return gmcid;
    }

    public void setGmcid(String gmcid) {
        this.gmcid = gmcid;
    }

    public int getGstate() {
        return gstate;
    }

    public void setGstate(int gstate) {
        this.gstate = gstate;
    }

    public String getGregday() {
        return gregday;
    }

    public void setGregday(String gregday) {
        this.gregday = gregday;
    }

    //
    @Override
    public ContentValues convertDataToContentValues() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, getGmcname());
        contentValues.put(PHONE, getGmcphone());
        contentValues.put(USE, getGstate());
        contentValues.put(SEQ, getGseq());
        contentValues.put(MID, getGmid());
        contentValues.put(MCID, getGmcid());
        contentValues.put(REGDAY, getGregday());

        return contentValues;
    }

    @Override
    public void convertContentValuesToData(ContentValues contentValues) {
        setGseq(contentValues.getAsInteger(SEQ));
        setGmcname(contentValues.getAsString(NAME));
        setGmcphone(contentValues.getAsString(PHONE));
        setGstate(contentValues.getAsInteger(USE));
        setGmid(contentValues.getAsString(MID));
        setGmcid(contentValues.getAsString(MCID));
        setGregday(contentValues.getAsString(REGDAY));
    }

    public ContentValues convertDataToContentValuesSendDB() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, getGmcname());
        contentValues.put(PHONE, getGmcphone());
        contentValues.put(USE, getGstate());

        return contentValues;
    }

    public void convertContentValuesToDataRecvDB(ContentValues contentValues) {
        setGmcname(contentValues.getAsString(NAME));
        setGmcphone(contentValues.getAsString(PHONE));
        setGstate(contentValues.getAsInteger(USE));
    }

    @Override
    public String getDetails() {
        return null;
    }
}
