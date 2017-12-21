package home.safe.com.trans;

import java.io.Serializable;

/**
 * Created by plupin724 on 2017-11-13.
 *
 * 데이터 흐름을 확인하기 위한 테스트 dto 클래스
 */

public class TestListViewDTO implements Serializable{
    String num;
    String tranName;
    String time;
    String author;
    String text;
    



    public TestListViewDTO(String num, String tranName, String time, String author, String text) {
        this.num = num;
        this.tranName = tranName;
        this.time = time;
        this.author = author;
        this.text = text;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTranName() {
        return tranName;
    }

    public void setTranName(String tranName) {
        this.tranName = tranName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TestListViewDTO{" +
                "num='" + num + '\'' +
                ", tranName='" + tranName + '\'' +
                ", time='" + time + '\'' +
                ", author='" + author + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
