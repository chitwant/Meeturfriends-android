package com.meetfriend.app.acra;

import android.content.Context;

import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderFactory;

public class YourOwnSenderfactory implements ReportSenderFactory {


    // NB requires a no arg constructor.

    public ReportSender create(Context context, ACRAConfiguration config) {
        return new YourOwnSender(config);
    }
}

