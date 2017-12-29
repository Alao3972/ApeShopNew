package com.example.andylao.apeshop;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditItem extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText editTitle;
    EditText editDescription;
    EditText editPrice;
    EditText editEmail;
    EditText editAddress;
    EditText editPostalCode;

    boolean spinnerList;

    int userId;
    int itemId;

    String title, description, category, email, address, postalCode, country, province;
    int price;

    Button updateBtn;

    boolean updateCheck;

    Spinner categorySpinner;
    Spinner countrySpinner;
    Spinner provinceSpinner;

    int counter;

    ArrayAdapter<CharSequence> adapter;

    DatabaseHelper dbHelper;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
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
        item = new Item();//Category Spinner
        categorySpinner = (Spinner) findViewById(R.id.edit_category_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.categoriesArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //country Spinner
        countrySpinner = (Spinner) findViewById(R.id.edit_country_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.countryArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Category Spinner
        categorySpinner = (Spinner) findViewById(R.id.edit_category_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.categoriesArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        provinceSpinner = (Spinner) findViewById(R.id.edit_province_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.provinceArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(adapter);
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        populateItem();
        updateClick();



    }

    private void updateClick() {
        updateBtn = (Button) findViewById(R.id.edit_btn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newTitle = editTitle.getText().toString();
                String newDescription = editDescription.getText().toString();
                String newCategory = categorySpinner.getSelectedItem().toString();
                String getprice = editPrice.getText().toString();
                int newPrice = Integer.parseInt(getprice);
                String newEmail = editEmail.getText().toString();
                String newAddress = editAddress.getText().toString();
                String newPostalCode = editPostalCode.getText().toString();
                String newCountry = countrySpinner.getSelectedItem().toString();
                String newProvince = provinceSpinner.getSelectedItem().toString();

                updateCheck = false;

                String updateId = Integer.toString(itemId);

                updateCheck = dbHelper.updateItem(updateId, newTitle, newDescription, newCategory, newPrice, newEmail, newAddress, newPostalCode, newCountry, newProvince );

                if (updateCheck){
                    Intent intent= new Intent(getBaseContext(),MyItem.class);
                    Toast.makeText(getBaseContext(),  "Update Success" + updateId, Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }

            }
        });
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

        editTitle = (EditText) findViewById(R.id.edit_title_txt);
        editDescription = (EditText) findViewById(R.id.edit_description_txt);
        editPrice = (EditText) findViewById(R.id.edit_price);
        editEmail = (EditText) findViewById(R.id.edit_email_txt);
        editAddress = (EditText) findViewById(R.id.edit_address_txt);
        editPostalCode = (EditText) findViewById(R.id.edit_postalCode_txt);

        editTitle.setText(title);
        editDescription.setText(description);
        editPrice.setText(""+price);
        editEmail.setText(email);
        editAddress.setText(address);
        editPostalCode.setText(postalCode);

        spinnerList = false;
        counter = 0;
        while (!spinnerList){
            categorySpinner.setSelection(counter);
            if(categorySpinner.getSelectedItem().toString().equals(category)){
                spinnerList = true;
                }
            else{
                counter++;
            }

        }
        spinnerList = false;
        counter = 0;
        while (!spinnerList){
            countrySpinner.setSelection(counter);
            if(countrySpinner.getSelectedItem().toString().equals(country)){
                spinnerList = true;
            }
            else{
                counter++;
            }

        }
        spinnerList = false;
        counter = 0;
        while (!spinnerList){
            provinceSpinner.setSelection(counter);
            if(provinceSpinner.getSelectedItem().toString().equals(province)){
                spinnerList = true;
            }
            else{
                counter++;
            }
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Back Button to home page
     * @param view
     */
    public void showItems(View view){
        Intent intent = new Intent(this, MyItem.class);
        startActivity(intent);
    }
}
