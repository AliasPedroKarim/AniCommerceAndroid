package com.karim.anicommerce.staggeredgridlayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.karim.anicommerce.ProductGridFragment;
import com.karim.anicommerce.R;
import com.karim.anicommerce.network.ImageRequester;
import com.karim.anicommerce.network.ProductEntry;

import java.util.List;

/**
 * Adapter used to show an asymmetric grid of products, with 2 items in the first column, and 1
 * item in the second column, and so on.
 */
public class StaggeredProductCardRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredProductCardViewHolder> {

    private List<ProductEntry> productList;
    private ImageRequester imageRequester;

    private Fragment fragment;

    public StaggeredProductCardRecyclerViewAdapter(List<ProductEntry> productList, Fragment fragment) {
        this.productList = productList;
        imageRequester = ImageRequester.getInstance();

        this.fragment = fragment;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3;
    }

    @NonNull
    @Override
    public StaggeredProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.shr_staggered_product_card_first;
        if (viewType == 1) {
            layoutId = R.layout.shr_staggered_product_card_second;
        } else if (viewType == 2) {
            layoutId = R.layout.shr_staggered_product_card_third;
        }

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new StaggeredProductCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull StaggeredProductCardViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            ProductEntry product = productList.get(position);
            holder.productTitle.setText(product.libelle);
            holder.productPrice.setText(product.prixHt);
            imageRequester.setImageFromUrl(holder.productImage, product.url);
        }

        // Handle product click
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProductGridFragment) fragment).displayProduct(productList.get((Integer) v.getTag()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
