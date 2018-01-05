package home.safe.com.guarder;

import android.content.ContentValues;

import com.safe.home.pgchanger.PreTestVO;
import com.safe.home.pgchanger.ProGuardianVO;

import java.io.Serializable;

/**
 * Created by hotki on 2017-11-28.
 */

public class GuarderVO extends ProGuardianVO implements Serializable {

    private int gseq;
    private String gmid;
    private String gmcid;
    private int gstate;
    private long gregday;

    private String gmcname;
    private String gmcphone;

    public GuarderVO() {
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

    public long getGregday() {
        return gregday;
    }

    public void setGregday(long gregday) {
        this.gregday = gregday;
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
