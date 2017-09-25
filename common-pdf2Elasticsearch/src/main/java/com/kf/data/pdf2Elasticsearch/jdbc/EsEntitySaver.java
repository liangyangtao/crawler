package com.kf.data.pdf2Elasticsearch.jdbc;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;

import com.kf.data.elasticsearch.tools.ESUtils;
import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.pdf2Elasticsearch.entity.PdfLinkEsEntity;

public class EsEntitySaver {

	public boolean saveEsEntity(PdfLinkEsEntity pdfCodeLink) {
		try {
			System.out.println("保存索引" + pdfCodeLink.getPdfLink());
			Client client = ESUtils.getClient();
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(KfConstant.esIndexName, KfConstant.esDataType,
					pdfCodeLink.getUnmd());
			indexRequestBuilder.setSource(pdfCodeLink.toJsonString());
			indexRequestBuilder.execute().actionGet();
			ESUtils.refreshIndex(client, KfConstant.esIndexName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

}
