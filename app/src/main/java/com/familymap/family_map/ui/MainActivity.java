package com.familymap.family_map.ui;

import android.annotation.SuppressLint;
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
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

public class MainActivity extends AppCompatActivity /*implements LoginFragment.Listener*/ {
    private static int REQ_CODE_SETTINGS = 0;

    private MenuItem searchMenuItem;
    private MenuItem settingsMenuItem;

    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        invalidateOptionsMenu();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (fragment == null) {
            if (DataCache.hasUser()) {
                fragment = new MapFragment();
            }
            else {
                fragment = new LoginFragment();
            }
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    public void loggedIn() {
        invalidateOptionsMenu();
        fm.beginTransaction().remove(fragment).commit();
        fragment = new MapFragment();
        fm.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (DataCache.hasUser()) {
            new MenuInflater(this).inflate(R.menu.fragment_map_menu, menu);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}