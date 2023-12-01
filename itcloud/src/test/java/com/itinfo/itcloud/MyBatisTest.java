package com.itinfo.itcloud;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:../../../../../main/webapp/WEB-INF/spring/sql-context.xml"})
public class MyBatisTest {

    //SqlSessionFactory 객체를 자동으로 생성
    @Inject
    private SqlSessionFactory sqlFactory;

    //SqlSessionFactory 객체가 제대로 만들어졌는지 Test
    @Test
    public void testFactory() {
        System.out.println(sqlFactory);
    }

    //MyBatis와 Mysql 서버가 제대로 연결되었는지 Test
    @Test
    public void testSession() throws Exception{
        try(SqlSession session = sqlFactory.openSession()){
            System.out.println(session);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
