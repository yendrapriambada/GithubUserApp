package academy.bangkit.githubuserapp.ui

import academy.bangkit.githubuserapp.adapter.ListUserGithubAdapter
import academy.bangkit.githubuserapp.database.Favorite
import academy.bangkit.githubuserapp.databinding.FragmentDataFollowBinding
import academy.bangkit.githubuserapp.model.Items
import academy.bangkit.githubuserapp.viewmodel.FavoriteViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class FavoriteActivity : AppCompatActivity() {

    private var _binding: FragmentDataFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListUserGithubAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentDataFollowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "User's Favorite"

        adapter = ListUserGithubAdapter()
        adapter.notifyDataSetChanged()
        adapter.notifyItemRangeRemoved(0, adapter.itemCount)

        val viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        val layoutManager = LinearLayoutManager(this)
        viewModel.getFavoriteUser()

        binding.apply {
            rvUserfoll.layoutManager = layoutManager
            rvUserfoll.setHasFixedSize(true)
            rvUserfoll.adapter = adapter
        }

        viewModel.getFavoriteUser().observe(this) {
            if (!(it == null || it.size == 0)) {
                showLottie(false)
                val listFav: ArrayList<Items> = toList(it)
                adapter.setList(listFav)
            } else {
                adapter.clearList()
                showLottie(true)
            }
        }

        adapter.setOnItemClickCallback(object : ListUserGithubAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Items) {
                showSelectedUserGithub(data)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showSelectedUserGithub(data: Items) {
        Intent(this, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
            it.putExtra(DetailActivity.EXTRA_ID, data.id)
            it.putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
            startActivity(it)
        }
    }

    private fun showLottie(isLottieVisible: Boolean) {
        binding.animationView.apply {
            layoutDataEmpty.visibility = if (isLottieVisible) View.VISIBLE else View.GONE
        }
    }

    private fun toList(users: List<Favorite>): ArrayList<Items> {
        val listUsers = ArrayList<Items>()
        for (user in users) {
            val userMapped =
                user.avatar_url?.let { user.login?.let { it1 -> Items(it1, it, user.id) } }
            userMapped?.let { listUsers.add(it) }
        }
        return listUsers
    }
}