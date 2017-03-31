package org.opendatakit.demoAndroidlibraryClasses.properties;

import java.io.File;
import java.io.FileFilter;
import org.opendatakit.demoAndroidlibraryClasses.properties.PropertyManager.DynamicPropertiesInterface;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public class DynamicPropertiesCallback implements DynamicPropertiesInterface {
    private final String appName;
    private final String tableId;
    private final String instanceId;
    private final String activeUser;
    private final String locale;
    private final String username;
    private final String userEmail;

    public DynamicPropertiesCallback(String appName, String tableId, String instanceId, String activeUser, String locale, String username, String userEmail) {
        this.appName = appName;
        this.tableId = tableId;
        this.instanceId = instanceId;
        this.activeUser = activeUser;
        this.locale = locale;
        this.username = username;
        this.userEmail = userEmail;
    }

    public String getActiveUser() {
        return this.activeUser;
    }

    public String getLocale() {
        return this.locale;
    }

    public String getUsername() {
        return this.username;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getAppName() {
        return this.appName;
    }

    public String getInstanceDirectory() {
        if(this.tableId == null) {
            throw new IllegalStateException("getInstanceDirectory() unexpectedly invoked outside of a form.");
        } else {
            String mediaPath = ODKFileUtils.getInstanceFolder(this.appName, this.tableId, this.instanceId);
            return mediaPath;
        }
    }

    public String getUriFragmentNewInstanceFile(String uriDeviceId, String extension) {
        if(this.tableId == null) {
            throw new IllegalStateException("getUriFragmentNewInstanceFile(...) unexpectedly invoked outside of a form.");
        } else {
            String mediaPath = ODKFileUtils.getInstanceFolder(this.appName, this.tableId, this.instanceId);
            File f = new File(mediaPath);
            f.mkdirs();

            String filePath;
            String fileName;
            File[] files;
            do {
                filePath = Long.toString(System.currentTimeMillis()) + "_" + uriDeviceId;
                fileName = filePath.replaceAll("[\\p{Punct}\\p{Space}]", "_");
                final String finalFileName = fileName;
                files = f.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        String name = pathname.getName();
                        if(!name.startsWith(finalFileName)) {
                            return false;
                        } else {
                            int idx = name.indexOf(46);
                            if(idx != -1) {
                                String firstPart = name.substring(0, idx);
                                if(firstPart.equals(finalFileName)) {
                                    return true;
                                }
                            } else if(name.equals(finalFileName)) {
                                return true;
                            }

                            return false;
                        }
                    }
                });
            } while(files != null && files.length != 0);

            filePath = mediaPath + File.separator + fileName + (extension != null && extension.length() > 0?"." + extension:"");
            return ODKFileUtils.asUriFragment(this.appName, new File(filePath));
        }
    }
}
