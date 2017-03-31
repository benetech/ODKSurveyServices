package org.opendatakit.demoAndroidCommonClasses.dependencies;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import org.opendatakit.demoAndroidCommonClasses.application.CommonApplication;
import org.opendatakit.survey.R;

public class DependencyChecker {
 public static final String surveyAppPkgName = "org.opendatakit.survey";
 private static final String oiFileMgrPkgName = "org.openintents.filemanager";
 private static final String servicesAppPkgName = "org.opendatakit.services";
 private static final String tables = "tables";
 private static final String scan = "scan";
 private final Activity activity;
 private final Context context;

 public DependencyChecker(Activity activity) {
 this.activity = activity;
 this.context = activity.getApplicationContext();
 }

 public boolean checkDependencies() {
 boolean oiInstalled;
 if(!"tables".equals(((CommonApplication)this.context).getToolName()) && !"scan".equals(((CommonApplication)this.context).getToolName())) {
 oiInstalled = true;
 } else {
 oiInstalled = isPackageInstalled(this.context, "org.openintents.filemanager");
 }

 boolean servicesInstalled = isPackageInstalled(this.context, "org.opendatakit.services");
 if(oiInstalled && servicesInstalled) {
 return true;
 } else {
 this.alertMissing(oiInstalled, servicesInstalled);
 return false;
 }
 }

 private void alertMissing(boolean oiInstalled, boolean servicesInstalled) {
 String title = this.context.getString(R.string.dependency_missing);
 String message;
 if(oiInstalled && !servicesInstalled) {
 message = this.context.getString(R.string.services_missing);
 } else if(!oiInstalled && servicesInstalled) {
 message = this.context.getString(R.string.oi_missing);
 } else {
 message = this.context.getString(R.string.oi_and_services_missing);
 title = this.context.getString(R.string.dependencies_missing);
 }

 AlertDialog alert = this.buildAlert(title, message);
 alert.show();
 }

 private AlertDialog buildAlert(String title, String message) {
 Builder builder = new Builder(this.activity);
 builder.setTitle(title);
 builder.setMessage(message);
 builder.setCancelable(false);
 builder.setPositiveButton("OK", new OnClickListener() {
 public void onClick(DialogInterface dialog, int which) {
 DependencyChecker.this.activity.finish();
 System.exit(0);
 }
 });
 return builder.create();
 }

 public static boolean isPackageInstalled(Context context, String packageName) {
 PackageManager pm = context.getPackageManager();

 try {
 pm.getPackageInfo(packageName, 1);
 return true;
 } catch (NameNotFoundException var4) {
 return false;
 }
 }
 }
