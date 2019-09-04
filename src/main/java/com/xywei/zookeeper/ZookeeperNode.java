package com.xywei.zookeeper;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author future
 * @Datetime 2019年9月3日 下午6:03:43<br/>
 * @Description 测试使用API操作节点
 */
public class ZookeeperNode {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeper.class);

	private final static String HOST_IP = "192.168.36.151";
	private final static int ZOOKEEPER_PORT = 2181;
	private final static String CONNECTION_STRING = "192.168.36.151:2181";
	private final static int SESSION_TIMEOUT = 70 * 1000;
	private static ArrayList<String> paths = new ArrayList<String>();
	private static ZooKeeper zooKeeper = null;

//	@Test
	@Before
	public void intiZooKeeperNode() {

		try {

			zooKeeper = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, new Watcher() {

				@Override
				public void process(WatchedEvent event) {

				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void closeConnection() {
		if (zooKeeper != null) {
			try {
				zooKeeper.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 
	 * @Datetime 2019年9月4日 上午11:30:26<br/>
	 * @Description
	 * @param path
	 * @return true-不存在节点，false-存在
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static boolean nodeExist(String path) throws KeeperException, InterruptedException {
		return ((zooKeeper.exists(path, false) == null) ? false : true);
	}

	/**
	 * 测试连接
	 * 
	 * @Datetime 2019年9月3日 下午6:21:30<br/>
	 * @Description
	 */
	@Test
	public void testConnect() {

		try {
			List<String> children = zooKeeper.getChildren("/", false);
			for (String child : children) {

				LOGGER.info(">>>>>>>>>>>>>>" + child);

				LOGGER.info("**************" + zooKeeper.getData("/" + child, false, null));

			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 添加节点，先添加一级的
	 * 
	 * @Datetime 2019年9月4日 上午9:17:10<br/>
	 * @Description
	 */
	@Test
	public void testCreateNode() {

		try {
			// 如果添加多个节点，需要先判断父节点是否存在
			Stat exists = zooKeeper.exists("/java", false);
			if (exists != null) {

				String messageString = zooKeeper.create("/java/spring", "来自java".getBytes(), Ids.OPEN_ACL_UNSAFE,
						CreateMode.PERSISTENT_SEQUENTIAL);
				LOGGER.info("添加节点的信息：>>>>>>>>>>>>>>>>>>>>>>>>" + messageString);

			} else {
				LOGGER.info("节点不存在");
			}

		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(), e.getStackTrace());
		}
	}

	/**
	 * TODO 错误写法，只能创建父类 创建节点，假如父节点不存在，则先创建父节点
	 * 
	 * @Datetime 2019年9月4日 上午9:41:17<br/>
	 * @Description
	 */
	public void testCreateNodes(String path) {

		if (path == null) {
			// 初始化路径
			path = "/java/spring/ioc";
		}

		String parentPath = path.substring(0, path.lastIndexOf("/"));

		// 不是根路径
		if (parentPath != null) {
			try {
				if (!nodeExist(parentPath)) {
					// 父节点不存在就先创建父节点
					testCreateNodes(parentPath);
				} else {
					testCreateNodes(path);
				}
				zooKeeper.create(path, ("我是节点" + path + ",值是" + path).getBytes(), Ids.OPEN_ACL_UNSAFE,
						CreateMode.PERSISTENT);
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage(), e.getStackTrace());
			}
		} else {
			LOGGER.info(path + "父路径是根路径");
			// 是根路径直接先判断是否存在
			try {
				if (!nodeExist(path)) {
					LOGGER.info(path + "不存在直接创建");
					zooKeeper.create(path, ("我是节点" + path + ",值是" + path).getBytes(), Ids.OPEN_ACL_UNSAFE,
							CreateMode.PERSISTENT);
				} else {
					LOGGER.info(path + "已经存在");
				}
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage(), e.getStackTrace());
			}

		}

	}

	/**
	 * TODO 正确的创建多级节点的方法
	 * 
	 * @Datetime 2019年9月4日 上午10:30:43<br/>
	 * @Description
	 */
	@Test
	public void testCreateNodes() {
		String path = "/java/spring/ioc";
		initnitPaths(path);
		if (null != paths) {
			LOGGER.info("要创建的节点有" + paths);
			for (String pathString : paths) {
				try {
					if (!nodeExist(pathString)) {
						zooKeeper.create(pathString, ("my value is " + pathString).getBytes(), Ids.OPEN_ACL_UNSAFE,
								CreateMode.PERSISTENT);
					} else {
						LOGGER.info(pathString + "已经存在");
					}
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
					LOGGER.info(e.getMessage(), e.getStackTrace());
				}
			}
		}

		LOGGER.info(">>>>>" + paths);
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

	/**
	 * 获取节点的值，包括子节点
	 * 
	 * @Datetime 2019年9月4日 上午9:35:43<br/>
	 * @Description
	 */
	public void testGetNodeData() {
//		zooKeeper.getChildren("/java", false, )
	}

	/**
	 * 获取给定的路径，找回相应的子节点、值
	 * 
	 * @Datetime 2019年9月4日 上午11:41:21<br/>
	 * @Description
	 */
	@Test
	public void testGetNodeValue() {

		String path = "/java";

		try {
			byte[] value = zooKeeper.getData(path, false, null);
			String val = new String(value);
			LOGGER.info("当前节点的值：" + val);
			List<String> children = zooKeeper.getChildren(path, false);
			LOGGER.info("孩子节点>>>>>>>>>>>>" + children);
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
