package com.tectoy.tecpaperinsert.list;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.tectoy.tecpaperinsert.R;
import com.tectoy.tecpaperinsert.model.Product;

import java.util.ArrayList;

/**
 * @company TECTOY
 * @department development and support
 * @author nascimentofe
 *
 */

public class ProductListAdapter extends ArrayAdapter<Product> {

    public ProductListAdapter(Activity context, ArrayList<Product> products) {
        super(context, R.layout.item_product, products);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View vList = convertView;
        if(vList == null){
            vList = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_product,
                    parent,
                    false
            );
        }

        Product product = getItem(position);

        ImageView img = vList.findViewById(R.id.img_item_product);
        TextView nome = vList.findViewById(R.id.nome_item_product);
        TextView valor = vList.findViewById(R.id.valor_item_product);

        if(product.getImage().equals("") || product.getImage().equals("imagem.jpg")){
            // NAO HÁ IMAGEM NO PRODUTO
            Picasso.get().load("https://img.icons8.com/dotty/2x/id-not-verified.png").into(img);
        }else{
            Picasso.get().load("http://tecpaper.tk/" + product.getImage()).into(img);
        }
        nome.setText(product.getName());
        valor.setTextColor(ContextCompat.getColor(getContext(), R.color.ic_launcher_background));
        valor.setText("R$ " + product.getValue());

        return vList;
    }
}
