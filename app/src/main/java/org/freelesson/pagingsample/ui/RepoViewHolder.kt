package org.freelesson.pagingsample.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.freelesson.pagingsample.R
import org.freelesson.pagingsample.model.Repo

class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView
    private val description: TextView
    private val stars: TextView
    private val language: TextView
    private val forks: TextView
    private var repo: Repo? = null

    init {
        name = view.findViewById(R.id.repo_name)
        description = view.findViewById(R.id.repo_description)
        stars = view.findViewById(R.id.repo_stars)
        language = view.findViewById(R.id.repo_language)
        forks = view.findViewById(R.id.repo_forks)
        view.setOnClickListener {
            repo?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(repo: Repo?) {
        if (repo == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            description.visibility = View.GONE
            language.visibility = View.GONE
            stars.text = resources.getText(R.string.unknown)
            forks.text = resources.getText(R.string.unknown)
        } else {
            showRepoData(repo)
        }
    }

    private fun showRepoData(repo: Repo) {
        this.repo = repo
        name.text = repo.fullName
        var descriptionVisibility = View.GONE
        if (repo.description != null && repo.description.isNotEmpty()) {
            description.text = repo.description
            descriptionVisibility = View.VISIBLE
        }
        description.visibility = descriptionVisibility
        stars.text = repo.stars.toString()
        forks.text = repo.forks.toString()
        var languageVisibility = View.GONE
        if (!repo.language.isNullOrEmpty()) {
            val resources = itemView.context.resources
            language.text = resources.getString(R.string.language, repo.language)
            languageVisibility = View.VISIBLE
        }
        language.visibility = languageVisibility
    }

    companion object {
        fun create(parent: ViewGroup): RepoViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.repo_view_item, parent, false)
            return RepoViewHolder(view)
        }
    }


}