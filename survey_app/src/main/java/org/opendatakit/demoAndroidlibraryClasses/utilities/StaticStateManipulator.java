package org.opendatakit.demoAndroidlibraryClasses.utilities;

import java.util.ArrayList;
import java.util.Iterator;

public class StaticStateManipulator {
    private static StaticStateManipulator gStateManipulator = new StaticStateManipulator();
    private final ArrayList<StaticStateManipulator.IStaticFieldManipulator> mStaticManipulators = new ArrayList();

    public static synchronized StaticStateManipulator get() {
        return gStateManipulator;
    }

    public static synchronized void set(StaticStateManipulator sm) {
        gStateManipulator = sm;
    }

    protected StaticStateManipulator() {
    }

    public void reset() {
        Iterator i$ = this.mStaticManipulators.iterator();

        while(i$.hasNext()) {
            StaticStateManipulator.IStaticFieldManipulator fm = (StaticStateManipulator.IStaticFieldManipulator)i$.next();
            fm.reset();
        }

    }

    public void register(int order, StaticStateManipulator.IStaticFieldManipulator fm) {
        this.mStaticManipulators.add(fm);
    }

    public interface IStaticFieldManipulator {
        void reset();
    }
}
