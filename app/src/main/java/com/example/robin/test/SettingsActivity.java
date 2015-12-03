package com.example.robin.test;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;


/**
 * Created by robin on 02/12/15.
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();

    }


    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_general);

            Preference restart = findPreference("restart");
            restart.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent data = new Intent();
                    data.putExtra("isRestart", true);
                    getActivity().setResult(RESULT_OK, data);
                    getActivity().finish();
                    return false;
                }
            });
        }
    }
}
