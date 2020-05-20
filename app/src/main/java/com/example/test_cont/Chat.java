package com.example.test_cont;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class Chat extends Fragment {

    private ViewPager viewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;


    public Chat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_chat, container, false);

        viewPager =v.findViewById(R.id.main_tabs_pager);
        myTabsAccessorAdapter=new TabsAccessorAdapter(getFragmentManager());
        viewPager.setAdapter(myTabsAccessorAdapter);

        myTabLayout=v.findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(viewPager);

        return v;
    }
}
