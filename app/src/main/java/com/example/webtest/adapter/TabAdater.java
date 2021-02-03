package com.example.webtest.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.webtest.fragment.UserDetailFragment;
import com.example.webtest.fragment.UserListFragment;


public class TabAdater extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TabAdater(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                UserListFragment adviseNotesEntryFragment = new UserListFragment();
                return  adviseNotesEntryFragment;
            case 1:
                UserDetailFragment adviseNoteListFragment = new UserDetailFragment();
                return  adviseNoteListFragment;
            default:
                return null;
        }
    }
}