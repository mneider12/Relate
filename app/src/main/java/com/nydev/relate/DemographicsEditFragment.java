package com.nydev.relate;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.MonthDay;

/**
 * Created by markneider on 8/6/16.
 */
public class DemographicsEditFragment extends Fragment {

    private static final String NAME_KEY = "name";
    private static final String BIRTHDAY_KEY = "birthday";

    private DemographicsSaveListener mCallback;

    public interface DemographicsSaveListener {
        void saveName(Name name);
    }

    public static DemographicsEditFragment newInstance(Relationship relationship) {
        DemographicsEditFragment demographicsEditFragment = new DemographicsEditFragment();

        Bundle demographicArgs = new Bundle();
        demographicArgs.putString(NAME_KEY, relationship.getName().toString());
        demographicArgs.putString(BIRTHDAY_KEY, relationship.getBirthdayString());

        demographicsEditFragment.setArguments(demographicArgs);

        return demographicsEditFragment;
    }

    public void setDemographics(Relationship relationship) {
        View demographicsEditView = getView();
        String nameEditText = relationship.getName().toString();
        String birthdaySelectionText = relationship.getBirthdayString();

        setDemographicsDisplay(demographicsEditView, nameEditText, birthdaySelectionText);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstance) {
        final String NAME_DEFAULT = "Name";
        final String BIRTHDAY_DEFAULT = "Birthday";

        View demographicsEditView = layoutInflater.inflate(R.layout.demographics_edit_fragment,
                container, false);

        Bundle demographicArgs = getArguments();

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

    private void setDemographicsDisplay(
            View demographicsEditView, String nameEditText, String birthdaySelectionText) {

        TextView nameEditTextView =
                (TextView) demographicsEditView.findViewById(R.id.name_entry_view);
        nameEditTextView.setText(nameEditText);
        setNameEditTextWatcher(nameEditTextView);

        Button birthdaySelectionButton =
                (Button) demographicsEditView.findViewById(R.id.birthday_selection_button);
        birthdaySelectionButton.setText(birthdaySelectionText);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mCallback = (DemographicsSaveListener) context; // calling activity must implement DemographicsSaveListener
    }

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
