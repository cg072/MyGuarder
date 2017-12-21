package home.safe.com.trans;

import java.util.ArrayList;

/**
 * Created by plupin724 on 2017-12-18.
 */

public class TestTransFakeDB {
    String num;
    String type;
    String name;

    public ArrayList<String> insertDB(String num, String type, String name){
        this.num = num;
        this.type = type;
        this.name = name;
        ArrayList<String> retreg = new ArrayList<>();
        retreg.add(num);
        retreg.add(type);
        retreg.add(name);


        return retreg;
    }

}
