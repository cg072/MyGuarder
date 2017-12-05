package home.safe.com.myguarder;

/**
 * Created by JINNY_ on 2017-12-04.
 */

/**
*
* @author 경창현
* @version 1.0.0
* @text LocationVo
* @since 2017-12-04 오후 5:03
**/
public class LocationVO {

    private int lseq;
    private String llat;
    private String llong;
    private String lday;
    private String ltime;
    private String lid;

    public LocationVO(int lseq, String lday) {
        this.lseq = lseq;
        this.lday = lday;
    }

    public LocationVO(int lseq, String llat, String llong, String lday, String ltime, String lid) {
        this.lseq = lseq;
        this.llat = llat;
        this.llong = llong;
        this.lday = lday;
        this.ltime = ltime;
        this.lid = lid;
    }


    public int getLseq() {
        return lseq;
    }

    public void setLseq(int lseq) {
        this.lseq = lseq;
    }

    public String getLlat() {
        return llat;
    }

    public void setLlat(String llat) {
        this.llat = llat;
    }

    public String getLlong() {
        return llong;
    }

    public void setLlong(String llong) {
        this.llong = llong;
    }

    public String getLday() {
        return lday;
    }

    public void setLday(String lday) {
        this.lday = lday;
    }

    public String getLtime() {
        return ltime;
    }

    public void setLtime(String ltime) {
        this.ltime = ltime;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }
}
