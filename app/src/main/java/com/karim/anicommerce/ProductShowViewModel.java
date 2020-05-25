package com.karim.anicommerce;

import androidx.lifecycle.ViewModel;

import com.karim.anicommerce.network.ProductEntry;
import com.karim.anicommerce.utlis.OnHydrateProducts;

import java.util.List;

public class ProductShowViewModel extends ViewModel implements OnHydrateProducts<ProductEntry> {

    private ProductEntry productShow;

    public ProductShowViewModel() {
        this.productShow = null;
    }

    @Override
    public void onLoadProducts(List<ProductEntry> productEntries) {

    }

    public ProductEntry getProductShow() {
        return productShow;
    }

    public void setProductShow(ProductEntry productShow) {
        this.productShow = productShow;
    }
}
