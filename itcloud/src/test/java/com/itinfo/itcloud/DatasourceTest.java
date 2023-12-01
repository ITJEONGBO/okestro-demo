package com.itinfo.itcloud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:../../../../../main/webapp/WEB-INF/spring/sql-context.xml"})
public class DatasourceTest {

    @Inject
    private DataSource dataSource;

    @Test
    public void testConnection() throws Exception{
        try(Connection connection = dataSource.getConnection()){
            System.out.println(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
