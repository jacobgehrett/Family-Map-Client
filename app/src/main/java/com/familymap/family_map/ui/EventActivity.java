package com.familymap.family_map.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.familymap.family_map.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class EventActivity extends AppCompatActivity /*implements LoginFragment.Listener*/ {
    private static final int REQ_CODE_SETTINGS = 0;
    public static final String EXTRA_EVENT_ID = "extraEventID";
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.eventFragmentContainer);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        String eventID = getIntent().getStringExtra(EXTRA_EVENT_ID);
        Iconify.with(new FontAwesomeModule());
        fragment = MapFragment.newInstance(eventID);
        fm.beginTransaction()
                .add(R.id.eventFragmentContainer, fragment)
                .commit();
    }
}