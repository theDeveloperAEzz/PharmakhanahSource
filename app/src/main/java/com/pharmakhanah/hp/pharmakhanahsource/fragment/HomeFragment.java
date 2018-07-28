package com.pharmakhanah.hp.pharmakhanahsource.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pharmakhanah.hp.pharmakhanahsource.R;
import com.pharmakhanah.hp.pharmakhanahsource.model.adapters.HomePostAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    TextView mTextViewNoPostsHome;
    RecyclerView mRecyclerViewPostsHome;
    HomePostAdapter mHomePostAdapter;
    public static FloatingActionButton fabHome;
    ArrayList postsHomeArrayList;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        fabHome.setImageResource(R.mipmap.ic_post);
        mTextViewNoPostsHome.setVisibility(View.INVISIBLE);
        postsHomeArrayList = new ArrayList();
        mHomePostAdapter = new HomePostAdapter(postsHomeArrayList);
        mRecyclerViewPostsHome.setNestedScrollingEnabled(false);
        mRecyclerViewPostsHome.setAdapter(mHomePostAdapter);
        mRecyclerViewPostsHome.setLayoutManager(new LinearLayoutManager(getContext()));
        if (mHomePostAdapter.getItemCount() == 0) {
            mTextViewNoPostsHome.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    void initView() {
        mTextViewNoPostsHome = rootView.findViewById(R.id.txt_no_posts_home);
        mRecyclerViewPostsHome = rootView.findViewById(R.id.recycle_posts_home);
        fabHome = rootView.findViewById(R.id.fab_home);
    }

}
