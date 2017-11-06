package home.safe.com.myguarder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapView;

/**
 * Created by kch on 2017-11-06.
 */

/*
 * @author 경창현
 * @version 1.0.0
 * @text NaverMap Fragment
 * @since 2017-11-06 오후 11:44
 */

public class FragmentMap extends Fragment{

    private NMapContext mMapContext;
    private static final String CLIENT_ID = "k9RQzVyNn9UIoZ3OiV6Y";// 애플리케이션 클라이언트 아이디 값

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_map, container, false);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapContext = new NMapContext(super.getActivity());
        mMapContext.onCreate();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NMapView mapView = (NMapView)getView().findViewById(R.id.mapView);  //getView() = getContext() 치환가능
        mapView.setClientId(CLIENT_ID);
        mMapContext.setupMapView(mapView);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapContext.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }

    @Override
    public void onStop() {
        mMapContext.onStop();

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mMapContext.onDestroy();

        super.onDestroy();
    }
}
