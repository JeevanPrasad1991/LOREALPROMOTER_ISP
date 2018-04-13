package com.cpm.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.cpm.lorealpromoter.LoginActivity;
import com.cpm.lorealpromoter.R;

/**
 * Created by deepakp on 8/10/2017.
 */

@SuppressWarnings("deprecation")
public class AlertandMessages {

    private String data, condition;
    private Activity activity;

    public AlertandMessages(Activity activity, String data, String condition, Exception exception) {
        this.activity = activity;
        this.data = data;
        this.condition = condition;
    }

    public static void showSnackbarMsg(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackbarMsg(Context context, String message) {
        Snackbar.make(((Activity) context).getCurrentFocus(), message, Snackbar.LENGTH_SHORT).show();
    }


    public static void showToastMsg(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void editorDeleteAlert(Context context, String str, final Runnable task) {

        final AlertDialog builder = new AlertDialog.Builder(context).create();
        builder.setTitle("Alert");
        builder.setMessage(str);
        builder.setCancelable(false);
        builder.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                task.run();

            }
        });
        builder.setButton2("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.dismiss();
            }
        });
        builder.show();
    }

    public void backpressedAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to exit? Filled data will be lost").setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void backpressedAlertForStoreImage(final Context context, final Runnable task) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to exit? Filled data will be lost").setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        task.run();
                        ((Activity) context).finish();
                        ((Activity) context).overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public static void showAlert(final Activity activity, String str, final Boolean activityFinish) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Parinaam");
        builder.setMessage(str).setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (activityFinish) {
                            activity.finish();
                        } else {
                            dialog.dismiss();
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showAlertOldImageUpload(final Activity activity, String str, final Boolean activityFinish) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Parinaam");
        builder.setMessage(str).setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (activityFinish) {
                            activity.finish();
                        } else {
                            dialog.dismiss();
                            Intent i = new Intent(activity, LoginActivity.class);
                            activity.startActivity(i);
                            activity.overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            activity.finish();
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
