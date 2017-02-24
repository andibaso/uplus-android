package com.pega.ubank.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.pega.ubank.R;
import com.pega.ubank.fragment.AccountListFragment;
import com.pega.ubank.fragment.MoveMoneyFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 4;
    private final Context context;
    private  SparseArray<Fragment> registeredFragments = new SparseArray<>();
    private int[] tabImageRes = {
            R.drawable.accn,
            R.drawable.moven,
            R.drawable.offn,
            R.drawable.settn
    };
    private String[] tabTitles = {
           "Accounts" , "Move Money", "Offers", "Settings"
    };

    public FragmentAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return new AccountListFragment();
            case 1: // Fragment # 0 - This will show FirstFragment different title
            case 2:
            case 3:
                return new MoveMoneyFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Accounts";
            case 1:
                return "Move Money";
            case 2:
                return "Offers";
            case 3:
                return "Settings";
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    public View getTabView(int position) {
        View tab = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView title = (TextView) tab.findViewById(R.id.tabTitle);
        title.setText(tabTitles[position]);
        Drawable icon = ContextCompat.getDrawable(context, tabImageRes[position]);
        float density = context.getResources().getDisplayMetrics().density;
        int drawableSize = Math.round(30 * density);
        icon.setBounds(0,0, drawableSize, drawableSize);
        title.setCompoundDrawables(null, icon, null, null);
        return tab;
    }
}
