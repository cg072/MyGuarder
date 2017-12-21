package home.safe.com.trans;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by plupin724 on 2017-12-21.
 */

public class ListViewItemBasicTrans extends LinearLayout {

    TextView transitemseq;
    TextView transitemtype;
    TextView transitemtime;
    TextView transitemuser;

    public ListViewItemBasicTrans(Context context) {
        super(context);
        init(context);
    }

    public ListViewItemBasicTrans(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.elv_item_basicinfo_trans, this, true);

        transitemseq = (TextView)findViewById(R.id.transitemseq);
        transitemtype = (TextView)findViewById(R.id.transitemtype);
        transitemtime = (TextView)findViewById(R.id.transitemtime);
        transitemuser = (TextView)findViewById(R.id.transitemuser);

    }

    public void setText(String seq, String type, String time, String user){
        transitemseq.setText(seq);
        transitemtype.setText(type);
        transitemtime.setText(time);
        transitemuser.setText(user);

    }


}
