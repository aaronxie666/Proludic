package icn.proludic.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.joda.time.DateTime;
import org.json.JSONArray;

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
import icn.proludic.adapters.RecyclerViewChallengeFriendsListAdapter;
import icn.proludic.adapters.RecyclerViewChallengedFriendsAdapter;
import icn.proludic.adapters.RecyclerViewFriendsListAdapter;
import icn.proludic.misc.Constants;
import icn.proludic.misc.CustomComparator;
import icn.proludic.misc.Utils;
import icn.proludic.models.ChallengedFriendsModel;
import icn.proludic.models.FriendsModel;

import static android.view.View.GONE;
import static icn.proludic.misc.Constants.BROWSE_ALL;
import static icn.proludic.misc.Constants.DATE;
import static icn.proludic.misc.Constants.EXERCISES_KEY;
import static icn.proludic.misc.Constants.EXERCISE_NAME_KEY;
import static icn.proludic.misc.Constants.FAVOURITED;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_ACCEPTED;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_CLASS_NAME;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_IS_CHALLENGE;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_IS_COMPLETE;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_IS_PENDING;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_LENGTH;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_USER_REQUESTED;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_USER_REQUESTING;
import static icn.proludic.misc.Constants.FRIEND_REQUESTS_WEIGHT;
import static icn.proludic.misc.Constants.HOME_PARK_KEY;
import static icn.proludic.misc.Constants.MOST_USED;
import static icn.proludic.misc.Constants.ONE_TOUCH_WORKOUTS_KEY;
import static icn.proludic.misc.Constants.TERMS_FAQS_FRAGMENT_TAG;
import static icn.proludic.misc.Constants.TERMS_KEY;
import static icn.proludic.misc.Constants.TRACKED_EVENTS_CLASS_NAME;
import static icn.proludic.misc.Constants.TRUE;
import static icn.proludic.misc.Constants.USER;
import static icn.proludic.misc.Constants.USER_DESCRIPTION;
import static icn.proludic.misc.Constants.USER_HEARTS;
import static icn.proludic.misc.Constants.USER_WINS;
import static icn.proludic.misc.Constants.WORKOUTS_KEY;
import static icn.proludic.misc.Constants.WORKOUT_NAME_KEY;

/**
 * Author:  Chang XIE
 * Date: 18/07/2018
 * Package: icn.proludic.fragments
 * Project Name: proludic
 */

public class ChallengeFriendsFragment extends Fragment {
    @Nullable
    private TextView ChallengeFriendsNoDataTV, browseAll, challenging, noFriendsTV, challenged, noChallengingTV, noChallengedTV, challengedTopText, challengedRecordText, challengedTopText1, challengedTopText2, challengedTopText3, challengedRecordText1, challengedRecordText2, challengedRecordText3;
    private LinearLayout challengeFriendsPageTop;
    private RecyclerView friendsRecyclerView, challengedFriendsRecyclerView;
    private EditText ChallengeFriendsEt;
    private boolean isBrowseAll = true;
    private boolean isChallenging = false;
    private boolean isChallenged = false;
    private Utils utils;
    private RecyclerViewChallengeFriendsListAdapter friendsAdapter;
    private RecyclerViewChallengedFriendsAdapter challengedFriendsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_friends, container, false);

        initViews(view);
        return view;
    }

    private void initViews(View view) {
        ChallengeFriendsNoDataTV = view.findViewById(R.id.challenge_friends_no_data_tv);
        populateFriendsList(view);

        challengedFriendsRecyclerView = (RecyclerView) view.findViewById(R.id.challenged_friends_recyclerView);
        challengedFriendsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager fLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        challengedFriendsRecyclerView.setLayoutManager(fLayoutManager2);

        ChallengeFriendsEt = (EditText) view.findViewById(R.id.challenge_friends_browse_et);
        ChallengeFriendsEt.addTextChangedListener(customTextChangeListener);

        browseAll = view.findViewById(R.id.challenge_friends_tv_browse_all);
        browseAll.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        browseAll.setOnClickListener(customListener);
        challenging =  view.findViewById(R.id.challenge_friends_tv_challenging);
        challenging.setOnClickListener(customListener);
        noFriendsTV = view.findViewById(R.id.no_friends_tv);
        noChallengingTV =  view.findViewById(R.id.no_challenging_tv);
        noChallengedTV = view.findViewById(R.id.no_challenged_tv);
        challenged = (TextView) view.findViewById(R.id.challenge_friends_tv_challenged);
        challenged.setOnClickListener(customListener);

        challengeFriendsPageTop = (LinearLayout) view.findViewById(R.id.challenge_friends_page_top);
        challengedTopText = (TextView) view.findViewById(R.id.challenged_top_text);
        challengedTopText1 = (TextView) view.findViewById(R.id.challenged_top_text_1);
        challengedTopText2 = (TextView) view.findViewById(R.id.challenged_top_text_2);
        challengedTopText3 = (TextView) view.findViewById(R.id.challenged_top_text_3);
        challengedRecordText = (TextView) view.findViewById(R.id.challenged_record_text);
        challengedRecordText1 = (TextView) view.findViewById(R.id.challenged_record_text_1);
        challengedRecordText2 = (TextView) view.findViewById(R.id.challenged_record_text_2);
        challengedRecordText3 = (TextView) view.findViewById(R.id.challenged_record_text_3);



    }

    private View.OnClickListener customListener = new View.OnClickListener(){
        public void onClick(View v){
            switch(v.getId()){
                case R.id.challenge_friends_tv_browse_all:
                    challenging.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryHalf));
                    challenged.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryHalf));
                    v.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                    noFriendsTV.setVisibility(GONE);
                    noChallengedTV.setVisibility(GONE);
                    noChallengingTV.setVisibility(GONE);
                    challengeFriendsPageTop.setVisibility(GONE);

                    isBrowseAll = true;
                    isChallenging = false;
                    isChallenged = false;
                    friendObjects.clear();
                    friendsList.clear();
                    ChallengedFriends.clear();
                    challengedFriendsList.clear();
                    asyncQueryUserClass();
                    break;

                case R.id.challenge_friends_tv_challenging:
                    v.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                    browseAll.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryHalf));
                    challenged.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryHalf));
                    noFriendsTV.setVisibility(GONE);
                    noChallengingTV.setVisibility(GONE);
                    noChallengedTV.setVisibility(GONE);
                    challengeFriendsPageTop.setVisibility(GONE);


                    isChallenging = true;
                    isBrowseAll = false;
                    isChallenged = false;
                    friendObjects.clear();
                    friendsList.clear();
                    ChallengedFriends.clear();
                    challengedFriendsList.clear();
                    asyncQueryUserClass();
                    break;

                case R.id.challenge_friends_tv_challenged:
                    v.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                    challenging.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryHalf));
                    browseAll.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryHalf));
                    noFriendsTV.setVisibility(GONE);
                    noChallengedTV.setVisibility(GONE);
                    noChallengingTV.setVisibility(GONE);
                    challengeFriendsPageTop.setVisibility(GONE);
                    friendsRecyclerView.setVisibility(GONE);
                    challengeFriendsPageTop.setVisibility(View.VISIBLE);
                    isChallenged = true;
                    isBrowseAll = false;
                    isChallenging = false;
                    friendObjects.clear();
                    friendsList.clear();
                    ChallengedFriends.clear();
                    challengedFriendsList.clear();
                    try {
                        populateChallengedRecord();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//                    asyncQueryUserClass();
                    break;
            }

        }

    };


    final ArrayList<ParseObject> ChallengedFriends = new ArrayList<>();
    String ChallengedDate;
    int ChallengedLengthTime;
    String ChallengedEndDate;
    boolean IsWeight;




    private void populateChallengedRecord() throws ParseException {
         int challengedTime = ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTED, ParseUser.getCurrentUser()).whereEqualTo(FRIEND_REQUESTS_IS_CHALLENGE, true).whereEqualTo(FRIEND_REQUESTS_IS_PENDING, false).whereEqualTo(FRIEND_REQUESTS_ACCEPTED, true).whereEqualTo(FRIEND_REQUESTS_IS_COMPLETE, true).count() +
                 ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTING, ParseUser.getCurrentUser()).whereEqualTo(FRIEND_REQUESTS_IS_CHALLENGE, true).whereEqualTo(FRIEND_REQUESTS_IS_PENDING, false).whereEqualTo(FRIEND_REQUESTS_ACCEPTED, true).whereEqualTo(FRIEND_REQUESTS_IS_COMPLETE, true).count();
         challengedRecordText.setText(String.valueOf(challengedTime));

         Number Wins = ParseQuery.getQuery("_User").whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId()).getFirst().getNumber("wins");
         Number Loss = ParseQuery.getQuery("_User").whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId()).getFirst().getNumber("loss");
         Number Draw = ParseQuery.getQuery("_User").whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId()).getFirst().getNumber("draw");
         challengedRecordText1.setText(String.valueOf(Wins));
         challengedRecordText2.setText(String.valueOf(Loss));
         challengedRecordText3.setText(String.valueOf(Draw));

        ParseQuery<ParseObject>  query = ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME);
        query.whereEqualTo(FRIEND_REQUESTS_USER_REQUESTED, ParseUser.getCurrentUser()).whereEqualTo(FRIEND_REQUESTS_IS_CHALLENGE, true).whereEqualTo(FRIEND_REQUESTS_IS_PENDING, false).whereEqualTo(FRIEND_REQUESTS_ACCEPTED, true).whereEqualTo(FRIEND_REQUESTS_IS_COMPLETE, true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    for(ParseObject j:objects){
                        ParseUser ChallengedUser = j.getParseUser(FRIEND_REQUESTS_USER_REQUESTING);
                        ChallengedDate = j.getString(DATE);
                        ChallengedLengthTime = j.getInt(FRIEND_REQUESTS_LENGTH);
                        IsWeight = j.getBoolean(FRIEND_REQUESTS_WEIGHT);

                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
                        Date startDate = null;
                        try {
                            startDate = df.parse(ChallengedDate);
                        } catch (java.text.ParseException e1) {
                            e1.printStackTrace();
                        }
                        DateTime sDT = new DateTime(startDate).plusDays(ChallengedLengthTime);
                        Date endDate = sDT.toDate();
                        ChallengedEndDate = df.format(endDate);

                        System.out.println(ChallengedUser);
                        ChallengedFriends.add(ChallengedUser);

                        try {
                            if(ChallengedFriends != null){
                                challengedFriendsRecyclerView.setVisibility(View.VISIBLE);
                                ChallengeFriendsNoDataTV.setVisibility(View.INVISIBLE);
                                ParseObject.fetchAll(ChallengedFriends);
                                getChallengedFriendsDetails();
                                ChallengedFriends.clear();
                            }else{
                                ChallengeFriendsNoDataTV.setVisibility(View.VISIBLE);
                                challengedFriendsRecyclerView.setVisibility(View.INVISIBLE);

                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                    }


                }

            }

        });

        ParseQuery<ParseObject>  query2 = ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME);
        query2.whereEqualTo(FRIEND_REQUESTS_USER_REQUESTING, ParseUser.getCurrentUser()).whereEqualTo(FRIEND_REQUESTS_IS_CHALLENGE, true).whereEqualTo(FRIEND_REQUESTS_IS_PENDING, false).whereEqualTo(FRIEND_REQUESTS_ACCEPTED, true).whereEqualTo(FRIEND_REQUESTS_IS_COMPLETE, true);
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    for(ParseObject j:objects){
                        ParseUser ChallengedUser = j.getParseUser(FRIEND_REQUESTS_USER_REQUESTED);
                        ChallengedDate = j.getString(DATE);
                        ChallengedLengthTime = j.getInt(FRIEND_REQUESTS_LENGTH);
                        IsWeight = j.getBoolean(FRIEND_REQUESTS_WEIGHT);

                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
                        Date startDate = null;
                        try {
                            startDate = df.parse(ChallengedDate);
                        } catch (java.text.ParseException e1) {
                            e1.printStackTrace();
                        }
                        DateTime sDT = new DateTime(startDate).plusDays(ChallengedLengthTime);
                        Date endDate = sDT.toDate();
                        ChallengedEndDate = df.format(endDate);

                        System.out.println(ChallengedUser);
                        ChallengedFriends.add(ChallengedUser);

                        try {
                            if(ChallengedFriends != null){
                                challengedFriendsRecyclerView.setVisibility(View.VISIBLE);
                                ChallengeFriendsNoDataTV.setVisibility(View.INVISIBLE);
                                ParseObject.fetchAll(ChallengedFriends);
                                getChallengedFriendsDetails();
                                ChallengedFriends.clear();
                            }else{
                                ChallengeFriendsNoDataTV.setVisibility(View.VISIBLE);
                                challengedFriendsRecyclerView.setVisibility(View.INVISIBLE);

                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }


                }

            }
        });






//        challengeFriendsPageTop.setVisibility(View.VISIBLE);

        challengedFriendsAdapter = new RecyclerViewChallengedFriendsAdapter(getActivity(), challengedFriendsList, true);
        challengedFriendsRecyclerView.setAdapter(challengedFriendsAdapter);


    }


    private ArrayList<ChallengedFriendsModel> challengedFriendsList = new ArrayList<>();
    private void getChallengedFriendsDetails() throws ParseException {
        for (ParseObject j : ChallengedFriends) {
            String name = j.fetchIfNeeded().getString("name");
            String username = j.fetchIfNeeded().getString("username");
            String profilePicture = j.fetchIfNeeded().getString("profilePicture");
            String homePark = j.fetchIfNeeded().getString(HOME_PARK_KEY);
            int hearts = j.fetchIfNeeded().getInt(USER_HEARTS);
            Object tmpProfilePicture = ((DashboardActivity) getActivity()).utils.validateProfilePicture(profilePicture);
            String description = j.fetchIfNeeded().getString(USER_DESCRIPTION);
            String challengedStartDate = ChallengedDate;
            String challengedEndDate = ChallengedEndDate;
            boolean isWeight = IsWeight;
            Number friendScore = 11;
            Number UserScore = 66;


            Log.e("friends", name + " | " + profilePicture + " | ");
            challengedFriendsList.add(new ChallengedFriendsModel(j.fetchIfNeeded().getObjectId(), username, username, tmpProfilePicture, description, hearts, homePark, challengedStartDate, challengedEndDate, isWeight, friendScore, UserScore));
        }
//        Collections.sort(challengedFriendsList, new CustomComparator());
        RecyclerViewChallengedFriendsAdapter adapter = new RecyclerViewChallengedFriendsAdapter(getActivity(), challengedFriendsList, true);

//        adapter.setOnItemClickListener(new RecyclerViewFriendsListAdapter.onFriendsListItemClickListener() {
//            @Override
//            public void onItemClickListener(View view, int position, FriendsModel model) {
//                ProfileFragment frag = new ProfileFragment();
//                FragmentTransaction trans;
//                Bundle bundle = new Bundle();
//                trans = getFragmentManager().beginTransaction();
//                trans.setCustomAnimations(R.anim.slide_in, R.anim.slide_out_right, R.anim.slide_in, R.anim.slide_out_right);
//                trans.addToBackStack(Constants.FRIENDS_FRAGMENT_TAG);
//                bundle.putBoolean(Constants.PROFILE_IS_FRIEND, true);
//                bundle.putSerializable(Constants.FRIENDS_MODEL_KEY, model);
//                frag.setArguments(bundle);
//                trans.replace(R.id.dashboard_fragment_container, frag);
//                trans.commit();
//            }
//        });
        challengedFriendsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private boolean isChanged = false;
    private TextWatcher customTextChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(isChallenged){
                isChanged = true;
                filterChallengedFriends(s.toString().toLowerCase());

            }else {
                isChanged = true;
                filterFriends(s.toString().toLowerCase());
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    void filterChallengedFriends(String text){
        ArrayList<ChallengedFriendsModel> temp = new ArrayList<>();
        for(ChallengedFriendsModel d : challengedFriendsList){
            //or use .contains(text)
            if(d.getName().toLowerCase().startsWith(text)){
                temp.add(d);
            }
        }
        temp = ((DashboardActivity) getActivity()).removeChallengedFriendDuplicates(temp);
        if (text.length() == 0) {
            System.out.println("**********************************************************000list is="+challengedFriendsList);
            temp = challengedFriendsList;
            ChallengeFriendsNoDataTV.setVisibility(View.GONE);

        }

        if (temp.size() == 0 && isChanged) {
            ChallengeFriendsNoDataTV.setVisibility(View.VISIBLE);
            ChallengeFriendsNoDataTV.setText("you have no friends's name begin with that!--------");

        } else {
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& temp is" +temp);
            ChallengeFriendsNoDataTV.setVisibility(View.GONE);

            challengedFriendsAdapter.updateList(temp);                                                           //???????????????can not show
        }
    }


    void filterFriends(String text){
        ArrayList<FriendsModel> temp = new ArrayList<>();
        for(FriendsModel d : friendsList){
            //or use .contains(text)
            if(d.getName().toLowerCase().startsWith(text)){
                temp.add(d);
            }
        }
        temp = ((DashboardActivity) getActivity()).removeFriendDuplicates(temp);
        if (text.length() == 0) {
            temp = friendsList;
            ChallengeFriendsNoDataTV.setVisibility(View.GONE);
        }

        if (temp.size() == 0 && isChanged) {
            ChallengeFriendsNoDataTV.setVisibility(View.VISIBLE);
            ChallengeFriendsNoDataTV.setText("you have no friends's name begin with that!");
        } else {
            ChallengeFriendsNoDataTV.setVisibility(View.GONE);
            friendsAdapter.updateList(temp);
        }
    }




    private void populateFriendsList(View view) {
        friendsRecyclerView = (RecyclerView) view.findViewById(R.id.challenge_friends_recyclerView);
        friendsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager fLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        friendsRecyclerView.setLayoutManager(fLayoutManager);
        startMyTask(new AsyncFriends());

    }

    void startMyTask(AsyncTask<Object, Integer, Void> asyncTask) {
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
    private void asyncQueryUserClass(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if(isBrowseAll == true) {
                        for (ParseUser j : objects) {
                            try {
                                JSONArray sashidoFriends = j.getJSONArray("Friends");
                                if (sashidoFriends != null) {
                                    if (sashidoFriends.length() > 0) {
                                        friendsRecyclerView.setVisibility(View.VISIBLE);
                                        ChallengeFriendsNoDataTV.setVisibility(View.INVISIBLE);
                                        for (int i = 0; i < sashidoFriends.length(); i++) {
                                            friendsObjectID = sashidoFriends.optString(i);
                                            if (friendsObjectID != null) {
                                                friendObjects.add(ParseObject.createWithoutData("_User", friendsObjectID));

                                            }
                                        }
                                        ParseObject.fetchAll(friendObjects);
                                        getFriendsDetails();
                                    } else {
                                        ChallengeFriendsNoDataTV.setVisibility(View.VISIBLE);
                                        friendsRecyclerView.setVisibility(View.INVISIBLE);
                                    }
                                } else {
                                    ChallengeFriendsNoDataTV.setVisibility(View.VISIBLE);
                                    friendsRecyclerView.setVisibility(View.INVISIBLE);
                                }
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }

                        if(friendObjects.size() == 0){
                            noFriendsTV.setVisibility(View.VISIBLE);
                        }
                    }

                    else if(isChallenging == true) {
                        for (ParseUser j : objects) {
                            try {
                                JSONArray sashidoFriends = j.getJSONArray("Friends");
                                if (sashidoFriends != null) {
                                    if (sashidoFriends.length() > 0) {
                                        friendsRecyclerView.setVisibility(View.VISIBLE);
                                        ChallengeFriendsNoDataTV.setVisibility(View.INVISIBLE);
                                        for (int i = 0; i < sashidoFriends.length(); i++) {
                                            friendsObjectID = sashidoFriends.optString(i);
                                            if (friendsObjectID != null) {
                                                if (determineIfChallengeOrNot(ParseObject.createWithoutData("_User", friendsObjectID))){
                                                    friendObjects.add(ParseObject.createWithoutData("_User", friendsObjectID));
                                                }else{

                                                }
                                            }
                                        }
                                        ParseObject.fetchAll(friendObjects);
                                        getFriendsDetails();
                                    } else {
                                        ChallengeFriendsNoDataTV.setVisibility(View.VISIBLE);
                                        friendsRecyclerView.setVisibility(View.INVISIBLE);
                                    }
                                } else {
                                    ChallengeFriendsNoDataTV.setVisibility(View.VISIBLE);
                                    friendsRecyclerView.setVisibility(View.INVISIBLE);
                                }
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }

                        if(friendObjects.size() == 0){
                            noChallengingTV.setVisibility(View.VISIBLE);
                            }
                    }
//                    else if(isChallenged == true) {
//                        for (ParseUser j : objects) {
//                            try {
//                                JSONArray sashidoFriends = j.getJSONArray("Friends");
//                                if (sashidoFriends != null) {
//                                    if (sashidoFriends.length() > 0) {
//                                        friendsRecyclerView.setVisibility(View.VISIBLE);
//                                        ChallengeFriendsNoDataTV.setVisibility(View.INVISIBLE);
//                                        for (int i = 0; i < sashidoFriends.length(); i++) {
//                                            friendsObjectID = sashidoFriends.optString(i);
//                                            if (friendsObjectID != null) {
////                                                if (determineIfChallengeOrNot(ParseObject.createWithoutData("_User", friendsObjectID))){
////                                                    friendObjects.add(ParseObject.createWithoutData("_User", friendsObjectID));
////                                                }else{
////
////                                                }
//                                            }
//                                        }
//                                        ParseObject.fetchAll(friendObjects);
//                                        getFriendsDetails();
//                                    } else {
//                                        ChallengeFriendsNoDataTV.setVisibility(View.VISIBLE);
//                                        friendsRecyclerView.setVisibility(View.INVISIBLE);
//                                    }
//                                } else {
//                                    ChallengeFriendsNoDataTV.setVisibility(View.VISIBLE);
//                                    friendsRecyclerView.setVisibility(View.INVISIBLE);
//                                }
//                            } catch (ParseException e1) {
//                                e1.printStackTrace();
//                            }
//                        }
//
////                        if(friendObjects.size() == 0){
////                            noChallengedTV.setVisibility(View.VISIBLE);
////                        }else{
//                            challengeFriendsPageTop.setVisibility(View.VISIBLE);
//
////                        }
//                    }
                    friendsAdapter = new RecyclerViewChallengeFriendsListAdapter(getActivity(), friendsList, true);
                    friendsRecyclerView.setAdapter(friendsAdapter);
                } else {
                    Log.e("failed", "Failed " + e.getMessage());
                }
            }
        });
    }


    private boolean determineIfChallengeOrNot(ParseObject friend) throws ParseException {
        return ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTED, ParseUser.getCurrentUser()).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTING, friend).whereEqualTo(FRIEND_REQUESTS_IS_CHALLENGE, true).whereEqualTo(FRIEND_REQUESTS_IS_PENDING, false).whereEqualTo(FRIEND_REQUESTS_ACCEPTED, true).whereEqualTo(FRIEND_REQUESTS_IS_COMPLETE, false).count() > 0 ||
                ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTED, friend).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTING, ParseUser.getCurrentUser()).whereEqualTo(FRIEND_REQUESTS_IS_CHALLENGE, true).whereEqualTo(FRIEND_REQUESTS_IS_PENDING, false).whereEqualTo(FRIEND_REQUESTS_ACCEPTED, true).whereEqualTo(FRIEND_REQUESTS_IS_COMPLETE, false).count() > 0;
    }

    private boolean determineIfHaveChallengedOrNot(ParseQuery friend) throws ParseException{
        return ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTED, ParseUser.getCurrentUser()).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTING, friend).whereEqualTo(FRIEND_REQUESTS_IS_CHALLENGE, true).whereEqualTo(FRIEND_REQUESTS_IS_PENDING, false).whereEqualTo(FRIEND_REQUESTS_ACCEPTED, true).whereEqualTo(FRIEND_REQUESTS_IS_COMPLETE, true).count() > 0 ||
                ParseQuery.getQuery(FRIEND_REQUESTS_CLASS_NAME).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTED, friend).whereEqualTo(FRIEND_REQUESTS_USER_REQUESTING, ParseUser.getCurrentUser()).whereEqualTo(FRIEND_REQUESTS_IS_CHALLENGE, true).whereEqualTo(FRIEND_REQUESTS_IS_PENDING, false).whereEqualTo(FRIEND_REQUESTS_ACCEPTED, true).whereEqualTo(FRIEND_REQUESTS_IS_COMPLETE, true).count() > 0;
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
        RecyclerViewChallengeFriendsListAdapter adapter = new RecyclerViewChallengeFriendsListAdapter(getActivity(), friendsList, true);

//        adapter.setOnItemClickListener(new RecyclerViewFriendsListAdapter.onFriendsListItemClickListener() {
//            @Override
//            public void onItemClickListener(View view, int position, FriendsModel model) {
//                ProfileFragment frag = new ProfileFragment();
//                FragmentTransaction trans;
//                Bundle bundle = new Bundle();
//                trans = getFragmentManager().beginTransaction();
//                trans.setCustomAnimations(R.anim.slide_in, R.anim.slide_out_right, R.anim.slide_in, R.anim.slide_out_right);
//                trans.addToBackStack(Constants.FRIENDS_FRAGMENT_TAG);
//                bundle.putBoolean(Constants.PROFILE_IS_FRIEND, true);
//                bundle.putSerializable(Constants.FRIENDS_MODEL_KEY, model);
//                frag.setArguments(bundle);
//                trans.replace(R.id.dashboard_fragment_container, frag);
//                trans.commit();
//            }
//        });
        friendsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



}
