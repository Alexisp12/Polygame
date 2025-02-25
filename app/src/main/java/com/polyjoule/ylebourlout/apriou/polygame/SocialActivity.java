package com.polyjoule.ylebourlout.apriou.polygame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import static com.polyjoule.ylebourlout.apriou.polygame.InstagramApp.userInfo;


/**
 * Created by Alexis on 08/01/2018.
 */



public class SocialActivity extends Activity {



    /** Twitter

     /**developer account key for this app*/
    public final static String _consumerKey = "tnPpEIGRkNECX9AWi4AnQsUrM";
    /**developer secret for the app*/
    public final static String _consumerSecret = "cHJ1AGrqNrr33MTUlSfEM5jVW6OcZUJwhNBwd8fb1lrN512ZAg";
    /**app url*/
    public final static String TWIT_URL = "tnice-android:///";
    /*Token*/
    public final static String _accessToken = "574483380-Gor7Bt8ZtfxJiCjuZzKPRksJP0okUnYGY2kjlbI3";
    public final static String _accessTokenSecret = "aXleRgd5m3SW0hw1dClf7hCzLCDqoXD98eekbMqzausjX";

    /*Twitter instance*/
    private Twitter niceTwitter;
    /**request token for accessing user account*/
    private RequestToken niceRequestToken;
    /**shared preferences to store user details*/
    private SharedPreferences nicePrefs;

    private String TWEETSETS="TwitNicePrefs";

    //for error logging
    private String LOG_TAG = "SocialActivity";//alter for your Activity name

    private List<String> listPseudo;
    private List<String> listStatus;
    private List<String> listSource;
    private List<String> listPP;
    private List<Bitmap> listBitmap;
    public static List<Bitmap> listInsta;
    private ListView tweetListView;
    private ListView instaListView;

    // Instagram
    public static final String CLIENT_ID = "6246d0115fa34027991f46461b0d716b";
    public static final String CLIENT_SECRET = "1dd34c48ad5c48e4b3df3cd397f61c87";
    public static final String CALLBACK_URL = "https://www.instagram.com/polyjoule/";//https://www.instagram.com/polyjoule/";
    public static final String TAG_ID = "id";
    public static final String TAG_COUNTS = "counts";
    private GridView gvAllImages;
    //private HashMap<String, String> userInfo;
    private ArrayList<String> imageThumbList = new ArrayList<String>();
    private Context context;
    private int WHAT_FINALIZE = 0;
    private static int WHAT_ERROR = 1;
    private ProgressDialog pd;
    public static final String TAG_DATA = "data";
    public static final String TAG_IMAGES = "images";
    public static final String TAG_THUMBNAIL = "thumbnail";
    public static final String TAG_URL = "url";
    private ImageView imageInsta1;
    private ImageView imageInsta2;
    private ImageView imageInsta3;
    //private ImageView imageInsta1b;
    //private ImageView imageInsta2b;
    //private ImageView imageInsta3b;
    private TextView loadTweet;
    private TextView loadInsta;
    private Boolean successDone=false;

    private Button instagramButton;
    private Button twitterButton;

    private InstagramApp mInstaApp;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = mInstaApp.getUserInfo();
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(SocialActivity.this, "Check your network.",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    private TwitterStream twitterStream;
    private Context mThis;
    private Thread thread;
    private Thread thread2;
    private Thread thread3;
    private RelativeLayout tweetRL;
    private URL urlpp;
    private URL urlinstapics;
    private List<String> urlListInsta;
    private Boolean seekPP=false,seekStatus=false;
    private Boolean seekInstaPics=false;
    public Boolean isRunning=false;

    private int startUrl=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socialmain_layout);

        tweetListView = (ListView) findViewById(R.id.listViewTweet);
        instaListView = (ListView) findViewById(R.id.listViewInsta);
        final RelativeLayout tweetRl = (RelativeLayout) findViewById(R.id.layout_twitter);
        final RelativeLayout instaRL = (RelativeLayout) findViewById(R.id.layout_instagram);

        //final LinearLayout instaLL = (LinearLayout) findViewById(R.id.RLlistViewInsta);

        //imageInsta1 = (ImageView) (findViewById(R.id.image1));
        //imageInsta2 = (ImageView) (findViewById(R.id.image2));
        //imageInsta3 = (ImageView) (findViewById(R.id.image3));

        loadInsta = (TextView) (findViewById(R.id.loadingINSTA));
        loadTweet = (TextView) (findViewById(R.id.loadingTwitter));

        instagramButton = (Button) (findViewById(R.id.instagramButton));
        twitterButton = (Button) (findViewById(R.id.twitterButton));


        twitterButton.performClick();
        //imageInsta1.setVisibility(View.GONE);
        //imageInsta2.setVisibility(View.GONE);
        //imageInsta3.setVisibility(View.GONE);


        mThis= this;

        listPseudo = new ArrayList<String>();
        listStatus = new ArrayList<String>();
        listSource = new ArrayList<String>();
        listBitmap = new ArrayList<Bitmap>();
        listInsta = new ArrayList<Bitmap>();
        listPP = new ArrayList<String>();
        urlListInsta = new ArrayList<String>();


        twitterButton.setOnClickListener(new View.OnClickListener(){
            public void onClick (View tweetView){

                tweetRl.setVisibility(View.VISIBLE);
                instaRL.setVisibility(View.GONE);

                twitterButton.setBackgroundColor(getResources().getColor(R.color.colorClick));
                instagramButton.setBackgroundColor(getResources().getColor(R.color.colorButton));

                if(seekPP && seekStatus) {
                    if(listPseudo.size()==0){
                        twitterButton.performClick();
                    } else {
                    TweetAdaptateur adapter = new TweetAdaptateur(mThis, listPseudo, listStatus, listBitmap);
                    tweetListView.setAdapter(adapter);

                    loadTweet.setVisibility(View.GONE);


                    tweetListView.setOnItemClickListener(new ListView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                            Log.d("StatusClicked", listStatus.get(pos));
                            Log.d("UrlClicked", listSource.get(pos));
                            //// Ouvre une page web
                            //Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                            //startActivity(intent);
                            Intent intent = null;
                            try {
                                // get the Twitter app if possible
                                getPackageManager().getPackageInfo("com.twitter.android", 0);
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=Polyjoule"));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            } catch (Exception e) {
                                // no Twitter app, revert to browser
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Polyjoule"));
                            }
                            startActivity(intent);
                        }
                    });
                    }
                } else {
                    tweetRl.performClick();
                }
            }
        });
        // Twitter
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
                    configurationBuilder.setOAuthConsumerKey(_consumerKey)
                            .setOAuthConsumerSecret(_consumerSecret)
                            .setOAuthAccessToken(_accessToken)
                            .setOAuthAccessTokenSecret(_accessTokenSecret);
                    // The factory instance is re-useable and thread safe.
                    TwitterFactory tf = new TwitterFactory(configurationBuilder.build());

                    Twitter twitter = tf.getInstance();

                    List<Status> statuses = null;

                    //statuses = twitter.getHomeTimeline();
                    //statuses = twitter.getRetweetsOfMe();
                    statuses = twitter.getUserTimeline();

                    //System.out.println("Showing home timeline.");

                    for (Status status : statuses) {
                        Log.d("UserName",status.getUser().getName());
                        Log.d("status",status.getText());
                        if(listPseudo.size()<statuses.size()-1) { //nb tweet à afficher
                            listPseudo.add("@" + status.getUser().getName());
                            listStatus.add(status.getText());
                            listPP.add(status.getUser().getOriginalProfileImageURL());
                            Log.d("listPP",status.getUser().getBiggerProfileImageURL());

                            try {
                                urlpp = new URL(status.getUser().getMiniProfileImageURL());
                            } catch (MalformedURLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            thread2 = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    //Bitmap bitmap = null;
                                    try {
                                        HttpURLConnection connection = (HttpURLConnection) urlpp.openConnection();
                                        InputStream inputStream = connection.getInputStream();
                                        listBitmap.add(BitmapFactory.decodeStream(inputStream));
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    seekPP=true;

                                }
                            });
                            thread2.start();

                            for(int i =0; i<status.getText().length()-10;i++){
                                //Log.d("charAt "+i, status.getText().substring(i,i+1));
                                if("https://t".equals(status.getText().substring(i,i+9))){
                                    startUrl=i;
                                }
                            }

                            Log.d("startUrl",Integer.toString(startUrl));
                            if(status.getText().length()>startUrl) {
                                listSource.add(status.getText().substring(startUrl));
                            }else {
                                listSource.add("https://twitter.com/polyjoule?lang=fr");
                            }

                            //status.getUser().
                        }
                    }
                    seekStatus=true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            twitterButton.performClick();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
                //updateTweet();
            }
        });

        thread.start();

        // Instagram

        mInstaApp = new InstagramApp(this, CLIENT_ID,
                CLIENT_SECRET, CALLBACK_URL);

//        if(!successDone) {
//            // tvSummary.setText("Connected as " + mApp.getUserName());
//            //btnConnect.setText("Disconnect");
//            //llAfterLoginView.setVisibility(View.VISIBLE);
//            Log.d("OAuthInsta", "success");
//            userInfoHashmap = mInstaApp.getUserInfo();
//            mInstaApp.fetchUserName();
//            getAllMediaImages(instaRL);
//            successDone = true;
//            seekInstaPics=true;
//        }

        mInstaApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                if(!successDone) {
                    // tvSummary.setText("Connected as " + mApp.getUserName());
                    //btnConnect.setText("Disconnect");
                    //llAfterLoginView.setVisibility(View.VISIBLE);
                    Log.d("OAuthInsta", "success");
                    userInfoHashmap = mInstaApp.getUserInfo();
                    mInstaApp.fetchUserName();
                    getAllMediaImages(instaRL);
                    successDone = true;



                }

            }

            @Override
            public void onFail(String error) {
                Log.d("OAuthInsta","fail");
                //Toast.makeText(Social.this, error, Toast.LENGTH_SHORT)
                //.show();
            }
        });



        loadInsta.setOnClickListener(new View.OnClickListener(){
            public void onClick (View tweetView) {
                //mInstaApp.authorize();
            }
        });


            // TODO CODE POUR LANCER INSTA
//
//                Intent intent = null;
//                try {
//                    // get the Twitter app if possible
//                    getPackageManager().getPackageInfo("com.instagram.android", 0);
//                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/polyjoule"));
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                } catch (Exception e) {
//                    // no Twitter app, revert to browser
//                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/polyjoule/"));
//                }
//                startActivity(intent);


        instagramButton.setOnClickListener(new View.OnClickListener(){
            public void onClick (View tweetView){

                tweetRl.setVisibility(View.GONE);
                instaRL.setVisibility(View.VISIBLE);
                twitterButton.setBackgroundColor(getResources().getColor(R.color.colorButton));
                instagramButton.setBackgroundColor(getResources().getColor(R.color.colorClick));


                if(seekInstaPics) {
                    if(listInsta.size()!=0) {
                        InstagramAdaptateur instaAdapter = new InstagramAdaptateur(mThis, listInsta);
                        instaListView.setAdapter(instaAdapter);

                        loadInsta.setVisibility(View.GONE);


                        instaListView.setOnItemClickListener(new ListView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                                Intent intent = null;
                                try {
                                    // get the Twitter app if possible
                                    getPackageManager().getPackageInfo("com.instagram.android", 0);
                                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/polyjoule"));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                } catch (Exception e) {
                                    // no Twitter app, revert to browser
                                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/polyjoule/"));
                                }
                                startActivity(intent);
                            }
                        });




                    } else {
                        getAllMediaImages(instaRL);
                    }

                } else {
//                    if (mInstaApp.hasAccessToken()) {
//                        //seekInstaPics=true;
//                        Log.d("instaAppToken","yes");
//                        // tvSummary.setText("Connected as " + mApp.getUserName());
//                        //btnConnect.setText("Disconnect");
//                        //llAfterLoginView.setVisibility(View.VISIBLE);
//                        mInstaApp.fetchUserName();
                    getAllMediaImages(instaRL);
//
//                    }
                }

            }
        });








    }


    @Override
    public void onBackPressed() {
        //twitterStream.clearListeners();
        listBitmap.clear();
        listPP.clear();
        listStatus.clear();
        listPseudo.clear();
        listSource.clear();

        this.finish();

        Intent retourMenuIntent = new Intent(SocialActivity.this, Accueil.class);

        startActivity(retourMenuIntent);
    }

    // Instagram
    private void setImageGridAdapter() {

    }


    private void getAllMediaImages(final RelativeLayout instaRL) {
//        pd = ProgressDialog.show(context, "", "Loading images...");

        if(!isRunning) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                isRunning=true;
                int what = WHAT_FINALIZE;
                try {
                    // URL url = new URL(mTokenUrl + "&code=" + code);
                    Log.d("getAllMediaImages","yes");
                    Log.d("URLJSON","https://api.instagram.com/v1/users/"
                            + userInfo.get(TAG_ID)
                            + "/media/recent/?client_id="
                            + CLIENT_ID
                            + "&count="
                            + userInfo.get(TAG_COUNTS));
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = jsonParser
                            .getJSONFromUrlByGet("https://api.instagram.com/v1/users/"
                                    + "7075790315"//+ userInfoHashmap.get(TAG_ID)
                                    + "/media/recent/?access_token=7075790315.6246d01.e24643f7c1a749339b5427c6cb5592ab");//+mInstaApp.getTOken());
//                                        + CLIENT_ID
//                                        + "&count="
//                                        + userInfoHashmap.get(TAG_COUNTS));
                    JSONArray data = jsonObject.getJSONArray(TAG_DATA);

                    Log.d("data_length",Integer.toString(data.length()));
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);

                        JSONObject images_obj = data_obj
                                .getJSONObject(TAG_IMAGES);

                        JSONObject thumbnail_obj = images_obj
                                .getJSONObject(TAG_THUMBNAIL);

                        // String str_height =
                        // thumbnail_obj.getString(TAG_HEIGHT);
                        //
                        // String str_width =
                        // thumbnail_obj.getString(TAG_WIDTH);

                        String str_url = thumbnail_obj.getString(TAG_URL);

                        imageThumbList.add(str_url);
                        Log.d("imageThumbList",str_url);

                        if(data_i==data.length()-1){
                            //thread3 = new Thread(new Runnable() {
                                //@Override
                                //public void run() {
                                    Log.d("ListeBitmap","done");

                                    for (int i = 0; i < imageThumbList.size(); i++) {
                                        try {
                                            if(!urlListInsta.contains(imageThumbList.get(i)))
                                            {
                                                urlinstapics = new URL(imageThumbList.get(i));
                                                urlListInsta.add((imageThumbList.get(i)).toString());
                                            }
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            HttpURLConnection connection = (HttpURLConnection) (urlinstapics).openConnection();
                                            InputStream inputStream = connection.getInputStream();

                                            listInsta.add(BitmapFactory.decodeStream(inputStream));
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            loadInsta.setVisibility(View.GONE);
                                            seekInstaPics=true;
                                            isRunning=false;
                                            instagramButton.performClick();
                                        }
                                    });
                                //}
                           // });
                            //thread3.start();
                        }

                    }


                    System.out.println("jsonObject::" + jsonObject);

                } catch (Exception exception) {
                    exception.printStackTrace();
                    what = WHAT_ERROR;
                }
                // pd.dismiss();
                handler.sendEmptyMessage(what);
            }
        }).start();
        }
    }
}
