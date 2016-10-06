package com.b2infosoft.addley.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.b2infosoft.addley.Login;
import com.b2infosoft.addley.R;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.model.OfferItem;
import com.b2infosoft.addley.global.ServerPath;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rajesh on 3/11/2016.
 */

public class OfferRecycleViewAdapter extends RecyclerView.Adapter<OfferRecycleViewAdapter.CustomViewHolder>{

    private List<OfferItem> offerItems;
    private Context context;

    public OfferRecycleViewAdapter(Context context, List<OfferItem> offerItems) {
        this.offerItems = offerItems;
        this.context = context;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_best_offer_layout,null);
        CustomViewHolder customViewHolder=new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final OfferItem offerItem = offerItems.get(position);
        Picasso.with(context).load(ServerPath.getLogoPath(offerItem.getLogo()))
                .error(R.drawable.image_not_found)
                .placeholder(R.drawable.image_not_found)
                .into(holder.logo);
        holder.name.setText(offerItem.getName());
        holder.discount.setText("Discount : "+ offerItem.getDiscount());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= null;
                if(com.b2infosoft.addley.global.Login.isLogin(context)){
                    String uri = offerItem.getOfferLink().concat("&aff_sub=").concat(com.b2infosoft.addley.global.Login.getValue(context, Tag.USER_ID));
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    if(intent!=null)
                        context.startActivity(intent);
                }else {
                    if (com.b2infosoft.addley.global.Login.isGuest(context) == 1) {
                        String uri = offerItem.getOfferLink().concat("&aff_sub=").concat(String.valueOf(com.b2infosoft.addley.global.Login.getGuestId()));
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        if(intent!=null)
                            context.startActivity(intent);
                    } else {
                        context.startActivity(new Intent(context,Login.class));
                        /*
                        */
                        /*
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                context.startActivity(new Intent(context, Login.class));
                            }
                        });
                        builder.setNeutralButton("CONTINUE AS GUEST", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                com.b2infosoft.dmr.rajesh.addley_1.global.Login.setGuest(context,Tag.CONTINUE_AS_GUEST_ACTIVE);
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                        */
                    }
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
        protected TextView discount;
        protected Button button;
        public CustomViewHolder(View itemView) {
            super(itemView);
            this.logo =(ImageView) itemView.findViewById(R.id.best_offer_company_logo);
            this.name = (TextView) itemView.findViewById(R.id.best_offer_company_name);
            this.discount = (TextView) itemView.findViewById(R.id.best_offer_discount);
            this.button = (Button)itemView.findViewById(R.id.best_offer_action_get_offer_button);
        }
    }
}