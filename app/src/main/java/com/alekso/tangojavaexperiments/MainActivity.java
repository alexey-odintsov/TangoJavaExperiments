package com.alekso.tangojavaexperiments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alekso.tangojavaexperiments.databinding.ActivityMainBinding;
import com.google.atap.tangoservice.Tango;
import com.google.atap.tangoservice.TangoConfig;
import com.google.atap.tangoservice.TangoCoordinateFramePair;
import com.google.atap.tangoservice.TangoErrorException;
import com.google.atap.tangoservice.TangoEvent;
import com.google.atap.tangoservice.TangoInvalidException;
import com.google.atap.tangoservice.TangoOutOfDateException;
import com.google.atap.tangoservice.TangoPointCloudData;
import com.google.atap.tangoservice.TangoPoseData;
import com.google.atap.tangoservice.TangoXyzIjData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static Object sharedLock = new Object();
    private ActivityMainBinding mView;
    private Tango mTango;
    private TangoConfig mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(mView.appBar.toolbar);

        mView.appBar.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mView.drawerLayout, mView.appBar.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mView.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mView.navView.setNavigationItemSelectedListener(this);

        // Buttons for selecting camera view and Set up button click listeners
        mView.appBar.content.firstPersonButton.setOnClickListener(this);
        mView.appBar.content.thirdPersonButton.setOnClickListener(this);
        mView.appBar.content.topDownButton.setOnClickListener(this);

        // Button to reset motion tracking
        mView.appBar.content.resetMotion.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mView.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mView.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTango = new Tango(MainActivity.this, new Runnable() {
            @Override
            public void run() {
                synchronized (MainActivity.this) {
                    try {
                        mConfig = setupTangoConfig(mTango);
                        Log.d(TAG, "Connecting tango..");
                        mTango.connect(mConfig);
                        startupTango();
                        setupExtrinsics();
                    } catch (TangoOutOfDateException e) {
                        Log.e(TAG, "Tango out of date exception:", e);
                    } catch (TangoErrorException e) {
                        Log.e(TAG, "Tango error exception:", e);
                    } catch (TangoInvalidException e) {
                        Log.e(TAG, "Tango invalid exception:", e);
                    }
                }
            }
        });
    }

    private void setupExtrinsics() {
        // Get device to imu matrix.
        TangoPoseData device2IMUPose = new TangoPoseData();
        TangoCoordinateFramePair framePair = new TangoCoordinateFramePair();
        framePair.baseFrame = TangoPoseData.COORDINATE_FRAME_IMU;
        framePair.targetFrame = TangoPoseData.COORDINATE_FRAME_DEVICE;
        device2IMUPose = mTango.getPoseAtTime(0.0, framePair);
//        mRenderer.getModelMatCalculator().SetDevice2IMUMatrix(
//                device2IMUPose.getTranslationAsFloats(), device2IMUPose.getRotationAsFloats());

        // Get color camera to imu matrix.
        TangoPoseData color2IMUPose = new TangoPoseData();
        framePair.baseFrame = TangoPoseData.COORDINATE_FRAME_IMU;
        framePair.targetFrame = TangoPoseData.COORDINATE_FRAME_CAMERA_COLOR;
        color2IMUPose = mTango.getPoseAtTime(0.0, framePair);

//        mRenderer.getModelMatCalculator().SetColorCamera2IMUMatrix(
//                color2IMUPose.getTranslationAsFloats(), color2IMUPose.getRotationAsFloats());
    }

    @Override
    protected void onPause() {
        super.onPause();

        synchronized (this) {
            try {
                Log.d(TAG, "Disconnecting tango..");
                mTango.disconnect();
            } catch (TangoErrorException e) {
                Log.e(TAG, "Tango error exception:", e);
            }
        }

    }

    private TangoConfig setupTangoConfig(Tango tango) {
        TangoConfig config = tango.getConfig(TangoConfig.CONFIG_TYPE_DEFAULT);
        config.putBoolean(TangoConfig.KEY_BOOLEAN_MOTIONTRACKING, true);
        config.putBoolean(TangoConfig.KEY_BOOLEAN_AUTORECOVERY, true);
        return config;
    }

    private void motionReset() {
        mTango.resetMotionTracking();
    }

    private void startupTango() {
        final ArrayList<TangoCoordinateFramePair> framePairs = new ArrayList<>();
        framePairs.add(new TangoCoordinateFramePair(
                TangoPoseData.COORDINATE_FRAME_START_OF_SERVICE,
                TangoPoseData.COORDINATE_FRAME_DEVICE));

        // Listen for new Tango data.
        mTango.connectListener(framePairs, new Tango.TangoUpdateCallback() {
            @Override
            public void onPoseAvailable(TangoPoseData pose) {
                Log.d(TAG, "onPoseAvailable(pose: " + pose + ")");
                synchronized (sharedLock) {
                    float[] translation = pose.getTranslationAsFloats();
//                    if (!mRenderer.isValid()) {
//                        return;
//                    }
//
//                    mRenderer.getTrajectory().updateTrajectory(translation);
//                    mRenderer.getModelMatCalculator().updateModelMatrix(translation,
//                            pose.getRotationAsFloats());
//                    mRenderer.updateViewMatrix();
                }
            }

            @Override
            public void onXyzIjAvailable(TangoXyzIjData xyzIj) {
                Log.d(TAG, "onXyzIjAvailable(xyzIj: " + xyzIj + ")");
            }

            @Override
            public void onFrameAvailable(int cameraId) {
                super.onFrameAvailable(cameraId);
                Log.d(TAG, "onFrameAvailable(cameraId: " + cameraId + ")");
            }

            @Override
            public void onTangoEvent(TangoEvent event) {
                Log.d(TAG, "onTangoEvent(" + event.eventKey + " - " + event.eventValue + ")");
            }

            @Override
            public void onPointCloudAvailable(TangoPointCloudData pointCloud) {
                Log.d(TAG, "onPointCloudAvailable(pointCloud: " + pointCloud + ")");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_person_button:
//                mRenderer.setFirstPersonView();
                break;
            case R.id.top_down_button:
//                mRenderer.setTopDownView();
                break;
            case R.id.third_person_button:
//                mRenderer.setThirdPersonView();
                break;
            case R.id.reset_motion:
                motionReset();
                break;
            default:
                Log.w(TAG, "Unknown button click");
                return;
        }
    }
}