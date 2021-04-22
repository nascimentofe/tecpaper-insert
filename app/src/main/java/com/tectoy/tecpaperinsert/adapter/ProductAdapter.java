package com.tectoy.tecpaperinsert.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
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
    ArrayList<Product> listProducts;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public ProductAdapter(Context context, ArrayList<Product> products){
        mContext = context;
        listProducts = products;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new MyViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(listProducts.get(position).getId()));
        viewBinderHelper.closeLayout(String.valueOf(listProducts.get(position).getId()));

        holder.bind(listProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView txtName, txtPrice, txtEdit, txtDel, txtId, txtDesc;
        SwipeRevealLayout swipeRevealLayout;
        Context mContext;

        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            mContext = context;
            txtName = itemView.findViewById(R.id.txt_name_item);
            txtPrice = itemView.findViewById(R.id.txt_price_item);
            txtId = itemView.findViewById(R.id.txt_id_item);
            txtDesc = itemView.findViewById(R.id.txt_desc_item);
            img = itemView.findViewById(R.id.img_icon_item);

            swipeRevealLayout = itemView.findViewById(R.id.swipeLayout);
//            txtEdit = itemView.findViewById(R.id.txtEdit);
//            txtDel = itemView.findViewById(R.id.txtDelete);

        }

        public void bind(Product product){
            txtId.setText("#" + product.getId());
            txtName.setText(product.getName());
            txtDesc.setText(product.getDesc());
            txtPrice.setText("R$ " + product.getValue());

            if(product.getImage().equals("") || product.getImage().equals("imagem.jpg")){
                // NAO HÁ IMAGEM NO PRODUTO
                Picasso.get().load("https://img.icons8.com/dotty/2x/id-not-verified.png").into(img);
            }else{
                Picasso.get().load("http://tecpaper.tk/" + product.getImage()).into(img);
            }


//            // ACTIONS
//            txtEdit.setOnClickListener(v -> {
//                Toast.makeText(mContext, "Edit", Toast.LENGTH_LONG).show();
//            });
//
//            txtDel.setOnClickListener(v -> {
//                Toast.makeText(mContext, "Delete", Toast.LENGTH_LONG).show();
//            });
        }
    }
}