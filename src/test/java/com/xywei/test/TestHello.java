package com.xywei.test;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

public class TestHello {

	private static ArrayList<String> paths = new ArrayList<String>();

	public void testHello() {
		System.out.println("helloworld!");
	}

	@Test
	public void testa() {
		String string = "/a";
		String path = string.substring(0, string.lastIndexOf("/"));
		System.out.println(">>>" + path + "is null?" + path == null);
	}

	@Test
	public void testPath() {
		initnitPaths("/java/spring/ioc");
		System.out.println(paths);
	}

	private static void initnitPaths(String path) {
		if (path != null && !"/".equals(path) && !"".equals(path)) {
			paths.add(path);
			String parentString = path.substring(0, path.lastIndexOf("/"));
			initnitPaths(parentString);
		} else {
			Collections.reverse(paths);
		}
	}
}
