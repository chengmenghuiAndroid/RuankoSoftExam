/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.itee.exam.app.ui.common.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itee.exam.R;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

/**
 * 可滑动Tab
 *
 * @author moxin
 */
public abstract class SlidingTabsFragment extends Fragment {

    private ViewGroup tab;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sliding_tabs, container, false);
        tab = (ViewGroup) view.findViewById(R.id.tab);
        tab.addView(inflater.inflate(R.layout.layout_tab, tab, false));

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        SmartTabLayout viewPagerTab = (SmartTabLayout) view.findViewById(R.id.tab_layout);

        FragmentPagerItems pages = new FragmentPagerItems(getActivity());
        populateTabs(pages);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), pages);
        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SlidingTabsFragment.this.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    /**
     * 设置当前显示页
     * @param index
     * @param smoothScroll 是否显示拖动动画
     */
    public void setCurrentTabIndex(int index,boolean smoothScroll){
        if (index >= 0 && viewPager != null && viewPager.getAdapter() != null && index < viewPager.getAdapter().getCount()) {
            viewPager.setCurrentItem(index,smoothScroll);
        }
    }

    public void setTabBackgroundColor(int color) {
        tab.setBackgroundColor(color);
    }

    protected abstract void populateTabs(FragmentPagerItems pages);

    public void onPageSelected(int position) {

    }
}