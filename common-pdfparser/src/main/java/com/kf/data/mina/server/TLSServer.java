package com.kf.data.mina.server;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kf.data.mina.ssl.BogusSslContextFactory;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdfparser.jdbc.PdfReportLinksReader;
import com.kf.data.pdfparser.jdbc.PdfReportLinksWriter;

/****
 * 
 * @Title: TLSServer.java
 * @Package com.kf.data.mina.server
 * @Description: MINA 服务端
 * @author liangyt
 * @date 2017年10月12日 上午11:31:13
 * @version V1.0
 */
public class TLSServer {

	private TLSServerHandler tLSServerHandler = new TLSServerHandler();
	private PdfReportLinksReader pdfReportLinksReader = new PdfReportLinksReader();
	private PdfReportLinksWriter pdfReportLinksWriter = new PdfReportLinksWriter();
	Gson gson = new GsonBuilder().create();

	public TLSServer() {
		// 启动服务
		createAcceptor();
		while (true) {
			if (tLSServerHandler.getSessions().size() > 0) {
				List<PdfReportLinks> links = pdfReportLinksReader.readerPdfCodeLinkByRank(0);
				for (PdfReportLinks pdfReportLinks : links) {
					tLSServerHandler.broadcast(gson.toJson(pdfReportLinks));
					pdfReportLinksWriter.updatePdfReportRankById(pdfReportLinks.getId(), 3);
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	private void createAcceptor() {
		try {
			// 创建服务器端连接器
			SocketAcceptor acceptor = new NioSocketAcceptor();
			acceptor.getSessionConfig().setReadBufferSize(4096);
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
			// acceptor.setReuseAddress(true);
			// 获取默认过滤器
			DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
			// 设置加密过滤器
			// addSSLSupport(chain);
			// 设置编码过滤器和按行读取数据模式
			// chain.addLast("codec", new ProtocolCodecFilter(new
			// TextLineCodecFactory(Charset.forName("UTF-8"))));
			chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
			// 设定 对象传输工厂
			// ObjectSerializationCodecFactory factory = new
			// ObjectSerializationCodecFactory();
			// 设定传输最大值
			TextLineCodecFactory factory = new TextLineCodecFactory(Charset.forName("GBK"));
			factory.setDecoderMaxLineLength(Integer.MAX_VALUE);// 设定后服务器可以接收大数据
			factory.setEncoderMaxLineLength(Integer.MAX_VALUE);
			// 设置编码过滤器和按行读取数据模式
			chain.addLast("codeobj", new ProtocolCodecFilter(factory));
			// 设置事件处理器
			acceptor.setHandler(tLSServerHandler);
			// 处理器的代码如下
			// 服务绑定到此端口号
			acceptor.bind(new InetSocketAddress(6554));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addSSLSupport(DefaultIoFilterChainBuilder chain) throws Exception {
		SslFilter sslFilter = new SslFilter(BogusSslContextFactory.getInstance(true));
		chain.addLast("sslFilter", sslFilter);
	}
}