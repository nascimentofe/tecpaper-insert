package com.tectoy.tecpaperinsert.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tectoy.tecpaperinsert.R;
import com.tectoy.tecpaperinsert.api.TecpaperRestClient;
import com.tectoy.tecpaperinsert.model.Product;
import com.tectoy.tecpaperinsert.utils.Constants;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import com.loopj.android.http.*;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CONTEXT_INCLUDE_CODE;

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
    Product product;
    Bitmap bitmap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        vNewProduct = (ViewGroup) inflater.inflate(R.layout.fragment_new_product, container, false);

        if(getArguments().getSerializable("editProduct") != null){
            product = (Product) getArguments().getSerializable("editProduct");
        }else{
            if (getArguments().getString("newProduct") != null){
                strProduct = getArguments().getString("newProduct");
            }
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_READ_STORAGE || requestCode == Constants.REQUEST_WRITE_STORAGE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                fileImage = getImage();
            }else{
                // usuario negou o acesso
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
            if(checkFields()){
                try {
                    String id = editCode.getText().toString();
                    String name = editName.getText().toString();
                    String desc = editDesc.getText().toString();
                    String price = editPrice.getText().toString().replace(",", ".");

                    TecpaperRestClient client = new TecpaperRestClient(getContext(), getActivity());
                    RequestParams params = new RequestParams();
                    params.put("id", id);
                    params.put("name", name);
                    params.put("description", desc);
                    if(product != null){
                        params.put("update", "true");
                    }
                    params.put("price", Double.valueOf(price));
                    params.put("image", new FileInputStream(fileImage), fileImage.getName());

                    final ProgressDialog dialog = new ProgressDialog(getContext());
                    dialog.setMessage((product != null) ? "Atualizando produto..." : "Cadastrando produto...");
                    dialog.setIndeterminate(false);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    dialog.show();

                    if (product != null) {
                        client.updateProduct(dialog, params);
                    } else {
                        client.postProduct(dialog, params);
                    }

                    deleteImageOfDevice(fileImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
    }

    private void initViews() {
        editCode = (EditText) vNewProduct.findViewById(R.id.editNewProduct_code);
        editCode.setEnabled(false);
        imgNewProduct = (CircleImageView) vNewProduct.findViewById(R.id.img_new_product);
        editName = (EditText) vNewProduct.findViewById(R.id.editNewProduct_name);
        editDesc = (EditText) vNewProduct.findViewById(R.id.editNewProduct_desc);
        editPrice = (EditText) vNewProduct.findViewById(R.id.editNewProduct_price);
        btnEnviar = (TextView) vNewProduct.findViewById(R.id.btnEnviar);

        if (product != null){
            editCode.setText("" + product.getId());
            prepareImage(imgNewProduct);
            editName.setText(product.getName());
            editDesc.setText(product.getDesc());
            editPrice.setText("" + product.getValue());
            btnEnviar.setText("ATUALIZAR");
        }else{
            if (strProduct != null){
                editCode.setText(strProduct);
            }
        }
    }

    private void prepareImage(CircleImageView imgNewProduct) {
        String link = "http://tecpaper.tk/" + product.getImage();

        Picasso.get()
                .load(link)
                .into(imgNewProduct, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_WRITE_STORAGE);
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_READ_STORAGE);
                        }else{
                            fileImage = getImage();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("##ERRO", e.getMessage());
                    }
                });
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

    private File getImage(){

       bitmap = ((BitmapDrawable) imgNewProduct.getDrawable()).getBitmap();

       String time = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault())
               .format(System.currentTimeMillis());
       File path = Environment.getExternalStorageDirectory();
       File dir = new File(path + "/DCIM");
       dir.mkdirs();
       String imgName = time + ".PNG";
       File file = new File(dir, imgName);
       OutputStream out;

       try{
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
       }catch (Exception e){
        e.printStackTrace();
       }

       return file;

    }

    private boolean checkFields(){
        if(!editCode.getText().toString().equals("")){
            if(!editName.getText().toString().equals("")){
                if(!editDesc.getText().toString().equals("")){
                    if(!editPrice.getText().toString().equals("")){
                        if (fileImage != null){
                            return true;
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

        return false;
    }

    private void deleteImageOfDevice(File fileImage){
        if (fileImage.exists()){
            fileImage.delete();
            if(fileImage.exists()){
                try{
                    fileImage.getCanonicalFile().delete();
                    if(fileImage.exists()){
                        getContext().deleteFile(fileImage.getName());
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }

    }

}
