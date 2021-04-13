package com.familymap.family_map.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.familymap.family_map.R;
import com.familymap.family_map.model.DataCache;
import com.familymap.family_map.model.Event;
import com.familymap.family_map.model.Person;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    private static String ARG_EVENT_ID = "event-id";
    private static String ARG_CAMERA_POS = "camera-pos";
    private ImageView genderImageView;
    private TextView personNameTextView;
    private TextView eventDetailsTextView;
    private GoogleMap map;
    private Event selectedEvent;
    private Map<Marker, Event> markersToEvents;
    private List<Polyline> lines;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    public static MapFragment newInstance(String eventID) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventID);
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {

    }

    private boolean haveEventIdArgument() {
        return (getArguments() != null && getArguments().containsKey(ARG_EVENT_ID));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             final Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        //System.out.println("here");
        View v = inflater.inflate(R.layout.fragment_map, parent, false);

        genderImageView = (ImageView)v.findViewById(R.id.genderImageView);
        setEventInfoClickListener(genderImageView);
        setGenderIcon(null);

        personNameTextView = (TextView)v.findViewById(R.id.personNameTextView);
        setEventInfoClickListener(personNameTextView);

        eventDetailsTextView = (TextView)v.findViewById(R.id.eventDetailsTextView);
        setEventInfoClickListener(eventDetailsTextView);

        markersToEvents = new HashMap<>();
        selectedEvent = null;
        lines = new ArrayList<>();

        if (savedInstanceState == null) {
            if (haveEventIdArgument()) {
                String eventId = getArguments().getString(ARG_EVENT_ID);
                selectedEvent = DataCache.getEventById(eventId);
            }
        }
        else {
            if (savedInstanceState.containsKey(ARG_EVENT_ID)) {
                String eventId = savedInstanceState.getString(ARG_EVENT_ID);
                selectedEvent = DataCache.getEventById(eventId);
            }
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(
                /*(googleMap) -> {
                    map = googleMap;
                    //map.setMapType(Utils.toGoogleMapType(DataCache.getSettings().getMapType()));
                    map.setOnMarkerClickListener(markerClickListener);

                    populateMap(true); // boolean createEvents

                    if (savedInstanceState == null) {
                        if (selectedEvent != null) {
                            LatLng eventPosition = new LatLng(selectedEvent.getLatitude(),
                                    selectedEvent.getLongitude());
                            map.moveCamera(CameraUpdateFactory.newLatLng(eventPosition));
                        }
                    } else {
                        if (savedInstanceState.containsKey(ARG_CAMERA_POS)) {
                            CameraPosition cameraPosition =
                                    savedInstanceState.getParcelable(ARG_CAMERA_POS);
                            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        }
                    }

                }*/this);

        return v;
    }

    private void populateMap(boolean createEvents) {
    }

    private void setGenderIcon(Object o) {
    }

    private void setEventInfoClickListener(View genderImageView) {
    }

    /*public void onSettingsChanges(settingsResult result) {
        if (result.isEventChanges()) {
            selectedEvent = null;
            populateMap(true);
        }
        else if (result.isLineChanges()) {
            populateMap(false);
        }
    }*/

    private GoogleMap.OnMarkerClickListener markerClickListener =
            new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            selectedEvent = markersToEvents.get(marker);
            populateMap(false);
            Event e = (Event)marker.getTag();
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(e.getLatitude(),
                    e.getLongitude())));
            System.out.println(e.getType());
            return true;
        }
    };

    private View.OnClickListener setEventInfoClickListener = (v) -> {
        if (selectedEvent != null) {
            Person person = selectedEvent.getPerson();

            Intent intent = new Intent(getActivity(), PersonActivity.class);
            intent.putExtra(PersonActivity.EXTRA_PERSON_ID, person.getPersonId());

            startActivity(intent);
        }
    };

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        for (Event e : DataCache.getAllEvents()) {

            Marker newEvent = map.addMarker(new MarkerOptions().position(new LatLng(e.getLatitude(),
                    e.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(DataCache.getEventTypeColors().get(e.getType()).getColor())));
            newEvent.setTag(e);
        }
        map.setOnMarkerClickListener(markerClickListener);
        //map.animateCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}











































