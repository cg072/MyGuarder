package com.safe.home.pgchanger;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김진복 on 2017-11-21.
 * 데이터 관련 접근 관리용 클래스
 * activity에서 생성되는 기능관리 class.
 * changer 사용 예제
 */

public class PreTest {
    Context context;
    ProGuardianChanger proGuardianChanger;

    public PreTest(Context pcontext) {
        this.context = pcontext;
        connectChanger();
    }

    private void connectChanger() {
        //proGuardianChanger = new ProGuardianChanger(context, ProGuardianDBHelper.PG_TEST);
        proGuardianChanger = new ProGuardianChanger(context);
        proGuardianChanger.connectDB(ProGuardianDBHelper.PG_TEST);
    }

    public int testInsert() {

        int result = 0;
        for(int a=1; a <= 25; a++ ) {
            //입력 데이터
            PreTestVO preTestVO = new PreTestVO();
            preTestVO.setTest1("test1" + String.valueOf(a));
            preTestVO.setTest2(String.valueOf(a));
            preTestVO.setTest3("test3" + String.valueOf(a));

            //변환
            ContentValues contentValues = preTestVO.convertDataToContentValues();

            //db접근
            result += proGuardianChanger.insertData(contentValues);
        }

        return result;
    }

    public ProGuardianVO testSearch() {

        //조건
        PreTestVO conditionVO = new PreTestVO();
        //conditionVO.setTest1("test11");

        //검색
        ContentValues conditionValues = conditionVO.convertDataToContentValues();
        List<ProGuardianVO> list = new ArrayList<ProGuardianVO>();
        list = proGuardianChanger.searchData(conditionValues);

        //결과
        ProGuardianVO returnValue = new PreTestVO();
        if(list.size() > 0) {
            returnValue = list.get(0);
        }

        return returnValue;
    }

    public List<PreTestVO> testListSearch() {

        //조건
        PreTestVO conditionVO = new PreTestVO();
        //conditionVO.setTest1("test11");

        ContentValues conditionValues = conditionVO.convertDataToContentValues();

        //검색
        List<ProGuardianVO> list = new ArrayList<ProGuardianVO>();
        list = proGuardianChanger.searchData(conditionValues);

        //결과
        List<PreTestVO> returnList = new ArrayList<PreTestVO>();
        PreTestVO vo = new PreTestVO();
        for(int a=0; a < list.size(); a++)
        {
            vo = (PreTestVO)list.get(a);
            returnList.add(vo);
        }

        return returnList;
    }

    //테스트 테이블 삭제용 메서드
    public void testRemoveTable() {
        proGuardianChanger.removeTable();
    }

}
