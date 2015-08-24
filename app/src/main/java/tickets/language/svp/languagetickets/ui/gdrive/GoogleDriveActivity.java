package tickets.language.svp.languagetickets.ui.gdrive;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.widget.DataBufferAdapter;
import com.google.android.gms.wearable.Wearable;

import tickets.language.svp.languagetickets.R;
import tickets.language.svp.languagetickets.ui.ViewExtensions;

public class GoogleDriveActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private PlaceholderFragment frarment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_drive);

        mHasMore = true;

        mListView = (ListView)this.findViewById(R.id.google_drive_result_list);
        mResultsAdapter = new ResultsAdapter(this);
        mListView.setAdapter(mResultsAdapter);
        initGoogleApi(savedInstanceState);
        if (savedInstanceState == null) {
//            frarment = new PlaceholderFragment();
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container,frarment )
//                    .commit();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_google_drive, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private ResultsAdapter adapter;
        public ArrayAdapter<String> ad;
        public PlaceholderFragment() {

        }

       // public ResultsAdapter getAdapter(){
//            return adapter;
//        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            adapter = new ResultsAdapter(inflater.getContext());
            ad = new ArrayAdapter<String>(inflater.getContext(),android.R.layout.simple_list_item_1);
            View rootView = inflater.inflate(R.layout.fragment_google_drive, container, false);

            ListView list = ViewExtensions.findViewById(rootView, R.id.google_drive_result_list);
            list.setAdapter(ad);
            ad.add("Test 1");


            return rootView;
        }

        public void setGoogleApiClient(GoogleApiClient mGoogleApiClient) {
            // Create a query for a specific filename in Drive.
            Query query = new Query.Builder()
                    .addFilter(Filters.eq(SearchableField.TITLE, "db_languagetickets"))
                    .build();
            // Invoke the query asynchronously with a callback method
            Drive.DriveApi.query(mGoogleApiClient, query)
                    .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                        @Override
                        public void onResult(DriveApi.MetadataBufferResult result) {
                            // Success! Handle the query result.

                        }
                    });

        }
    }

    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private boolean mHasMore;
    private ListView mListView;
    private DataBufferAdapter<Metadata> mResultsAdapter;
    private String mNextPageToken;

    private void initGoogleApi(Bundle savedInstanceState){

        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
//                .addApiIfAvailable(Wearable.API)
                .addScope(Drive.SCOPE_FILE)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }
    /**
     * Retrieves results for the next page. For the first run,
     * it retrieves results for the first page.
     */
    private void retrieveNextPage() {
        // if there are no more results to retrieve,
        // return silently.
        if (!mHasMore) {
            return;
        }
        // retrieve the results for the next page.
//        Query query = new Query.Builder()
//                .setPageToken(mNextPageToken)
//                .build();
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, "db_languagetickets"))
                .build();
        Drive.DriveApi.query(mGoogleApiClient, query)
                .setResultCallback(metadataBufferCallback);
    }
    /**
     * Appends the retrieved results to the result buffer.
     */
    private final ResultCallback<DriveApi.MetadataBufferResult> metadataBufferCallback = new
            ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                        //showMessage("Problem while retrieving files");
                        return;
                    }
                    mResultsAdapter.append(result.getMetadataBuffer());
                    mNextPageToken = result.getMetadataBuffer().getNextPageToken();
                    mHasMore = mNextPageToken != null;
                }
            };

    @Override
    public void onConnected(Bundle bundle) {
        String EXISTING_FOLDER_ID = Drive.DriveApi.getRootFolder(mGoogleApiClient).getDriveId().toString();
        //frarment.setGoogleApiClient(mGoogleApiClient);
//        Drive.DriveApi.fetchDriveId(mGoogleApiClient, EXISTING_FOLDER_ID)
//                .setResultCallback(idCallback);
//        DriveFolder folder = Drive.DriveApi.getFolder(mGoogleApiClient,
//                Drive.DriveApi.getRootFolder(mGoogleApiClient).getDriveId());
//        folder.listChildren(mGoogleApiClient).setResultCallback(metadataResult);

        retrieveNextPage();
    }

    final private ResultCallback<DriveApi.DriveIdResult> idCallback = new ResultCallback<DriveApi.DriveIdResult>() {
        @Override
        public void onResult(DriveApi.DriveIdResult result) {
            if (!result.getStatus().isSuccess()) {
                //showMessage("Cannot find DriveId. Are you authorized to view this file?");
                return;
            }
            DriveFolder folder = Drive.DriveApi.getFolder(mGoogleApiClient, result.getDriveId());
            folder.listChildren(mGoogleApiClient).setResultCallback(metadataResult);
        }
    };

    final private ResultCallback<DriveApi.MetadataBufferResult> metadataResult = new
            ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
//                        showMessage("Problem while retrieving files");
                        return;
                    }

//                    mResultsAdapter.clear();
                    MetadataBuffer md = result.getMetadataBuffer();
                    mResultsAdapter.append(md);
//                    ResultsAdapter ra = frarment.getAdapter();
//                    ra.append(md);
//                    ra.notifyDataSetChanged();

//                    mResultsAdapter.append(result.getMetadataBuffer());
//                    showMessage("Successfully listed files.");
                }
            };

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {//RESOLVE_CONNECTION_REQUEST_CODE
                connectionResult.startResolutionForResult(this,
                        REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }
/*
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            // The Wearable API is unavailable
        }
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }
*/
    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }
    private FragmentManager getSupportFragmentManager(){
        return new ErrorDialogFragment().getFragmentManager();
    }
    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }
    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((GoogleDriveActivity) getActivity()).onDialogDismissed();
        }
    }
    public static class ResultsAdapter extends DataBufferAdapter<Metadata> {

        public ResultsAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(),
                        android.R.layout.simple_list_item_1, null);
            }
            Metadata metadata = getItem(position);
            TextView titleTextView =
                    (TextView) convertView.findViewById(android.R.id.text1);
            titleTextView.setText(metadata.getTitle());
            return convertView;
        }
    }
}
