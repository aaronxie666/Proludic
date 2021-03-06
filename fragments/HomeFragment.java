package icn.proludic.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import icn.proludic.AUSParksActivity;
import icn.proludic.DashboardActivity;
import icn.proludic.MapsActivity;
import icn.proludic.R;
import icn.proludic.adapters.RecyclerViewFriendsListAdapter;
import icn.proludic.adapters.RecyclerViewNearestParkAdapter;
import icn.proludic.misc.CircleTransform;
import icn.proludic.misc.CustomComparator;
import icn.proludic.misc.SharedPreferencesManager;
import icn.proludic.misc.Utils;
import icn.proludic.models.FriendsModel;
import icn.proludic.models.NearestParkModel;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static icn.proludic.misc.Constants.EXERCISE_IDS;
import static icn.proludic.misc.Constants.EXERCISE_IMAGE_KEY;
import static icn.proludic.misc.Constants.EXERCISE_NAME_KEY;
import static icn.proludic.misc.Constants.EXERCISE_OF_THE_WEEK;
import static icn.proludic.misc.Constants.EXTRAS_CLASS_NAME;
import static icn.proludic.misc.Constants.HOME_PARK_KEY;
import static icn.proludic.misc.Constants.LAST_ACTIVE;
import static icn.proludic.misc.Constants.LOCATIONS_CLASS_KEY;
import static icn.proludic.misc.Constants.LOCATION_NAME_KEY;
import static icn.proludic.misc.Constants.LOCATION_STARTING_POINT_KEY;
import static icn.proludic.misc.Constants.NOT_SELECTED;
import static icn.proludic.misc.Constants.NO_PICTURE;
import static icn.proludic.misc.Constants.ONE_HUNDRED_MILES;
import static icn.proludic.misc.Constants.USER_ACHIEVEMENTS;
import static icn.proludic.misc.Constants.USER_DESCRIPTION;
import static icn.proludic.misc.Constants.USER_HEARTS;
import static icn.proludic.misc.Constants.USER_PROFILE_PICTURE;
//add by chang
import static icn.proludic.misc.Constants.THIS_WEEK_HEARTS;
import static icn.proludic.misc.Constants.BEST_WEEK_HEARTS;
import static icn.proludic.misc.Constants.TRACKED_EVENTS_CLASS_NAME;
import static icn.proludic.misc.Constants.TRACKED_USER;

/**
 * Author:  Bradley Wilson
 * Date: 11/04/2017
 * Package: icn.proludic.fragments
 * Project Name: proludic
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback,OnDataPointListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private RecyclerView selectHomeParkRecycler;
    private TextView exerciseOFTheWeekTitle;
    private ImageView exerciseOfTheWeekImage;
    private boolean isAvailableAtHomePark;
    private View view;
    private RecyclerView friendsRecycler;
    private RecyclerView nearestParkRecyclerView;
    private ScrollView homeSV;
    private TextView friendsActivityTv;
    private ImageView profilePicture;
    private String profileImageUrl = "no_picture";
    private TextView userHearts;
    //add by chang
    private TextView heartsOrangeCount;
    private TextView heartsGoldCount;
    private  SwipeRefreshLayout mySwipeRefreshLayout;

    private LatLng mapsLocation;
    private MapView mMapView;
    private String parkTitle;
    private Utils utils;

    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    private GoogleApiClient mApiClient;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);

        //add by chang

//        int thisWeekHearts = ParseUser.getCurrentUser().getInt(THIS_WEEK_HEARTS);    //question: why this one is different from the database
//
//        int bestWeekHearts = ParseUser.getCurrentUser().getInt(BEST_WEEK_HEARTS);
//        if(bestWeekHearts<thisWeekHearts){
//            ParseUser.getCurrentUser().put(BEST_WEEK_HEARTS, thisWeekHearts);
//        }

//        Date d = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm");
//        String dayOfTheWeek = sdf.format(d);
//        if(dayOfTheWeek.equals("Monday 00:00")){
//
//            ParseUser.getCurrentUser().remove(THIS_WEEK_HEARTS);
//            ParseUser.getCurrentUser().put(THIS_WEEK_HEARTS, 0);
//        }




        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }
        utils = new Utils(getActivity());

        mApiClient = new GoogleApiClient.Builder(this.getContext())
                .addApi(Fitness.SENSORS_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        return view;
    }

    private void initViews(final View view) {
        try {
            final ParseObject exerciseOfTheWeek = ParseQuery.getQuery(EXTRAS_CLASS_NAME).getFirst().getParseObject(EXERCISE_OF_THE_WEEK);
            //exerciseOFTheWeekTitle = (TextView) view.findViewById(R.id.week_routine_title);

            //test
//            exerciseOfTheWeekImage = (ImageView) view.findViewById(R.id.home_target_routine);
//            final TextView notAtHomePark = (TextView) view.findViewById(R.id.not_at_home_park);
            //exerciseOFTheWeekTitle.setText(exerciseOfTheWeek.fetch().getString(EXERCISE_NAME_KEY));
            friendsActivityTv = (TextView) view.findViewById(R.id.friends_activity);
            profileImageUrl = ParseUser.getCurrentUser().getString(USER_PROFILE_PICTURE);
            //Log.e("eofw", exerciseOfTheWeek.fetch().getString(EXERCISE_NAME_KEY));
            //test
            //Picasso.with(getActivity()).load(exerciseOfTheWeek.fetch().getParseFile(EXERCISE_IMAGE_KEY).getUrl()).into(exerciseOfTheWeekImage);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        boolean doesHomeParkExist = true;
                        String homeParkName = "";

                        //test
//                        ImageView exerciseImage = view.findViewById(R.id.home_target_routine);
//                        if (exerciseImage != null) {
//                            if (getActivity() != null) {
//                                exerciseImage.setColorFilter(
//                                        new PorterDuffColorFilter(ContextCompat.getColor(getActivity(), R.color.colorSecondaryHalf), PorterDuff.Mode.SRC_ATOP));
//                            }
//                        }
                        try {
                            if (objects.get(0).getString(HOME_PARK_KEY).equals(NOT_SELECTED)) {
                                Log.e("isEmptyHomePark", "");
//                                notAtHomePark.setVisibility(View.VISIBLE);
                                doesHomeParkExist = false;
                            } else {
                                Log.e("isNotEmpty", "");
                                JSONArray exercises = ParseQuery.getQuery(LOCATIONS_CLASS_KEY).setLimit(999).get(objects.get(0).getString(HOME_PARK_KEY)).getJSONArray(EXERCISE_IDS);
                                homeParkName = ParseQuery.getQuery(LOCATIONS_CLASS_KEY).setLimit(999).get(objects.get(0).getString(HOME_PARK_KEY)).getString(LOCATION_NAME_KEY);
                                isAvailableAtHomePark = exercises.toString().contains(exerciseOfTheWeek.getObjectId());
                                if (isAvailableAtHomePark) {
//                                    notAtHomePark.setVisibility(View.GONE);
                                } else {
//                                    notAtHomePark.setVisibility(View.VISIBLE);
                                }
                            }
                            userHearts = view.findViewById(R.id.user_hearts);

                            //add by chang start
                            heartsOrangeCount = view.findViewById(R.id.hearts_orange_count);
                            heartsGoldCount = view.findViewById(R.id.hearts_gold_count);
                            getWeekHearts(view);
                            mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_home);
                            //add by chang end

                            //add by chang start
                            mySwipeRefreshLayout.setOnRefreshListener(
                                    new SwipeRefreshLayout.OnRefreshListener() {
                                        @Override
                                        public void onRefresh() {
                                            Log.i("message", "onRefresh called from SwipeRefreshLayout");

                                            // This method performs the actual data-refresh operation.
                                            // The method calls setRefreshing(false) when it's finished.
                                            myUpdateOperation();
                                        }
                                    }
                            );

                            //add by chang end


                            profilePicture = view.findViewById(R.id.user_profile_picture);
                            ((DashboardActivity) getActivity()).utils.populateUserDetails(getActivity(), (TextView) view.findViewById(R.id.user_full_name), (TextView) view.findViewById(R.id.user_home_park), doesHomeParkExist, (ImageView) view.findViewById(R.id.user_profile_picture), userHearts, homeParkName);

                            startMyTask(new AsyncFriends());
                            getTopRankedUser(view);
                            getTopRankedPark(view);


                            TextView findNearestPark = (TextView) view.findViewById(R.id.nearest_park_button);
                            findNearestPark.setOnClickListener(customListener);
                            TextView connectGoogleFit = (TextView) view.findViewById(R.id.connect_google_fit);
                            connectGoogleFit.setOnClickListener(customListener);

                            nearestParkRecyclerView = (RecyclerView) view.findViewById(R.id.findnearestparkrecyclerview);
                            nearestParkRecyclerView.setHasFixedSize(false);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            nearestParkRecyclerView.setLayoutManager(mLayoutManager);
                            homeSV = (ScrollView) view.findViewById(R.id.home_sv);
                            ParseUser.getCurrentUser().put(LAST_ACTIVE, new Date());
                            ParseUser.getCurrentUser().saveEventually();
                        } catch (ParseException e1) {
                            Log.e("failed", "failed" + e1.getLocalizedMessage());
                        }
                    } else {
                        Log.e("query failed", e.getLocalizedMessage());
                    }
                }
            });
        } catch (ParseException e) {
            Log.e("failed", e.getLocalizedMessage());
        }
    }
    //add by chang start
    private void myUpdateOperation(){
        getWeekHearts(view);

            mySwipeRefreshLayout.setRefreshing(false);

    }
    //add by chang end


    private void getTopRankedPark(final View view) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(LOCATIONS_CLASS_KEY);
        query.whereEqualTo("isFrench", SharedPreferencesManager.getString(getContext(), "locale").equals("fr"));
        query.orderByDescending(USER_ACHIEVEMENTS);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                TextView topRankedName = view.findViewById(R.id.top_community_name);
                ImageView topRankedProfilePicture = view.findViewById(R.id.top_community_profile_picture);
                TextView topRankedHearts = view.findViewById(R.id.top_community_hearts);
                topRankedName.setText(object.getString(LOCATION_NAME_KEY));
                Picasso.with(getActivity()).load(R.drawable.park).transform(new CircleTransform()).into(topRankedProfilePicture);
                topRankedHearts.setText(String.valueOf(object.getInt(USER_ACHIEVEMENTS)));
            }
        });
    }

    private void getTopRankedUser(final View view) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.orderByDescending("Hearts");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                TextView topRankedName = view.findViewById(R.id.top_rank_username);
                ImageView topRankedProfilePicture = view.findViewById(R.id.top_rank_profile_picture);
                TextView topRankedHearts = view.findViewById(R.id.top_rank_hearts);
                topRankedName.setText(object.getString("name"));
                Picasso.with(getActivity()).load(object.getString("profilePicture")).transform(new CircleTransform()).into(topRankedProfilePicture);
                topRankedHearts.setText(String.valueOf(object.getInt("Hearts")));
            }
        });
    }


    //add by chang
    private void getWeekHearts(final View view){
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(TRACKED_EVENTS_CLASS_NAME);
        ParseObject obj = ParseUser.createWithoutData("_User",ParseUser.getCurrentUser().getObjectId());
        query.whereEqualTo(TRACKED_USER,obj);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> eventList, ParseException e) {
                if (e == null) {
                    Calendar c = Calendar.getInstance();
                    c.setFirstDayOfWeek(Calendar.MONDAY);

                    c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MILLISECOND, 0);

                    Date monday = c.getTime();
                    Date nextModay = new Date(monday.getTime() + 7*24*60*60*1000);
                    int eventTotalHearts = 0;

                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");                                                                       //why I can not access to j.getDate("updatedAt")???????????????????????????????????????????
                    Date date = null;
                    for(ParseObject j : eventList){
                        try {
                            date = format.parse(j.getString("Date"));
                        } catch (java.text.ParseException e1) {
                            e1.printStackTrace();
                        }
                        if((date.after(monday)|| date.equals(monday)) && date.before(nextModay) ){
                            eventTotalHearts = j.getNumber("Hearts").intValue() + eventTotalHearts;
                        }else{
                            System.out.print("wwwwwwwwwwwwwwwwwwwronggggggggg"+monday +date);
                        }
                    }
                    System.out.println("-------------------------------------------------------------------"+eventTotalHearts);
                    ParseUser.getCurrentUser().put("ThisWeekHearts", eventTotalHearts);
                    int thisWeekHearts = 0;
                    int bestWeekHearts = 0;
                    try {
                        thisWeekHearts = ParseUser.getCurrentUser().fetchIfNeeded().getInt(THIS_WEEK_HEARTS);
                        bestWeekHearts = ParseUser.getCurrentUser().fetchIfNeeded().getInt(BEST_WEEK_HEARTS);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    heartsOrangeCount.setText(String.valueOf(thisWeekHearts));

                    if(bestWeekHearts<thisWeekHearts){
                        ParseUser.getCurrentUser().put(BEST_WEEK_HEARTS, thisWeekHearts);
                    }

                    try {
                        bestWeekHearts = ParseUser.getCurrentUser().fetchIfNeeded().getInt(BEST_WEEK_HEARTS);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }


                    heartsGoldCount.setText(String.valueOf(bestWeekHearts));

                    Log.d("score", "Retrieved " + eventList.size() + " events");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });


    }

    void startMyTask(AsyncTask<Object, Integer, Void> asyncTask) {
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    //test add by chang
    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
    private String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(yesterday());
    }

    private Date twoDaysAgo() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        return cal.getTime();
    }
    private String gettwoDaysAgo() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(twoDaysAgo());
    }

    private Date threeDaysAgo() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        return cal.getTime();
    }
    private String getthreeDaysAgo() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(threeDaysAgo());
    }



    private void getFriendsDetails() throws ParseException {
        ArrayList<FriendsModel> friendsList = new ArrayList<>();
        for (ParseObject j : friendObjects) {
            String name = getLastActiveDate(j.getDate(LAST_ACTIVE));




            String username = j.fetchIfNeeded().getString("username");
            String profilePicture = j.fetchIfNeeded().getString("profilePicture");
            int hearts = j.fetchIfNeeded().getInt(USER_HEARTS);
            String description = j.fetchIfNeeded().getString(USER_DESCRIPTION);
            String homePark = j.fetchIfNeeded().getString(HOME_PARK_KEY);
            Object tmpProfilePicture = ((DashboardActivity) getActivity()).utils.validateProfilePicture(profilePicture);
            //add by chang
            for(int i=0; i<friendsList.size()-1; i++){
                for(int k=0; k<friendsList.size()-1; k++){
                    //Log.e("debug","11111111111111111111111111111111111111111");
                    if(friendsList.get(k).getName().compareTo(friendsList.get(k+1).getName())<0){
                        //Log.e("debug","11111111111111111111111111111111111111111");
                        FriendsModel temp = friendsList.get(k);
                        friendsList.set(k, friendsList.get(k+1));
                        friendsList.set(k+1, temp);

                    }

                }

            }

            if (name.equals(((DashboardActivity) getActivity()).utils.getTodaysDateString())) {
                name = "Today";
            }else if (name.equals(getYesterdayDateString())){
                name ="1 d ago";
            }else if(name.equals(gettwoDaysAgo())){
                name = "2 d ago";
            }else if(name.equals(getthreeDaysAgo())){
                name = "3 d ago";
            }

            Log.e("friends", name + " | " + profilePicture + " | ");
            friendsList.add(new FriendsModel(j.fetchIfNeeded().getObjectId(), name, username, tmpProfilePicture, description, hearts, homePark));

        }




        //Collections.sort(friendsList, new CustomComparator());

        RecyclerViewFriendsListAdapter adapter = new RecyclerViewFriendsListAdapter(getActivity(), friendsList, false);
        friendsRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private String getLastActiveDate(Date date) {
        DateFormat dt = new SimpleDateFormat("dd/MM/yyy", Locale.getDefault());
        return dt.format(date);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        ArrayList<LatLng> markerPoints = new ArrayList<>();

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setTrafficEnabled(false);
        map.setIndoorEnabled(false);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.addMarker(new MarkerOptions().position(mapsLocation)
                .title(parkTitle));
        map.moveCamera(CameraUpdateFactory.newLatLng(mapsLocation));
        map.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }

    private class AsyncFriends extends AsyncTask<Object, Integer, Void> {
        @Override
        protected void onPreExecute() {
            asyncOnPre();
        }

        @Override
        protected Void doInBackground(Object... params) {
            asyncQueryUserClass();
            return null;
        }
    }

    private String friendsObjectID;
    private List<ParseObject> friendObjects = new ArrayList<>();

    private void asyncQueryUserClass() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (ParseUser j : objects) {
                        try {
                            JSONArray sashidoFriends = j.getJSONArray("Friends");
                            if (sashidoFriends != null) {
                                if (sashidoFriends.length() > 0) {
                                    friendsActivityTv.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < sashidoFriends.length(); i++) {
                                        friendsObjectID = sashidoFriends.optString(i);
                                        if (friendsObjectID != null) {
                                            friendObjects.add(ParseObject.createWithoutData("_User", friendsObjectID));
                                        }
                                    }
                                    ParseObject.fetchAll(friendObjects);
                                    getFriendsDetails();
                                } else {
                                    Log.e("debug", "setVisibility1");
                                    friendsActivityTv.setVisibility(View.GONE);
                                    friendsRecycler.setVisibility(View.GONE);
                                }
                            } else {
                                Log.e("debug", "setVisibility2");
                                friendsActivityTv.setVisibility(View.GONE);
                                friendsRecycler.setVisibility(View.GONE);
                            }
                        } catch (ParseException e1) {
                            Log.e("debug", "failed: " + e1.getLocalizedMessage());
                            e1.printStackTrace();
                        }
                    }
                } else {
                    Log.e("failed", "Failed " + e.getMessage());
                }
            }
        });
    }

    private View.OnClickListener customListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nearest_park_button:
                    TextView nearestParkButton = (TextView) v;
                    if (SharedPreferencesManager.getString(getContext(), "locale").equals("au")) {
                        Intent intent = new Intent(getActivity(), AUSParksActivity.class);
                        startActivity(intent);
                    } else {
                        if (nearestParkButton.getText().equals(getActivity().getResources().getString(R.string.findnearestpark))) {
                            nearestParkRecyclerView.setVisibility(View.VISIBLE);
                            nearestParkButton.setText(getActivity().getResources().getString(R.string.hidenearestparks));
                            populateNearestParkRecyclerView();
                        } else {
                            nearestParkButton.setText(getActivity().getResources().getString(R.string.findnearestpark));
                            nearestParkRecyclerView.setVisibility(View.GONE);
                        }
                    }
                    break;
                case R.id.connect_google_fit:
                    connectGoogleFit();
                    break;
            }
        }
    };



    private void populateNearestParkRecyclerView() {
        final ArrayList<NearestParkModel> nearestParkList = new ArrayList<>();
        final boolean[] isWithin100Miles = new boolean[1];
        final ParseGeoPoint userLocation = ((DashboardActivity) getActivity()).generateGeoPointLocation();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(LOCATIONS_CLASS_KEY);
        query.setLimit(6);
        query.whereWithinKilometers(LOCATION_STARTING_POINT_KEY, userLocation, ONE_HUNDRED_MILES);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                isWithin100Miles[0] = objects.size() > 0;
                if (isWithin100Miles[0]) {
                    for (ParseObject j : objects) {
                        double distance = distFrom(userLocation.getLatitude(), userLocation.getLongitude(), j.getParseGeoPoint(LOCATION_STARTING_POINT_KEY).getLatitude(), j.getParseGeoPoint(LOCATION_STARTING_POINT_KEY).getLongitude());
                        DecimalFormat df = new DecimalFormat("#.00");
                        String distanceFormated = df.format(distance);
                        nearestParkList.add(new NearestParkModel(j.getString(LOCATION_NAME_KEY), distanceFormated + " Miles", j.getParseGeoPoint(LOCATION_STARTING_POINT_KEY).getLatitude(), j.getParseGeoPoint(LOCATION_STARTING_POINT_KEY).getLongitude()));
                    }
                    RecyclerViewNearestParkAdapter adapter = new RecyclerViewNearestParkAdapter(getContext(), nearestParkList);
                    adapter.setOnItemClickListener(new RecyclerViewNearestParkAdapter.onNearestParkItemClickListener() {
                        @Override
                        public void onItemClickListener(View view, int position, NearestParkModel model) {
                            Log.e("latLong", model.getLatitude() + " " + model.getLongitude());
                            Intent intent = new Intent(getActivity(), MapsActivity.class);
                            intent.putExtra("Lat", model.getLatitude());
                            intent.putExtra("Long", model.getLongitude());
                            intent.putExtra("cLat", ((DashboardActivity) getActivity()).utils.getLatitude());
                            intent.putExtra("cLong", ((DashboardActivity) getActivity()).utils.getLongitude());
                            getActivity().startActivity(intent);
                        }
                    });
                    nearestParkRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    homeSV.fullScroll(View.FOCUS_DOWN);
                } else {
                    Log.e("no parks", "no parks");
                }
            }
        });
    }

    private void showsMapsDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_maps);
        dialog.show();
        MapsInitializer.initialize(getActivity());
        mMapView = (MapView) dialog.findViewById(R.id.mapView);
        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();    // needed to get the map to display immediately
        mMapView.getMapAsync(this);

    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371.00;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
        return (float) dist * 0.621;
    }

    private void asyncOnPre() {
        friendsRecycler = (RecyclerView) view.findViewById(R.id.friends_activity_recycler_view);
        friendsRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        friendsRecycler.setLayoutManager(mLayoutManager);
    }

    public static HomeFragment newInstance(int imageNum) {
        final HomeFragment f = new HomeFragment();
        final Bundle args = new Bundle();
        args.putInt("Position", imageNum);
        f.setArguments(args);
        return f;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (profilePicture != null) {
                if (ParseUser.getCurrentUser().getString(USER_PROFILE_PICTURE).equals(NO_PICTURE)) {
                    Picasso.with(getActivity()).load(R.drawable.no_profile).transform(new CircleTransform()).into(profilePicture);
                } else {
                    Picasso.with(getActivity()).load(ParseUser.getCurrentUser().getString(USER_PROFILE_PICTURE)).transform(new CircleTransform()).into(profilePicture);
                }
            }

            if (userHearts != null) {
                userHearts.setText(String.valueOf(ParseUser.getCurrentUser().getInt(USER_HEARTS)));
            }
            //add by chang
//            if(heartsOrangeCount != null){
//
//                try {
//                    heartsOrangeCount.setText(String.valueOf(ParseUser.getCurrentUser().fetchIfNeeded().getInt(THIS_WEEK_HEARTS)));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            if(heartsGoldCount != null){
//                try {
//                    heartsGoldCount.setText(String.valueOf(ParseUser.getCurrentUser().fetchIfNeeded().getInt(BEST_WEEK_HEARTS)));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }

        }
    }

    /*            *
     *            *
     * GOOGLE FIT *
     *            *
     *            */

    private void connectGoogleFit() {
        super.onStart();
        mApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e( "GoogleFit", "CONNECTED" );
        utils.makeText("Successfully linked with Google Fit.", Toast.LENGTH_SHORT);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e( "GoogleFit", "CONNECTION SUSPENDED" );
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if( !authInProgress ) {
            utils.makeText("Failed to link with Google Fit. Please try again.", Toast.LENGTH_SHORT);
            try {
                authInProgress = true;
                connectionResult.startResolutionForResult( this.getActivity(), REQUEST_OAUTH );
            } catch(IntentSender.SendIntentException e ) {

            }
        } else {
            Log.e( "GoogleFit", "authInProgress" );
        }
    }

    @Override
    public void onDataPoint(DataPoint dataPoint) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == REQUEST_OAUTH ) {
            authInProgress = false;
            if( resultCode == RESULT_OK ) {
                if( !mApiClient.isConnecting() && !mApiClient.isConnected() ) {
                    Log.e( "GoogleFit", "CONNECTED" );
                    mApiClient.connect();
                }
            } else if( resultCode == RESULT_CANCELED ) {
                Log.e( "GoogleFit", "RESULT_CANCELED" );
            }
        } else {
            Log.e("GoogleFit", "requestCode NOT request_oauth");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Fitness.SensorsApi.remove( mApiClient, this )
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            mApiClient.disconnect();
                        }
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }
}
