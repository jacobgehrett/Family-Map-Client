package com.familymap.family_map.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

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
    private Drawable maleIcon;
    private Drawable femaleIcon;
    private boolean firstTime = true;

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
        if (selectedEvent != null) {
            addLines(selectedEvent);
        }
    }

    private boolean haveEventIdArgument() {
        return (getArguments() != null && getArguments().containsKey(ARG_EVENT_ID));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (selectedEvent != null) {
            addLines(selectedEvent);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             final Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        //System.out.println("here");
        View v = inflater.inflate(R.layout.fragment_map, parent, false);
        if (selectedEvent != null) {
            addLines(selectedEvent);
        }
        genderImageView = (ImageView)v.findViewById(R.id.genderImageView);
        genderImageView.setOnClickListener(setEventInfoClickListener);
        setGenderIcon(null);

        personNameTextView = (TextView)v.findViewById(R.id.personNameTextView);
        personNameTextView.setOnClickListener(setEventInfoClickListener);

        eventDetailsTextView = (TextView)v.findViewById(R.id.eventDetailsTextView);
        eventDetailsTextView.setOnClickListener(setEventInfoClickListener);

        markersToEvents = new HashMap<>();
        selectedEvent = null;
        lines = new ArrayList<>();

        if (savedInstanceState == null) {
            if (haveEventIdArgument()) {
                String eventId = getArguments().getString(ARG_EVENT_ID);
                selectedEvent = DataCache.getEventById(eventId);
                if (selectedEvent != null) {
                    addLines(selectedEvent);
                }
                //addLines(selectedEvent);
            }
        }
        else {
            if (savedInstanceState.containsKey(ARG_EVENT_ID)) {
                String eventId = savedInstanceState.getString(ARG_EVENT_ID);
                selectedEvent = DataCache.getEventById(eventId);
                if (selectedEvent != null) {
                    addLines(selectedEvent);
                }
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

        Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).
                colorRes(R.color.teal_200).sizeDp(40);
        maleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                colorRes(R.color.blue).sizeDp(40);
        femaleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                colorRes(R.color.pink).sizeDp(40);
        genderImageView.setImageDrawable(genderIcon);
        personNameTextView.setText(R.string.default_string);
        return v;
    }

    private void populateMap(boolean createEvents) {
    }

    private void setGenderIcon(Object o) {
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
        @SuppressLint("SetTextI18n")
        @Override
        public boolean onMarkerClick(Marker marker) {
            selectedEvent = markersToEvents.get(marker);
            populateMap(false);
            Event e = (Event)marker.getTag();
            selectedEvent = e;
            if (selectedEvent != null) {
                addLines(selectedEvent);
            }
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(e.getLatitude(),
                    e.getLongitude())));
            if (e.getGender().equals("m")) {
                genderImageView.setImageDrawable(maleIcon);
            }
            else {
                genderImageView.setImageDrawable(femaleIcon);
            }

            personNameTextView.setText(e.getFirstName() + " " + e.getLastName());
            eventDetailsTextView.setText(selectedEvent.getType().toUpperCase() + ": " + selectedEvent.getCity() + ", " +
                    selectedEvent.getCountry() + " (" + selectedEvent.getDate() + ")");

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
    int drawWidth;
    private void drawFamilyLines(Person p, double latitude, double longitude) {
        drawWidth = drawWidth - 4;
        if ((p.getFather() != null) && (DataCache.getPersonEvents(p.getFather()).size() > 0)) {
            Polyline line = map.addPolyline(new PolylineOptions()
                    .add(new LatLng(latitude, longitude), new LatLng(DataCache.getPersonEvents(p.getFather()).get(0).getLatitude(),
                            DataCache.getPersonEvents(p.getFather()).get(0).getLongitude()))
                    .width(drawWidth)
                    .color(Color.CYAN));
            lines.add(line);
            drawFamilyLines(p.getFather(), DataCache.getPersonEvents(p.getFather()).get(0).getLatitude(), DataCache.getPersonEvents(p.getFather()).get(0).getLongitude());
        }
        if ((p.getMother() != null) && (DataCache.getPersonEvents(p.getMother()).size() > 0)) {
            Polyline line = map.addPolyline(new PolylineOptions()
                    .add(new LatLng(latitude, longitude), new LatLng(DataCache.getPersonEvents(p.getMother()).get(0).getLatitude(),
                            DataCache.getPersonEvents(p.getMother()).get(0).getLongitude()))
                    .width(drawWidth)
                    .color(Color.CYAN));
            lines.add(line);
            drawFamilyLines(p.getMother(), DataCache.getPersonEvents(p.getMother()).get(0).getLatitude(), DataCache.getPersonEvents(p.getMother()).get(0).getLongitude());
        }
        drawWidth = drawWidth + 4;
    }

    private void addLines(Event selectedEvent) {
        if (map != null) {
            for (Polyline line : lines) {
                line.remove();
            }
            lines.clear();
            if (selectedEvent != null) {
                Person eventPerson = selectedEvent.getPerson();
                double myLatitude = selectedEvent.getLatitude();
                double myLongitude = selectedEvent.getLongitude();
                if (DataCache.getSettings().isSpouse() && DataCache.getSettings().isMale() && DataCache.getSettings().isFemale()) {

                    Person spouse = eventPerson.getSpouse();
                    Event spouseBirth = DataCache.getPersonEvents(spouse).get(0);
                    double latitude = spouseBirth.getLatitude();
                    double longitude = spouseBirth.getLongitude();

                    Polyline line = map.addPolyline(new PolylineOptions()
                            .add(new LatLng(myLatitude, myLongitude), new LatLng(latitude, longitude))
                            .width(5)
                            .color(Color.RED));
                    lines.add(line);
                }
                if (DataCache.getSettings().isFamilyTree() && DataCache.getPersonEvents(eventPerson).contains(selectedEvent)) {
                    drawWidth = 15;
                    drawFamilyLines(eventPerson, myLatitude, myLongitude);
                }
                if (DataCache.getSettings().isLifeStory()) {
                    for (int i = 0; i < (DataCache.getPersonEvents(eventPerson).size() - 1); ++i) {
                        Polyline line = map.addPolyline(new PolylineOptions()
                                .add(new LatLng(DataCache.getPersonEvents(eventPerson).get(i).getLatitude(),
                                        DataCache.getPersonEvents(eventPerson).get(i).getLongitude()),
                                        new LatLng(DataCache.getPersonEvents(eventPerson).get(i + 1).getLatitude(),
                                        DataCache.getPersonEvents(eventPerson).get(i + 1).getLongitude()))
                                .width(5)
                                .color(Color.BLUE));
                        lines.add(line);
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (selectedEvent != null) {
            addLines(selectedEvent);
        }
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        for (Event e : DataCache.getAllFilteredEvents()) {

            Marker newEvent = map.addMarker(new MarkerOptions().position(new LatLng(e.getLatitude(),
                    e.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(DataCache.getEventTypeColors().get(e.getType().toLowerCase()).getColor())));
            newEvent.setTag(e);
        }
        map.setOnMarkerClickListener(markerClickListener);
        if (selectedEvent != null) {
            LatLng eventPosition = new LatLng(selectedEvent.getLatitude(),
                    selectedEvent.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLng(eventPosition));
            if (selectedEvent.getGender().equals("m")) {
                genderImageView.setImageDrawable(maleIcon);
            }
            else {
                genderImageView.setImageDrawable(femaleIcon);
            }

            personNameTextView.setText(selectedEvent.getFirstName() + " " + selectedEvent.getLastName());
            eventDetailsTextView.setText(selectedEvent.getType().toUpperCase() + ": " + selectedEvent.getCity() + ", " +
                    selectedEvent.getCountry() + " (" + selectedEvent.getDate() + ")");
        }
        //map.animateCamera(CameraUpdateFactory.newLatLng(sydney));
        firstTime = false;
        if (selectedEvent != null) {
            addLines(selectedEvent);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        if (!firstTime) {
            if (selectedEvent != null) {
                addLines(selectedEvent);
            }
            markersToEvents.clear();
            map.clear();
            map.setOnMapLoadedCallback(this);
            for (Event e : DataCache.getAllFilteredEvents()) {

                Marker newEvent = map.addMarker(new MarkerOptions().position(new LatLng(e.getLatitude(),
                        e.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(DataCache.getEventTypeColors().get(e.getType().toLowerCase()).getColor())));
                newEvent.setTag(e);
            }
            map.setOnMarkerClickListener(markerClickListener);
            if (selectedEvent != null) {
                LatLng eventPosition = new LatLng(selectedEvent.getLatitude(),
                        selectedEvent.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLng(eventPosition));
                if (selectedEvent.getGender().equals("m")) {
                    genderImageView.setImageDrawable(maleIcon);
                }
                else {
                    genderImageView.setImageDrawable(femaleIcon);
                }

                personNameTextView.setText(selectedEvent.getFirstName() + " " + selectedEvent.getLastName());
                eventDetailsTextView.setText(selectedEvent.getType().toUpperCase() + ": " + selectedEvent.getCity() + ", " +
                        selectedEvent.getCountry() + " (" + selectedEvent.getDate() + ")");
            }
            if (selectedEvent != null) {
                addLines(selectedEvent);
            }
        }
    }
}











































