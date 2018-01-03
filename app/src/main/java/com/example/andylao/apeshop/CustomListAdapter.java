package com.example.andylao.apeshop;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;

/**
 * Created by Andy Lao on 2018-01-02.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemTitle;
    private final byte[][] imageByte;
    private Bitmap image;

    public CustomListAdapter(Activity context, String[] itemTitle, byte[][] imageByte) {
        super(context, R.layout.item_list_view, itemTitle);

        this.context=context;
        this.itemTitle = itemTitle;
        this.imageByte=imageByte;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_list_view, null,true);

        TextView txtTitle = rowView.findViewById(R.id.item_title);
        ImageView imageView = rowView.findViewById(R.id.item_image_view);

        txtTitle.setText(itemTitle[position]);
        image = convertByteToBitmap(imageByte[position]);

        imageView.setImageBitmap(image);
        return rowView;

    };
    public Bitmap convertByteToBitmap(byte[] byteArray)
    {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }
}