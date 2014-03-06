package com.blm.nimboclient;

import java.io.File;
import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.nostra13.universalimageloader.cache.disc.impl.FileCountLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class NimboClientMainActivity extends ActionBarActivity {

	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

	private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
	
    private static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    
    @SuppressWarnings("unused")
	private static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;
    
	private AccountManager accountManager;

	private Spinner userSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.configureImageLoader();
		
		setContentView(R.layout.activity_nimbo_client_main);

		String[] names = this.getAccountNames();
		
		userSpinner = (Spinner) this.findViewById(R.id.user_spinner);
		ArrayAdapter<CharSequence> userAdapter = 
				new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, names);
		userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		userSpinner.setAdapter(userAdapter);
	}

	/**
	 * Initialize and return the image loader configuration
	 * @return
	 */
	private void configureImageLoader() {
		
		File cacheDir = StorageUtils.getCacheDirectory(this);

		Log.i(this.getClass().getName(), "nimbo cache dir: " + cacheDir);

		//	        .imageDecoder(new MediaImageDecoder(this.getContentResolver()))
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
			.memoryCacheExtraOptions(50, 50)
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.discCache(new FileCountLimitedDiscCache(cacheDir, 100))
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			.build();
		
/*		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
	        .memoryCacheExtraOptions(480, 800)
	        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
	        .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
	        .denyCacheImageMultipleSizesInMemory()
	        .discCache(new FileCountLimitedDiscCache(cacheDir, 100))
	        .build();*/
		
		ImageLoader.getInstance().init(config);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		/*
		 * Used in action bar
		 */
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.nimbo_client_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_search:
			openSearch();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		case R.id.action_discard:
			this.clearCache();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Clear the cache and notify the user
	 */
	private void clearCache() {
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.clearMemoryCache();
		imageLoader.clearDiscCache();
		
		Toast toast = Toast.makeText(this, this.getResources().getString(R.string.toast_discard), Toast.LENGTH_LONG);
		toast.show();
	}

	private void openSearch() {
		System.out.println("I guess open activity here");
	}

	private void openSettings() {
		System.out.println("open activity here");
	}

	public void sendMessage(View view) {

		/*
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
		*/
	}

	/**
	 * Use this method to obtain a token to validate against the server.
	 * This means we have to use the logged in data to send.
	 * Can scalatra return protected data? (an ssh key?)
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getToken() {
		
		String[] names = this.getAccountNames();
		if (names.length == 0) {
			// this happens when the sample is run in an emulator which has no
			// google account
			// added yet.
			show("No account available. Please add an account to the phone first.");
			return null;
		}
		String email = names[0];

		try {
			return GoogleAuthUtil.getToken(this, email, SCOPE);
		} catch (GooglePlayServicesAvailabilityException playEx) {
			// GooglePlayServices.apk is either old, disabled, or not present.
			this.showError(playEx.getConnectionStatusCode());
		} catch (UserRecoverableAuthException userRecoverableException) {
			// Unable to authenticate, but the user can fix this.
			// Forward the user to the appropriate activity.
			this.startActivityForResult(
					userRecoverableException.getIntent(), REQUEST_CODE_RECOVER_FROM_AUTH_ERROR);
		} catch (GoogleAuthException fatalException) {
			/*("Unrecoverable error " + fatalException.getMessage(),
					fatalException);*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

    /**
     * This method is a hook for background threads and async tasks that need to update the UI.
     * It does this by launching a runnable under the UI thread.
     * TODO see the other show item stuff
     */
    public void show(final String message) {
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textview.setText(message);
            }
        })*/;
    }

    /**
     * This method is a hook for background threads and async tasks that need to launch a dialog.
     * It does this by launching a runnable under the UI thread.
     */
    public void showError(final int code) {

    	/*
    	 * do something else here since this seems like its special demo case
    	 * TODO learn how to use async and run on ui thread bit
    	 */
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
              Dialog d = GooglePlayServicesUtil.getErrorDialog(
                  code,
                  this,
                  REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
              d.show();
            }
        })*/;
    }
	
	public AccountManager getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public Spinner getUserSpinner() {
		return userSpinner;
	}

	public void setUserSpinner(Spinner userSpinner) {
		this.userSpinner = userSpinner;
	}

	private String[] getAccountNames() {
		accountManager = AccountManager.get(this);
		Account[] accounts = accountManager
				.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
		String[] names = new String[accounts.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = accounts[i].name;
		}
		return names;
	}
}
