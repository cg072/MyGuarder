package home.safe.com.guarder;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.safe.home.pgchanger.PreTestVO;
import com.safe.home.pgchanger.ProGuardianVO;

import java.io.Serializable;

/**
 * Created by hotki on 2017-11-28.
 */

public class GuarderVO extends ProGuardianVO implements Serializable, Parcelable {

    final static private String SEQ = "gseq";
    final static private String NAME = "gmcname";
    final static private String PHONE = "gmcphone";
    final static private String USE = "gstate";         // 0일경우 비등록, 1일 경우 등록된
    final static private String MID = "gmid";           // 피지킴이 아이디
    final static private String MCID = "gmcid";         // 지킴이 아이디
    final static private String REGDAY = "gregday";

    private int gseq = -1;
    private int gstate = -1;
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
    public GuarderVO(String name, String phone,String gmcid, int gstate) {
        this.gmcname = name;
        this.gmcphone = phone;
        this.gmcid = gmcid;
        this.gstate = gstate;
    }

    public GuarderVO(int gseq, int gstate, String gmcname, String gmcphone, String gmid, String gmcid, String gregday) {
        this.gseq = gseq;
        this.gstate = gstate;
        this.gmcname = gmcname;
        this.gmcphone = gmcphone;
        this.gmid = gmid;
        this.gmcid = gmcid;
        this.gregday = gregday;
    }

    protected GuarderVO(Parcel in) {
        gseq = in.readInt();
        gstate = in.readInt();
        gmcname = in.readString();
        gmcphone = in.readString();
        gmid = in.readString();
        gmcid = in.readString();
        gregday = in.readString();
    }

    public static final Creator<GuarderVO> CREATOR = new Creator<GuarderVO>() {
        @Override
        public GuarderVO createFromParcel(Parcel in) {
            return new GuarderVO(in);
        }

        @Override
        public GuarderVO[] newArray(int size) {
            return new GuarderVO[size];
        }
    };

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

            contentValues.put(SEQ, this.gseq);

        if(gmcname != null){
            contentValues.put(NAME, this.gmcname);
        }

        if(gmcphone != null) {
            contentValues.put(PHONE, this.gmcphone);
        }
            contentValues.put(USE, this.gstate);

        if(gmid != null){
            contentValues.put(MID, this.gmid);
        }
        if(gmcid != null) {
            contentValues.put(MCID, this.gmcid);
        }
        if(gregday != null){
            contentValues.put(REGDAY, this.gregday);
        }

        return contentValues;
    }

    @Override
    public void convertContentValuesToData(ContentValues contentValues) {
        //this.gseq = contentValues.getAsInteger(SEQ);
        this.gmcname = contentValues.getAsString(NAME);
        this.gmcphone = contentValues.getAsString(PHONE);
        if(contentValues.getAsInteger(USE) != null) {
            this.gstate = contentValues.getAsInteger(USE);
        }
        this.gmid = contentValues.getAsString(MID);
        this.gmcid = contentValues.getAsString(MCID);
        this.gregday = contentValues.getAsString(REGDAY);
    }

    public ContentValues convertDataToContentValuesSendDB() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, gmcname);
        contentValues.put(PHONE, gmcphone);
        if(gstate != -1) {
            contentValues.put(USE, gstate);
        }

        return contentValues;
    }

    public void convertContentValuesToDataRecvDB(ContentValues contentValues) {
        setGmcname(contentValues.getAsString(NAME));
        setGmcphone(contentValues.getAsString(PHONE));
        setGstate(contentValues.getAsInteger(USE));
    }

    public ContentValues convertDataToContentValuesSendServer() {

        ContentValues contentValues = new ContentValues();
        if(gmid != null) {
            contentValues.put(MID, gmid);
        }
        if(gmcid != null) {
            contentValues.put(MCID, gmcid);
        }
        if(gstate != -1) {
            contentValues.put(USE, gstate);
        }
        if(gmcname != null) {
            contentValues.put(NAME, gmcname);
        }
        if(gmcphone != null) {
            contentValues.put(PHONE, gmcphone);
        }

        return contentValues;
    }

    public void convertContentValuesToDataRecvServer(ContentValues contentValues) {
        setGmcphone(contentValues.getAsString(PHONE));
        setGmcname(contentValues.getAsString(NAME));
        setGmid(contentValues.getAsString(MID));
        setGmcid(contentValues.getAsString(MCID));
        setGstate(contentValues.getAsInteger(USE));
    }


    @Override
    public String getDetails() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(gseq);
        parcel.writeInt(gstate);
        parcel.writeString(gmcname);
        parcel.writeString(gmcphone);
        parcel.writeString(gmid);
        parcel.writeString(gmcid);
        parcel.writeString(gregday);
    }


}
