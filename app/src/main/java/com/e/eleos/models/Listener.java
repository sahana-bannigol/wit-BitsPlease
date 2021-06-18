package com.e.eleos.models;

import com.cloudant.sync.event.Subscribe;
import com.cloudant.sync.event.notifications.ReplicationCompleted;
import com.cloudant.sync.event.notifications.ReplicationErrored;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Listener {
    private final CountDownLatch latch;
    public List<Throwable> errors = new ArrayList<Throwable>();
    public int documentsReplicated = 0;
    public int batchesReplicated = 0;

    public Listener(CountDownLatch latch) {
        this.latch = latch;
    }

    @Subscribe
    public void complete(ReplicationCompleted event) {
        this.documentsReplicated += event.documentsReplicated;
        this.batchesReplicated += event.batchesReplicated;
        latch.countDown();
    }

    @Subscribe
    public void error(ReplicationErrored event) {
        this.errors.add(event.errorInfo);
        latch.countDown();
    }

}
