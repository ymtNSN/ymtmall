package com.ymtmall.commons.tool.tkmapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author yangmingtian
 */
public interface TkMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
