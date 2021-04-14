package com.familymap.family_map.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.familymap.family_map.R;
import com.familymap.family_map.model.DataCache;
import com.familymap.family_map.model.Event;
import com.familymap.family_map.model.Person;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

public class EventActivity extends AppCompatActivity /*implements LoginFragment.Listener*/ {
    private static int REQ_CODE_SETTINGS = 0;
    public static final String EXTRA_EVENT_ID = "extraEventID";
    private String eventID;
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.eventFragmentContainer);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        eventID = getIntent().getStringExtra(EXTRA_EVENT_ID);
        Iconify.with(new FontAwesomeModule());
        fragment = MapFragment.newInstance(eventID);
        fm.beginTransaction()
                .add(R.id.eventFragmentContainer, fragment)
                .commit();
    }
}