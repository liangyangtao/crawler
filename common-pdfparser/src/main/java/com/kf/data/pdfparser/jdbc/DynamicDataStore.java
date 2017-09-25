package com.kf.data.pdfparser.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.SqlAdapter;
import com.kf.data.mybatis.factory.BaseDao;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.SqlAdapterMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @DynamicDataStore.java
 * @2017年5月8日
 * @author yinxin
 * @DynamicDataStore
 * @注释：根据传入的json动态建表 建字段
 */
public class DynamicDataStore {

//	private static ReadWriteLock lock = new ReentrantReadWriteLock(false);

	public void doStore(String json, String temppdfType) {

		JSONObject results = JSONObject.fromObject(json);
		String state = null;
		if (results.containsKey("state")) {
			state = results.getString("state");
		}
		if (state != null && state.equals("ok")) {
			JSONArray infoArray = results.getJSONArray("info");

			if (infoArray != null && infoArray.size() > 0) {
				for (Object infoobject : infoArray) {
					JSONArray info = JSONArray.fromObject(infoobject);
					Map<String, Object> map = new HashMap<>();
					String tableName = null;
					for (Object object : info) {
						JSONObject obj = JSONObject.fromObject(object);
						tableName = obj.getString("tableName");
						String property = obj.getString("property");
						// 判断表是否存在，否则建表
						// lock.writeLock().lock();
						// String showTable = "show TABLES;";
						// List<String> showTables = ShowData(showTable);
						// if (!showTables.contains(tableName)) {
						// String createTableSql = "create table " + tableName
						// + "( id bigint(20) NOT NULL AUTO_INCREMENT ,PRIMARY
						// KEY (id) ) ;";
						// new BaseDao().executeSql(createTableSql);
						// }
						// 判断字段是否存在
						// String showColumn = "SHOW COLUMNS FROM " + tableName
						// + ";";
						// List<String> showColumns = ShowData(showColumn);
						// if (!showColumns.contains(property)) {
						// String alterColumn = null;
						// if (property.equals("value")) {
						// alterColumn = "alter table " + tableName + " add " +
						// property + " text ";
						// } else {
						// alterColumn = "alter table " + tableName + " add " +
						// property + " varchar(255)";
						// }
						// new BaseDao().executeSql(alterColumn);
						// }
						String value = obj.getString("value");
						map.put(property, value);
						// lock.writeLock().unlock();
					}
					if (temppdfType.equals("半年报")) {
						tableName = tableName.replace("_year_", "_semiannual_");
					} else if (temppdfType.equals("季报")) {
						tableName = tableName.replace("_year_", "_quarter_");
					} else {
					}
					// 保存
					String saveSql = "insert INTO " + tableName + " ";
					saveData(map, saveSql);
				}

			} else {

				// do not thing
			}
		}

	}

	/**
	 * 查询出多个结果
	 * 
	 * @param sql
	 * @return
	 */
	private List<String> ShowData(String sql) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		List<String> tables = null;
		try {
			SqlAdapterMapper sqlAdapterMapper = sqlSession.getMapper(SqlAdapterMapper.class);
			SqlAdapter sa = new SqlAdapter();
			sa.setSql(sql);
			tables = sqlAdapterMapper.showTables(sa);
		} catch (Exception e) {
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return tables;
	}

	/**
	 * 保存数据
	 * 
	 * @param map
	 */
	private void saveData(Map<String, Object> map, String sql) {
		new BaseDao().executeMapSql(sql, map);
	}

}
