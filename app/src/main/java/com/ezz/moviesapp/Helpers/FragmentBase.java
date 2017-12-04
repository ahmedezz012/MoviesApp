package com.ezz.moviesapp.Helpers;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by samar ezz on 11/10/2017.
 */

public abstract class FragmentBase extends Fragment{

    protected abstract void setListeners();
    protected abstract void initializeViews(View view);
}
