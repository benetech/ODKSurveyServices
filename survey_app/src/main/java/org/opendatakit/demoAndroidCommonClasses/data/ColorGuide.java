package org.opendatakit.demoAndroidCommonClasses.data;

public final class ColorGuide {
    private final int mForeground;
    private final int mBackground;

    public ColorGuide(int foreground, int background) {
        this.mForeground = foreground;
        this.mBackground = background;
    }

    public final int getForeground() {
        return this.mForeground;
    }

    public final int getBackground() {
        return this.mBackground;
    }
}
