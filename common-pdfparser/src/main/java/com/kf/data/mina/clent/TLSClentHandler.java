package com.kf.data.mina.clent;

import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdfparser.parser.PdfReportLinkPaser;

/***
 * 
 * @Title: TLSClentHandler.java
 * @Package com.kf.data.mina.clent
 * @Description: 接受传送来的pdf链接
 * @author liangyt
 * @date 2017年10月12日 上午11:21:38
 * @version V1.0
 */
public class TLSClentHandler extends IoHandlerAdapter {

	private static Log logger = LogFactory.getLog(TLSClentHandler.class);
	Gson gson = new GsonBuilder().create();
	PdfReportLinkPaser pdfReportLinkPaser = new PdfReportLinkPaser();

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("与服务器建立连接");
		session.write("SEND");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		InetSocketAddress remoteAddress = (InetSocketAddress) session.getRemoteAddress();
		logger.info(remoteAddress + "和服务器断开了链接                  Session 失效");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
		InetSocketAddress remoteAddress = (InetSocketAddress) session.getRemoteAddress();
		logger.info(remoteAddress + "有异常");

	}

	/***
	 * 处理接收到的url
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// logger.info("接收到消息" + message);
		if (message instanceof Integer) {
		} else if (message instanceof String) {
			String temp = (String) message;
			try {
				PdfReportLinks pdfReportLinks = gson.fromJson(temp, PdfReportLinks.class);
				pdfReportLinkPaser.parserPdfReportLinks(pdfReportLinks);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}