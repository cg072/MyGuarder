package home.safe.com.notice;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;

/**
 * Created by plupin724 on 2017-11-16.
 * 공지사항 리스트뷰의 어댑터 객체 생성
 */

public class AdapterNotice extends BaseExpandableListAdapter {

    ArrayList<TestDtoNotice> items;

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return items.size();
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }
}
