package com.nydev.relate;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by markneider on 8/6/16.
 * Fragment to display demographic information
 */
public class DemographicsViewFragment extends Fragment {

    private static final String NAME_KEY = "name";
    private static final String BIRTHDAY_KEY = "birthday";

    public interface OnEditDemographicsButtonListener {
        void editDemographics(View editButton);
    }

    /**
     * Create a fragment to edit demographics info for a relationship
     * @param relationship relationship to edit
     * @return fragment to edit relationship demographics
     */
    public static DemographicsViewFragment newInstance(Relationship relationship) {
        DemographicsViewFragment demographicsViewFragment = new DemographicsViewFragment();

        // setup arguments
        Bundle demographicArgs = new Bundle();
        demographicArgs.putString(NAME_KEY, relationship.getName().toString());
        demographicArgs.putString(BIRTHDAY_KEY, relationship.getBirthdayString());

        demographicsViewFragment.setArguments(demographicArgs);

        return demographicsViewFragment;
    }

    /**
     * Create fragment view and initialize fields
     * @param layoutInflater {@inheritDoc}
     * @param container {@inheritDoc}
     * @param savedInstance {@inheritDoc}
     * @return initialized fragment view
     */
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstance) {

        View demographicsLayout =
                layoutInflater.inflate(R.layout.demographics_view_fragment, container, false);

        loadRelationship(demographicsLayout);

        return demographicsLayout;
    }

    private void loadRelationship(View demographicsLayout) {

        Bundle demographicsArgs = getArguments();

        TextView nameView = (TextView) demographicsLayout.findViewById(R.id.name_text_view);
        String nameString = demographicsArgs.getString(NAME_KEY, "");
        if (nameString.equals("")) {
            nameView.setVisibility(View.GONE);
        } else {
            nameView.setText(nameString);
        }

        TextView birthdayView = (TextView) demographicsLayout.findViewById(R.id.birthday_text_view);
        String birthdayString = demographicsArgs.getString(BIRTHDAY_KEY, "");
        if (birthdayString.equals("")) {
            birthdayView.setVisibility(View.GONE);
        } else {
            birthdayView.setText(birthdayString);
        }
    }
}
