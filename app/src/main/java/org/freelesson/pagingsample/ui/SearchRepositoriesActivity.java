package org.freelesson.pagingsample.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.freelesson.pagingsample.Injection;
import org.freelesson.pagingsample.R;


public class SearchRepositoriesActivity extends AppCompatActivity {
    private static final String LAST_SEARCH_QUERY = "last_search_query";
    private static final String DEFAULT_QUERY = "Android";
    SearchRepositoriesViewModel viewModel;
    ReposAdapter adapter = new ReposAdapter();
    RecyclerView list;
    TextView emptyList;
    EditText search_repo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_repositories);
        emptyList = findViewById(R.id.emptyList);
        list = findViewById(R.id.list);
        search_repo = findViewById(R.id.search_repo);
        viewModel = new ViewModelProvider(this, Injection.provideViewModelFactory(this)).get(SearchRepositoriesViewModel.class);
        DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);

        list.addItemDecoration(decoration);
        setupScrollListener();

        initAdapter();
        String query = savedInstanceState!= null && savedInstanceState.getString(LAST_SEARCH_QUERY)!=null ? savedInstanceState.getString(LAST_SEARCH_QUERY) : DEFAULT_QUERY;
        viewModel.searchRepo(query);
        initSearch(query);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(LAST_SEARCH_QUERY,viewModel.lastQueryValue());
    }

    private void initSearch(String query) {
        search_repo.setText(query);
        search_repo.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId== EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput();
                return true;
            } else {
                return false;
            }
        });

        search_repo.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput();
                return true;
            }
            return false;
        });
    }
    private void updateRepoListFromInput() {
        if (!search_repo.getText().toString().trim().isEmpty()) {
            list.scrollToPosition(0);
            viewModel.searchRepo(search_repo.getText().toString().trim());
            adapter.submitList(null);
        }
    }
    private void initAdapter() {
        list.setAdapter(adapter);
        viewModel.repos.observe(this, repos -> {
            Log.d("Activity","list "+repos.size());
            showEmptyList(repos.isEmpty());
            adapter.submitList(repos);
        });
        viewModel.networkErrors.observe(this, s -> Toast.makeText(getApplicationContext(),"\uD83D\uDE28 Wooops "+s,Toast.LENGTH_LONG).show());

    }

    private void setupScrollListener() {
        final LinearLayoutManager layoutManager = (LinearLayoutManager) list.getLayoutManager();
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView,dx,dy);
                assert layoutManager != null;
                int totalItemCount = layoutManager.getItemCount();
                int visibleItemCount = layoutManager.getChildCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                viewModel.listScrolled(visibleItemCount,lastVisibleItem,totalItemCount);
            }
        });
    }
    private void showEmptyList(boolean show) {
        if (show) {
            emptyList.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            emptyList.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
    }
}
