package icn.proludic;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

import org.joda.time.TimeOfDay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyTestService extends IntentService {
    public MyTestService() {
        super("MyTestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground();

        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("user", ParseUser.getCurrentUser());


// Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        ParseQuery query = ParseQuery.getQuery("TrackedEvents").whereEqualTo("User", ParseUser.getCurrentUser()).whereEqualTo("Date", date);
        int hearts = 0;
        try {
            ParseObject today = query.getFirst();
            hearts = (int) today.getNumber("Hearts");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("))_)_+_++)+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_++_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+");
        push.setMessage(ParseUser.getCurrentUser().getUsername() + " you earn " + hearts + " hearts today!!!");
//        push.setMessage("Click me to see the details about your challenges!");


        push.sendInBackground();

        Log.i("MyTestService", "Service running");
    }
}
