package missiontocure.com.m2c.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;
import missiontocure.com.m2c.CheckConnection;
import missiontocure.com.m2c.Constants;
import missiontocure.com.m2c.ExtraFunctions;
import missiontocure.com.m2c.FirebaseNotificationService;
import missiontocure.com.m2c.NotificationListener;
import missiontocure.com.m2c.R;
import missiontocure.com.m2c.database.DatabaseHandler;

public class MainActivity extends AppCompatActivity implements  BaseSliderView.OnSliderClickListener{

    private android.support.v7.widget.Toolbar toolbar;

    GridView gridView_category;
    SharedPreferences sharedPreferences;
    CustomGridAdapter customGridAdapter;
    Boolean notifyVal = false;
    Button btn_bookToken;
    int VERSION_CODE=0;
    TextView tv_quote;
    ShowcaseView builder;
    SliderLayout sliderLayout;
    public static  String[] categories = {"Homoeopathy" , "Academics","Know Your Doctors","Appointment Bookings","My Profile","Gallery","Location","Contact Us","Help","Feedback and Reviews","Notifications"};
    public static int [] images={R.drawable.homeopathytwo,R.drawable.academicstwo,R.drawable.doctortwo,R.drawable.booking,R.drawable.profile,R.drawable.gallery,R.drawable.locationtwo,R.drawable.contact,R.drawable.help,R.drawable.feedtwo,R.drawable.notitwo};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            VERSION_CODE = pInfo.versionCode;
        }catch (PackageManager.NameNotFoundException e){

        }
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar);
        // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        startService(new Intent(MainActivity.this, FirebaseNotificationService.class));
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF,MODE_PRIVATE);
        tv_quote=(TextView)findViewById(R.id.tv_quote);
        tv_quote.setText(sharedPreferences.getString(Constants.LAST_QUOTE, "Daily Quotes will appear here."));
        Intent i = getIntent();
        String act = "null";
        act = i.getStringExtra("Act");
        showGuide();
        if(new CheckConnection().CheckConnection(this) && act.equals("Splash")) {
            new CheckForUpdate().execute();
            new UpdateQuote().execute();
        }
        FirebaseMessaging.getInstance().subscribeToTopic("noti");
       // initSlider
            sliderLayout = (SliderLayout) findViewById(R.id.slider);

        initSlider();
        sliderLayout.setDuration(5000);
        sliderLayout.setCurrentPosition(0);
        sliderLayout.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
        //Start Service
        startService(new Intent(this, NotificationListener.class));
		//Getting Components
        customGridAdapter = new CustomGridAdapter(categories, images);
        gridView_category=(GridView)findViewById(R.id.gridView_category);
        gridView_category.setDrawSelectorOnTop(true);
        gridView_category.setAdapter(customGridAdapter);
        btn_bookToken = (Button) findViewById(R.id.book_token_btn);
        btn_bookToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(builder != null){
                    builder.hide();
                }
                Intent i = new Intent(MainActivity.this, BookToken.class);
                startActivity(i);
            }
        });



    }

    public void initSlider(){

        DefaultSliderView textSliderView = new DefaultSliderView(this);
        textSliderView
                .image(R.drawable.all_doc_c)
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                .setOnSliderClickListener(MainActivity.this);
        sliderLayout.addSlider(textSliderView);

        textSliderView=new DefaultSliderView(this);
        textSliderView
                .image(R.drawable.office_one)
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                .setOnSliderClickListener(MainActivity.this);
        sliderLayout.addSlider(textSliderView);

        textSliderView=new DefaultSliderView(this);
        textSliderView
                .image(R.drawable.office_two)
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
        .setOnSliderClickListener(MainActivity.this);
        sliderLayout.addSlider(textSliderView);

        textSliderView=new DefaultSliderView(this);
        textSliderView
                .image(R.drawable.office_three)
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                .setOnSliderClickListener(MainActivity.this);
        sliderLayout.addSlider(textSliderView);

        textSliderView=new DefaultSliderView(this);
        textSliderView
                .image(R.drawable.office_four)
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                .setOnSliderClickListener(MainActivity.this);
        sliderLayout.addSlider(textSliderView);

        textSliderView=new DefaultSliderView(this);
        textSliderView
                .image(R.drawable.office_five)
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                .setOnSliderClickListener(MainActivity.this);
        sliderLayout.addSlider(textSliderView);

        textSliderView=new DefaultSliderView(this);
        textSliderView
                .image(R.drawable.office_six)
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                .setOnSliderClickListener(MainActivity.this);
        sliderLayout.addSlider(textSliderView);

        textSliderView=new DefaultSliderView(this);
        textSliderView
                .image(R.drawable.office_seven)
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                .setOnSliderClickListener(MainActivity.this);
        sliderLayout.addSlider(textSliderView);

        textSliderView=new DefaultSliderView(this);
        textSliderView
                .image(R.drawable.office_eight)
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                .setOnSliderClickListener(MainActivity.this);
        sliderLayout.addSlider(textSliderView);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, AboutAppActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
       Intent i = new Intent(MainActivity.this, GalleryActivity.class);

        startActivity(i);

    }



    public class CustomGridAdapter extends BaseAdapter{
        String[] results;
        Context ctx;
        int[] imgId;
        private LayoutInflater inflater=null;
        public CustomGridAdapter( String[] prgmNameList, int[] prgmImages) {
            // TODO Auto-generated constructor stub
            results=prgmNameList;
            ctx=getApplicationContext();
            imgId=prgmImages;
            inflater = ( LayoutInflater )ctx.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return results.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        public class Holder
        {
            TextView tv,coming_soon;
            ImageView img,img_notify;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Holder holder=new Holder();
            View rowView;
			//Getting Grid Items Components
            rowView = inflater.inflate(R.layout.grid_item, null);
            holder.tv=(TextView) rowView.findViewById(R.id.grid_item_text);
            holder.img=(ImageView) rowView.findViewById(R.id.grid_item_image);
            holder.img_notify=(ImageView)rowView.findViewById(R.id.notify_icon);
            holder.coming_soon = (TextView)rowView.findViewById(R.id.coming_soon);
            holder.tv.setText(results[position]);
            holder.img.setImageResource(imgId[position]);
           
			if(notifyVal && position == 4){
                 //   holder.img_notify.setVisibility(View.VISIBLE);
            }
            //Onclick listener on a item
			rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = holder.tv.getText().toString();
					//Check which item was clicked. Can also use switch
                   if (name.equals("Appointment Bookings")) {
                        Intent i = new Intent(MainActivity.this, BookingsActivity.class);
                       i.putExtra("already_book",false);
                        startActivity(i);
                    }else if (name.equals("My Profile")) {
                        Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(i);
                    }else if (name.equals("Gallery")) {
                        Intent i = new Intent(MainActivity.this, GalleryActivity.class);
                        startActivity(i);
                    }else if (name.equals("Know Your Doctors")) {
                        Intent i = new Intent(MainActivity.this, DoctorActivity.class);
                        startActivity(i);
                    }
                    else if (name.equals("Homoeopathy")) {
                        Intent i = new Intent(MainActivity.this, HomeopathyActivity.class);
                        startActivity(i);
                    }else if (name.equals("Help")) {
                        Intent i = new Intent(MainActivity.this, HelpActivity.class);
                        startActivity(i);
                    }else if (name.equals("Contact Us")) {
                        Intent i = new Intent(MainActivity.this, ContactActivity.class);
                        startActivity(i);
                    }
                    else if (name.equals("Feedback and Reviews")) {
                        Intent i = new Intent(MainActivity.this, ReviewActivity.class);
                        startActivity(i);
                    }
                    else if (name.equals("Location")) {
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=19.232079,72.853327(Mission To Cure)");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                    } else if (name.equals("Academics")) {
                       Intent i = new Intent(MainActivity.this, AcademicsActivity.class);
                       startActivity(i);
                   }else if(name.equals("Notifications")){

                       Intent i = new Intent(MainActivity.this, NotificationActivity.class);
                       startActivity(i);
                   }

                }
            });
            return rowView;
        }
    }

    @Override
    protected void onDestroy() {
        sliderLayout.stopAutoCycle();
        SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(Constants.ACTIVITY_STATE, false);
        ed.commit();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(Constants.ACTIVITY_STATE, true);
        ed.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(Constants.ACTIVITY_STATE, false);
        ed.commit();

    }

    @Override
    protected void onPause() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(notifyReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(notifyReceiver,
                new IntentFilter("notify"));
    }
    private BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            String sender = intent.getStringExtra("sender");
            String msg=intent.getStringExtra("message");
            String category = intent.getStringExtra("category");
            db.addMessage(sender,sharedPreferences.getString(Constants.EMAIL_ID,"none"), msg,category);
            notifyVal = true;
            customGridAdapter.notifyDataSetChanged();
        }
    };
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
	// Check if new version of app is available
    public class CheckForUpdate extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "http://missiontocure.com/android/update_check.php";
            try {
                URL requestUrl = new URL(url);
                URLConnection con = requestUrl.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb=new StringBuilder();
                int cp;
                while((cp=in.read())!=-1) {
                    sb.append((char) cp);
                }
                String json=sb.toString();
                return json;
            }catch (Exception e){
                Log.d("Exception",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                String[] arr = s.split("#");
                if (Integer.parseInt(arr[0])> VERSION_CODE) {
                    SweetAlertDialog sweet = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                    sweet.setCustomImage(R.drawable.update);
                    sweet.setTitleText("Update Available");
                    sweet.setConfirmText("Update");
                    sweet.setContentText(arr[1]);
                    if (arr[2].equals("true")) {
                        sweet.setCancelable(false);
                        sweet.showCancelButton(false);
                    } else {
                        sweet.showCancelButton(true);
                        sweet.setCancelText("Later");
                    }
                    sweet.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    });
                    sweet.show();
                }
            }
        }

    }
	
	//Updates the quote on the slider screen
    public class UpdateQuote extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "http://missiontocure.com/android/quote.php";
            try {
                URL requestUrl = new URL(url);
                URLConnection con = requestUrl.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb=new StringBuilder();
                int cp;
                while((cp=in.read())!=-1) {
                    sb.append((char) cp);
                }
                String json=sb.toString();
                return json;
            }catch (Exception e){
                Log.d("Exception",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s == null){

            }else {
                tv_quote.setText(s);
                sharedPreferences.edit().putString(Constants.LAST_QUOTE,s).commit();
            }
        }

    }
    //IMAGEGALLARY ACTIVITY
    public void showGuide(){
        boolean b = new ExtraFunctions().CheckForFirstTime(getApplicationContext(),Constants.MAIN_FIRST);
        if(b==true){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
           builder =  new ShowcaseView.Builder(this)
                    .setTarget(new ViewTarget(R.id.book_token_btn,this))
                    .setContentTitle("Book Appointment")
                    .setContentText("To book appointment directly from the app click here.")
                    .setStyle(R.style.CustomShowcaseTheme)
                    .hideOnTouchOutside()
                    .withNewStyleShowcase()
                    .replaceEndButton(R.layout.custom_button)
                    .build();

            builder.setButtonPosition(lp);
            sharedPreferences.edit().putBoolean(Constants.MAIN_FIRST,false).commit();
        }
    }
}

