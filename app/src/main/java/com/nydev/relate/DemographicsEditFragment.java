package com.nydev.relate;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by markneider on 8/6/16.
 * Fragment to input basic demographic info
 */
public class DemographicsEditFragment extends Fragment {

    private static final String NAME_KEY = "name"; // key in demographics args for name
    private static final String BIRTHDAY_KEY = "birthday"; // key in demographics args for birthday

    private DemographicsSaveListener mCallback; // calling context - needs to implement DemographicsSaveListener

    /**
     * Define communication interface between this fragment and the calling activity
     */
    public interface DemographicsSaveListener {
        /**
         * Save name inputted in this fragment back to the calling activity
         * @param name name from the name entry view
         */
        void saveName(Name name);
    }

    /**
     * Create a fragment to edit demographics info for a relationship
     * @param relationship relationship to edit
     * @return fragment to edit relationship demographics
     */
    public static DemographicsEditFragment newInstance(Relationship relationship) {
        DemographicsEditFragment demographicsEditFragment = new DemographicsEditFragment();

        // setup arguments
        Bundle demographicArgs = new Bundle();
        demographicArgs.putString(NAME_KEY, relationship.getName().toString());
        demographicArgs.putString(BIRTHDAY_KEY, relationship.getBirthdayString());

        demographicsEditFragment.setArguments(demographicArgs);

        return demographicsEditFragment;
    }

    /**
     * Initialize demographic values to views
     * @param relationship relationship being edited
     */
    @SuppressWarnings("unused") // to be used in the future
    public void setDemographics(Relationship relationship) {
        String nameEditText = relationship.getName().toString();
        String birthdaySelectionText = relationship.getBirthdayString();

        setDemographicsDisplay(getView(), nameEditText, birthdaySelectionText);
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
        final String NAME_DEFAULT = ""; // if name isn't set, set name text to blank so the hint will show
        final String BIRTHDAY_DEFAULT = ""; // if birthday isn't set, set birthday text to blank so the hint will show

        View demographicsEditView = layoutInflater.inflate(R.layout.demographics_edit_fragment,
                container, false);

        Bundle demographicArgs = getArguments(); // if we are editing an existing relationship, demographicsArgs will be set

        String nameEditText, birthdaySelectionText;
        if (demographicArgs == null) {
            nameEditText = NAME_DEFAULT;
            birthdaySelectionText = BIRTHDAY_DEFAULT;
        }  else { // demographics args exists
            nameEditText = demographicArgs.getString(NAME_KEY, NAME_DEFAULT);
            birthdaySelectionText = demographicArgs.getString(BIRTHDAY_KEY, BIRTHDAY_DEFAULT);
        }

        setDemographicsDisplay(demographicsEditView, nameEditText, birthdaySelectionText);

        return demographicsEditView;
    }

    /**
     * Find and initialize views with existing data
     * @param demographicsEditView root view of this fragment (needed because this may be called before root view is initialized to getView()
     * @param nameEditText text to set in name_entry_view
     * @param birthdaySelectionText text to set in birthday_selection_button
     */
    private void setDemographicsDisplay(
            View demographicsEditView, String nameEditText, String birthdaySelectionText) {

        TextView nameEditTextView =
                (TextView) demographicsEditView.findViewById(R.id.name_entry_view);
        nameEditTextView.setText(nameEditText);
        setNameEditTextWatcher(nameEditTextView); // TextWatcher will call saveName in calling activity afterTextChange

        Button birthdaySelectionButton =
                (Button) demographicsEditView.findViewById(R.id.birthday_selection_button);
        birthdaySelectionButton.setText(birthdaySelectionText);
    }

    /**
     * save a reference to the calling context for communication back
     * @param context calling context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mCallback = (DemographicsSaveListener) context; // calling activity must implement DemographicsSaveListener
    }

    /**
     * Set a TextWatcher on the name edit text view to save the name afterTextChanged
     * @param nameEditTextView view to set watcher on
     */
    private void setNameEditTextWatcher(TextView nameEditTextView) {
        nameEditTextView.addTextChangedListener(new TextWatcher() {
            /**
             * Not used
             * {@inheritDoc}
             * @param charSequence {@inheritDoc}
             * @param i {@inheritDoc}
             * @param i1 {@inheritDoc}
             * @param i2 {@inheritDoc}
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * Not used
             * {@inheritDoc}
             * @param charSequence {@inheritDoc}
             * @param i {@inheritDoc}
             * @param i1 {@inheritDoc}
             * @param i2 {@inheritDoc}
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * After text is changed in the name entry text field, save the new name to the relationship
             * @param nameEditable text from name edit text view
             */
            @Override
            public void afterTextChanged(Editable nameEditable) {
                String rawName = nameEditable.toString();
                Name relationshipName = new Name(rawName);
                mCallback.saveName(relationshipName);
            }
        });
    }
}
