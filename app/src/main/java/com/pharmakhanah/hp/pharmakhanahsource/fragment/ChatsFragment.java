package com.pharmakhanah.hp.pharmakhanahsource.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pharmakhanah.hp.pharmakhanahsource.R;
import com.pharmakhanah.hp.pharmakhanahsource.model.adapters.ChatsItemAdapter;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {
    TextView mTextViewNoChats;
    RecyclerView mRecyclerViewChats;
    ChatsItemAdapter mChatsItemAdapter;
    ArrayList chatsArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);
        mTextViewNoChats = rootView.findViewById(R.id.txt_no_chats);
        mTextViewNoChats.setVisibility(View.INVISIBLE);
        chatsArrayList = new ArrayList();
        mRecyclerViewChats = rootView.findViewById(R.id.recycle_my_chats);
        mChatsItemAdapter = new ChatsItemAdapter(chatsArrayList);
        mRecyclerViewChats.setNestedScrollingEnabled(false);
        mRecyclerViewChats.setAdapter(mChatsItemAdapter);
        mRecyclerViewChats.setLayoutManager(new LinearLayoutManager(getContext()));
        if (mChatsItemAdapter.getItemCount() == 0) {
            mTextViewNoChats.setVisibility(View.VISIBLE);
        }
        return rootView; }


}
