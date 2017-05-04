/*
 * Copyright (C) 2012-2013 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opendatakit.survey.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opendatakit.demoAndroidlibraryClasses.consts.IntentConsts;
import org.opendatakit.demoAndroidCommonClasses.activities.BaseActivity;
import org.opendatakit.demoAndroidlibraryClasses.application.AppAwareApplication;
import org.opendatakit.demoAndroidlibraryClasses.properties.CommonToolProperties;
import org.opendatakit.demoAndroidlibraryClasses.properties.PropertiesSingleton;
import org.opendatakit.demoAndroidlibraryClasses.provider.FormsProviderAPI;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.demoAndroidCommonClasses.webkitserver.utilities.UrlUtils;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.survey.R;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Code to display a splash screen
 *
 * @author Carl Hartung
 *
 */
public class SplashScreenActivity extends BaseActivity {

  private static final String t = "SplashScreenActivity";

  private int mImageMaxWidth;
  private int mSplashTimeout = 2000; // milliseconds

  private String appName;
  private AlertDialog mAlertDialog;
  private static final boolean EXIT = true;

  private static final int PERMISSION_ALL = 1;
  String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.GET_ACCOUNTS};

  @SuppressWarnings("deprecation")
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if(!hasPermissions(this, PERMISSIONS)){
      WebLogger.getLogger(appName).i(t, "Asking for permissions: " + appName);
      ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
    } else {
      initialize();
    }
  }
  
  @Override
  public String getAppName() {
    return appName;
  }

  private void endSplashScreen() {

    // launch new activity and close splash screen
    Uri data = getIntent().getData();
    Bundle extras = getIntent().getExtras();

    Intent i = new Intent(this, MainMenuActivity.class);
    if ( data != null ) {
      i.setData(data);
    }
    if ( extras != null ) {
      i.putExtras(extras);
    }
    i.putExtra(IntentConsts.INTENT_KEY_APP_NAME, appName);
    startActivity(i);
    finish();
  }

  // decodes image and scales it to reduce memory consumption
  private Bitmap decodeFile(File f) {
    Bitmap b = null;
    try {
      // Decode image size
      BitmapFactory.Options o = new BitmapFactory.Options();
      o.inJustDecodeBounds = true;

      FileInputStream fis = new FileInputStream(f);
      BitmapFactory.decodeStream(fis, null, o);
      try {
        fis.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      int scale = 1;
      if (o.outHeight > mImageMaxWidth || o.outWidth > mImageMaxWidth) {
        scale = (int) Math.pow(
            2,
            (int) Math.round(Math.log(mImageMaxWidth / (double) Math.max(o.outHeight, o.outWidth))
                / Math.log(0.5)));
      }

      // Decode with inSampleSize
      BitmapFactory.Options o2 = new BitmapFactory.Options();
      o2.inSampleSize = scale;
      fis = new FileInputStream(f);
      b = BitmapFactory.decodeStream(fis, null, o2);
      try {
        fis.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (FileNotFoundException e) {
    }
    return b;
  }

  private void startSplashScreen(String path) {

    // add items to the splash screen here. makes things less distracting.
    ImageView iv = (ImageView) findViewById(R.id.splash);
    LinearLayout ll = (LinearLayout) findViewById(R.id.splash_default);

    File f = new File(path);
    if (f.exists()) {
      iv.setImageBitmap(decodeFile(f));
      ll.setVisibility(View.GONE);
      iv.setVisibility(View.VISIBLE);
    }

    // create a thread that counts up to the timeout
    Thread t = new Thread() {
      int count = 0;

      @Override
      public void run() {
        try {
          super.run();
          while (count < mSplashTimeout) {
            sleep(100);
            count += 100;
          }
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          endSplashScreen();
        }
      }
    };
    t.start();
  }

  private void createErrorDialog(String errorMsg, final boolean shouldExit) {
    mAlertDialog = new AlertDialog.Builder(this).create();
    mAlertDialog.setIcon(android.R.drawable.ic_dialog_info);
    mAlertDialog.setMessage(errorMsg);
    DialogInterface.OnClickListener errorListener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int i) {
        switch (i) {
        case DialogInterface.BUTTON_POSITIVE:
          if (shouldExit) {
            finish();
          }
          break;
        }
      }
    };
    mAlertDialog.setCancelable(false);
    mAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), errorListener);
    mAlertDialog.show();
  }

  @Override
  public void databaseAvailable() {
  }

  @Override
  public void databaseUnavailable() {
  }

  public static boolean hasPermissions(Context context, String... permissions) {
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
      for (String permission : permissions) {
        if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
          return false;
        }
      }
    }
    return true;
  }

  public void initialize(){
    // verify that the external SD Card is available.
    try {
      ODKFileUtils.verifyExternalStorageAvailability();
    } catch (RuntimeException e) {
      createErrorDialog(e.getMessage(), EXIT);
      return;
    }

    mImageMaxWidth = getWindowManager().getDefaultDisplay().getWidth();

    // this splash screen should be a blank slate
    setContentView(R.layout.splash_screen);

    // external intent
    appName = getIntent().getStringExtra(IntentConsts.INTENT_KEY_APP_NAME);
    if ( appName == null ) {
      appName = ODKFileUtils.getOdkDefaultAppName();
    }

    Uri uri = getIntent().getData();
    if (uri != null) {
      // initialize to the URI, then we will customize further based upon the
      // savedInstanceState...
      final Uri uriFormsProvider = FormsProviderAPI.CONTENT_URI;
      final Uri uriWebView = UrlUtils.getWebViewContentUri(this);
      if (uri.getScheme().equalsIgnoreCase(uriFormsProvider.getScheme()) &&
              uri.getAuthority().equalsIgnoreCase(uriFormsProvider.getAuthority())) {
        List<String> segments = uri.getPathSegments();
        if (segments != null && segments.size() == 1) {
          appName = segments.get(0);
        } else if (segments != null && segments.size() >= 2) {
          appName = segments.get(0);
        } else {
          String err = "Invalid " + uri.toString() + " uri. Expected two segments.";
          Log.e(t, err);
          Intent i = new Intent();
          setResult(RESULT_CANCELED, i);
          finish();
          return;
        }
      } else if ( uri.getScheme().equals(uriWebView.getScheme()) &&
              uri.getAuthority().equals(uriWebView.getAuthority()) &&
              uri.getPort() == uriWebView.getPort()) {
        List<String> segments = uri.getPathSegments();
        if (segments != null && segments.size() == 1) {
          appName = segments.get(0);
        } else {
          String err = "Invalid " + uri.toString() +
                  " uri. Expected one segment (the application name).";
          Log.e(t, err);
          Intent i = new Intent();
          setResult(RESULT_CANCELED, i);
          finish();
          return;
        }
      } else {
        String err = getString(R.string.unrecognized_uri,
                uri.toString(),
                uriWebView.toString(),
                uriFormsProvider.toString());
        Log.e(t, err);
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
        return;
      }
    }
    WebLogger.getLogger(appName).i(t, "SplashScreenActivity appName: " + appName);

    // get the package info object with version number
    PackageInfo packageInfo = null;
    try {
      packageInfo = getPackageManager().getPackageInfo(getPackageName(),
              PackageManager.GET_META_DATA);
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    }


    PropertiesSingleton props = CommonToolProperties.get(getApplicationContext(), appName);

    String toolFirstRunKey = PropertiesSingleton.toolFirstRunPropertyName
            (((AppAwareApplication) getApplication()).getToolName());

    String toolVersionKey = PropertiesSingleton.toolVersionPropertyName
            (((AppAwareApplication) getApplication()).getToolName());



    Boolean firstRun = props.getBooleanProperty(toolFirstRunKey);
    Boolean showSplash = props.getBooleanProperty(CommonToolProperties.KEY_SHOW_SPLASH);

    String splashPath = props.getProperty(CommonToolProperties.KEY_SPLASH_PATH);

    // if you've increased version code, then update the version number and set firstRun to true
    String sKeyLastVer = props.getProperty(toolVersionKey);
    long keyLastVer = (sKeyLastVer == null || sKeyLastVer.length() == 0) ? -1L : Long.valueOf(sKeyLastVer);
    if (keyLastVer < packageInfo.versionCode) {
      props.setProperty(toolVersionKey, Integer.toString(packageInfo.versionCode));
      props.writeProperties();

      firstRun = true;
    }

    // do all the first run things
    if (((firstRun == null) ? true : firstRun) || ((showSplash == null) ? false : showSplash)) {
      props.setBooleanProperty(toolFirstRunKey, false);
      props.writeProperties();
      startSplashScreen(splashPath);
    } else {
      endSplashScreen();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    switch (requestCode) {
      case PERMISSION_ALL: {
        if (grantResults.length > 0) {
          if (hasPermissions(this, PERMISSIONS)) {
            WebLogger.getLogger(appName).i(t, "Permissions are granted " + appName);
            initialize();
          } else {
            WebLogger.getLogger(appName).i(t, "Some permissions are not granted " + appName);
            Toast.makeText(this, R.string.permissions_not_granted, Toast.LENGTH_LONG).show();
            finish();
          }
        }
      }
    }
  }

}
