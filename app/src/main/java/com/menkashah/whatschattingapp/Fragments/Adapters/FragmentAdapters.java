package com.menkashah.whatschattingapp.Fragments.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.menkashah.whatschattingapp.Fragments.Call;
import com.menkashah.whatschattingapp.Fragments.Chats;
import com.menkashah.whatschattingapp.Fragments.Status;

public class FragmentAdapters extends FragmentPagerAdapter{
    public FragmentAdapters(@NonNull FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapters(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0: return  new Chats();
            case 1: return  new Status();
            case 2: return  new Call();
            default:return  new Chats();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="null";

        if(position==0){
            title="CHATS";
        }
        if(position==1){
            title="STATUS";
        }
        if(position==2){
            title="Calls";
        }
        return title;
    }
}
