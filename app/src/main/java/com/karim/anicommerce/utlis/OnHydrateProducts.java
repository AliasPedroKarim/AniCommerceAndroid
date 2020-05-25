package com.karim.anicommerce.utlis;

import java.util.List;

public interface OnHydrateProducts<T> {

    public void onLoadProducts(List<T> tList);

}
