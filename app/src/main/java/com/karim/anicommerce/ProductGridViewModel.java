package com.karim.anicommerce;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.karim.anicommerce.network.ProductEntry;
import com.karim.anicommerce.utlis.OnHydrateProducts;

import java.util.List;

public class ProductGridViewModel extends ViewModel implements OnHydrateProducts<ProductEntry> {

    private MutableLiveData<List<ProductEntry>> products;

    public ProductGridViewModel() {
        this.products = new MutableLiveData<List<ProductEntry>>();

        this.products.setValue(ProductEntry.initProductEntryList(MainActivity.getMainActivityResources(), this));
    }

    @Override
    public void onLoadProducts(List<ProductEntry> productEntries) {
        this.products.setValue(productEntries);
    }

    public MutableLiveData<List<ProductEntry>> getProducts() {
        return products;
    }

    public void setProducts(MutableLiveData<List<ProductEntry>> products) {
        this.products = products;
    }

}
