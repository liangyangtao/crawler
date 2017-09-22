package com.kf.data.elasticsearch.tools;

import java.net.InetAddress;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.kf.data.fetcher.tools.KfConstant;


/**
 * ES工具类
 * 
 *
 */
public class ESUtils {

	private static TransportClient transportClient = null;

	public synchronized static Client getClient() {
		if (transportClient == null) {
			transportClient = getClusterClient(KfConstant.esClusterName, true, KfConstant.esPort, KfConstant.esUrl);
		}
		return transportClient;

	}

	/**
	 * 初始化并连接elasticsearch集群，返回连接后的client @ clusterName 中心节点名称 @
	 * clientTransportSniff 是否自动发现新加入的节点 @ port 节点端口 @ hostname 集群节点所在服务器IP，支持多个
	 * 
	 * @return 返回连接的集群的client
	 */
	public static TransportClient getClusterClient(String clusterName, boolean clientTransportSniff, int port,
			String hostname) {
		TransportClient transportClient = null;
		try {
			Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName)
					.put("client.transport.sniff", true).put("client.transport.ping_timeout", "120s").build();
			transportClient = TransportClient.builder().settings(settings).build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostname), port));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transportClient;
	}

	public static boolean queryIndexExists(Client client, String indexName) {
		IndicesExistsRequest indicesExistsRequest = new IndicesExistsRequest(indexName.toLowerCase());
		indicesExistsRequest.indices();
		return client.admin().indices().exists(indicesExistsRequest).actionGet().isExists();
	}

	public static void refreshIndex(Client client, String indexName) {
		IndicesAdminClient adminClient = client.admin().indices();
		// 查询索引是否存在
		if (queryIndexExists(client, indexName)) {
			adminClient.open(new OpenIndexRequest(indexName)).actionGet();
		}
		try {
			// 刷新索引
			adminClient.refresh(new RefreshRequest(indexName)).actionGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteIndex(Client client, String indexName, String indexType, String id) {
		client.prepareDelete(indexName.toLowerCase(), indexType.toLowerCase(), id).execute().actionGet();
	}

	public static void deleteIndex(Client client, String indexName) {
		if (queryIndexExists(client, indexName)) {
			client.admin().indices().prepareDelete(indexName.toLowerCase()).execute().actionGet();
		}
	}
}
