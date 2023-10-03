package academy.bangkit.githubuserapp.ui

import academy.bangkit.githubuserapp.R
import academy.bangkit.githubuserapp.adapter.ListUserGithubAdapter
import academy.bangkit.githubuserapp.databinding.ActivityMainBinding
import academy.bangkit.githubuserapp.databinding.ActivitySettingsBinding
import academy.bangkit.githubuserapp.model.Items
import academy.bangkit.githubuserapp.viewmodel.MainViewModel
import academy.bangkit.githubuserapp.viewmodel.SettingsViewModel
import academy.bangkit.githubuserapp.viewmodel.ViewModelFactory
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingSettings: ActivitySettingsBinding
    private lateinit var adapter: ListUserGithubAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingSettings = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User's Search"

        adapter = ListUserGithubAdapter()
        adapter.notifyDataSetChanged()
        adapter.notifyItemRangeRemoved(0, adapter.itemCount)

        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
        val layoutManager = LinearLayoutManager(this)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        binding.apply {
            rvUsergithub.layoutManager = layoutManager
            rvUsergithub.setHasFixedSize(true)
            rvUsergithub.adapter = adapter
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.queryHint = resources.getString(R.string.search_hint)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    mainViewModel.searchUserGithub(query)
                    return true
                }

                override fun onQueryTextChange(query: String): Boolean {
                    mainViewModel.searchUserGithub(query)
                    return true
                }
            })
        }
        mainViewModel.listUserGithub.observe(this) { searchResult ->
            setSearchView(searchResult)
        }
        mainViewModel.isEmpty.observe(this) {
            setClearArray(it)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.isLottieVisible.observe(this) {
            showLottie(it)
        }

        adapter.setOnItemClickCallback(object : ListUserGithubAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Items) {
                showSelectedUserGithub(data)
            }
        })

        // Observe Theme
        val pref = SettingPreferences.getInstance(dataStore)
        val settingsViewModel =
            ViewModelProvider(this, ViewModelFactory(pref)).get(SettingsViewModel::class.java)

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                bindingSettings.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                bindingSettings.switchTheme.isChecked = false
            }
        }
    }

    private fun showSelectedUserGithub(data: Items) {
        Intent(this@MainActivity, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
            it.putExtra(DetailActivity.EXTRA_ID, data.id)
            it.putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
            startActivity(it)
        }
    }

    private fun setSearchView(searchResult: ArrayList<Items>) {
        if (searchResult.isNotEmpty()) {
            adapter.setList(searchResult)
        } else {
            adapter.clearList()
            showLottie(true)
            Toast.makeText(
                this@MainActivity,
                resources.getString(R.string.user_not_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showLottie(isLottieVisible: Boolean) {
        binding.animationView.layoutDataEmpty.visibility =
            if (isLottieVisible) View.VISIBLE else View.GONE
    }

    private fun setClearArray(isTextEmpty: Boolean) {
        if (isTextEmpty) {
            adapter.clearList()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_fav -> {
                Intent(this@MainActivity, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
                true
            }
            R.id.btn_setting -> {
                Intent(this@MainActivity, SettingsActivity::class.java).also {
                    startActivity(it)
                }
                true
            }
            else -> true
        }
    }
}