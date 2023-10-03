package academy.bangkit.githubuserapp.ui

import academy.bangkit.githubuserapp.R
import academy.bangkit.githubuserapp.adapter.ListUserGithubAdapter
import academy.bangkit.githubuserapp.databinding.FragmentDataFollowBinding
import academy.bangkit.githubuserapp.model.Items
import academy.bangkit.githubuserapp.viewmodel.FollowingViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class DataFollowingFragment : Fragment() {

    private lateinit var adapter: ListUserGithubAdapter
    private var _binding: FragmentDataFollowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_data_follow, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(DetailActivity.EXTRA_USERNAME).toString()
        _binding = FragmentDataFollowBinding.bind(view)

        adapter = ListUserGithubAdapter()
        adapter.notifyDataSetChanged()

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)
        val layoutManager = LinearLayoutManager(activity)
        viewModel.getFollowingUser(username)

        binding.apply {
            rvUserfoll.layoutManager = layoutManager
            rvUserfoll.setHasFixedSize(true)
            rvUserfoll.adapter = adapter
        }

        viewModel.listFollowing.observe(viewLifecycleOwner) {
            if (!(it == null || it.size == 0)) adapter.setList(it) else showLottie(true)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        viewModel.isLottieVisible.observe(viewLifecycleOwner) {
            showLottie(it)
        }

        adapter.setOnItemClickCallback(object : ListUserGithubAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Items) {
                showSelectedUserGithub(data)
            }
        })
    }

    private fun showSelectedUserGithub(data: Items) {
        Intent(activity, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
            it.putExtra(DetailActivity.EXTRA_ID, data.id)
            it.putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
            startActivity(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLottie(isLottieVisible: Boolean) {
        binding.animationView.apply {
            layoutDataEmpty.visibility = if (isLottieVisible) View.VISIBLE else View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}