package home.safe.com.trans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by plupin724 on 2017-12-01.
 * 이동수단내역보기 프래그먼트에 리스트 뷰를 전달할 어댑터 클래스 작성
 */

public class AdapterListTrans extends BaseExpandableListAdapter{
    //ArrayList<TestListViewDTO> parentItems = new ArrayList<TestListViewDTO>();
    //HashMap<TestListViewDTO, String> childItems = new HashMap<>();

    ArrayList<TransIntegratedVO> parentItems = new ArrayList<TransIntegratedVO>();
    HashMap<TransIntegratedVO, String> childItems = new HashMap<>();

    /*public void addItem(TestListViewDTO test){

        parentItems.add(test);
        childItems.put(test, test.getText());
    }*/

    public void addItem(TransIntegratedVO test){

        parentItems.add(test);
        childItems.put(test, Integer.toString(test.getTseq()));
    }

    public void clear() {
        parentItems.clear();
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return parentItems.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        //return parentItems.get(i).getText();
        return  parentItems.get(i).getTseq();
    }

    @Override
    public long getGroupId(int i) {
        return 0;
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
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Context parentContext = viewGroup.getContext();

        ListViewItemBasicTrans itemBasic = new ListViewItemBasicTrans(parentContext.getApplicationContext());

        //TestListViewDTO parDto = parentItems.get(i);
        TransIntegratedVO parDto = parentItems.get(i);

        //itemBasic.setText(parDto.getNum(), parDto.getTranName(), parDto.getTime(), parDto.getAuthor());
        itemBasic.setText(Integer.toString(parDto.getTseq()), parDto.getTtype());

        return itemBasic;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        Context childContext = viewGroup.getContext();

        ListViewItemTextTrans itemText = new ListViewItemTextTrans(childContext.getApplicationContext());

        //TestListViewDTO childDto = parentItems.get(i);
        TransIntegratedVO childDto = parentItems.get(i);

        //itemText.setText(childDto.getText());
        itemText.setText(childDto.getTmemo());

        return itemText;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }


}
