package com.palantir.atlasdb.keyvalue.partition.status;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.palantir.atlasdb.keyvalue.partition.KeyValueEndpoint;


@JsonTypeInfo(use=Id.CLASS, property="@class")
public abstract class EndpointWithStatus {

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endpoint == null) ? 0 : endpoint.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EndpointWithStatus other = (EndpointWithStatus) obj;
        if (endpoint == null) {
            if (other.endpoint != null)
                return false;
        } else if (!endpoint.equals(other.endpoint))
            return false;
        return true;
    }

    @JsonProperty("endpoint")
    final KeyValueEndpoint endpoint;

    public final KeyValueEndpoint get() {
        return endpoint;
    }

    @JsonCreator
    public EndpointWithStatus(@JsonProperty("endpoint") KeyValueEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    public abstract boolean shouldUseForRead();
    public abstract boolean shouldCountForRead();
    public abstract boolean shouldUseForWrite();
    public abstract boolean shouldCountForWrite();

    public boolean shouldUseFor(boolean write) {
        if (write) {
            return shouldUseForWrite();
        }
        return shouldUseForRead();
    }

    public boolean shouldCountFor(boolean write) {
        if (write) {
            return shouldCountForWrite();
        }
        return shouldCountForRead();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " kvs=" + endpoint.hashCode();
    }

}
