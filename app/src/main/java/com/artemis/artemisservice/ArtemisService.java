package com.artemis.artemisservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Xin Qian on 2019/1/17.
 */

public class ArtemisService extends Service {
    public ArtemisService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ArtemisHttpServer.getInstance().start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ArtemisHttpServer.getInstance().stop();
    }
}
