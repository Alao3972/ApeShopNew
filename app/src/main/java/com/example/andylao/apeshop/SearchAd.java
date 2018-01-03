package com.example.andylao.apeshop;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchAd extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Spinner categorySpinner;
    ArrayAdapter<CharSequence> adapter;
    ListView listItem;
    public Cursor itemCursor;
    public Cursor singleItemCursor;
    public String[] itemList;
    public ArrayList<String> itemIdList;
    String title, description, category, email, address, postalCode, country, province;
    int price;
    int itemId;
    String searchInput;
    ListAdapter listAdapter;

    DatabaseHelper dbHelper;

    byte[] selectedByte;
    byte[][] imageArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        searchInput = intent.getExtras().getString("searchInput", " ");

        categorySpinner = (Spinner) findViewById(R.id.search_category_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.categoriesArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = categorySpinner.getSelectedItem().toString();
                if (!category.equals("Select")){
                    itemCursor = dbHelper.browseItem(category);
                    populateItemList();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        itemIdList = new ArrayList<>();
        listItem = findViewById(R.id.searchListView);
        listItem.setBackgroundColor(Color.BLACK);

        if (searchInput.equals(" ")){
            itemCursor = dbHelper.getItem();
            populateItemList();
        }
        else{
            itemCursor = dbHelper.searchItemList(searchInput);
            populateItemList();
        }
    }
    private void populateItemList() {
        int count = itemCursor.getCount();
        int counter;
        counter = 0;

        itemList = new String[count];
        imageArray = new byte[count][];

        if (count == 0){
            Toast.makeText(getBaseContext(), "No Ads Posted" , Toast.LENGTH_LONG).show();
            listItem.setAdapter(null);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "HI", Toast.LENGTH_LONG).show();
        }
        if(count != 0){
            Toast.makeText(this, "Bye", Toast.LENGTH_LONG).show();
            while(itemCursor.moveToNext()){
                itemList[counter] = itemCursor.getString(2);
                imageArray[counter] =  itemCursor.getBlob(11);
                itemIdList.add(itemCursor.getString(0));

                counter++;
            }
            CustomListAdapter listAdapter = new CustomListAdapter(this, itemList, imageArray);
            listItem.setAdapter(listAdapter);

            listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(getBaseContext(), ViewItem.class);
                    itemId = Integer.parseInt(itemIdList.get(position));

                    singleItemCursor = dbHelper.getSingleItem(itemId);

                    while(singleItemCursor.moveToNext()){
                        title = singleItemCursor.getString(2);
                        description = singleItemCursor.getString(3);
                        category = singleItemCursor.getString(4);
                        price = Integer.parseInt(singleItemCursor.getString(5));
                        email = singleItemCursor.getString(6);
                        address = singleItemCursor.getString(7);
                        postalCode = singleItemCursor.getString(8);
                        country = singleItemCursor.getString(9);
                        province = singleItemCursor.getString(10);
                    }

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
            case R.id.nav_log_out:
                Intent l= new Intent(this,MainActivity.class);
                stopService(new Intent(getBaseContext(), BackgroundService.class));
                startActivity(l);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
