package com.familymap.family_map.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SearchActivity extends AppCompatActivity {

    private static final int PERSON_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_ITEM_VIEW_TYPE = 1;

    Drawable genderIcon;
    Drawable femaleIcon;
    Drawable maleIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_search);
        genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).
                colorRes(R.color.teal_200).sizeDp(40);
        maleIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).
                colorRes(R.color.blue).sizeDp(40);
        femaleIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).
                colorRes(R.color.pink).sizeDp(40);
        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        List<Person> people = (List<Person>) DataCache.getAllPeople();
        List<Event> events = (List<Event>) DataCache.getAllFilteredEvents();

        SearchAdapter adapter = new SearchAdapter(people, events);

        SearchView searchView = findViewById(R.id.SearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerView.setAdapter(adapter);
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> implements Filterable {
        private final List<Person> people;
        private final List<Person> peopleFull;
        private final List<Event> events;
        private final List<Event> eventsFull;

        SearchAdapter(List<Person> people, List<Event> events) {
            this.people = people;
            peopleFull = new ArrayList<>(people);
            this.events = events;
            eventsFull = new ArrayList<>(events);
        }

        @Override
        public int getItemViewType(int position) {
            return position < people.size() ? PERSON_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.s_person_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.s_event_item, parent, false);
            }

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if(position < people.size()) {
                holder.bind(people.get(position));
            } else {
                holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }

        @Override
        public Filter getFilter() {
            return filter;
        }

        private final Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Object> peopleFilteredList = new ArrayList<>();
                List<Object> eventsFilteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {

                }
                else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Person itemP : peopleFull) {
                        if (itemP.getFirstName().toLowerCase().contains(filterPattern) ||
                                itemP.getLastName().toLowerCase().contains(filterPattern)) {
                            peopleFilteredList.add(itemP);
                        }
                    }
                    for (Event itemE : eventsFull) {
                        if (itemE.getCountry().toLowerCase().contains(filterPattern) ||
                                itemE.getCity().toLowerCase().contains(filterPattern) ||
                                itemE.getType().toLowerCase().contains(filterPattern) ||
                                (String.valueOf(itemE.getDate())).contains(filterPattern)) {
                            eventsFilteredList.add(itemE);
                        }
                    }
                }
                peopleFilteredList.addAll(eventsFilteredList);
                FilterResults results = new FilterResults();
                results.values = peopleFilteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                people.clear();
                events.clear();
                List PE = (List)results.values;
                for (Object item : PE) {
                    if (item instanceof Person) {
                        people.add((Person)item);
                    }
                    else {
                        events.add((Event)item);
                    }
                }
                notifyDataSetChanged();
            }
        };
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView icon;
        private final TextView eventInfo;
        private final TextView name;

        private final int viewType;
        private Person person;
        private Event event;

        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                icon = itemView.findViewById(R.id.personGenderImageView_s);
                eventInfo = null;
                name = itemView.findViewById(R.id.name_s);
            } else {
                icon = itemView.findViewById(R.id.eventImageView_s);
                eventInfo = itemView.findViewById(R.id.type_s);
                name = itemView.findViewById(R.id.person_s);
            }
        }

        @SuppressLint("SetTextI18n")
        private void bind(Person person) {
            this.person = person;
            if (person.getGender().equals("m")) {
                icon.setImageDrawable(maleIcon);
            }
            else {
                icon.setImageDrawable(femaleIcon);
            }
            name.setText(person.getFirstName() + " " + person.getLastName());
        }

        @SuppressLint("SetTextI18n")
        private void bind(Event event) {
            this.event = event;
            icon.setImageDrawable(genderIcon);
            eventInfo.setText(event.getType().toUpperCase() + ": " + event.getCity() + ", " +
                    event.getCountry() + " (" + event.getDate() + ")");
            name.setText(event.getFirstName() + " " + event.getLastName());
        }

        @Override
        public void onClick(View view) {
            Intent intent;
            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra(PersonActivity.EXTRA_PERSON_ID, person.getPersonId());
            }
            else {
                intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra(EventActivity.EXTRA_EVENT_ID, event.getEventID());
            }
            startActivity(intent);
        }
    }
}
