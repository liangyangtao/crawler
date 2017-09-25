package com.kf.data.mybatis.mapper;

import java.util.List;

import com.kf.data.mybatis.entity.NeeqNotice;
import com.kf.data.mybatis.entity.SqlAdapter;

public interface NeeqNoticeMapper {

	List<NeeqNotice> readerNeeqNotice(SqlAdapter sqlAdapter);
}
