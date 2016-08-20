package com.example.dinesh.GCm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.example.dinesh.iota.NotificationCentre;
import com.example.dinesh.iota.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;


public class GcmIntentService  extends IntentService {
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            Log.e("E",extras.getString("message"));
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                if (extras.getString("message") != null)
                    sendMessageNotification(extras.getString("message"), "notify_message");
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
    private boolean sendMessageNotification(String Response, String PrefKey) {

        Log.d("@sendNotification", Response);

        try {
            new PostSyncAndSendNotification().execute(Response);

            return true;
        } catch (Exception e) {
            Log.e("@sendNotification", e.toString());
        }
        return false;
    }

    protected class PostSyncAndSendNotification extends AsyncTask<String, Integer, String> {

        protected void onPostExecute(String params) {
                mBuilder = new NotificationCompat.Builder(getBaseContext());
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder.setContentTitle("Updates")
                        .setContentText(params)
                        .setSmallIcon(R.mipmap.ic_launcher);


                Intent i = new Intent(GcmIntentService.this, NotificationCentre.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("BUMPY_ROAD", params);


                mBuilder.setContentIntent(PendingIntent.getActivity(GcmIntentService.this, 0, i,PendingIntent.FLAG_ONE_SHOT));

                mNotificationManager.notify(1, mBuilder.build());



        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }
//        @Override
//        protected void onPostExecute(UpdatesItem post)
//        {
//            if (post != null)
//            {
//                mBuilder = new NotificationCompat.Builder(getBaseContext());
//                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                mBuilder.setSmallIcon(R.mipmap.ic_cs);
//                mBuilder.setContentTitle("CollegeSpace Updates");
//                mBuilder.setContentText(post.getTitle());
//                mBuilder.setDefaults(mNotificationDefaults);
//
//                Intent postDetailsIntent = new Intent(GcmIntentService.this, PostDetails.class);
//                postDetailsIntent.putExtra("post_title_bar", "Updates");
//                postDetailsIntent.putExtra("post_id", Integer.toString(post.getID()));
//                postDetailsIntent.putExtra("post_title", post.getTitle());
//                postDetailsIntent.putExtra("post_content", post.getContent());
//                postDetailsIntent.putExtra("post_url", post.getLink());
//                postDetailsIntent.putExtra("post_date", post.getDate());
//
//                mBuilder.setContentIntent(PendingIntent.getActivity(GcmIntentService.this, 0, postDetailsIntent, PendingIntent.FLAG_ONE_SHOT));
//
//                mNotificationManager.notify(post.getID(), mBuilder.build());
//            }
//        }
//
//        @Override
//        protected UpdatesItem doInBackground(String... params)
//        {
//            String download_url = params[0];
//            try
//            {
//                //Some pref related stuffs -- less managed
//                //TODO: Manage this code
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//                boolean notifyme = prefs.getBoolean("notify_me", true);
//                boolean donotify = prefs.getBoolean(params[1], true);
//
//                mNotificationDefaults = Notification.DEFAULT_LIGHTS;
//                if (prefs.getBoolean("notify_broadcast_vibrate", true))
//                    mNotificationDefaults |= Notification.DEFAULT_VIBRATE;
//                if (prefs.getBoolean("notify_broadcast_sound", true))
//                    mNotificationDefaults |= Notification.DEFAULT_SOUND;
//
//                jsonParser jParser = new jsonParser();
//                JSONObject feedObj = jParser.getJSONObjectFromUrl(download_url);
//                UpdatesItem aItem = Misc.getUpdateItemObject(getResources(), feedObj);
//
//                mDBHelper.UpdatesDeleteItemIfExist(aItem.getID());
//                mDBHelper.UpdatesAddItemIfNotExist(aItem);
//
//                if (notifyme && donotify)
//                    return aItem;
//            }
//            catch (Exception ex)
//            { Log.e("@GcmIntentService", ex.getMessage()); }
//            return null;
//        }
//    }
    }
}
