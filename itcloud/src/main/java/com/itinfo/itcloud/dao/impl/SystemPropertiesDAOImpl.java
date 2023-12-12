package com.itinfo.itcloud.dao.impl;

import com.itinfo.itcloud.dao.SystemPropertiesDAO;
import com.itinfo.itcloud.model.SystemPropertiesVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

@Repository
public class SystemPropertiesDAOImpl implements SystemPropertiesDAO {

    @Inject
    private SqlSession sqlSession;

    @Override
    public SystemPropertiesVO searchSystemProperties() {
        return sqlSession.selectOne("Properties.searchSystemProperties") ;
    }

    @Override
    public int updateSystemProperties(SystemPropertiesVO systemPropertiesVO) {
        return sqlSession.update("Properties.updateSystemProperties", systemPropertiesVO);
    }
}