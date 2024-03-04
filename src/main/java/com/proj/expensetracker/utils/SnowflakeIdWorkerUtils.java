package com.proj.expensetracker.utils;

/**
 * @author Norbs
 * @date 2024 Feb 4
 * @description
 * SnowFlake's structure is as follows (each part separated by -):
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 *
 * 1 bit sign, the highest bit is the sign bit because the long basic type in Java is signed,
 * positive numbers are 0, negative numbers are 1, so the ID is generally positive, and the highest bit is 0.
 *
 * 41 bits of timestamp (in milliseconds), note that 41 bits of timestamp do not store the current timestamp,
 * but store the difference in timestamps (current timestamp - start timestamp).
 * Here, the start timestamp is generally the time when our ID generator starts using,
 * specified by our program (such as the startTime property of the IdWorker class below).
 * With 41 bits of timestamp, it can be used for 69 years, where T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69.
 *
 * 10 bits of data center and machine ID, can be deployed on 1024 nodes, including 5 bits of datacenterId and 5 bits of workerId.
 *
 * 12 bits of sequence, counting within a millisecond, 12 bits of sequence number support 4096 ID numbers generated per millisecond
 * (the same machine, the same timestamp).
 *
 * Adding up to exactly 64 bits, for a Long type.
 *
 * The advantage of SnowFlake is that it is incrementally sorted according to time overall, and there will be no ID collisions
 * within the entire distributed system (distinguished by data center ID and machine ID).
 * It is also efficient. According to tests, SnowFlake can generate about 260,000 IDs per second.
 */
public class SnowflakeIdWorkerUtils {

    // Start timestamp (2015-01-01)
    private final long twepoch = 1420041600000L;

    // Number of bits occupied by machine ID
    private final long workerIdBits = 5L;

    // Number of bits occupied by data center ID
    private final long datacenterIdBits = 5L;

    // Maximum supported machine ID, the result is 31
    // (this shifting algorithm can quickly calculate the maximum decimal number that can be represented by a certain number of binary digits)
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    // Maximum supported data center ID, the result is 31
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    // Number of bits occupied by the sequence in the ID
    private final long sequenceBits = 12L;

    // Machine ID is shifted left by 12 bits
    private final long workerIdShift = sequenceBits;

    // Data center ID is shifted left by 17 bits (12 + 5)
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    // Timestamp is shifted left by 22 bits (5 + 5 + 12)
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    // Mask for generating sequence, here it is 4095 (0b111111111111=0xfff=4095)
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    // Worker machine ID (0~31)
    private long workerId;

    // Data center ID (0~31)
    private long datacenterId;

    // Millisecond sequence (0~4095)
    private long sequence = 0L;

    // Timestamp of the last generated ID
    private long lastTimestamp = -1L;

    /**
     * @param workerId Worker ID (0~31)
     * @param datacenterId Datacenter ID (0~31)
     */
    public SnowflakeIdWorkerUtils(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * Get the ID
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        // If the current time is less than the timestamp of the last generated ID,
        // it indicates that the system clock has been rolled back, so an exception should be thrown
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // If generated at the same time, proceed with the sequence within the millisecond
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // Millisecond sequence overflow
            if (sequence == 0) {
                // Block until the next millisecond to obtain a new timestamp
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        // Timestamp changes, sequence within the millisecond resets
        else {
            sequence = 0L;
        }

        // Timestamp of the last generated ID
        lastTimestamp = timestamp;

        // Shift and combine by bitwise OR to form a 64-bit ID
        return ((timestamp - twepoch) << timestampLeftShift) //
                | (datacenterId << datacenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }

    /**
     * Block until the next millisecond to obtain a new timestamp.
     * @param lastTimestamp The timestamp of the last generated ID
     * @return The current timestamp
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * Returns the current time in milliseconds.
     * @return Current time (milliseconds)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

}
