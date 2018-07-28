package com.pharmakhanah.hp.pharmakhanahsource.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pharmakhanah.hp.pharmakhanahsource.R;
import com.pharmakhanah.hp.pharmakhanahsource.model.adapters.NotificationItemAdapter;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class NotificationsFragment extends Fragment {
    TextView mTextViewNoNotifications;
    RecyclerView mRecyclerViewNotifications;
    NotificationItemAdapter mNotificationItemAdapter;
    ArrayList notificationsArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        mTextViewNoNotifications = rootView.findViewById(R.id.txt_no_notifications);
        mTextViewNoNotifications.setVisibility(View.INVISIBLE);
        notificationsArrayList = new ArrayList();
        mRecyclerViewNotifications = rootView.findViewById(R.id.recycle_my_notifications);
        mNotificationItemAdapter = new NotificationItemAdapter(notificationsArrayList);
        mRecyclerViewNotifications.setNestedScrollingEnabled(false);
        mRecyclerViewNotifications.setAdapter(mNotificationItemAdapter);
        mRecyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        if (mNotificationItemAdapter.getItemCount() == 0) {
            mTextViewNoNotifications.setVisibility(View.VISIBLE);
        }
        return rootView; }


}
