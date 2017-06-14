/*
 * Copyright (C) 2012 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opendatakit.services.sync.service.logic;

import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.aggregate.odktables.rest.SyncState;
import org.opendatakit.aggregate.odktables.rest.entity.Column;
import org.opendatakit.aggregate.odktables.rest.entity.TableResource;
import org.opendatakit.demoAndroidlibraryClasses.database.data.BaseTable;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnDefinition;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnList;
import org.opendatakit.demoAndroidlibraryClasses.database.data.OrderedColumns;
import org.opendatakit.demoAndroidlibraryClasses.database.data.Row;
import org.opendatakit.demoAndroidlibraryClasses.database.data.TableDefinitionEntry;
import org.opendatakit.demoAndroidlibraryClasses.database.data.UserTable;
import org.opendatakit.demoAndroidlibraryClasses.database.service.DbHandle;
import org.opendatakit.demoAndroidlibraryClasses.exception.ServicesAvailabilityException;
import org.opendatakit.demoAndroidlibraryClasses.provider.DataTableColumns;
import org.opendatakit.survey.R;
import org.opendatakit.services.sync.service.SyncExecutionContext;
import org.opendatakit.demoAndroidlibraryClasses.sync.service.SyncAttachmentState;
import org.opendatakit.demoAndroidlibraryClasses.sync.service.SyncOutcome;
import org.opendatakit.demoAndroidlibraryClasses.sync.service.TableLevelResult;

import java.util.ArrayList;
import java.util.List;

/**
 * SyncProcessor implements the cloud synchronization logic for Tables.
 *
 * @author the.dylan.price@gmail.com
 * @author sudar.sam@gmail.com
 *
 */
class ProcessRowDataSyncAttachments extends ProcessRowDataSharedBase {

  private static final String TAG = ProcessRowDataSyncAttachments.class.getSimpleName();

  private static final double minPercentage = 75.0;
  private static final double maxPercentage = 100.0;

  private final ProcessManifestContentAndFileChanges manifestProcessor;

  public ProcessRowDataSyncAttachments(SyncExecutionContext sharedContext) {
    super(sharedContext);
    this.manifestProcessor = new ProcessManifestContentAndFileChanges(sc);
    setUpdateNotificationBounds(minPercentage, maxPercentage, 1);
  }

  /**
   * Synchronize the table data rows.
   * <p>
   * Note that if the db changes under you when calling this method, the tp
   * parameter will become out of date. It should be refreshed after calling
   * this method.
   * <p>
   * This method does NOT synchronize any non-instance files; it assumes the
   * database schema has already been sync'd.
   *
   * @param tableResource
   *          the table resource from the server, either from the getTables()
   *          call or from a createTable() response.
   * @param te
   *          definition of the table to synchronize
   * @param orderedColumns
   *          well-formed ordered list of columns in this table.
   * @param fileAttachmentColumns
   *          list of columns that might contain file attachment filenames.
   * @param attachmentState
   * @return true if changes need to be pulled from the server before continuing
   * @throws ServicesAvailabilityException
   */
  public void syncAttachments(TableResource tableResource,
      TableDefinitionEntry te, OrderedColumns orderedColumns,
      ArrayList<ColumnDefinition> fileAttachmentColumns,
      SyncAttachmentState attachmentState) throws ServicesAvailabilityException {

    // Prepare the tableLevelResult.
    String tableId = te.getTableId();
    TableLevelResult tableLevelResult = sc.getTableLevelResult(tableId);
    getLogger().i( TAG, "syncAttachments - tableId: " + tableId +
        " attachmentState: " + attachmentState.toString());

    if ( fileAttachmentColumns.isEmpty() ) {
      publishUpdateNotification(R.string.sync_attachment_no_changes, tableId, maxPercentage);
      return;
    }

    publishUpdateNotification(R.string.sync_count_attachment_changes, tableId, minPercentage);

    String local_id_table = "L__" + tableId;

    // figure out how many rows there are to sync.
    int rowsToSyncCount = 0;
    {
      DbHandle db = null;
      try {
        db = sc.getDatabase();




        // We need to create a temporary table and fill it with all the IDs of
        // the rows that may have attachments that should be sync'd to the server.
        //
        // This creates a static list of ids that we can then work off of.
        //
        // If we didn't do this, in the course of syncing attachments from the
        // first batch of local rows, we would be updating the field values that
        // were used to select the rows with attachments to sync, and would lose
        // track of where the second batch of rows with attachments to sync begins.
        //
        List<Column> columns = new ArrayList<Column>();
        columns.add(
            new Column(ID_COLUMN, ID_COLUMN, ElementDataType.string.name(),
                "[]"));
        ColumnList columnList = new ColumnList(columns);

        // create the table (drop it first -- to get an empty table)
        sc.getOdkDbServiceConnection().getDatabaseService().deleteLocalOnlyTable(sc.getAppName(), db, local_id_table);
        sc.getOdkDbServiceConnection().getDatabaseService()
            .createLocalOnlyTableWithColumns(sc.getAppName(), db, local_id_table, columnList);


        String sqlCommand;
        Object[] bindArgs = new Object[]{ SyncState.in_conflict.name(), SyncState.synced_pending_files.name() };

        {
          StringBuilder sqlCommandBuilder = new StringBuilder();
          sqlCommandBuilder.append("INSERT INTO ").append(local_id_table)
              .append(" (").append(ID_COLUMN).append(" ) SELECT DISTINCT ")
              .append(DataTableColumns.ID.getText()).append(" FROM ").append(tableId)
              .append(" WHERE ")
              .append(DataTableColumns.SYNC_STATE.getText()).append(" IN (?, ?) AND ")
              .append(DataTableColumns.ID.getText()).append(" NOT IN (SELECT DISTINCT ")
              .append(DataTableColumns.ID.getText()).append(" FROM ").append(tableId).append(" WHERE ")
              .append(DataTableColumns.SAVEPOINT_TYPE.getText()).append(" IS NULL)");
          sqlCommand = sqlCommandBuilder.toString();
        }

        // create the list of IDs
        sc.getOdkDbServiceConnection().getDatabaseService().privilegedExecute(sc.getAppName(), db, sqlCommand, bindArgs);

        // now count the number
        StringBuilder b = new StringBuilder();
        b.append("SELECT COUNT(*) as rowCount FROM ").append(local_id_table);

        BaseTable bt = sc.getOdkDbServiceConnection().getDatabaseService().arbitrarySqlQuery(sc.getAppName(), db, null,
            b.toString(), null, null, null);
        if ( bt.getNumberOfRows() != 1 || bt.getColumnIndexOfElementKey("rowCount") != 0 ) {
          tableLevelResult
              .setMessage("Unable to retrieve count of rows with attachments to reconcile with "
                  + "server");
          tableLevelResult.setSyncOutcome(SyncOutcome.LOCAL_DATABASE_EXCEPTION);
          return;
        }
        rowsToSyncCount = bt.getRowAtIndex(0).getDataType(0, Integer.class);

      } finally {
        sc.releaseDatabase(db);
        db = null;
      }
    }

    SyncOutcome tableLevelSyncOutcome = SyncOutcome.WORKING;

    if ( rowsToSyncCount != 0 ) {
      setUpdateNotificationBounds(minPercentage, maxPercentage, rowsToSyncCount);

      int fetchOffset = 0;
      int fetchLimit = (orderedColumns.getColumnDefinitions().size() > maxColumnsToUseLargeFetchLimit)
          ? smallFetchLimit : largeFetchLimit;

      String whereClause;
      {
        StringBuilder whereClauseBuilder = new StringBuilder();
        whereClauseBuilder.append(DataTableColumns.ID.getText()).append(" IN (SELECT ")
            .append(ID_COLUMN).append(" FROM ").append(local_id_table)
            .append(" LIMIT ? OFFSET ? )");
        whereClause = whereClauseBuilder.toString();
      }

      for (; ; ) {

        publishUpdateNotification(R.string.sync_fetch_batch_attachment_changes, tableId, -1.0);

        UserTable localDataTable;
        try {
          // //////////////////////////////////////////////////
          // //////////////////////////////////////////////////
          {
            DbHandle db = null;
            try {
              db = sc.getDatabase();
              String[] empty = {};
              Object[] bindArgs = new Object[] {fetchLimit, fetchOffset};

              localDataTable = sc.getOdkDbServiceConnection().getDatabaseService()
                  .privilegedSimpleQuery(sc.getAppName(), db, tableId, orderedColumns, whereClause,
                      bindArgs, empty, null, new String[] { DataTableColumns.ID.getText() },
                      new String[] { "ASC" }, fetchLimit, fetchOffset);
            } finally {
              sc.releaseDatabase(db);
              db = null;
            }
          }

          fetchOffset += localDataTable.getNumberOfRows();

          /**************************
           * PART 2: UPDATE THE DATA
           **************************/

          // loop through the localRow table
          for (int i = 0; i < localDataTable.getNumberOfRows(); i++) {
            Row localRow = localDataTable.getRowAtIndex(i);
            String stateStr = localRow.getDataByKey(DataTableColumns.SYNC_STATE.getText());
            SyncState state = (stateStr == null) ? null : SyncState.valueOf(stateStr);

            getLogger().i(TAG, "syncAttachments examining row " + localRow.getDataByKey(DataTableColumns.ID.getText()));

            boolean syncAttachments = false;
            // the local row wasn't impacted by a server change
            // see if this local row should be pushed to the server.
            if (state == SyncState.in_conflict) {
              if (!fileAttachmentColumns.isEmpty()) {
                // fetch the file attachments for an in_conflict row but don't delete
                // anything and never update the state to synced (it must stay in in_conflict)
                syncAttachments = true;
              }
            } else if (state == SyncState.synced_pending_files) {
              // if we succeed in fetching and deleting the local files to match the server
              // then update the state to synced.
              syncAttachments = true;
            }

            if (syncAttachments) {
              // And try to push the file attachments...
              try {
                boolean outcome = true;

                SyncAttachmentState filteredAttachmentState = (state == SyncState.in_conflict ?
                    SyncAttachmentState.DOWNLOAD :
                    attachmentState);

                outcome = manifestProcessor.syncRowLevelFileAttachments(
                            sc.getUriWithServerCompatibleScheme(tableResource.getInstanceFilesUri()),
                            tableId, localRow, fileAttachmentColumns, attachmentState);

                if (outcome) {
                  if (state == SyncState.synced_pending_files) {
                    // OK -- we succeeded in putting/getting all attachments
                    // update our state to the synced state.
                    DbHandle db = null;
                    try {
                      db = sc.getDatabase();
                      sc.getOdkDbServiceConnection().getDatabaseService()
                          .privilegedUpdateRowETagAndSyncState(sc.getAppName(), db, tableId,
                              localRow.getDataByKey(DataTableColumns.ID.getText()), localRow.getDataByKey(DataTableColumns.ROW_ETAG.getText()),
                              SyncState.synced.name());
                    } finally {
                      sc.releaseDatabase(db);
                      db = null;
                    }
                  }
                }
              } catch (Throwable e) {
                getLogger().printStackTrace(e);
                tableLevelSyncOutcome = sc.exceptionEquivalentOutcome(e);
                getLogger().e(TAG, "[synchronizeTableRest] error synchronizing attachments " + e.toString());
              }
              tableLevelResult.incLocalAttachmentRetries();

              getLogger().i(TAG, "syncAttachments completed processing for " + localRow.getDataByKey(DataTableColumns.ID.getText()));

              int idString;
              switch (attachmentState) {
              default:
              case NONE:
                idString = R.string.sync_skipping_attachments_server_row;
                break;
              case SYNC:
                idString = R.string.sync_syncing_attachments_server_row;
                break;
              case UPLOAD:
                idString = R.string.sync_uploading_attachments_server_row;
                break;
              case DOWNLOAD:
                idString = R.string.sync_downloading_attachments_server_row;
                break;
              }

              publishUpdateNotification(idString, tableId);
            }
          }
        } catch (Exception e) {
          exception("synchronizeTable - pushing data up to server", tableId, e, tableLevelResult);
          return;
        }

        if (localDataTable.getNumberOfRows() < fetchLimit) {
          // done!
          // OK. Now we have pushed everything.
          // because of the 409 (CONFLICT) alterRows enforcement on the
          // server, we know that our data records are consistent and
          // our processing is complete.

          // And now update that we've pushed our changes to the server.
          tableLevelResult.setPushedLocalData(true);
          break;
        }
      }
    }

    if ( tableLevelSyncOutcome != SyncOutcome.WORKING ) {
      tableLevelResult.setSyncOutcome(tableLevelSyncOutcome);
      tableLevelResult.setMessage("exception while syncing row-level attachments");
    }

    publishUpdateNotification(R.string.sync_attachment_completed, tableId, maxPercentage);

    // if we got here, perhaps some of the attachments were sync'd.
    return;
  }
}
