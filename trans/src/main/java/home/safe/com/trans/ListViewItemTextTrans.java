package home.safe.com.trans;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by plupin724 on 2017-12-21.
 */

public class ListViewItemTextTrans extends LinearLayout implements View.OnClickListener{
    TextView transitemtext;
    Button transBtnMotify;

    String recvTseq;
    String recvTtype;

    Context context;

    public ListViewItemTextTrans(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public ListViewItemTextTrans(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.elv_item_textinfo_trans, this, true);

        transitemtext = (TextView)findViewById(R.id.transitemtext);

        transBtnMotify = (Button)findViewById(R.id.transBtnModify);
        transBtnMotify.setOnClickListener(this);


    }

    public void setText(String text){
        if(text.equals("")){
            transitemtext.setText("입력된 내용이 없습니다");
        }else{
            transitemtext.setText(text);
        }

    }

    public void getText(String tteq, String ttype){
        this.recvTseq = tteq;
        this.recvTtype = ttype;
    }

    /*해야할일
    * 1. 클릭을 했을 때, 어댑터에서 정보를 받아서 다이얼로그 혹은 새로운 레이아웃을 띄운다
    * 2. 수정할 수 있는 레이아웃을 만들고, 수정 여부를 묻는다
    * 3. 다이얼로그 혹은 레이아웃의 확인 버튼을 누르면
    *   -> 매니저를 통해 수정을 한다
    *   -> 수정이 될 때, 화면에 다시 그려준다*/


    /*
    * 수정일때, 입력일 때, 기능을 다르게 하고,
    * 길게 눌렀을 때는 컨텍스트 메뉴 혹은,  누른 정보를 가지고 입력창으로 넘어가게 하자
    *
    * 알아볼것!!!
    * 자동슬라이딩 하는 방법을 알아볼것
    * 길게 누른 정보를 가지고 입력창에 세팅을 해주기
    * 그때, 입력창을 수정과/ 입력으로 구분 해 주어야 함
     *
     *
     * 혹은,
     * 길게 누르면 컨텍스트 메뉴를 띄우기
    *
    * */

    @Override
    public void onClick(View view) {
        if(view == transBtnMotify){

            /*DialogTransModify dialog = new DialogTransModify(getContext().getApplicationContext());


            dialog.show();*/


            String b = transitemtext.getText().toString();

            Toast.makeText(getContext().getApplicationContext(), recvTseq+recvTtype+b, Toast.LENGTH_LONG).show();
        }

    }
}
