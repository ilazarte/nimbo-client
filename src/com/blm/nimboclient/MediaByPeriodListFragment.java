package com.blm.nimboclient;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.blm.nimboclient.period.PeriodAdapter;
import com.blm.nimboclient.period.PeriodIndex;

public class MediaByPeriodListFragment extends ListFragment {

	private PeriodIndex index = null;

    @Override
	public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
		
    	this.setRetainInstance(true);
    	this.index = PeriodIndex.create(this.getActivity());
    	
		PeriodAdapter adapter = new PeriodAdapter(this.getActivity(), index);
		this.setListAdapter(adapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		TextView textview = (TextView) this.getActivity().findViewById(R.id.total_count_text);
		Integer total = index.getTotal();
		textview.setText("Total: " + total);

		this.setHasOptionsMenu(true);
		
		/*
		 * Says this is the right place to remove divider
		 * http://stackoverflow.com/questions/13992168/how-to-set-the-divider-to-null-of-a-listfragment-custom-layout
		 */
    	this.getListView().setDivider(null);
		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.media_by_period_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}
