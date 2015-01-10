/*
 * Copyright 2014 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package org.onlab.packet.ndp;

import org.onlab.packet.BasePacket;
import org.onlab.packet.Data;
import org.onlab.packet.IPacket;

import java.nio.ByteBuffer;

/**
 * Implements ICMPv6 Router Advertisement packet format. (RFC 4861)
 */
public class RouterAdvertisement extends BasePacket {
    public static final byte HEADER_LENGTH = 12; // bytes

    protected byte currentHopLimit;
    protected byte mFlag;
    protected byte oFlag;
    protected short routerLifetime;
    protected int reachableTime;
    protected int retransmitTimer;

    /**
     * Gets current hop limit.
     *
     * @return the current hop limit
     */
    public byte getCurrentHopLimit() {
        return this.currentHopLimit;
    }

    /**
     * Sets current hop limit.
     *
     * @param currentHopLimit the current hop limit to set
     * @return this
     */
    public RouterAdvertisement setCurrentHopLimit(final byte currentHopLimit) {
        this.currentHopLimit = currentHopLimit;
        return this;
    }

    /**
     * Gets managed address configuration flag.
     *
     * @return the managed address configuration flag
     */
    public byte getMFlag() {
        return this.mFlag;
    }

    /**
     * Sets managed address configuration flag.
     *
     * @param mFlag the managed address configuration flag to set
     * @return this
     */
    public RouterAdvertisement setMFlag(final byte mFlag) {
        this.mFlag = mFlag;
        return this;
    }

    /**
     * Gets other configuration flag.
     *
     * @return the other configuration flag
     */
    public byte getOFlag() {
        return this.oFlag;
    }

    /**
     * Sets other configuration flag.
     *
     * @param oFlag the other configuration flag to set
     * @return this
     */
    public RouterAdvertisement setOFlag(final byte oFlag) {
        this.oFlag = oFlag;
        return this;
    }

    /**
     * Gets router lifetime.
     *
     * @return the router lifetime
     */
    public short getRouterLifetime() {
        return this.routerLifetime;
    }

    /**
     * Sets router lifetime.
     *
     * @param routerLifetime the router lifetime to set
     * @return this
     */
    public RouterAdvertisement setRouterLifetime(final short routerLifetime) {
        this.routerLifetime = routerLifetime;
        return this;
    }

    /**
     * Gets reachable time.
     *
     * @return the reachable time
     */
    public int getReachableTime() {
        return this.reachableTime;
    }

    /**
     * Sets reachable time.
     *
     * @param reachableTime the reachable time to set
     * @return this
     */
    public RouterAdvertisement setReachableTime(final int reachableTime) {
        this.reachableTime = reachableTime;
        return this;
    }

    /**
     * Gets retransmission timer.
     *
     * @return the retransmission timer
     */
    public int getRetransmitTimer() {
        return this.retransmitTimer;
    }

    /**
     * Sets retransmission timer.
     *
     * @param retransmitTimer the retransmission timer to set
     * @return this
     */
    public RouterAdvertisement setRetransmitTimer(final int retransmitTimer) {
        this.retransmitTimer = retransmitTimer;
        return this;
    }

    @Override
    public byte[] serialize() {
        byte[] payloadData = null;
        if (this.payload != null) {
            this.payload.setParent(this);
            payloadData = this.payload.serialize();
        }

        int payloadLength = 0;
        if (payloadData != null) {
            payloadLength = payloadData.length;
        }

        final byte[] data = new byte[HEADER_LENGTH + payloadLength];
        final ByteBuffer bb = ByteBuffer.wrap(data);

        bb.put(this.currentHopLimit);
        bb.put((byte) ((this.mFlag & 0x1) << 7 | (this.oFlag & 0x1) << 6));
        bb.putShort(routerLifetime);
        bb.putInt(reachableTime);
        bb.putInt(retransmitTimer);

        if (payloadData != null) {
            bb.put(payloadData);
        }

        return data;
    }

    @Override
    public IPacket deserialize(byte[] data, int offset, int length) {
        final ByteBuffer bb = ByteBuffer.wrap(data, offset, length);
        int bscratch;

        this.currentHopLimit = bb.get();
        bscratch = bb.get();
        this.mFlag = (byte) ((bscratch >> 7) & 0x1);
        this.oFlag = (byte) ((bscratch >> 6) & 0x1);
        this.routerLifetime = bb.getShort();
        this.reachableTime = bb.getInt();
        this.retransmitTimer = bb.getInt();

        this.payload = new Data();
        this.payload = this.payload.deserialize(data, bb.position(), bb.limit()
                - bb.position());
        this.payload.setParent(this);

        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 5807;
        int result = super.hashCode();
        result = prime * result + this.currentHopLimit;
        result = prime * result + this.mFlag;
        result = prime * result + this.oFlag;
        result = prime * result + this.routerLifetime;
        result = prime * result + this.reachableTime;
        result = prime * result + this.retransmitTimer;
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof RouterAdvertisement)) {
            return false;
        }
        final RouterAdvertisement other = (RouterAdvertisement) obj;
        if (this.currentHopLimit != other.currentHopLimit) {
            return false;
        }
        if (this.mFlag != other.mFlag) {
            return false;
        }
        if (this.oFlag != other.oFlag) {
            return false;
        }
        if (this.routerLifetime != other.routerLifetime) {
            return false;
        }
        if (this.reachableTime != other.reachableTime) {
            return false;
        }
        if (this.retransmitTimer != other.retransmitTimer) {
            return false;
        }
        return true;
    }
}
