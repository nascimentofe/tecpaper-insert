package com.tectoy.tecpaperinsert.list;

import android.app.Activity;
import android.content.Context;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tectoy.tecpaperinsert.R;
import com.tectoy.tecpaperinsert.model.Product;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @company TECTOY
 * @department development and support
 * @author nascimentofe
 *
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<Product> mList;

    public ProductAdapter(Context context, ArrayList<Product> list){
        mContext = context;
        mList = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Product product = mList.get(position);

        holder.txtName.setText(product.getName());
        holder.txtName.setText("R$ " + product.getValue());

        if(product.getImage().equals("") || product.getImage().equals("imagem.jpg")){
        // NAO HÁ IMAGEM NO PRODUTO
            Picasso.get().load("https://img.icons8.com/dotty/2x/id-not-verified.png").into(holder.img);
        }else{
            Picasso.get().load("http://tecpaper.tk/" + product.getImage()).into(holder.img);
        }
        holder.txtName.setText(product.getName());
        holder.txtPrice.setTextColor(ContextCompat.getColor(mContext, R.color.ic_launcher_background));
        holder.txtPrice.setText("R$ " + product.getValue());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView txtName, txtPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_name_item);
            txtPrice = itemView.findViewById(R.id.txt_price_item);
            img = itemView.findViewById(R.id.img_icon_item);
        }
    }
}