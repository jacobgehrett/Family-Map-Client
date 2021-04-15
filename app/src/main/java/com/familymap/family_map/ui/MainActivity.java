package com.familymap.family_map.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity /*implements LoginFragment.Listener*/ {

    private MenuItem searchMenuItem;
    private MenuItem settingsMenuItem;

    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

    public MainActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invalidateOptionsMenu();
        setContentView(R.layout.activity_main);
        Iconify.with(new FontAwesomeModule());
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
        if (DataCache.hasUser()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.fragment_map_menu, menu);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.search:
                Intent j = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(j);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}