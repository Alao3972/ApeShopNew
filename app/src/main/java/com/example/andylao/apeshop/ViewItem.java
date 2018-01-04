package com.example.andylao.apeshop;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

public class ViewItem extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtTitle;
    TextView txtDescription;
    TextView txtPrice;
    TextView txtEmail;
    TextView txtProvince;
    Button mapBtn;
    Button emailBtn;
    Button backBtn;
    public Cursor imageCursor;
    DatabaseHelper dbHelper;

    String title, description, category, email, address, postalCode, country, province;
    int price, itemId;
    byte[] selectedImage;
    Bitmap currentImage;
    ImageView imageView;
    String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mapBtn = (Button) findViewById(R.id.viewMapBtn);
        emailBtn = (Button) findViewById(R.id.viewContactBtn);

        dbHelper = new DatabaseHelper(this);

        imageView = findViewById(R.id.view_image_view);

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo: 43.3903, -80.4032"));
                startActivity(intent);
            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("mailto:" + email)
                        .buildUpon()
                        .appendQueryParameter("subject", title)
                        .build();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(Intent.createChooser(emailIntent, title));
            }
        });
        Intent intent = getIntent();
        searchInput = intent.getExtras().getString("searchInput", " ");

    populateItem();

    }
    private void populateItem() {
        Intent itemIntent = getIntent();

        itemId = itemIntent.getIntExtra("itemId", 0);
        title = itemIntent.getExtras().getString("title", " ");
        description = itemIntent.getExtras().getString("description", " ");
        category = itemIntent.getExtras().getString("category", " ");
        price = itemIntent.getIntExtra("price", 0);
        email = itemIntent.getExtras().getString("email", " ");
        address = itemIntent.getExtras().getString("address", " ");
        postalCode = itemIntent.getExtras().getString("postalCode", " ");
        country = itemIntent.getExtras().getString("country", " ");
        province = itemIntent.getExtras().getString("province", " ");

        imageCursor = dbHelper.getSingleItem(itemId);
        while(imageCursor.moveToNext()){

            selectedImage = imageCursor.getBlob(11);
        }
        currentImage = convertByteToBitmap(selectedImage);
        imageView.setImageBitmap(currentImage);

        txtTitle = findViewById(R.id.titleView);
        txtDescription = findViewById(R.id.descriptionView);
        txtPrice = findViewById(R.id.priceView);
        txtEmail = findViewById(R.id.emailView);
        txtProvince = findViewById(R.id.provinceView);

        txtTitle.setText(title);
        txtDescription.setText(description);
        txtPrice.setText("$"+price);
        txtEmail.setText(email);
        txtProvince.setText(province);

    }
    public Bitmap convertByteToBitmap(byte[] byteArray)
    {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * Back Button to Search page
     */
    public void searchItems(View view){
        Intent intent = new Intent(this, SearchAd.class);
        intent.putExtra("searchInput", searchInput);
        startActivity(intent);
    }
}
