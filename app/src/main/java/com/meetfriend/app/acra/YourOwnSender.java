package com.meetfriend.app.acra;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;

import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collections.ImmutableSet;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Set;

public class YourOwnSender implements ReportSender {
    private final ACRAConfiguration config;
//    private FileWriter crashReport = null;


    public YourOwnSender(@NonNull ACRAConfiguration config) {
        //System.out.println("in YourOwnSender ");
        this.config = config;
    }

           /* public YourOwnSender(){
                File logFile = new File(Environment.getExternalStorageDirectory(), "log.txt");

                try {
                    crashReport = new FileWriter(logFile, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            */

    @Override
    public void send(@NonNull Context context, @NonNull CrashReportData errorContent) throws ReportSenderException {
        System.out.println("in send ");
        final String subject = context.getPackageName() + " Crash Report";
        final String body = buildBody(errorContent);
        writeToFile(body);
        File dir = new File(Environment.getExternalStorageDirectory() + "/Meet Friend");
        String pathToMyAttachedFile = "Meet Friend.txt";
        File file = new File(dir, pathToMyAttachedFile);
        Uri uri = Uri.fromFile(file);

        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"+"rajasaadatwali786@gmail.com"));
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(emailIntent);
    }

    public String buildBody(@NonNull CrashReportData errorContent) {
        System.out.println("in build ");
        Set<ReportField> fields = config.getReportFields();
        if (fields.isEmpty()) {
            fields = new ImmutableSet<ReportField>(ACRAConstants.DEFAULT_MAIL_REPORT_FIELDS);
        }

        final StringBuilder builder = new StringBuilder();
        for (ReportField field : fields) {
            builder.append(field.toString()).append('=');
            builder.append(errorContent.get(field));
            builder.append('\n');
        }
        return builder.toString();
    }

    public void writeToFile(String data) {
        System.out.println("in write to file ");
        String filepath = Environment.getExternalStorageDirectory() + "/Meet Friend" + "/Meet Friend.txt";
        File file = new File(filepath);
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
        if (file.exists()) {
            try {
                if (file_size < 5120) {
                    FileOutputStream fOut = new FileOutputStream(file, true);
                    OutputStreamWriter writer = new OutputStreamWriter(fOut);
                    writer.write(data);
                    writer.close();
                } else {
                    FileOutputStream writer = new FileOutputStream(filepath);
                    writer.write((new String()).getBytes());
                    writer.close();

                    FileOutputStream fOut = new FileOutputStream(file, true);
                    OutputStreamWriter writer1 = new OutputStreamWriter(fOut);
                    writer1.write(data);
                    writer1.close();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            File dir = new File(Environment.getExternalStorageDirectory() + "/Meet Friend");
            dir.mkdirs();
            File newfile = new File(dir, "Meet Friend.txt");
            try {
                file.createNewFile();
                FileOutputStream fOut = new FileOutputStream(newfile, true);
                OutputStreamWriter writer1 = new OutputStreamWriter(fOut);
                writer1.write(data);
                writer1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
