package tickets.language.svp.languagetickets.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;

import java.io.PrintWriter;
import java.io.StringWriter;

import tickets.language.svp.languagetickets.RootActivity;
import tickets.language.svp.languagetickets.ui.activities.CrashActivity;

/**
 * Created by Pasha on 9/13/2015.
 */
public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Activity activity;
    private final String LINE_SEPARATOR = "\n";
    public UncaughtExceptionHandler(Activity activity) {
        this.activity = activity;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);

        Intent intent = new Intent(activity.getBaseContext(), CrashActivity.class);
        intent.putExtra("error", errorReport.toString());
        activity.startActivityForResult(intent, ActivityOperationResult.Crash.toInt());

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    //@Override
    public void uncaughtException1(Thread thread, Throwable e) {
        //Throwable couse = paramThrowable.getCause();
//                        new AlertDialog.Builder(BaseActivity.this)
//                                .setTitle("Error")
//                                .setMessage(paramThrowable.getMessage())
//                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        System.exit(2);
//                                    }
//                                })
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .show();
        //Toast.makeText(BaseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        //System.exit(0); //Prevents the service/app from freezing
        final String str = e.getMessage();
        StackTraceElement[] stack = e.getStackTrace();
        Throwable th = e.fillInStackTrace();
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                //Toast.makeText(BaseActivity.this, str, Toast.LENGTH_LONG).show();
//                new AlertDialog.Builder(BaseActivity.this)
//                        .setTitle("Error")
//                        .setMessage(str)
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                System.exit(0);
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//                Looper.loop();
//            }
//        }.start();

//                        Toast.makeText(activity,
//                                "The application has crashed, and a report is sent to the admin",
//                                Toast.LENGTH_SHORT).show();
//                        System.exit(10);
    }
}
