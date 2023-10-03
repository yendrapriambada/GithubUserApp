package academy.bangkit.githubuserapp.ui

import academy.bangkit.githubuserapp.R
import academy.bangkit.githubuserapp.adapter.SectionsPagerAdapter
import academy.bangkit.githubuserapp.databinding.ActivityDetailBinding
import academy.bangkit.githubuserapp.viewmodel.DetailViewModel
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var tempUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR)
        viewModel.getDetailUser(username)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel.user.observe(this) {
            if (it != null) {
                binding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tempUser = it.login
                    tvLocation.text = it.location
                    tvCompany.text = it.company
                    if (it.name.isNullOrBlank()) tvName.visibility = View.GONE
                    if (it.location.isNullOrBlank()) icLoc.visibility = View.GONE
                    if (it.company.isNullOrBlank()) icCompany.visibility = View.GONE
                    tvJmlRepo.text = it.public_repos.toString()
                    tvJmlFollowing.text = it.following.toString()
                    tvJmlFollowers.text = it.followers.toString()
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .circleCrop()
                        .into(imgItemPhoto)
                }
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        var _ischecked = false
        viewModel.checkIsFavorited(username).observe(this) {
            if (it.isEmpty()) {
                binding.fabFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                _ischecked = false
            } else {
                binding.fabFav.setImageResource(R.drawable.ic_baseline_favorite_24)
                _ischecked = true
            }
        }

        binding.fabFav.setOnClickListener {
            _ischecked = !_ischecked
            if (_ischecked) {
                viewModel.addToFavorite(id, username, avatarUrl)
            } else {
                viewModel.deleteFromFavorite(id)
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        binding.apply {
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
        supportActionBar?.elevation = 0f
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.share_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sharebutton -> {
                val myIntent = Intent(Intent.ACTION_SEND)
                myIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hello, visit to this github:\nhttps://github.com/$tempUser"
                )
                myIntent.type = "text/plain"
                startActivity(Intent.createChooser(myIntent, "Share To:"))
                true
            }
            else -> true
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.name_followers,
            R.string.name_following
        )
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"
    }
}