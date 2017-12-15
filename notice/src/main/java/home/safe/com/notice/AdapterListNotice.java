package home.safe.com.notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by plupin724 on 2017-12-15.
 */

public class AdapterListNotice extends BaseExpandableListAdapter {

    //Context context;

    ArrayList<TestDtoNotice> parentArrItems = new ArrayList<TestDtoNotice>();
    HashMap<TestDtoNotice, String> childHashItems = new HashMap<>();

    public void addItem(TestDtoNotice test){

        parentArrItems.add(test);
        childHashItems.put(test, test.getTestcontents());


    }

    @Override
    public int getGroupCount() {
        return parentArrItems.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return parentArrItems.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return parentArrItems.get(i).getTestcontents();
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        /*Context parentContext = viewGroup.getContext();
        if(view == null){
            LayoutInflater parentInfalter = (LayoutInflater) parentContext.getSystemService(parentContext.LAYOUT_INFLATER_SERVICE);
            view = parentInfalter.inflate(R.layout.elv_item_title, viewGroup, false);
        }
        return view;*/

        Context parentContext = viewGroup.getContext();

        ListViewItemTitle itemTitle = new ListViewItemTitle(parentContext.getApplicationContext());

        TestDtoNotice parentDto = parentArrItems.get(i);

        itemTitle.setTitle(parentDto.getTesttitle(), parentDto.getTestauthor());

        return itemTitle;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        /*Context childContext = viewGroup.getContext();
        if(view == null){
            LayoutInflater childInflater = (LayoutInflater) childContext.getSystemService(childContext.LAYOUT_INFLATER_SERVICE);
            view = childInflater.inflate(R.layout.elv_item_contents, viewGroup, false);
        }
        return view;*/

        Context childContext = viewGroup.getContext();

        ListViewItemContents itemContents = new ListViewItemContents(childContext.getApplicationContext());

        TestDtoNotice childDto = parentArrItems.get(i);
        itemContents.setContents(childDto.getTestcontents());

        return itemContents;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
