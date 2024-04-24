package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.ovirt.AdminConnectionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataCenterServiceImplTest {

    @InjectMocks private DataCenterServiceImpl dc;
    @Mock private AdminConnectionService admin;

//    @BeforeEach
//    void setUp(){
//        admin.getConnection();
////        when(admin.getConnection()).thenReturn(
////                ConnectionBuilder.connection()
////                    .url("192.168.0.80")
////                    .user("admin")
////                    .password("admin!123")
////                    .insecure(true)
////                    .timeout(20000)
////                .build()
////        );
//    }

    @Test
    @DisplayName("test 출력")
    void test(){
        String a = "test";
        Assertions.assertEquals(a, "test");
//        assertThat(a).isEqualTo("test");
        System.out.println(a);
    }


//    @Test
//    @DisplayName("데이터센터 리스트 출력")
//    void getList() {
//
//        System.out.println(dcsService.list().send().dataCenters().size());
//        Assertions.assertEquals(0, dc.getList().size());
//    }

    @Test
    void getEvent() {
    }

    @Test
    void addDatacenter() {
    }

    @Test
    void getDatacenter() {
    }

    @Test
    void editDatacenter() {
    }

    @Test
    void deleteDatacenter() {
    }
}