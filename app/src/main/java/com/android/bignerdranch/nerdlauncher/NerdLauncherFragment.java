package com.android.bignerdranch.nerdlauncher;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ConradC on 8/5/15.
 */
public class NerdLauncherFragment extends ListFragment {
    private static final String TAG = "NerdLauncherFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);


        Log.i(TAG, "I've found " + activities.size() + " activities.");

        Collections.sort(activities, new Comparator<ResolveInfo>() {
            public int compare(ResolveInfo a, ResolveInfo b) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                        a.loadLabel(pm).toString(),
                        b.loadLabel(pm).toString());
            }
        });


/*        ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(getActivity(),
                android.R.layout.simple_list_item_1, activities) {
            public View getView(int pos, View convertView, ViewGroup parent) {
                View v = super.getView(pos, convertView, parent);
                TextView tv = (TextView)v;
                ResolveInfo ri = getItem(pos);
                tv.setText(ri.loadLabel(pm));
                return v;
            }

        };*/

        ActivityAdapter adapter = new ActivityAdapter(activities);

        setListAdapter(adapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ResolveInfo resolveInfo = (ResolveInfo)l.getAdapter().getItem(position);
        ActivityInfo activityInfo = resolveInfo.activityInfo;

        if (activityInfo == null) return;

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
        startActivity(i);
    }

    private class ActivityAdapter extends ArrayAdapter<ResolveInfo> {
        public ActivityAdapter(List<ResolveInfo> activities) {
            super(getActivity(), 0, activities);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_activity, null);
            }

            ResolveInfo ri = getItem(position);
            PackageManager pm = getActivity().getPackageManager();

            ImageView iconImageView = (ImageView) convertView.findViewById(R.id.activity_list_item_iconImageView);
            iconImageView.setImageDrawable(ri.loadIcon(pm));


            TextView titleTextView = (TextView) convertView.findViewById(R.id.activity_list_item_titleTextView);
            titleTextView.setText(ri.loadLabel(pm));

            return convertView;
        }
    }


}
