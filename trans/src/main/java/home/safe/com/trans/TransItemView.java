package home.safe.com.trans;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by plupin724 on 2017-11-13.
 * 아이템 리스트 뷰를 컨트롤 하는 자바 클래스 생성
 * 리스트뷰에 보여질 아이템을 생성함
 * items_trans_list.xml을 불러옴
 */


/*리니어 레이아웃을 상속 받았기 때문에 뷰에 바로 인플레이트 해 줄 수 있음*/
public class TransItemView extends LinearLayout {
    TextView listseq;
    TextView listtype;
    TextView listname;

    public TransItemView(Context context) {
        super(context);
        init(context);
    }

    public TransItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context){
        //레이아웃을 인플레이션 해줌
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        //items_trans_list.xml 파일을 지금의 뷰 클래스에 인플레이트 시켜줌
        inflater.inflate(R.layout.items_trans_list, this, true);

        listseq =  findViewById(R.id.listseq);
        listtype = findViewById(R.id.listtype);
        listname = findViewById(R.id.listname);

    }

    //텍스트 필드에 값을 세팅하는 메소드

    //이동수단 리스트의 순번을 세팅하기
    public void setSeq(String num){
        listseq.setText(num);
    }

    //이동수단 리스트의 종류를 세팅하기
    public void setType(String kind){
        listtype.setText(kind);
    }

    //이동수단 리스트의 이름을 세팅하기
    public void setName(String name){
        listname.setText(name);
    }
}
