package io.github.ynagarjuna1995.udacitybooklisting;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.example.admin.booklisting.R;

import static android.app.SearchManager.QUERY;

public class BookSearchActivity extends AppCompatActivity {

    SearchView searchView;

    private String GOOGLE_API_REQUEST_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            //use the query to search data
            String query = intent.getStringExtra(QUERY);
            Toast.makeText(getApplicationContext(), "Showing results for " + query, Toast.LENGTH_LONG).show();

            String queryTopic = query;

            query = query.replaceAll("\\s+", "");

            //Form the request URL using to query
            GOOGLE_API_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + query;

            searchView.clearFocus();
            searchView.onActionViewCollapsed();

            //intent to pass the book request URL to BookActivity to initiate searching
            intent = new Intent(BookSearchActivity.this, BookActivity.class);
            intent.putExtra("QUERY_URL", GOOGLE_API_REQUEST_URL);
            intent.putExtra("QUERY_NAME", queryTopic);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
}
