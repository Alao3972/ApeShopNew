package com.example.andylao.apeshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;


public class MyItem extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //private static final String TAG = "ListDataActivity";

    DatabaseHelper dbHelper;

    int userId;
    int itemId;
    ListView listContent;
    public Cursor itemCursor;
    public ArrayList<String> itemList;
    public ArrayList<String> itemIdList;
    String title, description, category, email, address, postalCode, country, province;
    int price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);

        userLogin();

        listContent = findViewById(R.id.myItemsListView);
        listContent.setBackgroundColor(Color.WHITE);

        itemList = new ArrayList<>();
        itemIdList = new ArrayList<>();
        itemCursor = dbHelper.getItemList(userId);

        populateMyItems();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void populateMyItems() {

        if (itemCursor.getCount() == 0){
            Toast.makeText(getBaseContext(), "No Ads Posted" , Toast.LENGTH_LONG).show();
        }
        else{
            while(itemCursor.moveToNext()){
                itemList.add(itemCursor.getString(2));
                itemIdList.add(itemCursor.getString(0));
                title = itemCursor.getString(2);
                description = itemCursor.getString(3);
                category = itemCursor.getString(4);
                price = Integer.parseInt(itemCursor.getString(5));
                email = itemCursor.getString(6);
                address = itemCursor.getString(7);
                postalCode = itemCursor.getString(8);
                country = itemCursor.getString(9);
                province = itemCursor.getString(10);

                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
                listContent.setAdapter(listAdapter);

                listContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(MyItem.this, EditItem.class);
                        itemId = Integer.parseInt(itemIdList.get(position));
                        Toast.makeText(getBaseContext(), title, Toast.LENGTH_LONG).show();

                        intent.putExtra("itemId", itemId);
                        intent.putExtra("title", title);
                        intent.putExtra("description", description);
                        intent.putExtra("category", category);
                        intent.putExtra("price", price);
                        intent.putExtra("email", email);
                        intent.putExtra("address", address);
                        intent.putExtra("postalCode", postalCode);
                        intent.putExtra("country", country);
                        intent.putExtra("province", province);

                        startActivity(intent);

                    }
                });

            }
        }
    }

//     lv.setOnItemClickListener(new OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position,
//        long id) {
//            Intent intent = new Intent(MainActivity.this, SendMessage.class);
//            String message = "abc";
//            intent.putExtra(EXTRA_MESSAGE, message);
//            startActivity(intent);
//        }
//    });

    public void userLogin(){

        SharedPreferences preferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userId = preferences.getInt("userId", 0);

        Toast.makeText(getBaseContext(), Integer.toString(userId) , Toast.LENGTH_LONG).show();

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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
