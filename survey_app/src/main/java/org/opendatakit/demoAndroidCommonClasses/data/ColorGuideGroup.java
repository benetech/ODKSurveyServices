package org.opendatakit.demoAndroidCommonClasses.data;

import java.util.Map;
import java.util.TreeMap;

import org.opendatakit.demoAndroidlibraryClasses.database.data.Row;
import org.opendatakit.demoAndroidlibraryClasses.database.data.UserTable;

public class ColorGuideGroup {
    private Map<String, ColorGuide> mRowIdToColors = new TreeMap();
    private ColorRuleGroup mCRG;
    private UserTable mUT;

    public ColorGuideGroup(ColorRuleGroup crg, UserTable ut) {
        if(crg != null) {
            this.mCRG = crg;
            if(ut != null) {
                this.mUT = ut;

                for(int i = 0; i < this.mUT.getNumberOfRows(); ++i) {
                    ColorGuide tcg = this.mCRG.getColorGuide(this.mUT.getColumnDefinitions(), this.mUT.getRowAtIndex(i));
                    this.mRowIdToColors.put(this.mUT.getRowId(i), tcg);
                }

            }
        }
    }

    public Map<String, ColorGuide> getAllColorGuides() {
        return this.mRowIdToColors;
    }

    public ColorGuide getColorGuideForRowIndex(int i) {
        ColorGuide cg = null;
        Row colorRow = null;

        try {
            colorRow = this.mUT.getRowAtIndex(i);
        } catch (IllegalArgumentException var5) {
            var5.printStackTrace();
        }

        if(colorRow != null && this.mRowIdToColors != null && this.mRowIdToColors.containsKey(this.mUT.getRowId(i))) {
            cg = (ColorGuide)this.mRowIdToColors.get(this.mUT.getRowId(i));
        }

        return cg;
    }

    public ColorGuide getColorGuideForRowId(String rowId) {
        ColorGuide cg = null;
        if(this.mRowIdToColors != null && this.mRowIdToColors.containsKey(rowId)) {
            cg = (ColorGuide)this.mRowIdToColors.get(rowId);
        }

        return cg;
    }
}
