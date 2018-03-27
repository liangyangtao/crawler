package com.kf.data.mina.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/****
 * 
 * @Title: TLSServerHandler.java
 * @Package com.kf.data.mina.server
 * @Description: 发送link 到客户端
 * @author liangyt
 * @date 2017年10月12日 上午11:26:25
 * @version V1.0
 */
public class TLSServerHandler extends IoHandlerAdapter {

	static Log logger = LogFactory.getLog(TLSServerHandler.class);
	static List<IoSession> sessions = Collections.synchronizedList(new ArrayList<IoSession>());
	static AtomicInteger atomicInteger = new AtomicInteger(0);

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		InetSocketAddress remoteAddress = (InetSocketAddress) session.getRemoteAddress();
		logger.info(remoteAddress + "客户端到链接异常", cause);
		super.exceptionCaught(session, cause);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		InetSocketAddress remoteAddress = (InetSocketAddress) session.getRemoteAddress();
		logger.info(remoteAddress + "与服务器断开连接");
		sessions.remove(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		InetSocketAddress remoteAddress = (InetSocketAddress) session.getRemoteAddress();
		sessions.add(session);
		logger.info(remoteAddress + "与服务器建立连接");

	}

	// 当客户端发送的消息到达时:
	public void messageReceived(IoSession session, Object message) {
		InetSocketAddress remoteAddress = (InetSocketAddress) session.getRemoteAddress();
		logger.info("接收到客户端" + remoteAddress + "消息" + message);
		if (message instanceof Integer) {

		} else if (message instanceof String) {
		}
	}

	public void messageSent(IoSession session, Object message) {

	};

	public void broadcast(String message) {
		synchronized (sessions) {
			if (sessions.size() > 0) {
				int index = atomicInteger.incrementAndGet() % sessions.size();
				IoSession session = sessions.get(index);
				if (session.isConnected()) {
					session.write(message);
				}
			}

		}
	}

	public List<IoSession> getSessions() {
		return sessions;
	}

}