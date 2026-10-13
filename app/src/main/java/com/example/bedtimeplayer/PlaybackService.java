package com.example.bedtimeplayer;

import android.app.*;
import android.content.*;
import android.media.*;
import android.net.Uri;
import android.os.*;

public class PlaybackService extends Service {
    public static final String CHANNEL="bedtime_playback";
    private MediaPlayer player;
    private Uri current;
    private boolean repeat=true;
    @Override public void onCreate(){super.onCreate(); NotificationChannel channel=new NotificationChannel(CHANNEL,"Bedtime playback",NotificationManager.IMPORTANCE_LOW);getSystemService(NotificationManager.class).createNotificationChannel(channel);}
    @Override public int onStartCommand(Intent intent,int flags,int startId){
        if(intent==null)return START_NOT_STICKY;
        String action=intent.getAction();
        if("STOP".equals(action)){stopPlayback();stopForeground(true);stopSelf();return START_NOT_STICKY;}
        if("REPEAT".equals(action)){repeat=intent.getBooleanExtra("enabled",true);if(player!=null)player.setLooping(repeat);return START_STICKY;}
        if("PLAY".equals(action)){
            repeat=intent.getBooleanExtra("repeat",repeat);
            String value=intent.getStringExtra("uri");
            if(value==null)return START_NOT_STICKY;
            Uri uri=Uri.parse(value);
            if(player!=null && uri.equals(current)){player.start();showNotification();return START_STICKY;}
            stopPlayback();current=uri;
            try{
                player=new MediaPlayer();player.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
                player.setDataSource(this,uri);player.setLooping(repeat);player.prepare();player.start();showNotification();
            }catch(Exception ex){stopPlayback();stopSelf();}
        }
        return START_STICKY;
    }
    private void showNotification(){
        Intent stop=new Intent(this,PlaybackService.class).setAction("STOP");
        PendingIntent pending=PendingIntent.getService(this,1,stop,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        Notification n=new Notification.Builder(this,CHANNEL).setContentTitle("Bedtime Player").setContentText("White noise playing").setSmallIcon(android.R.drawable.ic_media_play).addAction(new Notification.Action.Builder(android.R.drawable.ic_media_pause,"STOP",pending).build()).setOngoing(true).build();
        startForeground(11,n);
    }
    private void stopPlayback(){if(player!=null){try{player.stop();}catch(Exception ignored){}player.release();player=null;}current=null;}
    @Override public void onDestroy(){stopPlayback();super.onDestroy();}
    @Override public IBinder onBind(Intent intent){return null;}
}
