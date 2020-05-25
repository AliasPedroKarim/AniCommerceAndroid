package com.karim.anicommerce;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.internal.LinkedTreeMap;
import com.karim.anicommerce.application.Constants;
import com.karim.anicommerce.network.ProductEntry;
import com.karim.anicommerce.staggeredgridlayout.StaggeredProductCardRecyclerViewAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductGridFragment extends Fragment {
    private View viewInflate;

    private static ProductGridFragment instance;

    public static ProductGridFragment getInstance() {
        if (ProductGridFragment.instance == null) {
            return ProductGridFragment.instance = new ProductGridFragment();
        }

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ProductGridFragment.instance = this;

        // Inflate the layout for this fragment with the ProductGrid theme
        viewInflate = inflater.inflate(R.layout.shr_product_grid_fragment, container, false);

        // Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewInflate.findViewById(R.id.product_grid).setBackgroundResource(R.drawable.shr_product_grid_background_shape);
        }

        // Set up the tool bar
        setUpToolbar(viewInflate);

        ProductGridViewModel productGridViewModel = ViewModelProviders.of(this).get(ProductGridViewModel.class);

        // ProductEntry.initProductEntryList(getResources(), this)
        // Set up the RecyclerView

        productGridViewModel.getProducts().observe(this, new Observer<List<ProductEntry>>() {
            @Override
            public void onChanged(List<ProductEntry> productEntries) {
                buildRecyclerView(productEntries);
            }
        });

        return viewInflate;
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_close_product_single);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                getContext(),
                view.findViewById(R.id.product_grid),
                new AccelerateDecelerateInterpolator(),
                getContext().getResources().getDrawable(R.drawable.shr_menu), // Menu open icon
                getContext().getResources().getDrawable(R.drawable.shr_close_menu))); // Menu close icon

        // Elements backdrop
        MaterialButton nextButton = view.findViewById(R.id.backdrop_item_login);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationHost) getActivity()).navigateTo(new LoginFragment(), true); // Navigate to the next Fragment
            }
        });

    }

    private void prettifyData(List<ProductEntry> productEntries) {
        if (productEntries != null) {
            for (ProductEntry productEntry : productEntries) {
                if (productEntry.images != null && productEntry.images.size() > 0) {
                    productEntry.url = (String) ((LinkedTreeMap) productEntry.images.get(0).get("idImage")).get("cheminImage");
                }else{
                    productEntry.url = Constants.PRODUCTS_IMAGE_UNAVAILABLE;
                }

                productEntry.prixHt = productEntry.prixHt + "€";
                if (productEntry.idMagasin != null) {
                    productEntry.magasin = (String) productEntry.idMagasin.get("nom");
                }
            }
        }
    }

    public void buildRecyclerView(List<ProductEntry> productEntries) {
        prettifyData(productEntries);

        if (viewInflate != null) {
            RecyclerView recyclerView = viewInflate.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position % 3 == 2 ? 2 : 1;
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
            StaggeredProductCardRecyclerViewAdapter adapter = new StaggeredProductCardRecyclerViewAdapter(productEntries, this);
            recyclerView.setAdapter(adapter);
            int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_staggered_product_grid_spacing_large);
            int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_staggered_product_grid_spacing_small);
            recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
        }
    }

    public void displayProduct(ProductEntry productEntry) {

        ((NavigationHost) getActivity()).navigateTo(new ProductShowFragment(productEntry), true);
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater menuInflater) {
        // Fix Rotate app
        menu.clear();
        menuInflater.inflate(R.menu.shr_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

}
