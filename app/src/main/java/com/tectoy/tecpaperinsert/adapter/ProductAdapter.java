package com.tectoy.tecpaperinsert.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.squareup.picasso.Picasso;
import com.tectoy.tecpaperinsert.R;
import com.tectoy.tecpaperinsert.activity.NewProductActivity;
import com.tectoy.tecpaperinsert.api.TecpaperRestClient;
import com.tectoy.tecpaperinsert.fragment.NewProductFragment;
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
    Activity mActivity;
    ArrayList<Product> listProducts;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    TecpaperRestClient mClient;

    public ProductAdapter(Context context, Activity activity, ArrayList<Product> products){
        mActivity = activity;
        mContext = context;
        listProducts = products;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_product, parent, false);
        mClient = new TecpaperRestClient(mContext, mActivity);
        return new MyViewHolder(view, mContext, mActivity, mClient);
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
        TextView txtName, txtPrice, txtId, txtDesc;
        ImageButton btnEditar, btnExcluir;
        SwipeRevealLayout swipeRevealLayout;
        Context mContext;
        Activity mActivity;
        TecpaperRestClient mClient;

        public MyViewHolder(@NonNull View itemView, Context context, Activity activity, TecpaperRestClient client) {
            super(itemView);

            mContext = context;
            mActivity = activity;
            txtName = itemView.findViewById(R.id.txt_name_item);
            txtPrice = itemView.findViewById(R.id.txt_price_item);
            txtId = itemView.findViewById(R.id.txt_id_item);
            txtDesc = itemView.findViewById(R.id.txt_desc_item);
            img = itemView.findViewById(R.id.img_icon_item);

            mClient = client;
            btnEditar = itemView.findViewById(R.id.imgBtnItemProduct_edit);
            btnExcluir = itemView.findViewById(R.id.imgBtnItemProduct_delete);

            swipeRevealLayout = itemView.findViewById(R.id.swipeLayout);
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
                String link = "http://tecpaper.tk/" + product.getImage();
                Picasso.get().load(link).into(img);
            }

//            // ACTIONS
            btnEditar.setOnClickListener(v -> {
                NewProductFragment newProductFragment = new NewProductFragment();
                FragmentManager fm = ((FragmentActivity) mActivity).getSupportFragmentManager();
                Bundle data = new Bundle();
                data.putSerializable("editProduct", product);
                newProductFragment.setArguments(data);
                fm.beginTransaction()
                        .setCustomAnimations(
                                R.anim.right_to_left, R.anim.exit_rigth_to_left,
                                R.anim.left_to_right, R.anim.exit_left_to_rigth
                        )
                        .addToBackStack("NewProduct")
                        .replace(R.id.fragmentContainer, newProductFragment, "NewProduct")
                        .commit();
            });

            btnExcluir.setOnClickListener(v -> {
                final ProgressDialog dialog = new ProgressDialog(mContext);
                dialog.setMessage("Deletando produto...");
                dialog.setIndeterminate(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();

                mClient.deleteProduct(product.getId(), dialog);
            });
        }
    }
}