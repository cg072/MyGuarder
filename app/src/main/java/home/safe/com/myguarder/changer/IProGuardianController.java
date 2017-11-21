package home.safe.com.myguarder.changer;

import android.content.ContentValues;

import java.util.List;

/**
 * Created by 김진복 on 2017-11-20.
 * 컨트롤러용 인터페이스
 * ProGuardianChanger 사용하려면 구현필요.
 */

public interface IProGuardianController {
    int insert(ContentValues contentValues);
    int update(ContentValues contentValues);
    int remove(ContentValues contentValues);
    List<ContentValues> search(ContentValues contentValues);
    void setDBHelper(ProGuardianDBHelper proGuardianDBHelper);

}
