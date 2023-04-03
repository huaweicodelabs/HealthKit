
/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.huawei.codelabs.hihealth.happysport.viewmodels.hihealthbaseadapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.huawei.hms.hihealth.AutoRecorderController;
import com.huawei.hms.hihealth.data.Field;
import com.huawei.hms.hihealth.data.SamplePoint;
import com.huawei.hms.hihealth.options.OnSamplePointListener;

import java.util.concurrent.TimeUnit;

public class HiHealthBaseAdapter {
    private static final String TAG = "HiHealthBaseAdapter";

    private static final Object OBJECT = new Object();

    private static final Object PROCESS_OBJECT = new Object();

    public static final long SAMPLE_INTERVAL = 10 * 1000L;

    private static final long FIRST_DELAY = 1000L;

    private ISportListener mSportListener;

    private Context mContext;

    private AutoRecorderController autoRecorderController;

    private boolean mIsStopped;

    private long mBaseTimeStamp;

    private long mCurrentStartTime;

    private int mTempSetpValue;

    private int mSteps;

    private Handler mHandler;

    private Runnable mSampleHandler;

    // TODO: listener
    private OnSamplePointListener mListener = null;

    public HiHealthBaseAdapter(Context context, ISportListener listener) {
        mContext = context;
        mSportListener = listener;
        mSteps = 0;
        mBaseTimeStamp = 0L;
        mCurrentStartTime = 0L;
        mTempSetpValue = 0;
        mIsStopped = false;

        // TODO: client

        setupSample();
    }

    private void processSamplePoint(SamplePoint samplePoint) {
        synchronized (PROCESS_OBJECT) {
            if (mIsStopped) {
                return;
            }
        }

        if (mCurrentStartTime == 0) {
            mCurrentStartTime = samplePoint.getEndTime(TimeUnit.MILLISECONDS);
            mTempSetpValue = samplePoint.getFieldValue(Field.FIELD_STEPS).asIntValue();
            return;
        }

        mBaseTimeStamp = samplePoint.getEndTime(TimeUnit.MILLISECONDS);
        mSteps = samplePoint.getFieldValue(Field.FIELD_STEPS).asIntValue() - mTempSetpValue;
        mTempSetpValue = samplePoint.getFieldValue(Field.FIELD_STEPS).asIntValue();
    }

    private void setupSample() {
        mHandler = new Handler();
        mSampleHandler = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "sample real time data");
                if (mBaseTimeStamp != 0L && mSteps != 0) {
                    WalkingSportData walkingSportData =
                        new WalkingSportData(mBaseTimeStamp - mCurrentStartTime, mSteps);
                    mSportListener.onRecvData(walkingSportData);
                    mSteps = 0;
                    Log.d(TAG, String.format("feed data: %s", walkingSportData.toString()));
                }

                mHandler.postDelayed(this, SAMPLE_INTERVAL);
            }
        };
    }

    public boolean start(ISportListener listener) {
        Log.i(TAG, "begin to access sensor");

        synchronized (OBJECT) {
            mIsStopped = false;
        }

        // TODO: start

        mHandler.postDelayed(mSampleHandler, FIRST_DELAY);
        mCurrentStartTime = 0L;

        return true;
    }

    public boolean stop() {
        Log.i(TAG, "stop to access sensor");

        synchronized (OBJECT) {
            mIsStopped = true;
        }

        // TODO: stop

        mHandler.removeCallbacks(mSampleHandler);
        mBaseTimeStamp = 0L;
        mCurrentStartTime = 0L;
        mTempSetpValue = 0;
        mSteps = 0;

        return true;
    }
}
