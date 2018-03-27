package com.kf.data.pdfparser.es;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kf.data.elasticsearch.tools.ESUtils;
import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.pdfparser.entity.PdfLinkEsEntity;

public class PdfReportTextReader {

	public List<PdfLinkEsEntity> readPdfLinkInEsByNoticId(int noticeId) {
		List<PdfLinkEsEntity> pdfLinkEsEntities = new ArrayList<PdfLinkEsEntity>();
		Client client = ESUtils.getClient();
		boolean flag = ESUtils.queryIndexExists(client, KfConstant.esIndexName);
		if (flag) {
			MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("noticeId", noticeId);
			SearchRequestBuilder srq = client.prepareSearch(KfConstant.esIndexName).setTypes(KfConstant.esDataType);
			srq.setQuery(queryBuilder);
			SearchResponse response = srq.execute().actionGet();
			SearchHits hits = response.getHits();
			for (int i = 0; i < hits.getHits().length; i++) {
				SearchHit hit = hits.getHits()[i];
				Map<String, Object> val = hit.getSource();
				Gson gson = new GsonBuilder().create();
				String json = gson.toJson(val);
				PdfLinkEsEntity pdfLinkEsEntity = gson.fromJson(json, PdfLinkEsEntity.class);
				pdfLinkEsEntities.add(pdfLinkEsEntity);
			}
		}
		return pdfLinkEsEntities;
	}
}
