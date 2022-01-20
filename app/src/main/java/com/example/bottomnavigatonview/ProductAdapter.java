package com.example.bottomnavigatonview;

/**
 * Created by karanjaswani on 1/12/18.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position) throws IOException;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener =  listener;
    }

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Product> productList;
    //getting the context and product list with constructor
    public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_all_foods, null);
        return new ProductViewHolder(view, (OnItemClickListener) mListener);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        Product product = productList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(product.getTitle());
        holder.textViewCategory.setText(String.valueOf(product.getCategory()));
        holder.textViewQuantity.setText(String.valueOf(product.getQuantity()));
        holder.textViewDateofPurchase.setText(String.valueOf(product.getDateOfPurchase()));


        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewCategory, textViewQuantity, textViewDateofPurchase;
        ImageView imageView, mDeleteImage;

        public ProductViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewDateofPurchase = itemView.findViewById(R.id.textViewDateofPurchase);
            imageView = itemView.findViewById(R.id.imageView);

            mDeleteImage = itemView.findViewById(R.id.image_delete);

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            try {
                                listener.onDeleteClick(position);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }

    }
}