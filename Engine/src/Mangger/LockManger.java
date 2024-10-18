package Mangger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockManger {
    private Map<String, ReadWriteLock> locks = new HashMap<>();


    public void addLock(String sheetName) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        locks.put(sheetName, lock);
    }

    public void removeLock(String sheetName) {
        locks.remove(sheetName);
    }

    public Lock getReadLock(String sheetName) {
        return locks.get(sheetName).readLock();
    }

    public Lock getWriteLock(String sheetName) {
        return locks.get(sheetName).writeLock();
    }
}
