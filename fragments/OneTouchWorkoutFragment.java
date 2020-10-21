package icn.proludic.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import icn.proludic.R;
import icn.proludic.misc.Utils;


public class OneTouchWorkoutFragment extends Fragment {

    RecyclerView recyclerView;
    Utils utils;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_touch_workout, container, false);
        utils = new Utils(getActivity());
        return view;
    }


}
