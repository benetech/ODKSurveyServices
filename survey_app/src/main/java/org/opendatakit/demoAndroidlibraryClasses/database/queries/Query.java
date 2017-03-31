package org.opendatakit.demoAndroidlibraryClasses.database.queries;

import org.opendatakit.demoAndroidlibraryClasses.database.queries.BindArgs;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.QueryBounds;

public interface Query {
    String getSqlCommand();

    BindArgs getSqlBindArgs();

    QueryBounds getSqlQueryBounds();
}
