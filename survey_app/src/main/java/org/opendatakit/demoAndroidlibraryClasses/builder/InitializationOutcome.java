package org.opendatakit.demoAndroidlibraryClasses.builder;

import java.util.ArrayList;
import java.util.HashSet;

public class InitializationOutcome {
    public ArrayList<String> outcomeLineItems = new ArrayList();
    public boolean problemExtractingToolZipContent = false;
    public boolean problemDefiningTables = false;
    public boolean problemDefiningForms = false;
    public boolean problemImportingAssetCsvContent = false;
    public HashSet<String> assetsCsvFileNotFoundSet = new HashSet();

    public InitializationOutcome() {
    }

    public void add(String outcomeLineItem) {
        this.outcomeLineItems.add(outcomeLineItem);
    }
}
