package com.pixerapps.tabzie;

public interface TabClickListener {

    /**
     * Handle click event when tab is clicked.
     * @param position ViewPager item position.
     * @return Call setCurrentItem if return true.
     */
    public boolean onTabClick(int position);

}
