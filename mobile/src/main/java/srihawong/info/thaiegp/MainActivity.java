package srihawong.info.thaiegp;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public TableRow tableRow;
        public TextView textView;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.tableLayout);

            //ViewGroup tableLayout = (TableLayout) rootView.findViewById(R.id.tableLayout);

            Context context = getActivity().getApplicationContext();

            //TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();

            final String channelString = "CH3,CH5,CH7,CH9";
            TableRow.LayoutParams layoutParams;
            AQuery aq = new AQuery(context);

            String[] channel = channelString.split(",");
            tableRow = new TableRow(context);
            layoutParams = new TableRow.LayoutParams();
            textView = new TextView(context);
            textView.setLayoutParams(layoutParams);
            textView.setText("CH");
            tableRow.addView(textView);
            for(int i=1;i<48;i++){
                layoutParams.span = 1;
                textView = new TextView(context);
                        textView.setLayoutParams(layoutParams);
                        textView.setText("dd");
                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);

            for(int i=0,j=channel.length;i<j;i++){
                tableRow = new TableRow(context);
                textView = new TextView(context);
                textView.setLayoutParams(layoutParams);
                textView.setText(channel[i].toString());
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String url = "http://srihawong.info/app/thaiegp/channel.php?ch="+channel[i].toString()+"&date="+date;
                aq.ajax(url, JSONArray.class,3600,new AjaxCallback<JSONArray>(){
                    @Override
                    public void callback(String url, JSONArray object, AjaxStatus status) {
                        //super.callback(url, object, status);
                        Context cc = getActivity().getApplicationContext();
                        for(int k=0,l=object.length();k<l;k++){
                            try {
                                JSONObject dd = object.getJSONObject(k);
                                textView = new TextView(cc);
                                textView.setText(dd.getString("title_name_tha"));
                                tableRow.addView(textView);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }




                    }
                });

                tableRow.addView(textView);
                tableLayout.addView(tableRow);
            }



            /*
            for(int i=0;i<10;i++){
                TextView textView = new TextView(context);
                textView.setText("text"+String.valueOf(i));

                textView.setLayoutParams(new TableRow.LayoutParams());

                TableRow tableRow = new TableRow(context);

                tableLayout.addView(tableRow);

                tableRow.addView(textView);

            }
        */

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        public void guide(){

        }
    }

}
