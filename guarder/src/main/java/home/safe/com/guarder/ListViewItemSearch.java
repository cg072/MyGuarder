package home.safe.com.guarder;

import android.widget.Button;

/**
 * Created by hotki on 2017-11-14.
 */

public class ListViewItemSearch {

    private String name;
    private String phone;

    public void setTvName(String name) {
        this.name = name;
    }

    public void setTvPhone(String phone) {
        this.phone = phone;
    }

    public String getTvName(){
        return this.name;
    }

    public String getTvPhone(){
        return this.phone;
    }
}
