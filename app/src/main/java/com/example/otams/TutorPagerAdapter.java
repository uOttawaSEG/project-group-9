package com.example.otams;

import androidx.annotation.NonNull;
import  androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class TutorPagerAdapter extends FragmentStateAdapter {

    public TutorPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AvailabilityFragment();
            case 1:
                return new RequestsFragment();
            case 2:
                return new SessionsFragment();
            default:
                return new SessionsFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
