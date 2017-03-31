package org.opendatakit.demoAndroidCommonClasses.views;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.opendatakit.demoAndroidCommonClasses.activities.IOdkDataActivity;
import org.opendatakit.demoAndroidlibraryClasses.database.data.OrderedColumns;
import org.opendatakit.demoAndroidlibraryClasses.database.service.DbHandle;
import org.opendatakit.demoAndroidlibraryClasses.database.service.UserDbInterface;
import org.opendatakit.demoAndroidlibraryClasses.exception.ServicesAvailabilityException;
import org.opendatakit.demoAndroidCommonClasses.listener.DatabaseConnectionListener;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public class ExecutorContext implements DatabaseConnectionListener {
    private static final String TAG = "ExecutorContext";
    private static ExecutorContext currentContext = null;
    private final IOdkDataActivity activity;
    private final Object mutex = new Object();
    private final ExecutorService worker = Executors.newSingleThreadExecutor();
    private final LinkedList<ExecutorRequest> workQueue = new LinkedList();
    private Map<String, DbHandle> activeConnections = new HashMap();
    private Map<String, OrderedColumns> mCachedOrderedDefns = new HashMap();

    private static void updateCurrentContext(ExecutorContext ctxt) {
        if(currentContext != null) {
            ctxt.queueRequest(new ExecutorRequest(currentContext));
        }

        currentContext = ctxt;
        ctxt.activity.registerDatabaseConnectionBackgroundListener(ctxt);
    }

    private ExecutorContext(IOdkDataActivity fragment) {
        this.activity = fragment;
        updateCurrentContext(this);
    }

    public static synchronized ExecutorContext getContext(IOdkDataActivity fragment) {
        return currentContext != null && currentContext.activity == fragment?currentContext:new ExecutorContext(fragment);
    }

    private void triggerExecutorProcessor() {
        ExecutorProcessor processor = this.activity.newExecutorProcessor(this);
        Object var2 = this.mutex;
        synchronized(this.mutex) {
            if(!this.worker.isShutdown() && !this.worker.isTerminated() && !this.workQueue.isEmpty()) {
                this.worker.execute(processor);
            }

        }
    }

    public void queueRequest(ExecutorRequest request) {
        ExecutorProcessor processor = this.activity.newExecutorProcessor(this);
        Object var3 = this.mutex;
        synchronized(this.mutex) {
            if(!this.worker.isShutdown() && !this.worker.isTerminated()) {
                this.workQueue.add(request);
                this.worker.execute(processor);
            }

        }
    }

    public ExecutorRequest peekRequest() {
        Object var1 = this.mutex;
        synchronized(this.mutex) {
            return this.workQueue.isEmpty()?null:(ExecutorRequest)this.workQueue.peekFirst();
        }
    }

    public void popRequest(boolean trigger) {
        ExecutorProcessor processor = trigger?this.activity.newExecutorProcessor(this):null;
        Object var3 = this.mutex;
        synchronized(this.mutex) {
            if(!this.workQueue.isEmpty()) {
                this.workQueue.removeFirst();
            }

            if(!this.worker.isShutdown() && !this.worker.isTerminated() && trigger && !this.workQueue.isEmpty()) {
                this.worker.execute(processor);
            }

        }
    }

    void shutdownWorker() {
        WebLogger.getLogger(currentContext.getAppName()).i("ExecutorContext", "shutdownWorker - shutting down dataif Executor");
        Throwable t = null;
        Object var2 = this.mutex;
        synchronized(this.mutex) {
            if(!this.worker.isShutdown() && !this.worker.isTerminated()) {
                this.worker.shutdown();
            }

            try {
                this.worker.awaitTermination(3000L, TimeUnit.MILLISECONDS);
            } catch (Throwable var5) {
                t = var5;
            }
        }

        if(t != null) {
            WebLogger.getLogger(currentContext.getAppName()).w("ExecutorContext", "shutdownWorker - dataif Executor threw exception while shutting down");
            WebLogger.getLogger(currentContext.getAppName()).printStackTrace(t);
        }

        WebLogger.getLogger(currentContext.getAppName()).i("ExecutorContext", "shutdownWorker - dataif Executor has been shut down.");
    }

    public DbHandle getActiveConnection(String transId) {
        Object var2 = this.mutex;
        synchronized(this.mutex) {
            return (DbHandle)this.activeConnections.get(transId);
        }
    }

    public void registerActiveConnection(String transId, DbHandle dbHandle) {
        boolean alreadyExists = false;
        Object var4 = this.mutex;
        synchronized(this.mutex) {
            if(this.activeConnections.containsKey(transId)) {
                alreadyExists = true;
            } else {
                this.activeConnections.put(transId, dbHandle);
            }
        }

        if(alreadyExists) {
            WebLogger.getLogger(currentContext.getAppName()).e("ExecutorContext", "transaction id " + transId + " already registered!");
            throw new IllegalArgumentException("transaction id already registered!");
        }
    }

    private String getFirstActiveTransactionId() {
        Object var1 = this.mutex;
        synchronized(this.mutex) {
            Set transIds = this.activeConnections.keySet();
            return transIds.isEmpty()?null:(String)transIds.iterator().next();
        }
    }

    public void removeActiveConnection(String transId) {
        Object var2 = this.mutex;
        synchronized(this.mutex) {
            this.activeConnections.remove(transId);
        }
    }

    public OrderedColumns getOrderedColumns(String tableId) {
        Object var2 = this.mutex;
        synchronized(this.mutex) {
            return (OrderedColumns)this.mCachedOrderedDefns.get(tableId);
        }
    }

    public void putOrderedColumns(String tableId, OrderedColumns orderedColumns) {
        Object var3 = this.mutex;
        synchronized(this.mutex) {
            this.mCachedOrderedDefns.put(tableId, orderedColumns);
        }
    }

    public UserDbInterface getDatabase() {
        return this.activity.getDatabase();
    }

    public String getAppName() {
        return this.activity.getAppName();
    }

    public void releaseResources(String reason) {
        String errorMessage = ServicesAvailabilityException.class.getName() + ": releaseResources - shutting down worker (" + reason + ") -- rolling back all transactions and releasing all connections";

        while(true) {
            ExecutorRequest activeConns = this.peekRequest();
            if(activeConns == null) {
                WebLogger.getLogger(currentContext.getAppName()).i("ExecutorContext", "releaseResources - workQueue has been purged.");
                int var14 = 0;

                while(true) {
                    String transId = this.getFirstActiveTransactionId();
                    if(transId == null) {
                        WebLogger.getLogger(currentContext.getAppName()).w("ExecutorContext", "releaseResources - closed " + var14 + " associated dbHandles");
                        return;
                    }

                    DbHandle dbh = this.getActiveConnection(transId);
                    this.removeActiveConnection(transId);
                    if(dbh == null) {
                        WebLogger.getLogger(this.getAppName()).w("ExecutorContext", "Unexpected failure to retrieve dbHandle for " + transId);
                    }

                    UserDbInterface dbInterface = currentContext.getDatabase();
                    if(dbInterface != null) {
                        try {
                            WebLogger.getLogger(currentContext.getAppName()).i("ExecutorContext", "releaseResources - closing dbHandle " + dbh.toString());
                            dbInterface.closeDatabase(currentContext.getAppName(), dbh);
                            ++var14;
                        } catch (Throwable var11) {
                            WebLogger.getLogger(currentContext.getAppName()).w("ExecutorContext", "releaseResources - Exception thrown while trying to close dbHandle");
                            WebLogger.getLogger(currentContext.getAppName()).printStackTrace(var11);
                        }
                    }
                }
            }

            try {
                this.reportError(activeConns.callbackJSON, (String)null, errorMessage);
            } catch (Exception var12) {
                WebLogger.getLogger(this.getAppName()).w("ExecutorContext", "releaseResources - exception while cancelling outstanding requests");
            } finally {
                this.popRequest(false);
            }
        }
    }

    public void reportError(String callbackJSON, String transId, String errorMessage) {
        if(callbackJSON != null) {
            HashMap response = new HashMap();
            response.put("callbackJSON", callbackJSON);
            response.put("error", errorMessage);
            if(transId != null) {
                response.put("transId", transId);
            }

            String responseStr = null;

            try {
                responseStr = ODKFileUtils.mapper.writeValueAsString(response);
            } catch (JsonProcessingException var7) {
                WebLogger.getLogger(currentContext.getAppName()).e("ExecutorContext", "should never have a conversion error");
                WebLogger.getLogger(currentContext.getAppName()).printStackTrace(var7);
                throw new IllegalStateException("should never have a conversion error");
            }

            this.activity.signalResponseAvailable(responseStr);
        }

    }

    public void reportSuccess(String callbackJSON, String transId, ArrayList<List<Object>> data, Map<String, Object> metadata) {
        HashMap response = new HashMap();
        response.put("callbackJSON", callbackJSON);
        if(transId != null) {
            response.put("transId", transId);
        }

        if(data != null) {
            response.put("data", data);
        }

        if(metadata != null) {
            response.put("metadata", metadata);
        }

        String responseStr = null;

        try {
            responseStr = ODKFileUtils.mapper.writeValueAsString(response);
        } catch (JsonProcessingException var8) {
            WebLogger.getLogger(currentContext.getAppName()).e("ExecutorContext", "should never have a conversion error");
            WebLogger.getLogger(currentContext.getAppName()).printStackTrace(var8);
            throw new IllegalStateException("should never have a conversion error");
        }

        this.activity.signalResponseAvailable(responseStr);
    }

    public void databaseAvailable() {
        this.triggerExecutorProcessor();
    }

    public void databaseUnavailable() {
        new ExecutorContext(this.activity);
    }

    public synchronized boolean isAlive() {
        return !this.worker.isShutdown() && !this.worker.isTerminated();
    }
}
