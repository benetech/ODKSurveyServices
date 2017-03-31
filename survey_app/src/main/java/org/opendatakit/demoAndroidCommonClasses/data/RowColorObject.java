package org.opendatakit.demoAndroidCommonClasses.data;

public final class RowColorObject {
    private final String mRowId;
    private final int mRowIndex;
    private final String mHexForeground;
    private final String mHexBackground;

    public RowColorObject(String rowId, int rowIndex, String foreground, String background) {
        this.mRowId = rowId;
        this.mRowIndex = rowIndex;
        this.mHexForeground = foreground;
        this.mHexBackground = background;
    }

    public final String getRowId() {
        return this.mRowId;
    }

    public final int getRowIndex() {
        return this.mRowIndex;
    }

    public final String getForegroundColor() {
        return this.mHexForeground;
    }

    public final String getBackgroundColor() {
        return this.mHexBackground;
    }
}
