package com.karim.anicommerce;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.internal.LinkedTreeMap;
import com.karim.anicommerce.network.ImageRequester;
import com.karim.anicommerce.network.ProductEntry;

public class ProductShowFragment extends Fragment {
    private ProductShowViewModel mViewModel;
    private ProductEntry productEntry = null;

    private final ImageRequester imageRequester;

    public ProductShowFragment() {
        this(null);
    }
    public ProductShowFragment(ProductEntry productEntry) {
        this.setProductEntry(productEntry);

        imageRequester = ImageRequester.getInstance();
    }

    public static ProductShowFragment newInstance() {
        return new ProductShowFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_show_fragment, container, false);

        ProductShowViewModel productShowViewModel = ViewModelProviders.of(this).get(ProductShowViewModel.class);

        if (this.getProductEntry() != null) {
            productShowViewModel.setProductShow(this.getProductEntry());
        }

        ProductEntry productEntry = productShowViewModel.getProductShow();

        if (productEntry != null) {
            TextView priceProduct = view.findViewById(R.id.priceProduct);
            TextView descriptionProduct = view.findViewById(R.id.descriptionProduct);
            TextView nameProduct = view.findViewById(R.id.nameProduct);
            TextView nameVendorProduct = view.findViewById(R.id.nameVendorProduct);
            NetworkImageView imageProduct = view.findViewById(R.id.imageProduct);

            priceProduct.setText(productEntry.prixHt);
            descriptionProduct.setText(productEntry.description);
            nameProduct.setText(productEntry.libelle);
            nameVendorProduct.setText("de " + productEntry.magasin);
            imageRequester.setImageFromUrl(imageProduct, productEntry.url);

            if (productEntry.associerCategories != null && productEntry.associerCategories.size() > 0) {
                for (LinkedTreeMap<String, Object> map : productEntry.associerCategories) {
                    View chipView = inflater.inflate(R.layout.chip_template, container, false);

                    Chip chip = ((Chip) chipView.findViewById(R.id.chip_template));

                    chip.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);

                    chip.setText((String) ((LinkedTreeMap<String, Object>) map.get("idCategorie")).get("libelle"));

                    ((ChipGroup) view.findViewById(R.id.chip_list_categorie)).addView(chip);
                }
            }

            View tootlBar = view.findViewById(R.id.toolbar_close_product_single);

            tootlBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), false);
                }
            });
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProductShowViewModel.class);

    }

    public ProductEntry getProductEntry() {
        return productEntry;
    }

    public void setProductEntry(ProductEntry productEntry) {
        this.productEntry = productEntry;
    }
}
