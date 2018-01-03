package com.example.andylao.apeshop;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditItem extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText editTitle;
    EditText editDescription;
    EditText editPrice;
    EditText editEmail;
    EditText editAddress;
    EditText editPostalCode;

    boolean spinnerList;

    int itemId;

    String title, description, category, email, address, postalCode, country, province;
    int price;

    byte[] selectedImage;
    byte[] newImage;

    Button updateBtn;
    Button deleteBtn;

    boolean updateCheck;
    boolean deleteCheck;

    Spinner categorySpinner;
    Spinner countrySpinner;
    Spinner provinceSpinner;

    int counter;

    ArrayAdapter<CharSequence> adapter;

    DatabaseHelper dbHelper;
    Button editImageBtn;
    ImageView editImageView;
    Bitmap currentImage;
    public Cursor imageCursor;
    Bitmap bitmap;
    File destination = null;
    InputStream inputStreamImg;
    String imgPath = null;
    int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    byte[] image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dbHelper = new DatabaseHelper(this);

        editImageView = findViewById(R.id.edit_image_view);
        editImageBtn = findViewById(R.id.edit_image_btn);
        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();

            }
        });

        //Category Spinner
        categorySpinner = findViewById(R.id.edit_category_spinner);
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
        countrySpinner = findViewById(R.id.edit_country_spinner);
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
        categorySpinner = findViewById(R.id.edit_category_spinner);
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
        deleteClick();


    }

    private void deleteClick() {
        deleteBtn = (Button) findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCheck = false;
                String deleteId = Integer.toString(itemId);
                deleteCheck = dbHelper.deleteItem(deleteId);
                if (deleteCheck){
                    Intent intent= new Intent(getBaseContext(),MyItem.class);
                    Toast.makeText(getBaseContext(),  "Delete Success" , Toast.LENGTH_LONG).show();
                    startActivity(intent);

                }

            }
        });
    }

    private void updateClick() {
        updateBtn = findViewById(R.id.edit_btn);
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
                newImage = convertImage(bitmap);

                updateCheck = false;

                String updateId = Integer.toString(itemId);

                updateCheck = dbHelper.updateItem(updateId, newTitle, newDescription, newCategory, newPrice, newEmail, newAddress, newPostalCode, newCountry, newProvince , newImage);

                if (updateCheck){
                    Intent intent= new Intent(getBaseContext(),MyItem.class);
                    Toast.makeText(getBaseContext(),  "Update Success", Toast.LENGTH_LONG).show();
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

        imageCursor = dbHelper.getSingleItem(itemId);

        while(imageCursor.moveToNext()){

            selectedImage = imageCursor.getBlob(11);
        }
        currentImage = convertByteToBitmap(selectedImage);

        editImageView.setImageBitmap(currentImage);

        editTitle = findViewById(R.id.edit_title_txt);
        editDescription = findViewById(R.id.edit_description_txt);
        editPrice = findViewById(R.id.edit_price);
        editEmail = findViewById(R.id.edit_email_txt);
        editAddress = findViewById(R.id.edit_address_txt);
        editPostalCode = findViewById(R.id.edit_postalCode_txt);

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

    /**
     * Back Button to home page
     */
    public void showItems(View view){
        Intent intent = new Intent(this, MyItem.class);
        startActivity(intent);
    }

    public Bitmap convertByteToBitmap(byte[] byteArray)
    {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }
    private byte[] convertImage(Bitmap b){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();

    }
    public void imageChooser(){

        ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, 100);
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(android.Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                //this
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error1", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error2", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                editImageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath.toString());
                editImageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



}
