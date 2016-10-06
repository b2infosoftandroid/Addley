package com.b2infosoft.addley.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.SearchCoupon;
import com.b2infosoft.addley.global.ServerPath;
import com.b2infosoft.addley.lazyimageloading.ImageLoader;
import com.b2infosoft.addley.model.CouponItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerCouponAdapter extends RecyclerView.Adapter<RecyclerCouponAdapter.OfferVH> implements RecyclerOnItemClickListener {
    private Context context;
    private List<CouponItem> data;
    private ImageLoader imageLoader;
    public RecyclerCouponAdapter(Context context) {
        this.context = context;
    }

    public RecyclerCouponAdapter(List<CouponItem> data) {
        this.data = data;
    }

    public RecyclerCouponAdapter(Context context, List<CouponItem> data) {
        this.context = context;
        this.data = data;
        //imageLoader = new ImageLoader(context);
    }

    @Override
    public OfferVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_dashboard_top_story_item_layout, parent, false);
        return new OfferVH(view, this);
    }

    @Override
    public void onBindViewHolder(OfferVH holder, final int position) {
        final CouponItem offerItem = data.get(position);


        Picasso.with(context).load(ServerPath.getCouponLogoPath(offerItem.getCompanyImage()))
                .error(R.drawable.image_not_found)
                .placeholder(R.drawable.image_not_found)
                .into(holder.logo);

        //System.out.println(ServerPath.getCouponLogoPath(offerItem.getCompanyImage()));
        //imageLoader.DisplayImage(ServerPath.getCouponLogoPath(offerItem.getCompanyImage()),holder.logo);
        holder.logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CouponItem item = data.get(position);
                Intent intent = new Intent(context, SearchCoupon.class);
                intent.putExtra(Tag.COUPON_COMPANY_ID, item.getCompanyId());
                context.startActivity(intent);
            }
        });
        holder.name.setText(offerItem.getCompanyCount() + "\nOFFERS");
        //holder.name.setText(offerItem.getCompanyCount() + "\n"+offerItem.getCompanyId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onItemClicked(View view, int position) {
        //Toast.makeText(view.getContext(), "Clicked position: " + position, Toast.LENGTH_SHORT).show();
        //this.data.remove(position);
        //notifyItemRemoved(position);
    }

    public void add(List<CouponItem> items) {
        int previousDataSize = this.data.size();
        this.data.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }

    public static class OfferVH extends RecyclerView.ViewHolder {
        protected ImageView logo;
        protected TextView name;
        public OfferVH(View view, final RecyclerOnItemClickListener recyclerOnItemClickListener) {
            super(view);
            this.logo =(ImageView) itemView.findViewById(R.id.top_story_offer_image);
            this.name = (TextView) itemView.findViewById(R.id.top_story_offer_number);
        }
    }

}