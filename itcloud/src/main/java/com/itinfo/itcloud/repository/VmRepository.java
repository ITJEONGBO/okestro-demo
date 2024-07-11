package com.itinfo.itcloud.repository;

import com.itinfo.itcloud.model.entity.VmSamplesHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VmRepository extends JpaRepository<VmSamplesHistoryEntity, Integer> {

//    SELECT * FROM Vm_Samples_History v WHERE v.history_Datetime =
//            (SELECT MAX(v2.history_Datetime) FROM Vm_Samples_History v2 WHERE v2.vm_Id = v.vm_Id)
//    ORDER BY v.cpu_Usage_Percent DESC
    List<VmSamplesHistoryEntity> findFirstByVmStatusOrderByCpuUsagePercentDesc(int vmStatus);
}
