package com.b2infosoft.addley.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by rajesh on 3/11/2016.
 */

public class CouponRecycleViewAdapter extends RecyclerView.Adapter<CouponRecycleViewAdapter.CustomViewHolder>{
    private List<Coupons> offerItems;
    private Context context;
    public CouponRecycleViewAdapter(Context context, List<Coupons> offerItems) {
        this.offerItems = offerItems;
        this.context = context;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_coupon_layout,null);
        CustomViewHolder customViewHolder=new CustomViewHolder(view);
        return customViewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final Coupons offerItem = offerItems.get(position);
        Picasso.with(context).load(ServerPath.getCouponLogoPath(offerItem.getCouponImage()))
                .error(R.drawable.image_not_found)
                .placeholder(R.drawable.image_not_found)
                .into(holder.logo);
        holder.name.setText(offerItem.getCouponOfferTitle());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (com.b2infosoft.addley.global.Login.isLogin(context)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(offerItem.getCouponName());
                    builder.setMessage("COUPON CODE : " + offerItem.getCouponOfferCode());
                    builder.setPositiveButton("COPY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            copyToClipboard(offerItem.getCouponOfferCode());
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    context.startActivity(new Intent(context.getApplicationContext(), Login.class));
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return (null != offerItems ? offerItems.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected ImageView logo;
        protected TextView name;
        protected Button button;
        public CustomViewHolder(View itemView) {
            super(itemView);
            this.logo =(ImageView) itemView.findViewById(R.id.best_offer_company_logo);
            this.name = (TextView) itemView.findViewById(R.id.best_offer_company_name);
            this.button = (Button)itemView.findViewById(R.id.best_offer_action_get_offer_button);
        }
    }
    public void copyToClipboard(String copyText) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(copyText);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)context.
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