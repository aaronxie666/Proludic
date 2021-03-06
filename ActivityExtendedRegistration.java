package icn.proludic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import icn.proludic.misc.AgeManager;
import icn.proludic.misc.CircleTransform;
import icn.proludic.misc.Constants;
import icn.proludic.misc.ExifUtil;
import icn.proludic.misc.SashidoHelper;
import icn.proludic.misc.SharedPreferencesManager;
import icn.proludic.misc.Utils;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static icn.proludic.misc.Constants.EMAIL;
import static icn.proludic.misc.Constants.EMPTY;
import static icn.proludic.misc.Constants.FACEBOOK;
import static icn.proludic.misc.Constants.FALSE;
import static icn.proludic.misc.Constants.FEMALE;
import static icn.proludic.misc.Constants.LOGIN_TYPE;
import static icn.proludic.misc.Constants.MALE;
import static icn.proludic.misc.Constants.NAME;
import static icn.proludic.misc.Constants.NO_PICTURE;
import static icn.proludic.misc.Constants.PASSWORD;
import static icn.proludic.misc.Constants.SHARED_PREFS_GENDER;
import static icn.proludic.misc.Constants.SHARED_PREFS_HEIGHT;
import static icn.proludic.misc.Constants.SHARED_PREFS_OVER_18;
import static icn.proludic.misc.Constants.SHARED_PREFS_USERNAME_OR_UPLOAD;
import static icn.proludic.misc.Constants.SHARED_PREFS_WEIGHT;
import static icn.proludic.misc.Constants.TRUE;
import static icn.proludic.misc.Constants.USERNAME;
import static icn.proludic.misc.Constants.USERNAME_INPUT;
import static icn.proludic.misc.Constants.USER_FACEBOOK_ID;
import static icn.proludic.misc.Constants.USER_PROFILE_PICTURE;
import static icn.proludic.misc.Validate.isValid;

/**
 * Author:  Bradley Wilson
 * Date: 27/07/2017
 * Package: icn.proludic
 * Project Name: proludic
 */

public class ActivityExtendedRegistration extends AppCompatActivity {

    private Context context = ActivityExtendedRegistration.this;
    private String loginType, name, age, username, email, password, facebookID;
    private Object profilePicture;
    private ViewAnimator e_reg_va;
    private final int VA_CHILD_AGE = 0;
    private final int VA_CHILD_USERNAME_OR_UPLOAD = 1;
    private final int VA_CHILD_GENDER = 2;
    private final int VA_CHILD_HEIGHT = 3;
    private final int VA_CHILD_WEIGHT = 4;
    private final int VA_CHILD_TERMS_AND_CONDITIONS = 5;
    private ArrayList<RadioButton> listOfRadioButtons;
    private Utils utils;
    private EditText usernameET, feetET, inchesET, weightET;
    private RadioGroup genderGroup;
    private DatePicker dp;

    final static int PERMISSIONS_REQUEST_CAMERA = 1;
    final static int PERMISSIONS_REQUEST_STORAGE = 2;
    final static int REQUEST_PHOTO_CAPTURE = 3;
    final static int REQUEST_SELECT_PHOTO = 4;
    final static String TAG = "ActivityExtendedReg";

    View parentLayout;
    Activity activity = ActivityExtendedRegistration.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_registration);
        parentLayout = findViewById(android.R.id.content);
        utils = new Utils(context);
        initExtraData();
        initViews();
    }

    private void initViews() {
        setupViewAnimator();

        usernameET = (EditText) findViewById(R.id.et_username);
        genderGroup = (RadioGroup) findViewById(R.id.gender_group);
        feetET = (EditText) findViewById(R.id.foot_et);
        inchesET = (EditText) findViewById(R.id.inches_et);
        weightET = (EditText) findViewById(R.id.kg_et);

        ImageView nextButton = (ImageView) findViewById(R.id.next_button);
        nextButton.setTag(0);
        nextButton.setOnClickListener(customListener);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.bottom_rg);
        int count = radioGroup.getChildCount();
        listOfRadioButtons = new ArrayList<RadioButton>();
        for (int i = 0; i < count; i++) {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                listOfRadioButtons.add((RadioButton)o);
            }
        }
        listOfRadioButtons.get(e_reg_va.getDisplayedChild()).setChecked(true);

        dp = (DatePicker) findViewById(R.id.date_value);
        dp.setMaxDate(new Date().getTime());

        /*if (nulls != null)
            preloadFieldVals();*/
    }

    /*private void preloadFieldVals() {
        RadioButton male = genderGroup.findViewById(R.id.male);
        RadioButton female = genderGroup.findViewById(R.id.female);

        String[] prefs = {SHARED_PREFS_USERNAME_OR_UPLOAD, SHARED_PREFS_GENDER, SHARED_PREFS_HEIGHT, SHARED_PREFS_WEIGHT};
        for (String pref : prefs) {
            if (!nulls.contains(pref)) {
                switch (pref) {
                    case SHARED_PREFS_USERNAME_OR_UPLOAD:
                        usernameET.setText(SharedPreferencesManager.getString(context, pref));
                        break;
                    case SHARED_PREFS_GENDER:
                        if (SharedPreferencesManager.getString(context, pref).equals(MALE)) {
                            male.setChecked(true);
                            female.setChecked(false);
                        } else {
                            male.setChecked(false);
                            female.setChecked(true);
                        }
                        break;
                    case SHARED_PREFS_HEIGHT:
                        String height = SharedPreferencesManager.getString(context, pref);
                        String feet = height.substring(0, height.indexOf(" f"));
                        String inches = height.substring(height.indexOf("t ") + 2, height.indexOf(" i"));
                        feetET.setText(feet);
                        inchesET.setText(inches);
                        break;
                    case SHARED_PREFS_WEIGHT:
                        String weight = SharedPreferencesManager.getString(context, pref);
                        weight = weight.substring(0, weight.indexOf(" kg"));
                        weightET.setText(weight);
                        break;
                }
            }
        }
    }*/

    ImageView userProfilePicture;
    private void setupViewAnimator() {
        e_reg_va = (ViewAnimator) findViewById(R.id.extended_registration_va);
        Animation inAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        Animation outAnim = AnimationUtils.loadAnimation(this, R.anim.slide_out);
        e_reg_va.setInAnimation(inAnim);
        e_reg_va.setOutAnimation(outAnim);

        View v = null;
        v = getLayoutInflater().inflate(R.layout.e_registration_child_age, null);
        e_reg_va.addView(v, VA_CHILD_AGE);
        e_reg_va.setDisplayedChild(VA_CHILD_AGE);

        if (loginType.equals(FACEBOOK)) {
            v = getLayoutInflater().inflate(R.layout.e_registration_child_username, null);
            e_reg_va.addView(v, VA_CHILD_USERNAME_OR_UPLOAD);
            SharedPreferencesManager.setString(context, USER_PROFILE_PICTURE, (String) profilePicture);
        } else {
            v = getLayoutInflater().inflate(R.layout.e_registration_upload_photo, null);
            e_reg_va.addView(v, VA_CHILD_USERNAME_OR_UPLOAD);
            userProfilePicture = (ImageView) findViewById(R.id.user_profile_picture);
            userProfilePicture.setTag(R.drawable.no_profile);
            userProfilePicture.setOnClickListener(customListener);
        }
        v = getLayoutInflater().inflate(R.layout.e_registration_child_gender, null);
        e_reg_va.addView(v, VA_CHILD_GENDER);
        v = getLayoutInflater().inflate(R.layout.e_registration_child_height, null);
        e_reg_va.addView(v, VA_CHILD_HEIGHT);
        v = getLayoutInflater().inflate(R.layout.e_registration_child_weight, null);
        e_reg_va.addView(v, VA_CHILD_WEIGHT);
        v = getLayoutInflater().inflate(R.layout.e_registration_terms_and_conditions, null);
        e_reg_va.addView(v, VA_CHILD_TERMS_AND_CONDITIONS);
    }

    private void initExtraData() {
        Intent intent = getIntent();
        loginType = intent.getExtras().getString(LOGIN_TYPE);
        Log.e("debug", "loginType is " + loginType);
        name = intent.getExtras().getString(NAME);
        username = intent.getExtras().getString(USERNAME);
        email = intent.getExtras().getString(EMAIL);
        password = intent.getExtras().getString(PASSWORD);
        profilePicture = intent.getExtras().getString(USER_PROFILE_PICTURE);
        facebookID = intent.getExtras().getString(USER_FACEBOOK_ID);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
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
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
            // do we need to show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.CAMERA)) {
                // explanation needed
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Camera Permission");
                builder.setMessage("To upload a photo, you need to grant the app permission to access your camera.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CAMERA);
                    }
                });
                builder.create();
                builder.show();
            } else {
                // no explanation needed
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
            }
        } else {
            cameraIntent();
        }
    }

    private void checkGalleryPermissions() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            // do we need to show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // explanation needed
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Storage Permission");
                builder.setMessage("To upload a photo, you need to grant the app permission to access your gallery.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_STORAGE);
                    }
                });
                builder.create();
                builder.show();
            } else {
                // no explanation needed
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_STORAGE);
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
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            File file = null;
            try {
                file = createImageFile();
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
            if (file != null) {
                Uri uri = FileProvider.getUriForFile(activity.getApplicationContext(), "icn.proludic.fileprovider", file);
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
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryIntent() {
        Log.e(TAG, "enters galleryIntent");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Log.e(TAG, "onActivityResult OK");
            if (requestCode == REQUEST_PHOTO_CAPTURE) {
                onCaptureResult(data);
            } else if (requestCode == REQUEST_SELECT_PHOTO && data != null) {
                onSelectResult(data);
            }
        }
    }

    private void onCaptureResult(Intent data) {
        progress = Snackbar.make(parentLayout, "Uploading photo...", Snackbar.LENGTH_INDEFINITE);
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
            activity.sendBroadcast(intent);
            saveFile(file);
        } catch (IOException e) {
            Snackbar.make(parentLayout, "Oops! Something went wrong. Try again or get in touch!", Snackbar.LENGTH_LONG).show();
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
        progress = Snackbar.make(parentLayout, "Uploading photo...", Snackbar.LENGTH_INDEFINITE);
        progress.show();
        try {
            Uri selectedFile = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(selectedFile, filePathColumn, null, null, null);
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
            Snackbar.make(parentLayout, "Oops! Something went wrong. Try again or get in touch!", Snackbar.LENGTH_LONG).show();
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    ParseFile parseFile;
    String photoUrl;
    private void saveFile(final File file) {
        parseFile = new ParseFile(file);
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.e(TAG, "Error is null");
                    photoUrl = parseFile.getUrl();
                    progress.dismiss();
                    Picasso.with(context).load(photoUrl).transform(new CircleTransform()).into(userProfilePicture);
                    userProfilePicture.setTag(420);
                } else {
                    Log.e(TAG, "Error is not null");
                    Snackbar.make(parentLayout, "Oops! Something went wrong. Try again or get in touch!", Snackbar.LENGTH_LONG).show();
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        });
    }

    private View.OnClickListener customListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.user_profile_picture:
                    selectImage();
                    break;
                case R.id.next_button:
                    switch (e_reg_va.getDisplayedChild()) {
                        case VA_CHILD_AGE:
                            int age = AgeManager.getAge(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
                            if (age >= 13) {
                                if (age >= 18) {
                                    SharedPreferencesManager.setString(context, Constants.SHARED_PREFS_OVER_18, TRUE);
                                } else {
                                    SharedPreferencesManager.setString(context, Constants.SHARED_PREFS_OVER_18, FALSE);
                                }
                                e_reg_va.showNext();
                            } else {
                                Toast.makeText(context, getString(R.string.age_verification), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case VA_CHILD_USERNAME_OR_UPLOAD:
                            if (loginType.equals(FACEBOOK)) {
                                if (usernameET.getText().toString().length() > 0) {
                                    if (isValid(USERNAME_INPUT, usernameET.getText().toString())) {
                                        SharedPreferencesManager.setString(context, SHARED_PREFS_USERNAME_OR_UPLOAD, usernameET.getText().toString());
                                        e_reg_va.showNext();
                                    } else {
                                        Toast.makeText(context, getString(R.string.wrong_format_username), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, getString(R.string.cant_be_empty_fields), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if ((Integer) userProfilePicture.getTag() != R.drawable.no_profile) {
                                    if (photoUrl != null) {
                                        if (!photoUrl.isEmpty()) {
                                            SharedPreferencesManager.setString(context, USER_PROFILE_PICTURE, photoUrl);
                                            Log.e("debug", photoUrl);
                                            SharedPreferencesManager.setString(context, SHARED_PREFS_USERNAME_OR_UPLOAD, photoUrl);
                                            e_reg_va.showNext();
                                        } else {
                                            Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT).show();
                                            selectImage();
                                        }
                                    } else {
                                        Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT).show();
                                        selectImage();
                                    }
                                } else {
                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityExtendedRegistration.this, R.style.customAlertDialog).create();
                                    alertDialog.setTitle(getString(R.string.sure_continue));
                                    alertDialog.setMessage(getString(R.string.are_you_sure_upload));
                                    alertDialog.setIcon(android.R.drawable.stat_sys_warning);
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    SharedPreferencesManager.setString(context, USER_PROFILE_PICTURE, NO_PICTURE);
                                                    SharedPreferencesManager.setString(context, SHARED_PREFS_USERNAME_OR_UPLOAD, " ");
                                                    e_reg_va.showNext();
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    alertDialog.show();
                                }

                            }
                            break;
                        case VA_CHILD_GENDER:
                            int selectedId = genderGroup.getCheckedRadioButtonId();
                            switch (selectedId) {
                                case R.id.male:
                                    SharedPreferencesManager.setString(context, SHARED_PREFS_GENDER, MALE);
                                    break;
                                case R.id.female:
                                    SharedPreferencesManager.setString(context, SHARED_PREFS_GENDER, FEMALE);
                                    break;
                            }
                            e_reg_va.showNext();
                            break;
                        case VA_CHILD_HEIGHT:
                            if (!feetET.getText().toString().trim().equals(EMPTY) && !inchesET.getText().toString().trim().equals(EMPTY)) {
                                int feet = Integer.parseInt(feetET.getText().toString().trim());
                                int inches = Integer.parseInt(inchesET.getText().toString().trim());
                                if (feet > 0 && feet < 8) {
                                    if (inches >= 0 && inches < 12) {
                                        SharedPreferencesManager.setString(context, SHARED_PREFS_HEIGHT, feet + " " + getString(R.string.feet) + " " + inches + " " + getString(R.string.inches));
                                        e_reg_va.showNext();
                                    } else {
                                        Toast.makeText(context, getString(R.string.enter_realistic_details), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, getString(R.string.enter_realistic_details), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, getString(R.string.cant_be_empty_fields), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case VA_CHILD_WEIGHT:
                            if (!weightET.getText().toString().trim().equals(EMPTY)) {
                                int kg = Integer.parseInt(weightET.getText().toString().trim());
                                if (kg > 0 && kg <= 999) {
                                    SharedPreferencesManager.setString(context, SHARED_PREFS_WEIGHT, kg + " " + getString(R.string.kg));
                                    e_reg_va.showNext();
                                    view.setTag(VA_CHILD_TERMS_AND_CONDITIONS);
                                    Picasso.with(context).load(R.drawable.ic_send).into((ImageView) view);
                                } else {
                                    Toast.makeText(context, getString(R.string.enter_realistic_details), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, getString(R.string.cant_be_empty_fields), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case VA_CHILD_TERMS_AND_CONDITIONS:
                            CheckBox terms = (CheckBox) findViewById(R.id.accept_terms);
                            if (terms.isChecked()) {
                                if ((Integer) view.getTag() == VA_CHILD_TERMS_AND_CONDITIONS) {
                                    if (loginType.equals(FACEBOOK)) {
                                        username = SharedPreferencesManager.getString(context, SHARED_PREFS_USERNAME_OR_UPLOAD);
                                    }
                                    boolean isOver18 = false;
                                    boolean isMale = false;
                                    if (SharedPreferencesManager.getString(context, SHARED_PREFS_OVER_18).equals(TRUE)) {
                                        isOver18 = true;
                                    }
                                    if (SharedPreferencesManager.getString(context, SHARED_PREFS_GENDER).equals(MALE)) {
                                        isMale = true;
                                    }

                                    /*if (loginType.equals(FACEBOOK)) {
                                        ParseUser.getCurrentUser().put(USER_IS_OVER_18, isOver18);
                                        ParseUser.getCurrentUser().setUsername(SharedPreferencesManager.getString(context, SHARED_PREFS_USERNAME_OR_UPLOAD));
                                        ParseUser.getCurrentUser().put(USER_BODY_WEIGHT, SharedPreferencesManager.getString(context, SHARED_PREFS_WEIGHT));
                                        ParseUser.getCurrentUser().put(USER_HEIGHT, SharedPreferencesManager.getString(context, SHARED_PREFS_HEIGHT));
                                        ParseUser.getCurrentUser().put(USER_IS_MALE, isMale);
                                        ParseUser.getCurrentUser().put(USER_HEARTS, 500);
                                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    createAchievementRecords();
                                                    SharedPreferencesManager.setString(context, SHARED_PREFS_TERMS_ACCEPTED, TRUE);
                                                    AppEventsLogger logger = AppEventsLogger.newLogger(context);
                                                    logger.logEvent("Facebook Login");
                                                    Map<String, String> articleParams = new HashMap<String, String>();
                                                    //param keys and values have to be of String type
                                                    articleParams.put("Facebook Sign Up", ParseUser.getCurrentUser().getEmail() + " signed up to Facebook.");
                                                    //up to 10 params can be logged with each event
                                                    FlurryAgent.logEvent("Facebook Sign Up", articleParams);
                                                    SashidoHelper.goToDashboard(ActivityExtendedRegistration.this);
                                                } else {
                                                    Log.e("failed", e.getLocalizedMessage());
                                                    SashidoHelper.showErrorDialog(context, e);
                                                }
                                            }
                                        });
                                    } else {*/
                                        SashidoHelper.register(context, ActivityExtendedRegistration.this, name, username, email, password, loginType, isOver18, SharedPreferencesManager.getString(context, SHARED_PREFS_HEIGHT),
                                                SharedPreferencesManager.getString(context, SHARED_PREFS_WEIGHT), isMale, SharedPreferencesManager.getString(context, USER_PROFILE_PICTURE), facebookID);
                                    //}
                                }
                            } else {
                                Toast.makeText(context, getString(R.string.please_accept_terms_and_conditions), Toast.LENGTH_SHORT).show();
                            }
                            break;

                    }
                    listOfRadioButtons.get(e_reg_va.getDisplayedChild()).setChecked(true);
                    break;
                case R.id.back_button:
                    backButtonPress();
                    break;
            }
        }
    };

    private void backButtonPress() {
        switch (e_reg_va.getDisplayedChild()) {
            case VA_CHILD_USERNAME_OR_UPLOAD:
            case VA_CHILD_GENDER:
            case VA_CHILD_HEIGHT:
            case VA_CHILD_WEIGHT:
            case VA_CHILD_TERMS_AND_CONDITIONS:
                e_reg_va.showPrevious();
                break;
        }
        listOfRadioButtons.get(e_reg_va.getDisplayedChild()).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        if (e_reg_va.getDisplayedChild() != 0) {
            backButtonPress();
        } else {
            super.onBackPressed();
        }
    }
}
