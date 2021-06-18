package com.dogroll.admin.vetlauncher;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

public class UpdateCheckService extends IntentService {

    public UpdateCheckService (){
        super("Spacefiller");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        String result = intent.getDataString();

    }
}
