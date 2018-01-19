package home.safe.com.trans;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by plupin724 on 2018-01-19.
 */

public class DialogTransModify extends Dialog implements View.OnClickListener{

    public DialogTransModify(@NonNull Context context) {
        super(context);
    }

    public DialogTransModify(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DialogTransModify(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_trans_modify);

    }

    @Override
    public void onClick(View view) {

    }


}
