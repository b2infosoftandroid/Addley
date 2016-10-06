package com.b2infosoft.addley.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.b2infosoft.addley.Login;
import com.b2infosoft.addley.R;
import com.b2infosoft.addley.model.Coupons;
import com.b2infosoft.addley.global.ServerPath;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rajesh on 5/13/2016.
 */

public class CouponListAdapter extends BaseAdapter {
    private Context context;
    private List<Coupons> categories;

    public CouponListAdapter(Context context, List<Coupons> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.fragment_coupon_layout, null);
        final Coupons category = categories.get(position);
        ImageView icon = (ImageView) view.findViewById(R.id.best_offer_company_logo);
        Picasso.with(context)
                .load(ServerPath.getCouponLogoPath(category.getCouponImage()))
                .placeholder(R.drawable.image_not_found)
                .error(R.drawable.image_not_found)
                .into(icon);
        // Log.d("PATH", ServerPath.getCategoryIconPath(category.getImg()));
        TextView msg = (TextView) view.findViewById(R.id.best_offer_company_name);
        msg.setText(category.getCouponOfferTitle());
        Button getOffer = (Button) view.findViewById(R.id.best_offer_action_get_offer_button);
        getOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (com.b2infosoft.addley.global.Login.isLogin(context)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(category.getCouponName());
                    builder.setMessage("COUPON CODE : " + category.getCouponOfferCode());
                    builder.setPositiveButton("COPY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            copyToClipboard(category.getCouponOfferCode());
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    context.startActivity(new Intent(context.getApplicationContext(), Login.class));
                }
            }
        });
        Button visit_site = (Button) view.findViewById(R.id.best_offer_action_visit_site_button);
        visit_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 // old code              Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ServerPath.getVisitPath()));
               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(category.getCouponOfferUrl()));
                context.startActivity(intent);
            }
        });
        return view;
    }

    public void copyToClipboard(String copyText) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(copyText);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.
                    getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText("COUPON CODE", copyText);
            clipboard.setPrimaryClip(clip);
        }
        Toast toast = Toast.makeText(context.getApplicationContext(),
                "COUPON CODE COPIED", Toast.LENGTH_SHORT);
        toast.show();
        //displayAlert("Your OTP is copied");
    }
}