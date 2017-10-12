package com.kf.data.mina.clent;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.mina.clent.listener.IoListener;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdfparser.jdbc.PdfCodetableReader;
import com.kf.data.pdfparser.jdbc.PdfReportLinksWriter;
import com.kf.data.pdfparser.thread.PdfReportLinkPaserWorker;

/****
 * 
 * @Title: TLSClinet.java
 * @Package com.kf.data.mina.clent
 * @Description: MINA 客户端
 * @author liangyt
 * @date 2017年10月12日 下午1:56:02
 * @version V1.0
 */
public class TLSClinet {

	public static Log logger = LogFactory.getLog(TLSClinet.class);
	private LinkedBlockingQueue<PdfReportLinks> pdfcodeLinkQueue = new LinkedBlockingQueue<PdfReportLinks>();
	private PdfReportLinksWriter pdfReportLinksWriter = new PdfReportLinksWriter();
	private PdfCodetableReader pdfCodetableReader = new PdfCodetableReader();
	public NioSocketConnector connector;
	public IoSession session;
	public ConnectFuture future;

	public TLSClinet() {
		init(KfConstant.minaServerIp);
	}

	public void init(String ip) {
		fillConnector(ip);
	}

	public void paser() {
		ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 0; i < 4; i++) {
			executor.execute(new PdfReportLinkPaserWorker(pdfcodeLinkQueue, pdfCodetableReader, pdfReportLinksWriter));
		}
		executor.shutdown();
	}

	private boolean fillConnector(String ip) {
		try {
			connector = new NioSocketConnector();
			connector.setConnectTimeoutMillis(3000);
			DefaultIoFilterChainBuilder chain = connector.getFilterChain();
			// 设定这个过滤器将一行一行(/r/n)的读取数据
			chain.addLast("myChin", new ProtocolCodecFilter(new TextLineCodecFactory()));
			// 创建接受数据的过滤器
			TextLineCodecFactory factory = new TextLineCodecFactory(Charset.forName("GBK"));
			factory.setDecoderMaxLineLength(Integer.MAX_VALUE);// 设定后服务器可以接收大数据
			factory.setEncoderMaxLineLength(Integer.MAX_VALUE);
			// 设置编码过滤器和按行读取数据模式
			connector.getFilterChain().addLast("codeobj", new ProtocolCodecFilter(factory));
			connector.setHandler(new TLSClentHandler(pdfcodeLinkQueue));
			connector.setDefaultRemoteAddress(new InetSocketAddress(ip, 6554));
			// 连接到服务器：
			connector.addListener(new IoListener() {
				@Override
				public void sessionDestroyed(IoSession arg0) throws Exception {
					while (true) {
						try {
							Thread.sleep(30000);
							future = connector.connect();
							future.awaitUninterruptibly();// 等待连接创建成功
							session = future.getSession();// 获取会话
							if (session.isConnected()) {
								logger.info("断线重连[" + connector.getDefaultRemoteAddress().getHostName() + ":"
										+ connector.getDefaultRemoteAddress().getPort() + "]成功");
								break;
							}
						} catch (Exception ex) {
							logger.info("重连服务器登录失败,30秒再连接一次", ex);
						}
					}
				}
			});

			while (true) {
				try {
					future = connector.connect();
					future.awaitUninterruptibly(); // 等待连接创建成功
					session = future.getSession(); // 获取会话
					break;
				} catch (RuntimeIoException e) {
					logger.info("连接服务器失败,30秒再连接一次", e);
					Thread.sleep(30000);// 连接失败后,重连间隔5s
				}
			}
		} catch (Exception e) {
			logger.info("链接服务器失败,30秒后重试", e);
			return false;
		}
		return true;
	}

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public ConnectFuture getFuture() {
		return future;
	}

	public void setFuture(ConnectFuture future) {
		this.future = future;
	}

}
