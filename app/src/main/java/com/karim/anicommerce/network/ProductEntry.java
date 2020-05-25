package com.karim.anicommerce.network;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.karim.anicommerce.ProductGridFragment;
import com.karim.anicommerce.R;
import com.karim.anicommerce.application.AniCommerceApplication;
import com.karim.anicommerce.application.Constants;
import com.karim.anicommerce.utlis.OnHydrateProducts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A product entry in the list of products.
 */
public class ProductEntry {
    private static final String TAG = ProductEntry.class.getSimpleName();

    public final String libelle;
    public final Uri dynamicUrl;
    public String magasin = null;
    public String url;
    public String prixHt;
    public final String description;
    public final String stock;
    public final ArrayList<LinkedTreeMap<String, Object>> images;
    public LinkedTreeMap<String, Object> idMagasin;
    public final ArrayList<LinkedTreeMap<String, Object>> associerCategories;

    public ProductEntry(
            String libelle,
            String dynamicUrl,
            String url,
            String prixHt,
            String description,
            String stock,
            ArrayList<LinkedTreeMap<String, Object>> images,
            LinkedTreeMap<String, Object> idMagasin,
            ArrayList<LinkedTreeMap<String, Object>> associerCategories) {

        this.libelle = libelle;
        this.dynamicUrl = Uri.parse(dynamicUrl);
        this.url = url != null ? url : Constants.PRODUCTS_IMAGE_UNAVAILABLE;
        this.prixHt = prixHt;
        this.description = description;
        this.stock = stock;

        this.images = images;
        this.idMagasin = idMagasin;
        this.associerCategories = associerCategories;

    }

    /**
     * Loads a raw JSON at R.raw.products and converts it into a list of ProductEntry objects
     */
    public static List<ProductEntry> initProductEntryList(Resources resources, final OnHydrateProducts<ProductEntry> productEntryOnHydrateProducts) {
        RequestQueue queueRequest = Volley.newRequestQueue(AniCommerceApplication.getAppContext());

        final Gson gson = new Gson();
        final Type productListType = new TypeToken<ArrayList<ProductEntry>>() {

        }.getType();

        InputStream inputStream = resources.openRawResource(R.raw.products);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int pointer;
            while ((pointer = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, pointer);
            }
        } catch (IOException exception) {
            Log.e(TAG, "Error writing/reading from the JSON file.", exception);
        } finally {
            try {
                inputStream.close();
            } catch (IOException exception) {
                Log.e(TAG, "Error closing the input stream.", exception);
            }
        }


        StringRequest request = new StringRequest(Request.Method.GET, Constants.PRODUCTS,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (productEntryOnHydrateProducts != null) {
                        productEntryOnHydrateProducts.onLoadProducts((ArrayList)gson.fromJson(response, productListType));
                    }
                    // if (gridFragment != null) {
                    //     gridFragment.buildRecyclerView((ArrayList)gson.fromJson(response, productListType));
                    // }
                }
            },
            new Response.ErrorListener() {
                @SuppressLint("ShowToast")
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AniCommerceApplication.getAppContext(), "Les Produits n'ont pas été trouvé.", Toast.LENGTH_SHORT);
                }
            });

        queueRequest.add(request);

        String jsonProductsString = writer.toString();

        return gson.fromJson(jsonProductsString, productListType);
    }
}