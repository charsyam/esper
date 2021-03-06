/**************************************************************************************
 * Copyright (C) 2006-2015 EsperTech Inc. All rights reserved.                        *
 * http://www.espertech.com/esper                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.filter;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class FilterServiceLockFine extends FilterServiceBase
{
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public FilterServiceLockFine() {
        super(new FilterServiceGranularLockFactoryReentrant());
    }

    public void acquireWriteLock() {
        lock.writeLock().lock();
    }

    public void releaseWriteLock() {
        lock.writeLock().unlock();
    }

    public FilterSet take(Set<String> statementId) {
        lock.readLock().lock();
        try {
            return super.takeInternal(statementId);
        }
        finally {
            lock.readLock().unlock();
        }
    }

    public void apply(FilterSet filterSet) {
        lock.readLock().lock();
        try {
            super.applyInternal(filterSet);
        }
        finally {
            lock.readLock().unlock();
        }
    }

    public long evaluate(EventBean theEvent, Collection<FilterHandle> matches) {
        lock.readLock().lock();
        try {
            return super.evaluateInternal(theEvent, matches);
        }
        finally {
            lock.readLock().unlock();
        }
    }

    public long evaluate(EventBean theEvent, Collection<FilterHandle> matches, String statementId) {
        lock.readLock().lock();
        try {
            return super.evaluateInternal(theEvent, matches, statementId);
        }
        finally {
            lock.readLock().unlock();
        }
    }

    public void add(FilterValueSet filterValueSet, FilterHandle callback) {
        super.addInternal(filterValueSet, callback);
    }

    public void remove(FilterHandle callback) {
        super.removeInternal(callback);
    }

    public void removeType(EventType type) {
        super.removeTypeInternal(type);
    }
}
