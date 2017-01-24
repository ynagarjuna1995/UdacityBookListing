package io.github.ynagarjuna1995.udacitybooklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.booklisting.R;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = BookActivity.class.getName();
    /*This is similar to tag that we use for fragments*/
    private static final int BOOK_LOADER_ID = 1;
    
    private TextView mEmptyTextView;
    private ProgressBar mProgressBar;

    private String GOOGLE_API_REQUEST_URL;
    
    private BookAdapter mBooksAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        GOOGLE_API_REQUEST_URL = bundle.getString("QUERY_URL");
        String queryName = bundle.getString("QUERY_NAME");

        setTitle(queryName);

        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyTextView);

        // Create a new adapter that takes an empty list of books as input
        mBooksAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mBooksAdapter);

        //Set the click listener to open a browser intent to display detailed book info
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Book book = mBooksAdapter.getItem(position);
                String url = book.getInfoLink();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        //Check the network connection for availability
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {

        // Create a new loader for the given URL
        return new BookLoader(this, GOOGLE_API_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        mProgressBar.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyTextView.setText(R.string.no_books);

        // Clear the adapter of previous book data
        mBooksAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mBooksAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {

        // Loader reset, so we can clear out our existing data.
        mBooksAdapter.clear();
    }
}
