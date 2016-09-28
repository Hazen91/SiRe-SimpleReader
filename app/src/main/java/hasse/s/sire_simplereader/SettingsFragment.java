package hasse.s.sire_simplereader;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference deleteLib = findPreference("delete_lib");
        Preference licenseInfo = findPreference("license_info");

        deleteLib.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Are you sure?");
                alertDialog.setMessage("Do you really want to delete all entries in your library?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                SQLiteHelper sqLiteHelper = new SQLiteHelper(getActivity());
                                sqLiteHelper.deleteAllEntries();
                                Toast.makeText(getActivity(),"All Entries deleted!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                return true;
            }
        });

        licenseInfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Open Source License Info");
                alertDialog.setMessage(getString(R.string.license_info));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                return true;
            }
        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        if(view != null)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                view.setBackgroundColor(getResources().getColor(R.color.colorMyWhite, null));
            } else
            {
                //noinspection deprecation
                view.setBackgroundColor(getResources().getColor(R.color.colorMyWhite));
            }
        }
        super.onViewCreated(view, savedInstanceState);
    }
}