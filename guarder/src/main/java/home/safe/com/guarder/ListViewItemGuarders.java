package home.safe.com.guarder;

import android.widget.Button;

/**
 * Created by hotki on 2017-11-14.
 */

public class ListViewItemGuarders {

    private String name;
    private String phone;
    private int sort;
    private boolean use = false;

    public void setTvName(String name) {
        this.name = name;
    }

    public void setTvPhone(String phone) {
        this.phone = phone;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTvName(){
        return this.name;
    }

    public String getTvPhone(){
        return this.phone;
    }

    public boolean getUse() {
        return this.use;
    }

    public int getSort() {
        return this.sort;
    }
}
