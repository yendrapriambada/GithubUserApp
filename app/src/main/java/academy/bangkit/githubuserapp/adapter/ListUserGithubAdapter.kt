package academy.bangkit.githubuserapp.adapter

import academy.bangkit.githubuserapp.databinding.ItemRowUserGithubBinding
import academy.bangkit.githubuserapp.model.Items
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ListUserGithubAdapter : RecyclerView.Adapter<ListUserGithubAdapter.ListViewHolder>() {

    private val listUserGithub = ArrayList<Items>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(listUsers: ArrayList<Items>) {
        listUserGithub.clear()
        listUserGithub.addAll(listUsers)
        notifyDataSetChanged()
    }

    fun clearList() {
        val size = listUserGithub.size
        listUserGithub.clear()
        notifyItemRangeRemoved(0, size)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowUserGithubBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, avatar) = listUserGithub[position]
        holder.binding.tvItemUsername.text = username
        Glide.with(holder.itemView.context)
            .load(avatar)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.imgItemPhoto)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUserGithub[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listUserGithub.size

    class ListViewHolder(var binding: ItemRowUserGithubBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: Items)
    }
}
