package home.safe.com.trans;

import java.io.Serializable;

/**
 * Created by plupin724 on 2017-11-13.
 *
 * 데이터 흐름을 확인하기 위한 테스트 dto 클래스
 */

public class TestListViewDTO implements Serializable{
    String num;
    String type;
    String name;


    public TestListViewDTO(String num, String type, String name) {
        this.num = num;
        this.type = type;
        this.name = name;
    }


    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestListViewDTO{" +
                "num='" + num + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
