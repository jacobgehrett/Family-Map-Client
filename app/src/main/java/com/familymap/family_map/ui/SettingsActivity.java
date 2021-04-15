package com.familymap.family_map.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.familymap.family_map.R;
import com.familymap.family_map.model.DataCache;

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

        lifeStorySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> DataCache.getSettings().setLifeStory(isChecked));

        familyTreeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> DataCache.getSettings().setFamilyTree(isChecked));

        spouseSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> DataCache.getSettings().setSpouse(isChecked));

        fatherSideSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> DataCache.getSettings().setFather(isChecked));

        motherSideSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> DataCache.getSettings().setMother(isChecked));

        maleEventsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> DataCache.getSettings().setMale(isChecked));

        femaleEventsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> DataCache.getSettings().setFemale(isChecked));

        logoutView.setOnClickListener(v -> {
            DataCache.setUser(null);
            DataCache.clear();
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        });
    }
}