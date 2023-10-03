package academy.bangkit.githubuserapp.adapter

import academy.bangkit.githubuserapp.ui.DataFollowersFragment
import academy.bangkit.githubuserapp.ui.DataFollowingFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity, dataKey: Bundle) :
    FragmentStateAdapter(activity) {

    private var fragmentBundle: Bundle? = null

    init {
        fragmentBundle = dataKey
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = DataFollowersFragment()
            1 -> fragment = DataFollowingFragment()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }
}