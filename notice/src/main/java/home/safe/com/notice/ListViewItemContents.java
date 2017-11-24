package home.safe.com.notice;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by plupin724 on 2017-11-24.
 * Author 박준규
 * 리스트뷰 타이틀을 컨트롤 하기 위한 클래스 작성
 * elv_item_contents.xml과 연동
 */

public class ListViewItemContents extends LinearLayout{
    TextView tvContents;

    public ListViewItemContents(Context context) {
        super(context);
        init(context);
    }

    public ListViewItemContents(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.elv_item_contents, this, true);

        tvContents = (TextView)findViewById(R.id.tvContents);
    }

    public void setContents(String contents){
        tvContents.setText(contents);
    }

}
