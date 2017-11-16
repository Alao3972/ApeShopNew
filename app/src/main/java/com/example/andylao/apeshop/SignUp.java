package com.example.andylao.apeshop;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class SignUp extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText address;
    EditText postalCode;
    Button signUpButton;

    Spinner countrySpinner;
    Spinner provinceSpinner;
    ArrayAdapter<CharSequence> adapter;

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;

    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DatabaseHelper(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //country Spinner
        countrySpinner = (Spinner) findViewById(R.id.sign_up_country_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.countryArray,android.R.layout.simple_spinner_item);
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

        //province Spinner
        provinceSpinner = (Spinner) findViewById(R.id.sign_up_province_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.provinceArray,android.R.layout.simple_spinner_item);
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

        // getting input values
        firstName = (EditText)findViewById(R.id.sign_up_first_name_txt);
        lastName = (EditText)findViewById(R.id.sign_up_last_name_txt);
        email = (EditText)findViewById(R.id.sign_up_email_txt);
        password = (EditText)findViewById(R.id.sign_up_password_txt);
        address = (EditText)findViewById(R.id.sign_up_address_txt);
        postalCode = (EditText)findViewById(R.id.sign_up_code_txt);

        //when sign up clicked
        //validate inputs
        signUpButton = (Button)findViewById(R.id.sign_up_btn);
        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String newFirstName = firstName.getText().toString();
                String newLastName = lastName.getText().toString();
                String newEmail = email.getText().toString();
                String newPassword = password.getText().toString();
                String newAddress = address.getText().toString();
                String newPostalCode = postalCode.getText().toString();
                String newCountry = countrySpinner.getSelectedItem().toString();
                String newProvince = provinceSpinner.getSelectedItem().toString();

                if (newFirstName.length()==0){
                    firstName.requestFocus();
                    firstName.setError("First Name cannot be Empty");
                }
                else if (newLastName.length()==0){
                    lastName.requestFocus();
                    lastName.setError("Last Name cannot be empty");
                }
                else if (newEmail.length()==0){
                    email.requestFocus();
                    email.setError("Email cannot be empty");
                }
                else if (newPassword.length()==0){
                    password.requestFocus();
                    password.setError("Password cannot be empty");
                }
                else if (newAddress.length()==0){
                    address.requestFocus();
                    address.setError("Address cannot be empty");
                }
                else if (newPostalCode.length()==0){
                    postalCode.requestFocus();
                    postalCode.setError("Postal/Zip Code cannot be empty");
                }
                else if (newCountry.matches("Select")){
                    countrySpinner.requestFocus();
                    Toast.makeText(getBaseContext(),"Country Not Selected", Toast.LENGTH_LONG).show();
                }
                else if (newProvince.matches("Select")){
                    provinceSpinner.requestFocus();
                    Toast.makeText(getBaseContext(),"Province Not Selected", Toast.LENGTH_LONG).show();
                }
                else{
                    // adds user to database after validation
                    dbHelper.addUser(newFirstName, newLastName, newEmail, newPassword, newAddress, newPostalCode, newCountry, newProvince);
                    startActivity(new Intent(SignUp.this, LogIn.class));
                    Toast.makeText(getBaseContext(), "Sign Up Successful!", Toast.LENGTH_LONG).show();
                }
            }
        });
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

        int id=item.getItemId();
        switch (id) {

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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Back Button to home page
     * @param view
     */
    public void showHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
// hide log in and sign up options after login sucessful

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        hideItem();
//    }
//
//
//    private void hideItem()
//    {
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        Menu nav_Menu = navigationView.getMenu();
//        nav_Menu.findItem(R.id.nav_settings).setVisible(false);
//    }
