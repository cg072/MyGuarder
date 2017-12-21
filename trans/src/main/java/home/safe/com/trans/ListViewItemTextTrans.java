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

public class ListViewItemTextTrans extends LinearLayout {
    TextView transitemtext;

    public ListViewItemTextTrans(Context context) {
        super(context);
        init(context);
    }

    public ListViewItemTextTrans(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.elv_item_textinfo_trans, this, true);

        transitemtext = (TextView)findViewById(R.id.transitemtext);

    }

    public void setText(String text){
        transitemtext.setText(text);
    }
}
