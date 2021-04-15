package com.familymap.family_map.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.familymap.family_map.R;
import com.familymap.family_map.model.DataCache;
import com.familymap.family_map.model.Person;
import com.jakewharton.processphoenix.ProcessPhoenix;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class SettingsActivity extends AppCompatActivity {
    Switch lifeStorySwitch;
    Switch familyTreeSwitch;
    Switch spouseSwitch;
    Switch fatherSideSwitch;
    Switch motherSideSwitch;
    Switch maleEventsSwitch;
    Switch femaleEventsSwitch;
    View logoutView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        lifeStorySwitch = findViewById(R.id.life_story);
        familyTreeSwitch = findViewById(R.id.family_tree);
        spouseSwitch = findViewById(R.id.spouse);
        fatherSideSwitch = findViewById(R.id.father_side);
        motherSideSwitch = findViewById(R.id.mother_side);
        maleEventsSwitch = findViewById(R.id.male_events);
        femaleEventsSwitch = findViewById(R.id.female_events);
        logoutView = findViewById(R.id.logout);

        lifeStorySwitch.setChecked(DataCache.getSettings().isLifeStory());
        familyTreeSwitch.setChecked(DataCache.getSettings().isFamilyTree());
        spouseSwitch.setChecked(DataCache.getSettings().isSpouse());
        fatherSideSwitch.setChecked(DataCache.getSettings().isFather());
        motherSideSwitch.setChecked(DataCache.getSettings().isMother());
        maleEventsSwitch.setChecked(DataCache.getSettings().isMale());
        femaleEventsSwitch.setChecked(DataCache.getSettings().isFemale());

        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getSettings().setLifeStory(isChecked);
            }
        });

        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getSettings().setFamilyTree(isChecked);
            }
        });

        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getSettings().setSpouse(isChecked);
            }
        });

        fatherSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getSettings().setFather(isChecked);
            }
        });

        motherSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getSettings().setMother(isChecked);
            }
        });

        maleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getSettings().setMale(isChecked);
            }
        });

        femaleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getSettings().setFemale(isChecked);
            }
        });

        logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.setUser(null);
                DataCache.clear();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
}