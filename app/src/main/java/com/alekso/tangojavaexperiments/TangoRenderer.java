package com.alekso.tangojavaexperiments;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.projecttango.tangoutils.Renderer;
import com.projecttango.tangoutils.renderables.CameraFrustumAndAxis;
import com.projecttango.tangoutils.renderables.Grid;
import com.projecttango.tangoutils.renderables.Trajectory;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by alekso on 14/03/2017.
 */

public class TangoRenderer extends Renderer implements GLSurfaceView.Renderer {
    private Grid mFloorGrid;
    private CameraFrustumAndAxis mCameraFrustumAndAxis;
    private Trajectory mTrajectory;
    private boolean mIsValid = false;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        mFloorGrid = new Grid();
        mCameraFrustumAndAxis = new CameraFrustumAndAxis();
        mTrajectory = new Trajectory(3);

        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setLookAtM(mViewMatrix, 0, 5f, 5f, 5f, 0f, 0f, 0f, 0f, 1f, 0f);
        mCameraFrustumAndAxis.setModelMatrix(getModelMatCalculator().getModelMatrix());

        mIsValid = true;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mCameraAspect = (float) width / height;
        Matrix.perspectiveM(mProjectionMatrix, 0, THIRD_PERSON_FOV, mCameraAspect, CAMERA_NEAR,
                CAMERA_FAR);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (MainActivity.sharedLock) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            mTrajectory.draw(getViewMatrix(), mProjectionMatrix);
            mFloorGrid.draw(getViewMatrix(), mProjectionMatrix);
            mCameraFrustumAndAxis.draw(getViewMatrix(), mProjectionMatrix);
        }
    }

    public CameraFrustumAndAxis getCameraFrustumAndAxis() {
        return mCameraFrustumAndAxis;
    }

    public Trajectory getTrajectory() {
        return mTrajectory;
    }

    public boolean isValid() {
        return mIsValid;
    }
}
