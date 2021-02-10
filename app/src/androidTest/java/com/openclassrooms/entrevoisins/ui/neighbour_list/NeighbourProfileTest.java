package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.DummyNeighbourGenerator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class NeighbourProfileTest {

    List<Neighbour> mListNeighbours = DummyNeighbourGenerator.generateNeighbours();
    Neighbour neighbour = mListNeighbours.get(0);

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void initProfileActivity(){
        onView(withId(R.id.list_neighbours)).check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void name_wrote_withSuccess() {
        String nameExpected = neighbour.getName();
        onView(allOf(withId(R.id.contact_name_profile_textView), withText(nameExpected)));
    }

    @Test
    public void profileActivityStartedWithSuccess(){
        onView(withId(R.id.activity_profile)).check(matches(isDisplayed()));
    }

}