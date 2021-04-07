package com.tectoy.tecpaperinsert.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.EdgeEffectCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tectoy.tecpaperinsert.R;
import com.tectoy.tecpaperinsert.activity.NewProductActivity;
import com.tectoy.tecpaperinsert.utils.Constants;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static androidx.core.graphics.TypefaceCompatUtil.getTempFile;

public class NewProductFragment extends Fragment {

    CircleImageView imgNewProduct;
    String strProduct;
    ViewGroup vNewProduct;
    EditText editCode, editName, editDesc, editPrice;
    TextView btnEnviar;
    File fileImage;
    ProgressBar progressBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        vNewProduct = (ViewGroup) inflater.inflate(R.layout.fragment_new_product, container, false);
        strProduct = Objects.requireNonNull(getArguments()).getString("data");

        initViews();

        initActions();

        return vNewProduct;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imgNewProduct.setImageURI(result.getUri());
                fileImage = criarArquivo(result.getUri());

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File criarArquivo(Uri resultUri) {
        return new File(getRealPathFromURI(resultUri));
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void initActions() {
        imgNewProduct.setOnClickListener(view -> {
            initCamera();
        });
        btnEnviar.setOnClickListener(view -> {
            sendData();
        });
    }

    private void sendData() {
        if(!editCode.getText().toString().equals("")){
            String id = editCode.getText().toString();
            if(!editName.getText().toString().equals("")){
                String name = editName.getText().toString();
                if(!editDesc.getText().toString().equals("")){
                    String desc = editDesc.getText().toString();
                    if(!editPrice.getText().toString().equals("")){
                        String price = editPrice.getText().toString();
                        if (fileImage != null){
                            Ion.with(getContext())
                                    .load("")
                                    .uploadProgressBar(progressBar)
                                    .setMultipartParameter("id", id)
                                    .setMultipartParameter("name", name)
                                    .setMultipartParameter("desc", desc)
                                    .setMultipartParameter("price", price)
                                    .setMultipartFile("image", "image/*", fileImage)
                                    .asJsonObject()
                                    .setCallback((e, result) -> {

                                    });
                        }else{
                            // ERRO NA IMAGEM
                            Toast.makeText(getContext(), "Foto não encontrada", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        // ERRO NO EDIT PRICE
                        editPrice.setError("Dados inválidos");
                    }
                }else{
                    // ERRO NO EDIT DESC
                    editDesc.setError("Dados inválidos");
                }
            }else{
                // ERRO NO EDIT NAME
                editName.setError("Dados inválidos");
            }
        }else{
            // ERRO NO EDIT CODE
            editCode.setError("É preciso escanear um produto");
        }
    }

    private void initViews() {
        editCode = (EditText) vNewProduct.findViewById(R.id.editNewProduct_code);
        editCode.setText(strProduct);
        editCode.setEnabled(false);

        imgNewProduct = (CircleImageView) vNewProduct.findViewById(R.id.img_new_product);
        editName = (EditText) vNewProduct.findViewById(R.id.editNewProduct_name);
        editDesc = (EditText) vNewProduct.findViewById(R.id.editNewProduct_desc);
        editPrice = (EditText) vNewProduct.findViewById(R.id.editNewProduct_price);
        btnEnviar = (TextView) vNewProduct.findViewById(R.id.btnEnviar);
        progressBar = (ProgressBar) vNewProduct.findViewById(R.id.progressBarNewProduct);
    }

    private void initCamera(){
        if(getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            startCrop();
        }else{
            Toast.makeText(getContext(), "Não há camera neste dispositivo!", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCrop() {
        CropImage.activity()
                .start(getContext(), this);
    }
}
