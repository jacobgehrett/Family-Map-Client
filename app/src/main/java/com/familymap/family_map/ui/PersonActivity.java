package com.familymap.family_map.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.familymap.family_map.R;
import com.familymap.family_map.model.DataCache;
import com.familymap.family_map.model.Event;
import com.familymap.family_map.model.Person;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.List;

public class PersonActivity extends AppCompatActivity {

    public static final String EXTRA_PERSON_ID = "extraPersonID";

    Drawable genderIcon;
    Drawable femaleIcon;
    Drawable maleIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_person);
        genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).
                colorRes(R.color.teal_200).sizeDp(40);
        maleIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).
                colorRes(R.color.blue).sizeDp(40);
        femaleIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female).
                colorRes(R.color.pink).sizeDp(40);
        String personID = getIntent().getStringExtra(EXTRA_PERSON_ID);

        Person p = DataCache.getPersonById(personID);

        TextView first_name_ = findViewById(R.id.setFirstName);
        TextView last_name_ = findViewById(R.id.setLastName);
        TextView gender_ = findViewById(R.id.setGender);

        first_name_.setText(p.getFirstName());
        last_name_.setText(p.getLastName());
        gender_.setText(p.getGender());

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);

        List<Event> events = DataCache.getPersonEvents(p);
        List<Person> people = new ArrayList<>();
        if (p.getFather() != null) {
            p.getFather().setRelation("Father");
            people.add(p.getFather());
        }
        if (p.getMother() != null) {
            p.getMother().setRelation("Mother");
            people.add(p.getMother());
        }
        if (p.getSpouse() != null) {
            p.getSpouse().setRelation("Spouse");
            people.add(p.getSpouse());
        }
        people.addAll(DataCache.getPersonChildren(p));

        expandableListView.setAdapter(new ExpandableListAdapter(events, people));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private static final int EVENT_GROUP_POSITION = 0;
        private static final int PERSON_GROUP_POSITION = 1;

        private final List<Event> events;
        private final List<Person> people;

        ExpandableListAdapter(List<Event> events, List<Person> people) {
            this.events = events;
            this.people = people;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return events.size();
                case PERSON_GROUP_POSITION:
                    return people.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return getString(R.string.eventsTitle);
                case PERSON_GROUP_POSITION:
                    return getString(R.string.peopleTitle);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return events.get(childPosition);
                case PERSON_GROUP_POSITION:
                    return people.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_person, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    titleView.setText(R.string.eventsTitle);
                    break;
                case PERSON_GROUP_POSITION:
                    titleView.setText(R.string.peopleTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case PERSON_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_item, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        @SuppressLint("SetTextI18n")
        private void initializeEventView(View eventItemView, final int childPosition) {
            ImageView eventImageView = eventItemView.findViewById(R.id.eventImageView);
            eventImageView.setImageDrawable(genderIcon);

            TextView eventType = eventItemView.findViewById(R.id.type);
            eventType.setText(events.get(childPosition).getType().toUpperCase() + ": " +
                    events.get(childPosition).getCity() + ", " +
                    events.get(childPosition).getCountry() + " (" +
                    events.get(childPosition).getDate() + ")");

            TextView person = eventItemView.findViewById(R.id.person);
            person.setText(events.get(childPosition).getFirstName() + " " +
                    events.get(childPosition).getLastName());

            eventItemView.setOnClickListener(v -> {
                Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                intent.putExtra(EventActivity.EXTRA_EVENT_ID, events.get(childPosition).getEventID());

                startActivity(intent);
            });
        }

        @SuppressLint("SetTextI18n")
        private void initializePersonView(View personItemView, final int childPosition) {
            ImageView personGenderImageView = personItemView.findViewById(R.id.personGenderImageView);
            if (people.get(childPosition).getGender().equals("m")) {
                personGenderImageView.setImageDrawable(maleIcon);
            }
            else {
                personGenderImageView.setImageDrawable(femaleIcon);
            }

            TextView name = personItemView.findViewById(R.id.name);
            name.setText(people.get(childPosition).getFirstName() + " " +
                    people.get(childPosition).getLastName());

            TextView relationship = personItemView.findViewById(R.id.relationship);
            relationship.setText(people.get(childPosition).getRelation());

            personItemView.setOnClickListener(v -> {
                Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                intent.putExtra(PersonActivity.EXTRA_PERSON_ID, people.get(childPosition).getPersonId());

                startActivity(intent);
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}