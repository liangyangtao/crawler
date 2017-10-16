package com.kf.data.tianyancha.watch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/***
 * 
 * @Title: PidRecorder.java
 * @Package com.kf.data.tianyancha.watch
 * @Description: 记录诚信进程
 * @author liangyt
 * @date 2017年10月16日 上午11:42:06
 * @version V1.0
 */
public class PidRecorder {

	private static Log logger = LogFactory.getLog(PidRecorder.class);
	public String pidName;
	public String batName;
	public String nodeName;

	public PidRecorder(String batName, String nodeName) {
		this.batName = batName;
		this.nodeName = nodeName;
	}

	public void start() {
		recordPid();

		if (nodeName.equals("WARCHPID")) {
			monitorWatch();
		}

	}

	private void recordPid() {
		int pid = ProgressReader.getMyPid();
		writerXml(nodeName, pid);
		readXML(nodeName);

	}

	public void writerXml(String nodeName, int pid) {
		try {
			String path = PidRecorder.class.getClassLoader().getResource("").toURI().getPath() + "pid.xml";
			File file = new File(path);
			SAXReader saxReader = new SAXReader();
			Document document = null;
			document = saxReader.read(file);
			Element root = document.getRootElement();
			List<Element> listnode = root.elements();
			for (int i = 0; i < listnode.size(); i++) {
				String name = listnode.get(i).getName();
				if (name.equals(nodeName)) {
					listnode.get(i).setText(pid + "");
					break;
				}
			}
			XMLWriter writer = new XMLWriter(new FileOutputStream(path));
			writer.write(document);
			writer.close();
		} catch (Exception e) {
			logger.error(e);
		}

	}

	public void readXML(String nodeName) {
		try {
			String path = PidRecorder.class.getClassLoader().getResource("").toURI().getPath() + "pid.xml";
			File file = new File(path);
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(file);
			Element root = document.getRootElement();
			Iterator<Element> iterator = root.elementIterator();
			while (iterator.hasNext()) {
				Element recordEless = (Element) iterator.next();
				if (!recordEless.getName().equals(nodeName)) {
					pidName = recordEless.getText();
				}
			}

		} catch (DocumentException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public void reStart() {
		if (!pidName.isEmpty()) {
			ProgressReader.skillProcess(Integer.parseInt(pidName));
		}
		ProgressReader.startBatch(batName);
		try {
			Thread.sleep(20 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		readXML(nodeName);
	}

	public void monitorWatch() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				boolean isExit = checkPidInProcess(pidName);
				if (!isExit) {
					if (!pidName.isEmpty()) {
						ProgressReader.skillProcess(Integer.parseInt(pidName));
					}
					ProgressReader.startBatch(batName);
					try {
						Thread.sleep(20 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					readXML(nodeName);
				}
			}

		}, 0, 5 * 60 * 1000);

	}

	private static boolean checkPidInProcess(String pid) {
		try {
			InputStream in = Runtime.getRuntime().exec("jps").getInputStream();
			BufferedReader b = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = b.readLine()) != null) {
				if (line.startsWith(pid + " ")) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// public static void main(String[] args) {
	// try {
	// InputStream in = Runtime.getRuntime().exec("jps").getInputStream();
	// BufferedReader b = new BufferedReader(new InputStreamReader(in));
	// String line = null;
	// while ((line = b.readLine()) != null) {
	// System.out.println(line);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

}