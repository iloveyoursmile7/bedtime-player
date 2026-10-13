package com.example.bedtimeplayer;

import android.app.*;
import android.os.*;
import android.content.*;
import android.net.Uri;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    private static final int PICK=100;
    private final ArrayList<String> names=new ArrayList<>();
    private final ArrayList<String> uris=new ArrayList<>();
    private int selected=0;
    private boolean repeat=true;
    private LinearLayout tracks;
    private TextView status;
    private Button repeatButton;
    private int dp(float v){return (int)(v*getResources().getDisplayMetrics().density+.5f);}
    private Button button(String label,int color){Button b=new Button(this);b.setText(label);b.setTextSize(31);b.setTextColor(Color.WHITE);b.setAllCaps(false);b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));return b;}
    @Override public void onCreate(Bundle saved){super.onCreate(saved);
        getWindow().setStatusBarColor(Color.rgb(15,22,33));getWindow().setNavigationBarColor(Color.rgb(15,22,33));
        android.content.SharedPreferences p=getSharedPreferences("tracks",MODE_PRIVATE);
        repeat=p.getBoolean("repeat",true);selected=p.getInt("selected",0);
        Set<String> keys=p.getStringSet("uris",Collections.emptySet());
        ArrayList<String> ordered=new ArrayList<>(keys);Collections.sort(ordered);
        for(String entry:ordered){int cut=entry.indexOf('|');if(cut>0){names.add(entry.substring(cut+1));uris.add(entry.substring(0,cut));}}
        LinearLayout root=new LinearLayout(this);root.setOrientation(1);root.setPadding(dp(14),dp(14),dp(14),dp(14));root.setBackgroundColor(Color.rgb(15,22,33));setContentView(root);
        TextView title=new TextView(this);title.setText("송이와 민준이의 굿나잇 빗소리");title.setTextColor(Color.WHITE);title.setTextSize(24);title.setGravity(Gravity.CENTER);root.addView(title,new LinearLayout.LayoutParams(-1,dp(82)));
        status=new TextView(this);status.setTextColor(0xffcbd5e1);status.setTextSize(18);status.setGravity(Gravity.CENTER);root.addView(status,new LinearLayout.LayoutParams(-1,dp(40)));
        Button add=button("+ ADD AUDIO FILES",0xff334155);root.addView(add,new LinearLayout.LayoutParams(-1,dp(65)));add.setOnClickListener(v->pick());
        ScrollView scroll=new ScrollView(this);tracks=new LinearLayout(this);tracks.setOrientation(1);scroll.addView(tracks);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout controls=new LinearLayout(this);controls.setOrientation(0);root.addView(controls,new LinearLayout.LayoutParams(-1,dp(330)));
        Button play=button("▶\nPLAY",0xff16834a),stop=button("■\nSTOP",0xffbd343b);
        LinearLayout.LayoutParams half=new LinearLayout.LayoutParams(0,-1,1);half.setMargins(dp(3),dp(3),dp(3),dp(3));controls.addView(play,half);LinearLayout.LayoutParams half2=new LinearLayout.LayoutParams(0,-1,1);half2.setMargins(dp(3),dp(3),dp(3),dp(3));controls.addView(stop,half2);
        play.setOnClickListener(v->{if(uris.isEmpty()) {pick();return;}if(selected>=uris.size())selected=0;Intent i=new Intent(this,PlaybackService.class).setAction("PLAY").putExtra("uri",uris.get(selected)).putExtra("repeat",repeat);startForegroundService(i);status.setText("Playing: "+names.get(selected));save();});
        stop.setOnClickListener(v->{startService(new Intent(this,PlaybackService.class).setAction("STOP"));status.setText("Stopped");});
        repeatButton=button("",0xff34465e);root.addView(repeatButton,new LinearLayout.LayoutParams(-1,dp(90)));repeatButton.setOnClickListener(v->{repeat=!repeat;if(!uris.isEmpty()) startService(new Intent(this,PlaybackService.class).setAction("REPEAT").putExtra("enabled",repeat));save();refresh();});
        refresh();
    }
    private void pick(){Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.setType("audio/*");i.addCategory(Intent.CATEGORY_OPENABLE);i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);startActivityForResult(i,PICK);}
    @Override protected void onActivityResult(int request,int result,Intent data){super.onActivityResult(request,result,data);if(request!=PICK||result!=RESULT_OK||data==null)return;
        if(data.getClipData()!=null){for(int j=0;j<data.getClipData().getItemCount();j++)addUri(data.getClipData().getItemAt(j).getUri());}else if(data.getData()!=null)addUri(data.getData());save();refresh();}
    private void addUri(Uri uri){try{getContentResolver().takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION);}catch(Exception ignored){}String s=uri.toString();if(!uris.contains(s)){uris.add(s);names.add(uri.getLastPathSegment()==null?"Audio file":Uri.decode(uri.getLastPathSegment()));}}
    private void save(){android.content.SharedPreferences.Editor e=getSharedPreferences("tracks",MODE_PRIVATE).edit();Set<String> set=new HashSet<>();for(int i=0;i<uris.size();i++)set.add(uris.get(i)+"|"+names.get(i));e.putStringSet("uris",set).putInt("selected",selected).putBoolean("repeat",repeat).apply();}
    private void refresh(){tracks.removeAllViews();if(uris.isEmpty())status.setText("Add a white-noise file to begin");else if(status.getText().toString().startsWith("Add"))status.setText("Ready: "+names.get(Math.min(selected,names.size()-1)));
        for(int j=0;j<names.size();j++){final int index=j;Button b=button((selected==j?"✓  ":"♪  ")+names.get(j),selected==j?0xff275d75:0xff273449);b.setTextSize(19);b.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,dp(72));lp.bottomMargin=dp(5);tracks.addView(b,lp);b.setOnClickListener(v->{selected=index;status.setText("Ready: "+names.get(index));save();refresh();});}
        repeatButton.setText(repeat?"🔁 REPEAT ONE: ON":"🔁 REPEAT ONE: OFF");repeatButton.setContentDescription(repeat?"Repeat selected track on":"Repeat selected track off");
    }
}
