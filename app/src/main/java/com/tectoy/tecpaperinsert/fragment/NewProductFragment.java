package com.tectoy.tecpaperinsert.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.tectoy.tecpaperinsert.R;
import com.tectoy.tecpaperinsert.api.TecpaperRestClient;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import com.loopj.android.http.*;

import org.json.JSONException;

import static android.app.Activity.RESULT_OK;

/**
 * @company TECTOY
 * @department development and support
 * @author nascimentofe
 *
 */

public class NewProductFragment extends Fragment {

    CircleImageView imgNewProduct;
    String strProduct;
    ViewGroup vNewProduct;
    EditText editCode, editName, editDesc, editPrice;
    TextView btnEnviar;
    File fileImage;

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
                imgNewProduct.setBorderColor(ContextCompat.getColor(getContext(), R.color.ic_launcher_background));

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
                        String price = editPrice.getText().toString().replace(",", ".");
                        if (fileImage != null){
                            try {
                                TecpaperRestClient client = new TecpaperRestClient(getContext(), getActivity());
                                RequestParams params = new RequestParams();
                                params.put("id", id);
                                params.put("name", name);
                                params.put("description", desc);
                                params.put("price", Double.valueOf(price));
                                params.put("image", new FileInputStream(fileImage), fileImage.getName());

                                final ProgressDialog dialog = new ProgressDialog(getContext());
                                dialog.setMessage("Cadastrando produto...");
                                dialog.setIndeterminate(false);
                                dialog.setCanceledOnTouchOutside(true);
                                dialog.setCancelable(true);
                                dialog.show();

                                client.postProduct(dialog, params);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else{
                            // ERRO NA IMAGEM
                            imgNewProduct.setBorderColor(ContextCompat.getColor(getContext(), R.color.red));
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
