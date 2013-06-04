package com.kotoblog.grabit;

import java.io.File;
import java.util.concurrent.Callable;

public class Main {

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 4; i++) {
			final int finalI = i;
			new Thread() {
				@Override
				public void run() {
					try {
						final File file = (finalI % 2) == 0 ? new File("abc") : new File("eee");

						ResourceLock.doStuff(file, new Callable() {
							@Override
							public Object call() throws Exception {
								if (!file.createNewFile()) {
									throw new Exception();
								}

								System.out.println("Created " + file.getName());

								if (!file.delete()) {
									throw new Exception();
								}

								System.out.println("Deleted " + file.getName());
								return null;
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

}
