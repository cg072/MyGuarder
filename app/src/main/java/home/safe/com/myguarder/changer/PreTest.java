package home.safe.com.myguarder.changer;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김진복 on 2017-11-21.
 * activity 기능관리 class.
 * changer 사용 예제
 * 데이터 관련 접근 및 이동을 관리하기 위한 클래스
 */

public class PreTest {
    Context context;
    ProGuardianChanger proGuardianChanger;

    public PreTest(Context context1) {
        this.context = context1;
    }


    public int testInsert() {
        //changer 사용시 어떤 데이터
        proGuardianChanger = new ProGuardianChanger(context, ProGuardianDBHelper.TABLE_TEST);
        int result = 0;
        for(int a=1; a <= 25; a++ ) {
            //입력 데이터
            PreTestVO preTestVO = new PreTestVO();
            preTestVO.setTest1("test1" + String.valueOf(a));
            preTestVO.setTest2(String.valueOf(a));
            preTestVO.setTest3("test3" + String.valueOf(a));

            //입력
            ContentValues contentValues = preTestVO.convertDataToContentValues();
            result += proGuardianChanger.insertData(contentValues);
        }

        return result;
    }

    public ProGuardianVO testSearch() {
        proGuardianChanger = new ProGuardianChanger(context, ProGuardianDBHelper.TABLE_TEST);

        //조건
        PreTestVO conditionVO = new PreTestVO();
        //conditionVO.setTest1("test11");

        //검색
        ContentValues conditionValues = conditionVO.convertDataToContentValues();
        List<ProGuardianVO> list = new ArrayList<ProGuardianVO>();
        list = proGuardianChanger.searchData(conditionValues);

        //결과 응용
        ProGuardianVO returnValue = new PreTestVO();
        if(list.size() > 0) {
            returnValue = list.get(0);
        }

        return returnValue;
    }

    public List<PreTestVO> testListSearch() {
        proGuardianChanger = new ProGuardianChanger(context);
        proGuardianChanger.connectDB(ProGuardianDBHelper.TABLE_TEST);

        //조건
        PreTestVO conditionVO = new PreTestVO();
        //conditionVO.setTest1("test11");

        ContentValues conditionValues = conditionVO.convertDataToContentValues();

        //검색
        List<ProGuardianVO> list = new ArrayList<ProGuardianVO>();
        list = proGuardianChanger.searchData(conditionValues);

        //결과 응용
        List<PreTestVO> returnList = new ArrayList<PreTestVO>();
        PreTestVO vo = new PreTestVO();
        for(int a=0; a < list.size(); a++)
        {
            vo = (PreTestVO)list.get(a);
            returnList.add(vo);
        }

        return returnList;
    }

    public void testRemoveTable() {
        proGuardianChanger = new ProGuardianChanger(context, ProGuardianDBHelper.TABLE_TEST);
        proGuardianChanger.removeTable();
    }

}
