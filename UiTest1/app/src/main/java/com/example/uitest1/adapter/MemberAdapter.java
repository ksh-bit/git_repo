package com.example.uitest1.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uitest1.R;
import com.example.uitest1.helper.PhotoHelper;
import com.example.uitest1.model.Member;

import java.util.List;

public class MemberAdapter extends ArrayAdapter<Member> {
    Activity activity;
    int resource;
    Bitmap bmp;

    public MemberAdapter(Context context, int resource, List<Member> objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        Member item = getItem(position);
        if (item != null) {
            ImageView imageViewImage = convertView.findViewById(R.id.imageViewImage);
            TextView textViewName = convertView.findViewById(R.id.textViewName);
            TextView textViewEmail = convertView.findViewById(R.id.textViewEmail);
            TextView textViewTel = convertView.findViewById(R.id.textViewTel);
            TextView textViewAddr = convertView.findViewById(R.id.textViewAddr);

            if (bmp != null && bmp.isRecycled()) {
                bmp.recycle();
                bmp = null;
            }
            if (!item.getImage().equals("")) {
                bmp = PhotoHelper.getInstance().getThumb(activity, item.getImage());
                imageViewImage.setImageBitmap(null);
                imageViewImage.setImageBitmap(bmp);
            } else {
                imageViewImage.setImageBitmap(null);
            }

            textViewName.setText(item.getName());
            textViewEmail.setText(item.getEmail());
            textViewTel.setText(item.getTel());
            textViewAddr.setText(item.getAddr());
        }

        return convertView;
    }
}
