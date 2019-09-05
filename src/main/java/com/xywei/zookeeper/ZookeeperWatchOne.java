package com.xywei.zookeeper;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.ZooDefs.Ids;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 写得很乱很差
 * @author future
 * @Datetime 2019年9月4日 下午1:36:18<br/>
 * @Description 测试通知机制，监控单个节点变化
 */
/**
 * @author future
 * @Datetime 2019年9月4日 下午1:52:29<br/>
 * @Description
 */
public class ZookeeperWatchOne {
	private static final Logger log = LoggerFactory.getLogger(ZookeeperWatchOne.class);

	private static final String CONN_STRING = "192.168.36.151:2181";

	private static ZooKeeper zooKeeper = null;

	private static final int SESSION_TIMEOUT = 120 * 1000;

	private static boolean FLAG = true;

	@Before
	public void initZookeeper() {

		try {

			zooKeeper = new ZooKeeper(CONN_STRING, SESSION_TIMEOUT, new Watcher() {

				@Override
				public void process(WatchedEvent event) {
//
//					// 监听根节点下的子节点变化
//					try {
//
//						List<String> children = zooKeeper.getChildren("/", true);
//
//						for (String pathString : children) {
//
//							getNodesValue("/" + pathString);
//
//						}
//
//					} catch (KeeperException | InterruptedException e) {
//						log.error(e.getMessage(), e.getStackTrace());
//						e.printStackTrace();
//					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e.getStackTrace());
		}
	}

	@After
	public void closeZookeeper() {

		if (null != zooKeeper) {
			try {
				zooKeeper.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e.getStackTrace());
			}

		}
	}

	/**
	 * 
	 * @Description 获取节点数据
	 * @Datetime 2019年9月4日 下午5:46:53<br/>
	 */
	public static void getNodesValue(String path) {

//		String oldValue = null;

		try {
			byte[] data = zooKeeper.getData(path, true, new Stat());
			String val = new String(data);
//			if (oldValue == null) {
//				oldValue = val;
//			}
//			if (!val.equals(oldValue)) {
				log.info("节点" + path + "数据发生变化>>>>>>>>>>>>>>>>>>>>>>>" + val);
//				oldValue = val;
//			}
			List<String> chirldrenList = zooKeeper.getChildren(path, true);
			if (null != chirldrenList) {
				for (String nodePath : chirldrenList) {
					getNodesValue(path + "/" + nodePath);
				}
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
			log.info(e.getMessage(), e.getStackTrace());
		}
	}

	/**
	 * 
	 * @Description 测试监控节点变化 自己写的不是最优的
	 * @Datetime 2019年9月4日 下午1:52:36<br/>
	 */
	@Test
	public void testWatchOne() {
		try {
			String path = "/";
			List<String> chirldrenList = zooKeeper.getChildren(path, true);
			if (null != chirldrenList) {
				for (String nodePath : chirldrenList) {
					getNodesValue("/" + nodePath);
				}
			}

			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException | KeeperException e) {
			e.printStackTrace();
		}
	}

//	@Test
	public static void testCreateWatchNode() {
		String path = "/x";
		String data = "watch message:imok";
		try {
			String createNodeMessage = zooKeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
			log.info(">>>>>>>>" + createNodeMessage);
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e.getStackTrace());
		}

		Watcher watcher = new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				String oldData = data;
				try {
					String zkData = new String(zooKeeper.getData(path, true, null));
//					log.info("监控节点>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + event.getPath());
					if (!oldData.equals(zkData)) {
						log.info("数据变化>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + zkData);
						oldData = zkData;
					}
				} catch (KeeperException | InterruptedException e) {
					log.error(e.getMessage(), e.getStackTrace());
					e.printStackTrace();
				}
			}
		};
//		while (FLAG) {
		try {
			log.info("**************************监控中********************************************");
			Stat stat = zooKeeper.exists(path, watcher);
			if (null == stat) {
				FLAG = false;
			}
//			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException | KeeperException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e.getStackTrace());
//			}

		}
	}

	@Test
	public void testDeleteNode() {
		String path = "/x";
		try {
			int version = 0;
			zooKeeper.delete(path, version);
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e.getStackTrace());
		}

	}

}
