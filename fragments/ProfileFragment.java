package icn.proludic.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import icn.proludic.DashboardActivity;
import icn.proludic.R;
import icn.proludic.adapters.RecyclerViewAchievementsAdapter;
import icn.proludic.adapters.RecyclerViewFriendRequestsAdapter;
import icn.proludic.adapters.RecyclerViewFriendsListAdapter;
import icn.proludic.adapters.RecyclerViewStatsAdapter;
import icn.proludic.misc.CircleTransform;
import icn.proludic.misc.Constants;
import icn.proludic.misc.CustomComparator;
import icn.proludic.misc.ExifUtil;
import icn.proludic.misc.SashidoHelper;
import icn.proludic.misc.SharedPreferencesManager;
import icn.proludic.misc.Utils;
import icn.proludic.models.AchievementModel;
import icn.proludic.models.FriendRequestsModel;
import icn.proludic.models.FriendsModel;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.view.View.GONE;
import static icn.proludic.misc.Constants.AFRICAN_ELEPHANT_COL;
import static icn.proludic.misc.Constants.ALL_TIME;
import static icn.proludic.misc.Constants.ASIAN_ELEPHANT_COL;
import static icn.proludic.misc.Constants.ASIAN_GUAR_COL;
import static icn.proludic.misc.Constants.BODYWEIGHT_BRONZE_COL;
import static icn.proludic.misc.Constants.BODYWEIGHT_COPPER_COL;
import static icn.proludic.misc.Constants.BODYWEIGHT_DIAMOND_COL;
import static icn.proludic.misc.Constants.BODYWEIGHT_GOLD_COL;
import static icn.proludic.misc.Constants.BODYWEIGHT_PLATINUM_COL;
import static icn.proludic.misc.Constants.BODYWEIGHT_SILVER_COL;
import static icn.proludic.misc.Constants.BURJ_KHALIFA_COL;
import static icn.proludic.misc.Constants.CHALLENGE_BEGIN_COL;
import static icn.proludic.misc.Constants.CROCODILE_COL;
import static icn.proludic.misc.Constants.DATE;
import static icn.proludic.misc.Constants.EIFFEL_TOWER_COL;
import static icn.proludic.misc.Constants.EMPIRE_STATE_BUILDING_COL;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_ACCEPTED;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_CLASS_NAME;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_IS_CHALLENGE;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_IS_COMPLETE;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_IS_PENDING;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_LENGTH;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_USER_REQUESTED;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_USER_REQUESTING;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_WEIGHT;
import static icn.proludic.misc.Constants.GETTING_STARTED_COL;
import static icn.proludic.misc.Constants.GIRAFFE_COL;
import static icn.proludic.misc.Constants.GOLDEN_GATE_BRIDGE_COL;
import static icn.proludic.misc.Constants.HIPPOPOTAMUS_COL;
import static icn.proludic.misc.Constants.HOME_PARK_KEY;
import static icn.proludic.misc.Constants.KEEPING_THE_PEACE_COL;
import static icn.proludic.misc.Constants.KODIAK_BEAR_COL;
import static icn.proludic.misc.Constants.LENGTH_LONG;
import static icn.proludic.misc.Constants.LENGTH_SHORT;
import static icn.proludic.misc.Constants.LONDON_EYE_COL;
import static icn.proludic.misc.Constants.MONTHLY;
import static icn.proludic.misc.Constants.NO_DATE;
import static icn.proludic.misc.Constants.NO_PICTURE;
import static icn.proludic.misc.Constants.PROFILE_IS_FRIEND;
import static icn.proludic.misc.Constants.PROFILE_PERFECT_COL;
import static icn.proludic.misc.Constants.PROLUDIC_BRONZE_COL;
import static icn.proludic.misc.Constants.PROLUDIC_COPPER_COL;
import static icn.proludic.misc.Constants.PROLUDIC_DIAMOND_COL;
import static icn.proludic.misc.Constants.PROLUDIC_GOLD_COL;
import static icn.proludic.misc.Constants.PROLUDIC_PLATINUM_COL;
import static icn.proludic.misc.Constants.PROLUDIC_SILVER_COL;
import static icn.proludic.misc.Constants.REGISTRATION_COL;
import static icn.proludic.misc.Constants.SOCIALIZE_COL;
import static icn.proludic.misc.Constants.SOCIAL_BUZZ_COL;
import static icn.proludic.misc.Constants.TODAY;
import static icn.proludic.misc.Constants.TRACKED_EVENTS_CLASS_NAME;
import static icn.proludic.misc.Constants.TRACKED_HEARTS;
import static icn.proludic.misc.Constants.TRACKED_TOTAL_EXERCISES;
import static icn.proludic.misc.Constants.USER;
import static icn.proludic.misc.Constants.USER_DESCRIPTION;
import static icn.proludic.misc.Constants.USER_DRAW;
import static icn.proludic.misc.Constants.USER_FULL_NAME;
import static icn.proludic.misc.Constants.USER_HEARTS;
import static icn.proludic.misc.Constants.USER_LOSSES;
import static icn.proludic.misc.Constants.USER_PROFILE_PICTURE;
import static icn.proludic.misc.Constants.USER_WINS;
import static icn.proludic.misc.Constants.VICTORIOUS_COL;
import static icn.proludic.misc.Constants.WEEKLY;
import static icn.proludic.misc.Constants.WEEKLY_WORKER_COL;
import static icn.proludic.misc.Constants.WHALE_SHARK_COL;
import static icn.proludic.misc.Constants.WHITE_RHINOCEROS_COL;
import static icn.proludic.misc.Constants.WORKING_OUT_COL;

/**
 * Author:  Bradley Wilson
 * Date: 11/04/2017
 * Package: icn.proludic.fragments
 * Project Name: proludic
 */

public class ProfileFragment extends Fragment {

    private RecyclerView friendsRecyclerView, statsRecyclerView;
    private RecyclerView achievementsRecyclerView;
    private EditText userProfileDescription;
    private TextView saveButton, descriptionCharLength, totalExercises, totalHearts;
    private LinearLayout addFriendButton, inviteFriendsButton;
    private Dialog friendsDialog;
    private View view;
    private TextView friendsTV;
    private int count;
    private ImageView profilePicture, rankIcon;
    private int SELECT_FILE = 100;
    private String picturePath;
    private ImageView cameraIcon;
    private TextView userHearts;
    private LinearLayout statsContainer;
    private boolean isFriend;
    private ParseUser friendObject;
    private Utils utils;
    private FriendsModel model;
    private ArrayList<String> userAchievementNames;

    final static int PERMISSIONS_REQUEST_CAMERA = 1;
    final static int PERMISSIONS_REQUEST_STORAGE = 2;
    final static int REQUEST_PHOTO_CAPTURE = 3;
    final static int REQUEST_SELECT_PHOTO = 4;
    final static String TAG = "ProfileFragment";

    //add by chang start
    private static String TAG_f = ProfileFragment.class.getName();
    private  SwipeRefreshLayout mySwipeRefreshLayout;
    //add by chang end

    private CallbackManager callbackManager;

    ShareDialog shareDialog;




    //add by chang start
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize facebook SDK.
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        // Create a callbackManager to handle the login responses.
        callbackManager = CallbackManager.Factory.create();

        shareDialog = new ShareDialog(this);

        // this part is optional
        shareDialog.registerCallback(callbackManager, callback);
    }
    //add by chang end

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        isFriend = getArguments().getBoolean(Constants.PROFILE_IS_FRIEND);
        utils = ((DashboardActivity) getActivity()).utils;
        if (isFriend) {
            model = (FriendsModel) getArguments().getSerializable(Constants.FRIENDS_MODEL_KEY);
            if (model != null) {
                friendObject = (ParseUser) ParseObject.createWithoutData("_User", model.getObjectId());
                try {
                    initViews(view, model);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), R.string.model_is_null, Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                initViews(view, null);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    //add by chang start
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Log.e("debug", "11111111111111111111111");
        setLinkShare(view);
    }
    //add by chang end



    private void checkFriendRequests(View view) throws ParseException {
        TextView badge = (TextView) view.findViewById(R.id.notification_badge);
        count = ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTED, ParseUser.getCurrentUser()).whereEqualTo(FRIEND_REQUESTS_IS_PENDING, true).count();
        if (badge != null) {
            if (count > 0) {
                badge.setVisibility(View.VISIBLE);
                badge.setText(String.valueOf(count));
            } else {
                badge.setVisibility(View.GONE);
            }
        }
    }

    void startMyTask(AsyncTask<Object, Integer, Void> asyncTask) {
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    // adds earned achievements to list
    public void getCurrentUserAchievements() {
        userAchievementNames = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserAchievements");
        if (isFriend) {
            query.whereEqualTo(USER, friendObject);
        } else {
            query.whereEqualTo(USER, ParseUser.getCurrentUser());
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject j : objects) {
                            if (j.getBoolean(KODIAK_BEAR_COL)) {
                                userAchievementNames.add(getString(R.string.kodiak_bear));
                            }
                            if (j.getBoolean(CROCODILE_COL)) {
                                userAchievementNames.add(getString(R.string.crocodile));
                            }
                            if (j.getBoolean(ASIAN_GUAR_COL)) {
                                userAchievementNames.add(getString(R.string.asian_guar));
                            }
                            if (j.getBoolean(GIRAFFE_COL)) {
                                userAchievementNames.add(getString(R.string.giraffe));
                            }
                            if (j.getBoolean(HIPPOPOTAMUS_COL)) {
                                userAchievementNames.add(getString(R.string.hippopotamus));
                            }
                            if (j.getBoolean(WHITE_RHINOCEROS_COL)) {
                                userAchievementNames.add(getString(R.string.white_rhinoceros));
                            }
                            if (j.getBoolean(ASIAN_ELEPHANT_COL)) {
                                userAchievementNames.add(getString(R.string.asian_elephant));
                            }
                            if (j.getBoolean(AFRICAN_ELEPHANT_COL)) {
                                userAchievementNames.add(getString(R.string.african_elephant));
                            }
                            if (j.getBoolean(WHALE_SHARK_COL)) {
                                userAchievementNames.add(getString(R.string.whale_shark));
                            }
                            if (j.getBoolean(LONDON_EYE_COL)) {
                                userAchievementNames.add(getString(R.string.london_eye));
                            }
                            if (j.getBoolean(EIFFEL_TOWER_COL)) {
                                userAchievementNames.add(getString(R.string.eiffel_tower));
                            }
                            if (j.getBoolean(EMPIRE_STATE_BUILDING_COL)) {
                                userAchievementNames.add(getString(R.string.empire_state_building));
                            }
                            if (j.getBoolean(BURJ_KHALIFA_COL)) {
                                userAchievementNames.add(getString(R.string.burj_khalifa));
                            }
                            if (j.getBoolean(GOLDEN_GATE_BRIDGE_COL)) {
                                userAchievementNames.add(getString(R.string.golden_gate_bridge));
                            }
                            if (j.getBoolean(GETTING_STARTED_COL)) {
                                userAchievementNames.add(getString(R.string.getting_started));
                            }
                            if (j.getBoolean(WORKING_OUT_COL)) {
                                userAchievementNames.add(getString(R.string.working_out));
                            }
                            if (j.getBoolean(PROFILE_PERFECT_COL)) {
                                userAchievementNames.add(getString(R.string.profile_perfect));
                            }
                            if (j.getBoolean(SOCIALIZE_COL)) {
                                userAchievementNames.add(getString(R.string.socialize));
                            }
                            if (j.getBoolean(SOCIAL_BUZZ_COL)) {
                                userAchievementNames.add(getString(R.string.social_buzz));
                            }
                            if (j.getBoolean(CHALLENGE_BEGIN_COL)) {
                                userAchievementNames.add(getString(R.string.challenge_begin));
                            }
                            if (j.getBoolean(VICTORIOUS_COL)) {
                                userAchievementNames.add(getString(R.string.victorious));
                            }
                            if (j.getBoolean(WEEKLY_WORKER_COL)) {
                                userAchievementNames.add(getString(R.string.weekly_worker));
                            }
                            if (j.getBoolean(KEEPING_THE_PEACE_COL)) {
                                userAchievementNames.add(getString(R.string.keeping_the_peace));
                            }
                            if (j.getBoolean(REGISTRATION_COL)) {
                                userAchievementNames.add(getString(R.string.registration));
                            }
                            if (j.getBoolean(PROLUDIC_COPPER_COL)) {
                                userAchievementNames.add(getString(R.string.proludic_copper));
                            }
                            if (j.getBoolean(PROLUDIC_BRONZE_COL)) {
                                userAchievementNames.add(getString(R.string.proludic_bronze));
                            }
                            if (j.getBoolean(PROLUDIC_SILVER_COL)) {
                                userAchievementNames.add(getString(R.string.proludic_silver));
                            }
                            if (j.getBoolean(PROLUDIC_GOLD_COL)) {
                                userAchievementNames.add(getString(R.string.proludic_gold));
                            }
                            if (j.getBoolean(PROLUDIC_PLATINUM_COL)) {
                                userAchievementNames.add(getString(R.string.proludic_platinum));
                            }
                            if (j.getBoolean(PROLUDIC_DIAMOND_COL)) {
                                userAchievementNames.add(getString(R.string.proludic_diamond));
                            }
                            if (j.getBoolean(BODYWEIGHT_COPPER_COL)) {
                                userAchievementNames.add(getString(R.string.bodyweight_copper));
                            }
                            if (j.getBoolean(BODYWEIGHT_BRONZE_COL)) {
                                userAchievementNames.add(getString(R.string.bodyweight_bronze));
                            }
                            if (j.getBoolean(BODYWEIGHT_SILVER_COL)) {
                                userAchievementNames.add(getString(R.string.bodyweight_silver));
                            }
                            if (j.getBoolean(BODYWEIGHT_GOLD_COL)) {
                                userAchievementNames.add(getString(R.string.bodyweight_gold));
                            }
                            if (j.getBoolean(BODYWEIGHT_PLATINUM_COL)) {
                                userAchievementNames.add(getString(R.string.bodyweight_platinum));
                            }
                            if (j.getBoolean(BODYWEIGHT_DIAMOND_COL)) {
                                userAchievementNames.add(getString(R.string.bodyweight_diamond));
                            }
                            Log.e("debug",Integer.toString(userAchievementNames.size()));
                            populateAchievementRecyclerView(userAchievementNames);
                        }
                    } else {
                        Toast.makeText(getContext(), R.string.account_deactivated, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("failed", "failed" + e.getLocalizedMessage());
                }
            }
        });
    }

    private class AsyncFriends extends AsyncTask<Object, Integer, Void> {
        @Override
        protected void onPreExecute() {
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
                                    friendsRecyclerView.setVisibility(View.VISIBLE);
                                    friendsTV.setVisibility(View.INVISIBLE);
                                    for (int i = 0; i < sashidoFriends.length(); i++) {
                                        friendsObjectID = sashidoFriends.optString(i);
                                        if (friendsObjectID != null) {
                                            friendObjects.add(ParseObject.createWithoutData("_User", friendsObjectID));
                                        }
                                    }
                                    ParseObject.fetchAll(friendObjects);
                                    getFriendsDetails();
                                } else {
                                    friendsTV.setVisibility(View.VISIBLE);
                                    friendsRecyclerView.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                friendsTV.setVisibility(View.VISIBLE);
                                friendsRecyclerView.setVisibility(View.INVISIBLE);
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                    Log.e("failed", "Failed " + e.getMessage());
                }
            }
        });
    }



    //add by chang start
    private void myUpdateOperation(){
          getCurrentUserAchievements();

//        friendObjects.clear();
//        System.out.println("======="+friendObjects.size());     //??????????why it will have duplicate friends.
//        populateFriendsList(view);
//        System.out.println("======="+friendObjects.size());


        mySwipeRefreshLayout.setRefreshing(false);

    }
    //add by chang end

    //add by chang start
    private void showChallengeFragment(){
        ChallengeFriendsFragment frag = new ChallengeFriendsFragment();
        FragmentTransaction trans;
        trans = getFragmentManager().beginTransaction();
        trans.setCustomAnimations(R.anim.slide_in, R.anim.slide_out_right, R.anim.slide_in, R.anim.slide_out_right);
        trans.addToBackStack("challenge_friends_fragment");
        trans.replace(R.id.dashboard_fragment_container, frag);
        trans.commit();
        Log.e("debug", "------------------------------------------------------");

    }


    //add by chang end

    private void initViews(View view, FriendsModel model) throws ParseException {
        TextView achievementsHeading = (TextView) view.findViewById(R.id.achievements_title);
        profilePicture = (ImageView) view.findViewById(R.id.profile_picture);
        rankIcon = (ImageView) view.findViewById(R.id.rank_icon);
        TextView userFullName = (TextView) view.findViewById(R.id.user_full_name);
        TextView userName = (TextView) view.findViewById(R.id.user_name);
        TextView userHomePark = view.findViewById(R.id.home_park);
        userHearts = (TextView) view.findViewById(R.id.user_hearts);
        TextView editProfileButton = (TextView) view.findViewById(R.id.edit_profile_button);
        friendsTV = (TextView) view.findViewById(R.id.no_friends_tv);
        userProfileDescription = (EditText) view.findViewById(R.id.profile_description);
        saveButton = (TextView) view.findViewById(R.id.save_description_button);
        addFriendButton = (LinearLayout) view.findViewById(R.id.add_friend_container);
        inviteFriendsButton = view.findViewById(R.id.invite_friends_container);
        FrameLayout notificationsButton = (FrameLayout) view.findViewById(R.id.notifications_button);
        descriptionCharLength = (TextView) view.findViewById(R.id.description_char_length);
        LinearLayout statsParentContainer = (LinearLayout) view.findViewById(R.id.stats_parent_container);
        LinearLayout friendsParentContainer = (LinearLayout) view.findViewById(R.id.friends_container);
        //add by chang start
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_profile);

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

        //add by chang start
        TextView challengeFriendsButton =(TextView) view.findViewById(R.id.challenge_friends_button);
        challengeFriendsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showChallengeFragment();
            }
        });
        //add by chang end
        if (isFriend) {
            statsParentContainer.setVisibility(GONE);
            friendsParentContainer.setVisibility(GONE);
            if (determineIfChallengeOrNot(friendObject)) {
                editProfileButton.setText(R.string.view_challenge);
            } else {
                editProfileButton.setText(getString(R.string.challenge_friend));
            }
            ViewGroup.LayoutParams lp = editProfileButton.getLayoutParams();
            lp.width = utils.convertDpToPixels(150);
            editProfileButton.setLayoutParams(lp);
            notificationsButton.setVisibility(GONE);

            if (model.getProfilePicture() instanceof String) {
                Picasso.with(getActivity()).load((String) model.getProfilePicture()).transform(new CircleTransform()).into(profilePicture);
            } else {
                Picasso.with(getActivity()).load(R.drawable.no_profile).transform(new CircleTransform()).into(profilePicture);
            }
            achievementsHeading.setText(R.string.friends_achievements);
            userFullName.setVisibility(GONE);
            userName.setText(model.getUsername());
            userHomePark.setText(utils.getHomeParkLocation(model.getHomePark()));
            userHearts.setText(Integer.toString(model.getHearts()));
            userProfileDescription.setText(model.getDescription());

            setWDL(view);
            populateAchievements(view);
            editProfileButton.setOnClickListener(customListener);
        } else {
            ViewGroup.LayoutParams lp = editProfileButton.getLayoutParams();
            lp.width = utils.convertDpToPixels(50);
            editProfileButton.setLayoutParams(lp);
            userProfileDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    descriptionCharLength.setText("0");
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (userProfileDescription.isEnabled()) {
                        descriptionCharLength.setVisibility(View.VISIBLE);
                        descriptionCharLength.setText(String.valueOf(s.length()));
                        if (s.length() > 140 || s.length() < 20) {
                            descriptionCharLength.setTextColor(Color.RED);
                            descriptionCharLength.setTypeface(descriptionCharLength.getTypeface(), Typeface.BOLD);
                        } else {
                            descriptionCharLength.setTextColor(Color.BLACK);
                            descriptionCharLength.setTypeface(descriptionCharLength.getTypeface(), Typeface.NORMAL);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            addFriendButton.setOnClickListener(customListener);
            inviteFriendsButton.setOnClickListener(customListener);
            notificationsButton.setOnClickListener(customListener);
            cameraIcon = (ImageView) view.findViewById(R.id.upload_photo_icon);
            populateFriendsList(view);
            boolean isEmail = ((DashboardActivity) getActivity()).utils.populateUserProfileDetails(getActivity(), userFullName, userName, profilePicture, userHearts, userProfileDescription, userHomePark);
            if (isEmail) {
                cameraIcon.setVisibility(View.VISIBLE);
                cameraIcon.setOnClickListener(customListener);
            }
            achievementsHeading.setText(R.string.your_achievements);
            setWDL(view);
            populateAchievements(view);
            editProfileButton.setOnClickListener(customListener);
            populateStatsRecycler(view);
        }
    }

    private void updateRankIcon() {
        String rank;
        Log.e("debug", "updateRankIcon " + Integer.toString(userAchievementNames.size()));
        if (userAchievementNames.contains(getString(R.string.proludic_diamond)) && userAchievementNames.contains(getString(R.string.bodyweight_diamond))) {
            rank = getString(R.string.diamond);
        }
        else if (userAchievementNames.contains(getString(R.string.proludic_platinum)) && userAchievementNames.contains(getString(R.string.bodyweight_platinum))) {
            rank = getString(R.string.platinum);
        }
        else if (userAchievementNames.contains(getString(R.string.proludic_gold)) && userAchievementNames.contains(getString(R.string.bodyweight_gold))) {
            rank = getString(R.string.gold);
        }
        else if (userAchievementNames.contains(getString(R.string.proludic_silver)) && userAchievementNames.contains(getString(R.string.bodyweight_silver))) {
            rank = getString(R.string.silver);
        }
        else if (userAchievementNames.contains(getString(R.string.proludic_bronze)) && userAchievementNames.contains(getString(R.string.bodyweight_bronze))) {
            rank = getString(R.string.bronze);
        }
        else if (userAchievementNames.contains(getString(R.string.proludic_copper)) && userAchievementNames.contains(getString(R.string.bodyweight_copper))) {
            rank = getString(R.string.copper);
        }
        else {
            rankIcon.setVisibility(GONE);
            return;
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rank").whereEqualTo("rankName", rank);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Picasso.with(getContext()).load(objects.get(0).getParseFile("rankImage").getUrl()).into(rankIcon);
                rankIcon.setVisibility(View.VISIBLE);
            }
        });
    }

    private void populateAchievements(View view) {
        achievementsRecyclerView = (RecyclerView) view.findViewById(R.id.achievements_recycler_view);
        achievementsRecyclerView.setHasFixedSize(true);
        GridLayoutManager aLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
        achievementsRecyclerView.setLayoutManager(aLayoutManager);
        getCurrentUserAchievements();
    }

    private void populateAchievementRecyclerView(final ArrayList<String> userAchievementNames) {
        final ArrayList<AchievementModel> achievementsList = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Achievements");
        query.orderByAscending("HeartsReceived");
        query.whereEqualTo("isFrench", SharedPreferencesManager.getString(getContext(), "locale").equals("fr"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.e("debug", "number of achievements: " + Integer.toString(objects.size()));
                    for (ParseObject j : objects) {
                        boolean isUnlocked;
                        isUnlocked = userAchievementNames.size() != 0 && userAchievementNames.contains(j.getString("AchievementName"));
                        // pulls only non-community, weight-based achievements
                        //if (j.getInt("Weight") > 0) {
                            achievementsList.add(new AchievementModel(getContext(), j.getString("AchievementName"), j.getParseFile("AchievementImage").getUrl(), j.getString("AchievementDescription"), j.getInt("Weight"), j.getInt("HeartsReceived"), j.getBoolean("IsCommunity"), isUnlocked));
                        //}
                    }
                    RecyclerViewAchievementsAdapter adapter = new RecyclerViewAchievementsAdapter(getActivity(), achievementsList);
                    adapter.setOnItemClickListener(new RecyclerViewAchievementsAdapter.onAchievementItemClickListener() {
                        @Override
                        public void onItemClickListener(View view, int position, String name, String description, String image, int weight) {
                            final Dialog dialog = new Dialog(getContext(), R.style.customAlertDialog);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_achievements);

                            TextView achievementName = (TextView) dialog.findViewById(R.id.achievement_title);
                            achievementName.setText(name);

                            ImageView achievementImage = (ImageView) dialog.findViewById(R.id.achievement_image);

                            if (!(achievementsList.get(position).isStatus())) {
                                achievementImage.setColorFilter(
                                        new PorterDuffColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN));
                            } else {
                                achievementImage.clearColorFilter();
                            }

                            Picasso.with(getActivity()).load(image).into(achievementImage);

                            TextView achievementDescription = (TextView) dialog.findViewById(R.id.achievement_description);
                            achievementDescription.setText(description);

                            TextView dialogClose = (TextView) dialog.findViewById(R.id.dialog_close);
                            dialogClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    });
                    achievementsRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    updateRankIcon();
                    Log.e("debug", "RecyclerView " + Integer.toString(userAchievementNames.size()));
                } else {
                    Log.e("Failed", "failed" + e.getLocalizedMessage());
                }
            }
        });
    }

    private void populateFriendsList(View view) {
        friendsRecyclerView = (RecyclerView) view.findViewById(R.id.friends_activity_recycler_view);
        friendsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager fLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        friendsRecyclerView.setLayoutManager(fLayoutManager);
        startMyTask(new AsyncFriends());
    }

    private void populateStatsRecycler(View view) {
        statsContainer = (LinearLayout) view.findViewById(R.id.stats_container);
        totalExercises = (TextView) view.findViewById(R.id.number_of_exercises);
        totalHearts = (TextView) view.findViewById(R.id.number_of_hearts);
        statsRecyclerView = (RecyclerView) view.findViewById(R.id.stats_recycler_view);
        statsRecyclerView.setHasFixedSize(false);
        LinearLayoutManager sLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        statsRecyclerView.setLayoutManager(sLayoutManager);
        String[] statsList = {getActivity().getResources().getString(R.string.today), getActivity().getResources().getString(R.string.weekly), getActivity().getResources().getString(R.string.monthly), getActivity().getResources().getString(R.string.all_time)};
        RecyclerViewStatsAdapter adapter = new RecyclerViewStatsAdapter(statsList);
        statsRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerViewStatsAdapter.onStatsClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                switch (position) {
                    case 0:
                        statsRecyclerView.findViewHolderForAdapterPosition(0).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryHalf));
                        statsRecyclerView.findViewHolderForAdapterPosition(1).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
                        statsRecyclerView.findViewHolderForAdapterPosition(2).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
                        statsRecyclerView.findViewHolderForAdapterPosition(3).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
                        getTotalExercisesAndHearts(TODAY);
                        break;
                    case 1:
                        statsRecyclerView.findViewHolderForAdapterPosition(1).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryHalf));
                        statsRecyclerView.findViewHolderForAdapterPosition(0).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
                        statsRecyclerView.findViewHolderForAdapterPosition(2).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
                        statsRecyclerView.findViewHolderForAdapterPosition(3).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
                        getTotalExercisesAndHearts(WEEKLY);
                        break;
                    case 2:
                        statsRecyclerView.findViewHolderForAdapterPosition(2).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryHalf));
                        statsRecyclerView.findViewHolderForAdapterPosition(0).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
                        statsRecyclerView.findViewHolderForAdapterPosition(1).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
                        statsRecyclerView.findViewHolderForAdapterPosition(3).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
                        getTotalExercisesAndHearts(MONTHLY);
                        break;
                    case 3:
                        statsRecyclerView.findViewHolderForAdapterPosition(3).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryHalf));
                        statsRecyclerView.findViewHolderForAdapterPosition(0).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
                        statsRecyclerView.findViewHolderForAdapterPosition(1).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
                        statsRecyclerView.findViewHolderForAdapterPosition(2).itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
                        getTotalExercisesAndHearts(ALL_TIME);
                        break;
                }
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void getTotalExercisesAndHearts(String timeString) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TRACKED_EVENTS_CLASS_NAME);
        final int[] tExercises = {0};
        final int[] tHearts = {0};
        switch (timeString) {
            case TODAY:
                String date = ((DashboardActivity) getActivity()).utils.getTodaysDateString();
                query.whereEqualTo(DATE, date);
                query.whereEqualTo(USER, ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() > 0) {
                                tExercises[0] = objects.get(0).getInt(TRACKED_TOTAL_EXERCISES);
                                tHearts[0] = objects.get(0).getInt(TRACKED_HEARTS);
                                totalExercises.setText(String.valueOf(tExercises[0]));
                                totalHearts.setText(String.valueOf(tHearts[0]));
                                statsContainer.setVisibility(View.VISIBLE);
                            } else if(objects.size()==0){
                                totalExercises.setText("0");
                                totalHearts.setText("0");
                                statsContainer.setVisibility(View.VISIBLE);
                            } else {
                                statsContainer.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.e("failed", "failed");
                        }
                    }
                });
                break;
            case WEEKLY:
                String todaysDate = ((DashboardActivity) getActivity()).utils.getTodaysDateString();
                String lastWeeksDate = ((DashboardActivity) getActivity()).utils.getDateBefore(((DashboardActivity) getActivity()).utils.getTodaysDate(), 7, true);
                List<String> dates = ((DashboardActivity) getActivity()).utils.getDates(lastWeeksDate, todaysDate);
                query.whereEqualTo(USER, ParseUser.getCurrentUser());
                query.whereContainedIn(DATE, dates);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() > 0) {
                                for (ParseObject j : objects) {
                                    tExercises[0] = tExercises[0] + j.getInt(TRACKED_TOTAL_EXERCISES);
                                    tHearts[0] = tHearts[0] + j.getInt(TRACKED_HEARTS);
                                }
                                totalExercises.setText(String.valueOf(tExercises[0]));
                                totalHearts.setText(String.valueOf(tHearts[0]));
                                statsContainer.setVisibility(View.VISIBLE);
                            } else if(objects.size()==0){
                                totalExercises.setText("0");
                                totalHearts.setText("0");
                                statsContainer.setVisibility(View.VISIBLE);
                            }else {
                                statsContainer.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.e("failed", "failed");
                        }
                    }
                });
                break;
            case MONTHLY:
                String tDate = ((DashboardActivity) getActivity()).utils.getTodaysDateString();
                String lastMonths = ((DashboardActivity) getActivity()).utils.getLastMonthsDate();
                List<String> dateArray = ((DashboardActivity) getActivity()).utils.getDates(lastMonths, tDate);
                query.whereEqualTo(USER, ParseUser.getCurrentUser());
                query.whereContainedIn(DATE, dateArray);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() > 0) {
                                for (ParseObject j : objects) {
                                    tExercises[0] = tExercises[0] + j.getInt(TRACKED_TOTAL_EXERCISES);
                                    tHearts[0] = tHearts[0] + j.getInt(TRACKED_HEARTS);
                                }
                                totalExercises.setText(String.valueOf(tExercises[0]));
                                totalHearts.setText(String.valueOf(tHearts[0]));
                                statsContainer.setVisibility(View.VISIBLE);
                            }else if(objects.size()==0){
                                totalExercises.setText("0");
                                totalHearts.setText("0");
                                statsContainer.setVisibility(View.VISIBLE);
                            } else {
                                statsContainer.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.e("failed", "failed");
                        }
                    }
                });
                break;
            case ALL_TIME:
                query.whereEqualTo(USER, ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() > 0) {
                                for (ParseObject j : objects) {
                                    tExercises[0] = tExercises[0] + j.getInt(TRACKED_TOTAL_EXERCISES);
                                    tHearts[0] = tHearts[0] + j.getInt(TRACKED_HEARTS);
                                }
                                totalExercises.setText(String.valueOf(tExercises[0]));
                                totalHearts.setText(String.valueOf(tHearts[0]));
                                statsContainer.setVisibility(View.VISIBLE);
                            }else if(objects.size()==0){
                                totalExercises.setText("0");
                                totalHearts.setText("0");
                                statsContainer.setVisibility(View.VISIBLE);
                            } else {
                                statsContainer.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.e("failed", "failed" + e.getLocalizedMessage());
                        }
                    }
                });
                break;
        }
    }

    private View.OnClickListener customListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.edit_profile_button:
                    TextView tv = (TextView) v;
                    if (tv.getText().toString().equals(getString(R.string.edit))) {
                        userProfileDescription.setEnabled(true);
                        userProfileDescription.requestFocus();
                        cameraIcon.setVisibility(View.VISIBLE);
                        cameraIcon.setOnClickListener(customListener);
                        Selection.setSelection(userProfileDescription.getText(), userProfileDescription.getText().toString().length());
                        saveButton.setVisibility(View.VISIBLE);
                        saveButton.setOnClickListener(customListener);
                    } else {
                        ParseObject po = ParseObject.createWithoutData("_User", model.getObjectId());
                        try {
                            boolean isChallenge = determineIfChallengeOrNot(po);
                            if (!isChallenge) {
                                Log.e("isChallenge", "no challenge exists");
                                showChallengeDialog(model.getUsername(), model.getProfilePicture(), po);
                            } else {
                                Log.e("isChallenge", "is a challenge");
                                showProgressDialog(po);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.save_description_button:
                    if (userProfileDescription.getText().toString().length() == 0) {
                        ((DashboardActivity) getActivity()).utils.makeText(getActivity().getResources().getString(R.string.enterdescription), LENGTH_LONG);
                    } else if (userProfileDescription.getText().toString().length() >= 140) {
                        ((DashboardActivity) getActivity()).utils.makeText(getActivity().getResources().getString(R.string.desctoolong), LENGTH_LONG);
                    } else if (userProfileDescription.getText().toString().length() <= 20) {
                        ((DashboardActivity) getActivity()).utils.makeText(getActivity().getResources().getString(R.string.desctooshort), LENGTH_LONG);
                    } else {
                        ParseUser.getCurrentUser().put("Description", userProfileDescription.getText().toString());
                        String output = userProfileDescription.getText().toString().substring(0, 1).toUpperCase() + userProfileDescription.getText().toString().substring(1);
                        userProfileDescription.setText(output);
                        userProfileDescription.setEnabled(false);
                        saveButton.setVisibility(GONE);
                        ParseUser.getCurrentUser().put("Description", userProfileDescription.getText().toString());
                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    descriptionCharLength.setVisibility(View.GONE);
                                    ((DashboardActivity) getActivity()).utils.makeText(getString(R.string.description_saved), LENGTH_LONG);
                                    SashidoHelper.earnAchievement(PROFILE_PERFECT_COL, getString(R.string.profile_perfect), getContext(), getView());
                                    populateAchievements(getView());
                                    cameraIcon.setVisibility(GONE);
                                    if (!ParseUser.getCurrentUser().getString(USER_PROFILE_PICTURE).equals(NO_PICTURE)) {
                                        Picasso.with(getContext()).load(ParseUser.getCurrentUser().getString(USER_PROFILE_PICTURE)).transform(new CircleTransform()).into(profilePicture);
                                    } else {
                                        Picasso.with(getContext()).load(R.drawable.no_profile).transform(new CircleTransform()).into(profilePicture);
                                    }
                                } else {
                                    Log.e("failed", "failed" + e.getLocalizedMessage());
                                }
                            }
                        });
                    }
                    break;
                case R.id.add_friend_container:
                    showAddFriendDialog();
                    break;
                case R.id.invite_friends_container:
                    Log.e("debug", "invite friends clicked");
                    showInviteFriendsDialog();
                    break;
                case R.id.add_friend_button:
                    EditText et = friendsDialog.findViewById(R.id.enter_friends_realname);
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                    query.whereEqualTo("name", et.getText().toString());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(final List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                if (objects.size() > 0) {
                                    if (objects.get(0).getString("name").equals(ParseUser.getCurrentUser().get("name"))) {
                                        ((DashboardActivity) getActivity()).utils.makeText(getString(R.string.cannot_add_yourself), LENGTH_SHORT);
                                    } else if (requestAlreadyExists(ParseUser.getCurrentUser(), objects.get(0), false)) {
                                        ((DashboardActivity) getActivity()).utils.makeText(getString(R.string.already_sent_request), LENGTH_SHORT);
                                    } else if (requestAlreadyExists(objects.get(0), ParseUser.getCurrentUser(), false)) {
                                        ((DashboardActivity) getActivity()).utils.makeText(getString(R.string.already_received_request), LENGTH_SHORT);
                                    }
                                    else {
                                        final ParseObject friendRequest = new ParseObject(FRIEND_REQUESTS_CLASS_NAME);
                                        friendRequest.put(FRIEND_REQUESTS_USER_REQUESTED, objects.get(0));
                                        friendRequest.put(FRIEND_REQUESTS_USER_REQUESTING, ParseUser.getCurrentUser());
                                        friendRequest.put(FRIEND_REQUESTS_IS_PENDING, true);
                                        friendRequest.put(FRIEND_REQUESTS_ACCEPTED, false);
                                        friendRequest.put(FRIEND_REQUESTS_IS_COMPLETE, false);
                                        friendRequest.put(FRIEND_REQUESTS_WEIGHT, false);
                                        friendRequest.put(FRIEND_REQUESTS_IS_CHALLENGE, false);
                                        friendRequest.put(FRIEND_REQUESTS_LENGTH, 0);

                                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                        //add by chang start
                                        installation.put("user",objects.get(0));
                                        installation.saveInBackground();
                                        System.out.println("----------------------------------------------------------------------------------------------"+installation.get("user"));
                                        //add by chang end

                                        friendRequest.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    friendsDialog.dismiss();
                                                    SashidoHelper.earnAchievement(SOCIALIZE_COL, getString(R.string.socialize), getContext(), getView());
                                                    populateAchievements(getView());
                                                    ((DashboardActivity) getActivity()).utils.makeText(getActivity().getResources().getString(R.string.friendrequestsuccess), LENGTH_SHORT);
                                                    //send push notification

                                                    //add by chang start
                                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                                    pushQuery.whereEqualTo("user", objects.get(0));


                                                    ParsePush push = new ParsePush();
                                                    push.setQuery(pushQuery);
                                                    push.setMessage(ParseUser.getCurrentUser().getUsername() +" Just Sent You A Friend Request! ");
                                                    push.sendInBackground();
                                                    //add by chang end

                                                } else {
                                                    ((DashboardActivity) getActivity()).utils.makeText(getActivity().getResources().getString(R.string.friendrequestfailed), LENGTH_SHORT);
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    ((DashboardActivity) getActivity()).utils.makeText(getActivity().getResources().getString(R.string.nouserexists), LENGTH_SHORT);
                                }
                            } else {
                                Log.e("failed", "failed" + e.getLocalizedMessage());
                            }
                        }
                    });
                    break;
                case R.id.notifications_button:
                    showNotificationsDialog();
                    break;
                case R.id.upload_photo_icon:
                    selectImage();
                    /*boolean result = ((DashboardActivity) getActivity()).checkPermission(getActivity());
                    if (result) {
                        ((DashboardActivity) getActivity()).setProfileImage(profilePicture);
                        ((DashboardActivity) getActivity()).showGalleryIntent();
                        Log.e("debug", "showGalleryIntent");
                    }*/
                    break;
            }
        }
    };

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Upload Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    checkCameraPermissions();
                } else if (items[item].equals("Choose from Gallery")) {
                    checkGalleryPermissions();
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void checkCameraPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
            // do we need to show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                // explanation needed
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Camera Permission");
                builder.setMessage("To upload a photo, you need to grant the app permission to access your camera.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CAMERA);
                    }
                });
                builder.create();
                builder.show();
            } else {
                // no explanation needed
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
            }
        } else {
            cameraIntent();
        }
    }

    private void checkGalleryPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            // do we need to show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // explanation needed
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Storage Permission");
                builder.setMessage("To upload a photo, you need to grant the app permission to access your gallery.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_STORAGE);
                    }
                });
                builder.create();
                builder.show();
            } else {
                // no explanation needed
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_STORAGE);
            }
        } else {
            galleryIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PERMISSION_GRANTED) {
                        cameraIntent();
                    } else {
                        utils.makeText("You need to grant this permission to continue.", Toast.LENGTH_SHORT);
                    }
                }
                break;
            case PERMISSIONS_REQUEST_STORAGE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PERMISSION_GRANTED) {
                        galleryIntent();
                    } else {
                        utils.makeText("You need to grant this permission to continue.", Toast.LENGTH_SHORT);
                    }
                }
                break;
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File file = null;
            try {
                file = createImageFile();
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
            if (file != null) {
                Uri uri = FileProvider.getUriForFile(getActivity().getApplicationContext(), "icn.proludic.fileprovider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                Log.e(TAG, "startActivity cameraIntent");
                startActivityForResult(intent, REQUEST_PHOTO_CAPTURE);
            }
        }
    }

    String currentPhotoPath;
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        String fileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryIntent() {
        Log.e(TAG, "enters galleryIntent");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Log.e(TAG, "onActivityResult OK");
            if (requestCode == REQUEST_PHOTO_CAPTURE) {
                onCaptureResult(data);
            } else if (requestCode == REQUEST_SELECT_PHOTO && data != null) {
                onSelectResult(data);
            }
        }
    }


    //test
    private FacebookCallback<Sharer.Result> callback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
            Log.v(TAG, "Successfully posted");
            // Write some code to do some operations when you shared content successfully.
        }

        @Override
        public void onCancel() {
            Log.v(TAG, "Sharing cancelled");
            // Write some code to do some operations when you cancel sharing content.
        }

        @Override
        public void onError(FacebookException error) {
            Log.v(TAG, error.getMessage());
            // Write some code to do some operations when some error occurs while sharing content.
        }
    };


    private void onCaptureResult(Intent data) {
        progress = Snackbar.make(getView(), "Uploading photo...", Snackbar.LENGTH_INDEFINITE);
        progress.show();
        try {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File file = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(file);
            Bitmap bitmap = rotateBitmap(file, contentUri, currentPhotoPath);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            intent.setData(contentUri);
            getActivity().sendBroadcast(intent);
            saveFile(file);
        } catch (IOException e) {
            Snackbar.make(getView(), "Oops! Something went wrong. Try again or get in touch!", Snackbar.LENGTH_LONG).show();
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    private Bitmap rotateBitmap(File file, Uri contentUri, String path) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;    // seems to be the magic number!
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        bitmap = ExifUtil.rotateBitmap(path, bitmap);
        return bitmap;
    }

    Snackbar progress;
    private void onSelectResult(Intent data) {
        progress = Snackbar.make(getView(), "Uploading photo...", Snackbar.LENGTH_INDEFINITE);
        progress.show();
        try {
            Uri selectedFile = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedFile, filePathColumn, null, null, null);
            if (cursor != null) {
                Log.e(TAG, "Cursor not null");
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                File file = new File(filePath);
                Bitmap bitmap = rotateBitmap(file, selectedFile, filePath);
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
                cursor.close();
                Log.e(TAG, "About to save file");
                saveFile(file);
            }
        } catch (IOException e) {
            Snackbar.make(getView(), "Oops! Something went wrong. Try again or get in touch!", Snackbar.LENGTH_LONG).show();
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    private ParseFile parseFile;
    private void saveFile(final File file) {
        parseFile = new ParseFile(file);
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.e(TAG, "Error is null");
                    final String url = parseFile.getUrl();
                    ParseUser.getCurrentUser().put(USER_PROFILE_PICTURE, url);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                progress.dismiss();
                                Picasso.with(getContext()).load(url).transform(new CircleTransform()).into(profilePicture);
                            } else {
                                Log.e(TAG, "failed to save new profile picture: " + e.getLocalizedMessage());
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "Error is not null");
                    Snackbar.make(getView(), "Oops! Something went wrong. Try again or get in touch!", Snackbar.LENGTH_LONG).show();
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        });
    }

    private boolean requestAlreadyExists(ParseObject sender, ParseObject recipient, boolean isChallenge) {
        boolean alreadySent = false;
        ParseQuery<ParseObject> getExistingRequests = ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME);
        getExistingRequests.whereEqualTo(FRIEND_REQUESTS_USER_REQUESTING, sender);
        getExistingRequests.whereEqualTo(FRIEND_REQUESTS_USER_REQUESTED, recipient);
        getExistingRequests.whereEqualTo(FRIEND_REQUESTS_IS_PENDING, true);
        getExistingRequests.whereEqualTo(FRIEND_REQUESTS_IS_CHALLENGE, isChallenge);
        try {
            getExistingRequests.getFirst();
            alreadySent = true;
        } catch (ParseException e) {
            // request does not exist
        }
        return alreadySent;
    }

    // needs fixing
    private void showInviteFriendsDialog() {
        utils.makeText("This feature is currently being updated.", Toast.LENGTH_SHORT);
//        try {
//            Intent i = new Intent(Intent.ACTION_SEND);
//            i.setType("text/plain");
//            i.putExtra(Intent.EXTRA_SUBJECT, "Proludic Sport");
//            String strShareMessage = "\nI'm inviting you to download this app!\n\n";
//            strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
//            Uri screenshotUri = Uri.parse("android.resource://icn.proludic/drawable/logo");
//            i.setType("image/png");
//            i.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//            i.putExtra(Intent.EXTRA_TEXT, strShareMessage);
//            startActivity(Intent.createChooser(i, "Share via"));
//        } catch(Exception e) {
//            // e.toString();
        }
        //test
        private void setLinkShare(View view) {
        //Log.e("debug", "111111111111111111111111");
        ShareLinkContent content = new ShareLinkContent.Builder()

                .setContentTitle("Proludic - Free sports anywhere!")
                .setImageUrl(Uri.parse("android.resource://icn.proludic/drawable/logo"))
                .setContentDescription("Proludic is a fantastic free sport app it is also a good community for you to challenge your friends")
                .setContentUrl(Uri.parse("https://www.facebook.com/proludic/"))
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#Proludic")
                        .build())
                .setQuote("work out everyday and w")
                .build();

        ShareButton shareButton = (ShareButton) view.findViewById(R.id.invite_friends_button);
        shareButton.setShareContent(content);


    }

    private void setWDL(View view) throws ParseException {
        TextView winTV = view.findViewById(R.id.winsTV);
        if (!isFriend) {
            winTV.setText(getString(R.string.win_draw_loss) + " : " + ParseUser.getCurrentUser().getInt(USER_WINS) + " - " + ParseUser.getCurrentUser().getInt(USER_DRAW) + " - " + ParseUser.getCurrentUser().getInt(USER_LOSSES));
        } else {
            winTV.setText(getString(R.string.win_draw_loss) + " : " + friendObject.fetchIfNeeded().getInt(USER_WINS) + " - " + friendObject.fetchIfNeeded().getInt(USER_DRAW) + " - " + friendObject.fetchIfNeeded().getInt(USER_LOSSES));
        }
    }

    private void showNotificationsDialog() {
        try {
            checkFriendRequests(view);
        } catch (ParseException e) {
            Log.e("debug", e.getLocalizedMessage());
        }
        final ArrayList<FriendRequestsModel> requestsList = new ArrayList<>();
        final Dialog notificationsDialog = new Dialog(getContext(), R.style.customAlertDialog);
        notificationsDialog.setContentView(R.layout.dialog_notifications);
        notificationsDialog.setTitle(R.string.requests);
        final RecyclerView notificationsRecyclerView = (RecyclerView) notificationsDialog.findViewById(R.id.notifications_recycler_view);
        notificationsRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager nLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        notificationsRecyclerView.setLayoutManager(nLayoutManager);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME);
        query.whereEqualTo(FRIEND_REQUESTS_IS_PENDING, true);
        query.whereEqualTo(FRIEND_REQUESTS_USER_REQUESTED, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                int c = 0;
                if (e == null) {
                    for (ParseObject j : objects) {
                        try {
                            c++;
                            requestsList.add(new FriendRequestsModel(j.getParseObject(FRIEND_REQUESTS_USER_REQUESTING).fetchIfNeeded().getObjectId(), j.getObjectId(), j.getParseObject(FRIEND_REQUESTS_USER_REQUESTING).fetchIfNeeded().getString(USER_PROFILE_PICTURE), j.getParseObject(FRIEND_REQUESTS_USER_REQUESTING).fetchIfNeeded().getString(USER_FULL_NAME), j.getBoolean(FRIEND_REQUESTS_IS_PENDING), j.getBoolean(FRIEND_REQUESTS_ACCEPTED), j.getBoolean(FRIEND_REQUESTS_IS_CHALLENGE), j.getBoolean(FRIEND_REQUESTS_WEIGHT), j.getInt(FRIEND_REQUESTS_LENGTH)));
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (count > 0) {
                        notificationsDialog.show();
                    } else {
                        ((DashboardActivity) getActivity()).utils.makeText(getContext().getResources().getString(R.string.nonotifications), LENGTH_LONG);
                    }
                    final RecyclerViewFriendRequestsAdapter adapter = new RecyclerViewFriendRequestsAdapter(getActivity(), requestsList);
                    adapter.setOnItemClickListener(new RecyclerViewFriendRequestsAdapter.onRequestItemClickListener() {
                        @Override
                        public void onItemClickListener(final View view, final int position, final String objectID, final String requestObjectID, boolean isChallenge) {
                            switch (view.getId()) {
                                case R.id.accept_friend_request:
                                    ParseObject po = ParseObject.createWithoutData("_User", objectID);

                                    if (isChallenge) {
                                        // Toast.makeText(getActivity(), "isChallenge", Toast.LENGTH_SHORT).show();
                                        acceptChallengeRequest(adapter, po, requestsList, notificationsDialog, position);
                                        // showNotificationsDialog();
                                    } else {
                                        // Toast.makeText(getActivity(), "isntChallenge", Toast.LENGTH_SHORT).show();
                                        acceptFriendRequest(adapter, po, requestsList, notificationsDialog, position);
                                        // showNotificationsDialog();
                                    }
                                    break;
                                case R.id.decline_friend_request:
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME);
                                    query.whereEqualTo("objectId", requestObjectID);
                                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                                        @Override
                                        public void done(ParseObject object, ParseException e) {
                                            if (e == null) {
                                                objects.get(0).put(FRIEND_REQUESTS_IS_PENDING, false);
                                                objects.get(0).put(FRIEND_REQUESTS_ACCEPTED, false);
                                                objects.get(0).saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        adapter.notifyDataSetChanged();
                                                        notificationsDialog.cancel();
                                                        showNotificationsDialog();
                                                    }
                                                });
                                            } else {
                                                Log.e("failed", "failed" + e.getLocalizedMessage());
                                            }
                                        }
                                    });
                                    break;
                            }
                        }
                    });
                    notificationsRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("failed", "Failed" + e.getLocalizedMessage());
                }
            }
        });
    }

    private void acceptChallengeRequest(final RecyclerViewFriendRequestsAdapter adapter, ParseObject po, final ArrayList<FriendRequestsModel> requestsList, final Dialog notificationsDialog, final int position) {
        ParseQuery<ParseObject> aQuery = ((DashboardActivity) getActivity()).utils.getQuery(ParseUser.getCurrentUser(), po, true, false, true, true);
        aQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject j : objects) {
                        j.put(FRIEND_REQUESTS_IS_PENDING, false);
                        j.put(FRIEND_REQUESTS_ACCEPTED, true);
                        String date = ((DashboardActivity) getActivity()).utils.getTodaysDateString();
                        j.put(DATE, date);
                        j.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    ((DashboardActivity) getActivity()).utils.makeText(getString(R.string.success), LENGTH_SHORT);
                                    removeItems(adapter, position, requestsList, notificationsDialog);
                                    try {
                                        SashidoHelper.earnAchievement(CHALLENGE_BEGIN_COL, getString(R.string.challenge_begin), getContext(), getView());
                                        populateAchievements(getView());
                                        checkFriendRequests(view);
                                    } catch (ParseException e1) {
                                        Log.e("profile", e1.getLocalizedMessage());
                                    }
                                } else {
                                    Log.e(ProfileFragment.class.getSimpleName(), "Failed " + e.getLocalizedMessage());
                                }
                            }
                        });
                    }
                } else {
                    Log.e(ProfileFragment.class.getSimpleName(), "Failed " + e.getLocalizedMessage());
                }
            }
        });
    }

    private void acceptFriendRequest(final RecyclerViewFriendRequestsAdapter adapter, final ParseObject po, final ArrayList<FriendRequestsModel> requestsList, final Dialog notificationsDialog, final int position) {
        Log.e("debug", "acceptFriendRequest");
        ParseQuery<ParseObject> aQuery = ((DashboardActivity) getActivity()).utils.getQuery(ParseUser.getCurrentUser(), po, false, false, true, true);
        aQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.e("debug", "e is not null");
                    Log.e("debug", Integer.toString(objects.size()));
                    for (final ParseObject j : objects) {
                        try {
                            j.put(FRIEND_REQUESTS_ACCEPTED, true);
                            j.put(FRIEND_REQUESTS_IS_PENDING, false);
                            j.put(FRIEND_REQUESTS_IS_COMPLETE, true);
                            ArrayList<String> tempFriends = new ArrayList<>();
                            JSONArray myFriends = j.getParseObject(FRIEND_REQUESTS_USER_REQUESTED).fetchIfNeeded().getJSONArray("Friends");
                            if (myFriends != null) {
                                Log.e("debug", "myFriends is not null");
                                if (myFriends.length() > 0) {
                                    for (int i = 0; i < myFriends.length(); i++) {
                                        tempFriends.add(myFriends.getString(i));
                                    }
                                }
                                Log.e("debug", "check");
                                if (!myFriends.toString().contains(po.fetchIfNeeded().getObjectId())) {
                                    tempFriends.add(po.fetchIfNeeded().getObjectId());
                                }
                            }
                            j.getParseObject(FRIEND_REQUESTS_USER_REQUESTED).put("Friends", tempFriends);
                            Log.e("debug", "put tempFriends");
                            JSONArray theirFriends = j.getParseObject(FRIEND_REQUESTS_USER_REQUESTING).fetchIfNeeded().getJSONArray("Friends");
                            ArrayList<String> newTempFriends = new ArrayList<String>();
                            if (theirFriends != null) {
                                Log.e("debug", "theirFriends are not null");
                                if (!theirFriends.toString().contains(ParseUser.getCurrentUser().getObjectId())) {
                                    Log.e("debug", "theirFriends do not contain the current user");
                                    // j.getParseObject(FRIEND_REQUESTS_USER_REQUESTING).put("Friends", newTempFriends);
                                    HashMap<String, Object> params = new HashMap<>();
                                    params.put("senderUserId", ParseUser.getCurrentUser().getObjectId());
                                    params.put("recipientUserId", po.fetchIfNeeded().getObjectId());
                                    ParseCloud.callFunctionInBackground("AddNewFriend", params, new FunctionCallback<String>() {
                                        public void done(String success, ParseException e) {
                                            if (e == null) {
                                                Log.e("debug", "addNewFriend");
                                            } else {
                                                Log.e("debug", e.getLocalizedMessage());
                                            }
                                        }
                                    });
                                } else {
                                    Log.e("debug", "theirFriends contain the current user");
                                }
                                j.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            ((DashboardActivity) getActivity()).utils.makeText("Success", LENGTH_SHORT);
                                            removeItems(adapter, position, requestsList, notificationsDialog);
                                            try {
                                                Log.e("debug", "checkFriendRequests");
                                                checkFriendRequests(view);
                                            } catch (ParseException e1) {
                                                Log.e("debug1", e1.getLocalizedMessage());
                                                e1.printStackTrace();
                                            }
                                        } else {
                                            Log.e("debug2", e.getLocalizedMessage());
                                            Log.e(ProfileFragment.class.getName(), "failed" + e.getLocalizedMessage());
                                        }
                                    }
                                });
                            }
                        } catch (JSONException | ParseException e1) {
                            Log.e("debug3", e1.getLocalizedMessage());
                            Log.e(ProfileFragment.class.getName(), e1.getMessage());
                        }
                    }
                } else {
                    Log.e("debug4", e.getLocalizedMessage());
                    Log.e(ProfileFragment.class.getName(), "failed" + e.getLocalizedMessage());
                }
            }
        });
    }

    private void removeItems(RecyclerViewFriendRequestsAdapter adapter, int position, ArrayList<FriendRequestsModel> requestsList, Dialog notificationsDialog) {
        if (adapter.getItemCount() == 1) {
            adapter.removeItem(requestsList, position);
            notificationsDialog.cancel();
        } else {
            adapter.removeItem(requestsList, position);
        }
    }

    private void showAddFriendDialog() {
        friendsDialog = new Dialog(getContext(), R.style.customAlertDialog);
        friendsDialog.setTitle(R.string.add_new_friend);
        friendsDialog.setContentView(R.layout.dialog_add_friend);
        TextView addFriendButton = (TextView) friendsDialog.findViewById(R.id.add_friend_button);
        addFriendButton.setOnClickListener(customListener);
        friendsDialog.show();
    }

    public static ProfileFragment newInstance(int position) {
        final ProfileFragment f = new ProfileFragment();
        final Bundle args = new Bundle();
        args.putInt("Position", position);
        args.putBoolean(PROFILE_IS_FRIEND, false);
        f.setArguments(args);
        return f;
    }


    private ArrayList<FriendsModel> friendsList = new ArrayList<>();
    private void getFriendsDetails() throws ParseException {
        for (ParseObject j : friendObjects) {
            String name = j.fetchIfNeeded().getString("name");
            String username = j.fetchIfNeeded().getString("username");
            String profilePicture = j.fetchIfNeeded().getString("profilePicture");
            String homePark = j.fetchIfNeeded().getString(HOME_PARK_KEY);
            int hearts = j.fetchIfNeeded().getInt(USER_HEARTS);
            Object tmpProfilePicture = ((DashboardActivity) getActivity()).utils.validateProfilePicture(profilePicture);
            String description = j.fetchIfNeeded().getString(USER_DESCRIPTION);
            Log.e("friends", name + " | " + profilePicture + " | ");
            friendsList.add(new FriendsModel(j.fetchIfNeeded().getObjectId(), username, username, tmpProfilePicture, description, hearts, homePark));
        }
        Collections.sort(friendsList, new CustomComparator());
        RecyclerViewFriendsListAdapter adapter = new RecyclerViewFriendsListAdapter(getActivity(), friendsList, true);
        adapter.setOnItemClickListener(new RecyclerViewFriendsListAdapter.onFriendsListItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position, FriendsModel model) {
                ProfileFragment frag = new ProfileFragment();
                FragmentTransaction trans;
                Bundle bundle = new Bundle();
                trans = getFragmentManager().beginTransaction();
                trans.setCustomAnimations(R.anim.slide_in, R.anim.slide_out_right, R.anim.slide_in, R.anim.slide_out_right);
                trans.addToBackStack(Constants.FRIENDS_FRAGMENT_TAG);
                bundle.putBoolean(Constants.PROFILE_IS_FRIEND, true);
                bundle.putSerializable(Constants.FRIENDS_MODEL_KEY, model);
                frag.setArguments(bundle);
                trans.replace(R.id.dashboard_fragment_container, frag);
                trans.commit();
            }
        });
        friendsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                if (!isFriend) {
                    checkFriendRequests(view);
                    populateAchievements(view);
                    if (userHearts != null) {
                        userHearts.setText(String.valueOf(ParseUser.getCurrentUser().getInt(USER_HEARTS)));
                    }
                    setWDL(view);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void showProgressDialog(final ParseObject po) {
        final Dialog dialog = new Dialog(getContext(), R.style.customAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_progress_challenge_friend);
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        TextView startDate = (TextView) dialog.findViewById(R.id.start_date);
        final TextView endDate = dialog.findViewById(R.id.end_date);
        TextView username = dialog.findViewById(R.id.current_username);
        ImageView profilePicture = dialog.findViewById(R.id.current_profile_picture);
        final TextView amount = dialog.findViewById(R.id.current_amount);
        final TextView cancelButton = dialog.findViewById(R.id.cancel_button);
        populateDetails(ParseUser.getCurrentUser(), username, profilePicture, amount, po, startDate, endDate, cancelButton);

        TextView friendsUsername = dialog.findViewById(R.id.friends_username);
        ImageView friendsPicture = dialog.findViewById(R.id.friends_profile_picture);
        final TextView friendsAmount = dialog.findViewById(R.id.friends_current_amount);
        populateDetails(po, friendsUsername, friendsPicture, friendsAmount, po, startDate, endDate, cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancelButton.getText().toString().equals(getActivity().getResources().getString(R.string.finish))) {
                    int myAmount = Integer.valueOf(amount.getText().toString());
                    int theirAmount = Integer.valueOf(friendsAmount.getText().toString());
                    if (myAmount > theirAmount) {
                        showScoreDialog(getString(R.string.you_won), po);
                        dialog.dismiss();
                    } else if (myAmount == theirAmount) {
                        showScoreDialog(getString(R.string.you_drew), po);
                        dialog.dismiss();
                    } else if (theirAmount > myAmount) {
                        showScoreDialog(getString(R.string.you_lost), po);
                        dialog.dismiss();
                    }
                } else {
                    dialog.cancel();
                }
            }
        });
        dialog.show();
    }

    private void showScoreDialog(String winType, final ParseObject po) {
        final Dialog dialog = new Dialog(getContext(), R.style.customAlertDialog);
        dialog.setContentView(R.layout.dialog_challenge_score);
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        TextView winTitle = dialog.findViewById(R.id.winTitle);
        ImageView userProfilePicture = dialog.findViewById(R.id.user_profile_picture);
        if (ParseUser.getCurrentUser().getString(USER_PROFILE_PICTURE).equals(NO_PICTURE)) {
            Picasso.with(getContext()).load(R.drawable.no_profile).transform(new CircleTransform()).into(userProfilePicture);
        } else {
            Picasso.with(getContext()).load(ParseUser.getCurrentUser().getString(USER_PROFILE_PICTURE)).transform(new CircleTransform()).into(userProfilePicture);
        }
        TextView winDescription = dialog.findViewById(R.id.challenge_description);
        TextView finishButton = dialog.findViewById(R.id.finish_button);

        if (winType.equals(getString(R.string.you_won))) {
            winTitle.setText(winType);
            winDescription.setText(getActivity().getResources().getString(R.string.won_challenge));
            SashidoHelper.earnAchievement(VICTORIOUS_COL, getString(R.string.victorious), getContext(), getView());
            populateAchievements(getView());
            int totalHearts = ParseUser.getCurrentUser().getInt(USER_HEARTS);
            totalHearts = totalHearts + 500;
            int totalWins = ParseUser.getCurrentUser().getInt(USER_WINS) + 1;
            ParseUser.getCurrentUser().put(USER_WINS, totalWins);
            ParseUser.getCurrentUser().put(USER_HEARTS, totalHearts);
            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            try {
                                updateOpponent(USER_LOSSES, po);
                                updateChallenge(po);
                            } catch (ParseException e1) {
                                Log.e(RecyclerViewFriendsListAdapter.class.getSimpleName(), "Failed " + e1.getLocalizedMessage());
                            }
                        }
                    }
                });
        } else if (winType.equals(getString(R.string.you_drew))) {
            winTitle.setText(winType);
            winDescription.setText(getActivity().getResources().getString(R.string.drew_challenge));
            int totalDraw = ParseUser.getCurrentUser().getInt(USER_DRAW) + 1;
            ParseUser.getCurrentUser().put(USER_DRAW, totalDraw);
            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            try {
                                updateOpponent(USER_DRAW, po);
                                updateChallenge(po);
                            } catch (ParseException e1) {
                                Log.e(RecyclerViewFriendsListAdapter.class.getSimpleName(), "Failed " + e1.getLocalizedMessage());
                            }
                        }
                    }
                });
        } else if (winType.equals(getString(R.string.you_lost))) {
            winTitle.setText(winType);
            winDescription.setText(getActivity().getResources().getString(R.string.lost_challenge));
            int totalLosses = ParseUser.getCurrentUser().getInt(USER_LOSSES) + 1;
            ParseUser.getCurrentUser().put(USER_LOSSES, totalLosses);
            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            try {
                                updateOpponent(USER_WINS, po);
                                updateChallenge(po);
                            } catch (ParseException e1) {
                                Log.e(RecyclerViewFriendsListAdapter.class.getSimpleName(), "Failed " + e1.getLocalizedMessage());
                            }
                        }
                    }
                });
        }

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateOpponent(String winType, ParseObject po) throws ParseException {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("senderUserId", ParseUser.getCurrentUser().getObjectId());
        params.put("recipientUserId", po.fetchIfNeeded().getObjectId());
        params.put("winType", winType);
        params.put("total", po.fetchIfNeeded().getInt(winType));
        params.put("totalHearts", po.fetchIfNeeded().getInt(USER_HEARTS));
        switch (winType) {
            case USER_WINS:
                ParseCloud.callFunctionInBackground("UpdateUserScore", params, new FunctionCallback<String>() {
                    public void done(String success, ParseException e) {

                    }
                });
                ParseCloud.callFunctionInBackground("AddHeartsToUser", params, new FunctionCallback<String>() {
                    public void done(String success, ParseException e) {

                    }
                });
                break;
            case USER_LOSSES:
            case USER_DRAW:
                ParseCloud.callFunctionInBackground("UpdateUserScore", params, new FunctionCallback<String>() {
                    public void done(String success, ParseException e) {

                    }
                });
                break;
        }

    }

    private void updateChallenge(ParseObject otherUser) throws ParseException {
        final ParseQuery<ParseObject> query;
        if (ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTED, ParseUser.getCurrentUser()).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTING, otherUser).whereEqualTo(FRIEND_REQUESTS_ACCEPTED, true)
                .whereEqualTo(FRIEND_REQUESTS_IS_PENDING, false).whereEqualTo(FRIEND_REQUESTS_IS_CHALLENGE, true).whereEqualTo(FRIEND_REQUESTS_IS_COMPLETE, false).find().size() > 0) {
            query = utils.getQuery(ParseUser.getCurrentUser(), otherUser, true, true, false, true);
        } else {
            query = utils.getQuery(ParseUser.getCurrentUser(),  otherUser, true, true, false, false);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    objects.get(0).put(FRIEND_REQUESTS_IS_COMPLETE, true);
                    objects.get(0).saveInBackground();
                }
            }
        });
    }

    private void populateDetails(final ParseObject currentUser, final TextView username, final ImageView profilePicture, final TextView amount, ParseObject otherUser, final TextView startDateTV, final TextView endDateTV, final TextView cancelButton) {
        try {
            final ParseQuery<ParseObject> query;
            if (ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTED, ParseUser.getCurrentUser()).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTING, otherUser).whereEqualTo(FRIEND_REQUESTS_ACCEPTED, true)
                    .whereEqualTo(FRIEND_REQUESTS_IS_PENDING, false).whereEqualTo(FRIEND_REQUESTS_IS_CHALLENGE, true).whereEqualTo(FRIEND_REQUESTS_IS_COMPLETE, false).find().size() > 0) {
                query = utils.getQuery(ParseUser.getCurrentUser(), otherUser, true, true, false, true);
            } else {
                query = utils.getQuery(ParseUser.getCurrentUser(),  otherUser, true, true, false, false);
            }
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        final int totalDays = objects.get(0).getInt(FRIEND_REQUESTS_LENGTH);
                        Log.e("totalDays", String.valueOf(totalDays));
                        String todaysDate = objects.get(0).getString(DATE);
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
                        Date startDate = null;
                        try {
                            startDate = df.parse(todaysDate);
                        } catch (java.text.ParseException e1) {
                            e1.printStackTrace();
                        }
                        DateTime sDT = new DateTime(startDate).plusDays(totalDays);
                        Date endDate = sDT.toDate();
                        startDateTV.setText(getString(R.string.start) + "  " + todaysDate);
                        endDateTV.setText(getString(R.string.end) + "  " + df.format(endDate));

                        if (endDateTV.getText().toString().substring(5).equals(utils.getTodaysDateString()) || endDate.before(utils.getTodaysDate())) {
                            cancelButton.setText(getActivity().getResources().getString(R.string.finish));
                        }

                        List<String> dates = utils.getDates(todaysDate, df.format(endDate));
                        final int[] tAmount = {0};
                        ParseQuery<ParseObject> query = ParseQuery.getQuery(TRACKED_EVENTS_CLASS_NAME);
                        query.whereEqualTo(USER, currentUser);
                        query.whereContainedIn(DATE, dates);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> newObjects, ParseException e) {
                                if (e == null) {
                                    try {
                                        username.setText(currentUser.fetchIfNeeded().getString("username"));
                                        if (currentUser.fetchIfNeeded().getString(USER_PROFILE_PICTURE).equals(NO_PICTURE)) {
                                            Picasso.with(getContext()).load(R.drawable.no_profile).transform(new CircleTransform()).into(profilePicture);
                                        } else {
                                            Picasso.with(getContext()).load(currentUser.fetchIfNeeded().getString(USER_PROFILE_PICTURE)).transform(new CircleTransform()).into(profilePicture);
                                        }

                                        if (objects.get(0).getBoolean(FRIEND_REQUESTS_WEIGHT)) {
                                            for (ParseObject j : newObjects) {
                                                tAmount[0] = tAmount[0] + j.getInt(TRACKED_TOTAL_EXERCISES);
                                            }
                                            amount.setText(" " + String.valueOf(tAmount[0]));
                                            amount.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_weight_white), null,null,null);
                                        } else {
                                            for (ParseObject j : newObjects) {
                                                tAmount[0] = tAmount[0] + j.getInt(TRACKED_HEARTS);
                                            }
                                            amount.setText(" " + String.valueOf(tAmount[0]));
                                            amount.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_heart_white), null, null, null);
                                        }
                                    } catch (ParseException e1) {
                                        Log.e(RecyclerViewFriendsListAdapter.class.getSimpleName(), "Failed " + e1.getLocalizedMessage());
                                    }
                                } else {
                                    Log.e(RecyclerViewFriendsListAdapter.class.getSimpleName(), "Failed " + e.getLocalizedMessage());
                                }
                            }
                        });
                    } else {
                        Log.e(RecyclerViewFriendsListAdapter.class.getSimpleName(), "Failed " + e.getLocalizedMessage());
                    }
                }
            });
        } catch (ParseException e1) {
            Log.e(RecyclerViewFriendsListAdapter.class.getSimpleName(), "Failed " + e1.getLocalizedMessage());
        }
    }

    private boolean determineIfChallengeOrNot(ParseObject friend) throws ParseException {
        return ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTED, ParseUser.getCurrentUser()).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTING, friend).whereEqualTo(FRIEND_REQUESTS_IS_CHALLENGE, true).whereEqualTo(FRIEND_REQUESTS_IS_PENDING, false).whereEqualTo(FRIEND_REQUESTS_ACCEPTED, true).whereEqualTo(FRIEND_REQUESTS_IS_COMPLETE, false).count() > 0 ||
                ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTED, friend).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTING, ParseUser.getCurrentUser()).whereEqualTo(FRIEND_REQUESTS_IS_CHALLENGE, true).whereEqualTo(FRIEND_REQUESTS_IS_PENDING, false).whereEqualTo(FRIEND_REQUESTS_ACCEPTED, true).whereEqualTo(FRIEND_REQUESTS_IS_COMPLETE, false).count() > 0;
    }

    private void showChallengeDialog(final String name, Object profilePicture, final ParseObject po) {
        final Dialog dialog = new Dialog(getContext(), R.style.customAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_challenge_friend);
        dialog.setCancelable(false);
        ImageView friendsProfilePicture = (ImageView) dialog.findViewById(R.id.friends_profile_picture);
        determineProfilePicture(profilePicture, friendsProfilePicture);

        TextView friendDescription = (TextView) dialog.findViewById(R.id.challenge_description);
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String start = getActivity().getResources().getString(R.string.startchallenge);
        SpannableString startSpannable = new SpannableString(start);
        startSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, start.length(), 0);
        builder.append(startSpannable);

        SpannableString nameSpannable = new SpannableString(" " + name + " ");
        nameSpannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorPrimary)), 0, name.length() + 1, 0);
        builder.append(nameSpannable);

        String end = getActivity().getResources().getString(R.string.endchallenge);
        SpannableString endSpannable = new SpannableString(end);
        endSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, end.length(), 0);
        builder.append(endSpannable);

        friendDescription.setText(builder);

        TextView noButton = (TextView) dialog.findViewById(R.id.no_button);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView yesButton = (TextView) dialog.findViewById(R.id.yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestAlreadyExists(ParseUser.getCurrentUser(), po, true)) {
                    ((DashboardActivity) getActivity()).utils.makeText(getString(R.string.already_sent_request), LENGTH_SHORT);
                } else if (requestAlreadyExists(po, ParseUser.getCurrentUser(), true)) {
                    ((DashboardActivity) getActivity()).utils.makeText(getString(R.string.already_received_request), LENGTH_SHORT);
                } else {
                    dialog.dismiss();
                    showSelectionDialog(po, name);
                }
            }
        });
        dialog.show();
    }

    private void determineProfilePicture(Object profilePicture, ImageView iv) {
        if (profilePicture instanceof String) {
            if (profilePicture.equals(NO_PICTURE)) {
                Picasso.with(getActivity()).load(R.drawable.no_profile).transform(new CircleTransform()).into(iv);
            } else {
                Picasso.with(getActivity()).load((String) profilePicture).transform(new CircleTransform()).into(iv);
            }
        }
    }

    private void showSelectionDialog(final ParseObject po, final String name) {
        final Dialog dialog = new Dialog(getContext(), R.style.customAlertDialog);
        dialog.setTitle(getActivity().getResources().getString(R.string.challengeTitle));
        dialog.setCancelable(false);
        LinearLayout ll = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setLayoutParams(lp);
        ImageView iv = new ImageView(getContext());
        iv.setImageResource(R.drawable.btn_weight);
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        lp1.weight = (float) 0.5;
        lp1.setMargins(utils.convertDpToPixels(20), utils.convertDpToPixels(5), utils.convertDpToPixels(20), utils.convertDpToPixels(5));
        iv.setLayoutParams(lp1);
        ll.addView(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showTimePeriodDialog(true, po, name);
            }
        });
        ImageView iv1 = new ImageView(getContext());
        iv1.setImageResource(R.drawable.btn_hearts);
        iv1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LinearLayout.LayoutParams lp11 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        lp11.weight = (float) 0.5;
        lp11.setMargins(utils.convertDpToPixels(20), utils.convertDpToPixels(5), utils.convertDpToPixels(20), utils.convertDpToPixels(5));
        iv1.setLayoutParams(lp1);
        ll.addView(iv1);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showTimePeriodDialog(false, po, name);
            }
        });
        dialog.addContentView(ll, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.show();
    }

    private void showTimePeriodDialog(final boolean isWeight, final ParseObject po, final String name) {
        final Dialog dialog = new Dialog(getContext(), R.style.customAlertDialog);
        dialog.setContentView(R.layout.dialog_slider);
        dialog.setTitle(getActivity().getResources().getString(R.string.choosetimechallenge));
        dialog.setCancelable(false);

        SeekBar time_seekbar = (SeekBar) dialog.findViewById(R.id.timer_seekbar);
        time_seekbar.incrementProgressBy(10);
        time_seekbar.setProgress(10);
        time_seekbar.setMax(140);
        final TextView seekBarValue = (TextView) dialog.findViewById(R.id.value_tv);
        seekBarValue.setText(R.string.one_day);
        time_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int time = progress / 10;
                switch (progress) {
                    case 10:
                        seekBarValue.setText(time + " " + getActivity().getResources().getString(R.string.day));
                        break;
                    case 20:
                    case 30:
                    case 40:
                    case 50:
                    case 60:
                    case 70:
                    case 80:
                    case 90:
                    case 100:
                    case 110:
                    case 120:
                    case 130:
                    case 140:
                        seekBarValue.setText(time + " " + getActivity().getResources().getString(R.string.days));
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView confirmButton = (TextView) dialog.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject newRequest = new ParseObject(FRIEND_REQUESTS_CLASS_NAME);
                newRequest.put(FRIEND_REQUESTS_USER_REQUESTING, ParseUser.getCurrentUser());
                newRequest.put(FRIEND_REQUESTS_USER_REQUESTED, po);
                newRequest.put(FRIEND_REQUESTS_ACCEPTED, false);
                newRequest.put(FRIEND_REQUESTS_IS_PENDING, true);
                newRequest.put(FRIEND_REQUESTS_IS_CHALLENGE, true);
                newRequest.put(FRIEND_REQUESTS_IS_COMPLETE, false);
                newRequest.put(FRIEND_REQUESTS_WEIGHT, isWeight);
                newRequest.put(DATE, NO_DATE);
                newRequest.put(FRIEND_REQUESTS_LENGTH, getDays(seekBarValue.getText().toString()));
                newRequest.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        utils.makeText(getActivity().getResources().getString(R.string.successchallengerequest) + " " + name + ".", LENGTH_LONG);
                        SashidoHelper.earnAchievement(CHALLENGE_BEGIN_COL, getString(R.string.challenge_begin), getContext(), getView());
                        populateAchievements(getView());
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }


    private int getDays(String s) {
        int iend = s.indexOf(" ");
        String str = null;
        if (iend != -1)
            str = s.substring(0, iend);
        return Integer.valueOf(str);
    }
}
