package com.soj.m1kes.nits;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.soj.m1kes.nits.adapters.gridview.adapters.MenuAdapter;
import com.soj.m1kes.nits.service.AgentContactsService;
import com.soj.m1kes.nits.service.ServiceManager;
import com.soj.m1kes.nits.service.objects.ServiceCallback;
import com.soj.m1kes.nits.sqlite.adapters.AgentContactsAdapter;
import com.soj.m1kes.nits.util.ActionBarUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.InputStream;

public class MenuScreen extends AppCompatActivity {

    Integer imageIds[] = {R.drawable.clients_one,R.drawable.jobs_one,
            R.drawable.schedule_one,R.drawable.services_one
           };
    GridView gridView;
    String []keyWords ={"Agents","Jobs","Schedule","Others"};
    private Context context = this;
    private LinearLayout parent_Slider;
    private SlidingUpPanelLayout sliding_layout;
    private ScrollView root_main;
    private AgentContactsService service = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);
        ActionBarUtils.setupTitleOnlyActionBar("Main Menu",this);
        service = new AgentContactsService(context);
        service.addCallback(response -> {
            if(response.contains("Added Contact Successfully")){
                System.out.println("Contact synced with server");
            }else if(response.contains("Failed to add new Contact")){
                System.out.println("Contact failed to sync with server");
            }
        });
        service.syncUnsyncedContacts();

        initGui();
        gridView = (GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new MenuAdapter(context,imageIds,keyWords));
        gridView.setNumColumns(2);
        gridView.setPadding(0,0,0,0);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                switch (position){

                    case 0://Agents
                        startActivity(new Intent(context,MainActivity.class));

                        break;
                    case 1://Jobs
                        startActivity(new Intent(context,JobsActivity.class));
                        break;
                    case 2: //Schedule

                        break;
                    case 3: //Others
                        startActivity(new Intent(context,OthersActivity.class));
                        break;
                    default:

                }

            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                new ServiceManager(context).Sync();
            }
        }, 5000);


        TextView tv = (TextView) findViewById(R.id.txtEmailCount);
        tv.setText("Offline");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                executeUpdater();
            }
        },1000);
    }

    private void executeUpdater(){

            new EmailCounter(context,this,new EmailCallback() {
                @Override
                public void onDone() {
                }}).execute();
    }


    interface EmailCallback{
        void onDone();
    }

   private static class EmailCounter extends AsyncTask<Void,Void,Void> {

        String host="10.29.13.40";
        String user="root";
        String password="PosT4queue#";
        String command1="mailq | wc -l";

       private Handler mHandler;
       private Context context;
       private Activity activity;
       private EmailCallback callback;


       public EmailCounter(Context context,Activity activity,EmailCallback callback){
           mHandler = new Handler();
           this.context = context;
           this.activity = activity;
           this.callback = callback;
       }

       public EmailCounter(Context context,Activity activity){
           mHandler = new Handler();
           this.context = context;
           this.activity = activity;
       }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {


            for(int j = 0;j < 3;j++) {

                try {

                    java.util.Properties config = new java.util.Properties();
                    config.put("StrictHostKeyChecking", "no");
                    JSch jsch = new JSch();
                    Session session = jsch.getSession(user, host, 22);
                    session.setPassword(password);
                    session.setConfig(config);
                    session.connect();
                    System.out.println("Connected");

                    Channel channel = session.openChannel("exec");
                    ((ChannelExec) channel).setCommand(command1);
                    channel.setInputStream(null);
                    ((ChannelExec) channel).setErrStream(System.err);

                    InputStream in = channel.getInputStream();
                    channel.connect();

                    byte[] tmp = new byte[1024];


                    while (true) {
                        while (in.available() > 0) {
                            int i = in.read(tmp, 0, 1024);
                            if (i < 0) break;

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    TextView tv = (TextView) activity.findViewById(R.id.txtEmailCount);
                                    if (tv != null)
                                        tv.setText(new String(tmp, 0, i));
                                }
                            });
                            System.out.print(new String(tmp, 0, i));
                        }
                        if (channel.isClosed()) {
                            System.out.println("exit-status: " + channel.getExitStatus());
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ee) {
                        }
                    }
                    channel.disconnect();
                    session.disconnect();
                    System.out.println("DONE");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(callback!=null)
            callback.onDone();
        }
    }


    private void initGui(){
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        double ratio = ((float) (width))/300.0;
        int height = (int)(ratio*50);
        parent_Slider = (LinearLayout) findViewById(R.id.parent_Slider);
        parent_Slider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,GetPixel(width,context)));
        sliding_layout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);

        root_main = (ScrollView)findViewById(R.id.root_main);

        new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //root_main.onInterceptTouchEvent();
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        };
    }

    // To convert pixels to dp units
    public int GetPixel(float f , Context context)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int)((f * displayMetrics.density) + 0.5f);
    }

}
