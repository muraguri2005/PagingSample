package org.freelesson.pagingsample.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.freelesson.pagingsample.R

class ReposAdapter internal constructor() :
    PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(UI_MODEL_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.repo_view_item) {
            RepoViewHolder.create(parent)
        } else {
            SeparatorViewHolder.create(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.RepoItem -> R.layout.repo_view_item
            is UiModel.SeparatorItem -> R.layout.separator_view_item
            null -> throw  UnsupportedOperationException("Unknown view")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel?.let {
            when (uiModel) {
                is UiModel.RepoItem -> (holder as RepoViewHolder).bind(uiModel.repo)
                is UiModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(uiModel.descriptionString)
            }
        }
    }

    companion object {
        private val UI_MODEL_COMPARATOR: DiffUtil.ItemCallback<UiModel> =
            object : DiffUtil.ItemCallback<UiModel>() {
                override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                    return (oldItem is UiModel.RepoItem && newItem is UiModel.RepoItem && oldItem.repo.fullName == newItem.repo.fullName) || (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem && oldItem.descriptionString == newItem.descriptionString)
                }

                override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                    oldItem == newItem
            }
    }
}