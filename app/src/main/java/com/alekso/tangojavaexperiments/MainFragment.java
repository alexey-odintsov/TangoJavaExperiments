package com.alekso.tangojavaexperiments;

import com.jme3.app.AndroidHarnessFragment;

/**
 * Created by alekso on 25/03/2017.
 */

public class MainFragment extends AndroidHarnessFragment {
    public MainFragment() {
        // Set the application class to run
        appClass = App.class.getCanonicalName();

        // Exit Dialog title & message
        exitDialogTitle = "Exit?";
        exitDialogMessage = "Are you sure you want to quit?";

        // Enable MouseEvents being generated from TouchEvents (default = true)
        mouseEventsEnabled = true;


    }
}
