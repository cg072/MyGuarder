package home.safe.com.myguarder;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kch on 2018-04-09.
 */

public class ActivityLocal  extends Application{

    List<Polyline> polylinesLocation = new ArrayList<>();
    List<Marker> markersLocation = new ArrayList<>();


    @Override
    public void onCreate() {
        //초기화
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void setEnd()
    {
        for(Polyline line : polylinesLocation)
        {
            line.remove();
        }
        polylinesLocation.clear();

        for(Marker marker : markersLocation)
        {
            marker.remove();
        }
        markersLocation.clear();
    }

    public List<Polyline> getPolylinesLocation() {
        return polylinesLocation;
    }

    public void setPolylinesLocation(List<Polyline> polylinesLocation) {
        this.polylinesLocation = polylinesLocation;
    }


    public List<Marker> getMarkersLocation() {
        return markersLocation;
    }

    public void setMarkersLocation(List<Marker> markersLocation) {
        this.markersLocation = markersLocation;
    }

    public int getPolylinesLocationSize() {
        Log.d("ActivityLocal","PolylinesLocationSize -"+markersLocation.size());
        return polylinesLocation.size();
    }

    public int getMarkersLocationSize() {
        Log.d("ActivityLocal","MarkersLocationSize -"+polylinesLocation.size());
        return markersLocation.size();
    }
}
