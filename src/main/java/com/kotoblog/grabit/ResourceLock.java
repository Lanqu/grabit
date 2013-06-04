package com.kotoblog.grabit;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

public class ResourceLock {

	private static Map<Object, ReentrantLock> locks = new TreeMap<Object, ReentrantLock>();
	private static final ReentrantLock lockForLock = new ReentrantLock();

	public static void doStuff(File file, Callable call) throws Exception {
		lockForLock.lock();

		ReentrantLock lock = locks.get(file);

		if (lock == null) {
			lock = new ReentrantLock();
			locks.put(file, lock);
		}

		lockForLock.unlock();

		lock.lock();

		try {
			call.call();
		} finally {
			lock.unlock();

			lockForLock.lock();
			locks.remove(file);
			lockForLock.unlock();
		}
	}

}
