package com.example.andylao.apeshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Button categoryBtn;

    EditText searchBar;
    String searchInput;
    ImageView imageView;

    User user;
    String URL = "https://image.flaticon.com/icons/png/512/194/194641.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //finds image view and execute the async task to download image
        imageView = (ImageView) findViewById(R.id.main_image);
        imageView.setTag(URL);
        new DownloadImageTask().execute(imageView);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        user = new User();

        // category btn click
        categoryBtn = findViewById(R.id.home_browse_btn);
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getBaseContext(),SearchAd.class);
                intent.putExtra("searchInput", " ");
                startActivity(intent);

            }
        });

        searchBar = findViewById(R.id.home_search_txt);
        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                //When enter is pressed the search input will be passed to the next activity
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    switch (i){
                        case KeyEvent.KEYCODE_ENTER:
                            Intent intent= new Intent(getBaseContext(),SearchAd.class);
                            searchInput = searchBar.getText().toString();
                            intent.putExtra("searchInput", searchInput);
                            startActivity(intent);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id=item.getItemId();
        switch (id){

            case R.id.nav_home:
                Intent h= new Intent(this,MainActivity.class);
                startActivity(h);

                break;
            case R.id.nav_sign_up:
                Intent i= new Intent(this,SignUp.class);
                startActivity(i);
                break;
            case R.id.nav_log_in:
                Intent g= new Intent(this,LogIn.class);
                startActivity(g);
                break;
            case R.id.nav_post_ad:
                Intent s= new Intent(this,PostAd.class);
                startActivity(s);
                break;
            case R.id.nav_my_items:
                Intent t= new Intent(this,MyItem.class);
                startActivity(t);
                break;
            case R.id.nav_log_out:
                Intent l= new Intent(this,MainActivity.class);
                stopService(new Intent(getBaseContext(), BackgroundService.class));
                startActivity(l);
                break;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * Post Ad page
     */
    public void postAd(View view){
        Intent intent = new Intent(this, PostAd.class);
        startActivity(intent);
    }
    // asynctask to download image
    public class DownloadImageTask extends AsyncTask<ImageView, Void, Bitmap> {

        ImageView imageView = null;

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            return download_Image((String)imageView.getTag());
        }
        // set imageview to image
        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
        // convert Url to bitmap
        private Bitmap download_Image(String url) {

            Bitmap bitmap =null;
            try{
                URL imgUrl = new URL(url);
                HttpURLConnection con = (HttpURLConnection)imgUrl.openConnection();
                InputStream inputStream = con.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                if (null != bitmap)
                    return bitmap;

            }catch(Exception e){}
            return bitmap;
        }
    }

}
