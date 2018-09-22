package com.spaceproject.generation.noise;

import com.badlogic.gdx.utils.Array;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NoiseThreadPoolExecutor extends ThreadPoolExecutor {

    private Array<NoiseGenListener> listeners;

    public NoiseThreadPoolExecutor(int numThreads) {
        super(numThreads, numThreads, 0,	 TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        //super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        listeners = new Array<NoiseGenListener>();

        System.out.println("NoiseThreadPool with " + numThreads + " threads");
    }


    public void addListener(NoiseGenListener listener) {
        listeners.add(listener);
    }

    private void notifyListenersNoiseFinished(NoiseThread noise) {
        for (int i = 0; i < listeners.size; i++) {
            listeners.get(i).threadFinished(noise);
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t == null) {
            //System.out.println("Task completed successfully!");
            notifyListenersNoiseFinished((NoiseThread)r);
        } else {
            System.out.println("Task failed:  " + t.getMessage());
        }
    }

}