package com.itinfo.itcloud.repository;

import com.itinfo.itcloud.repository.entity.VmSamplesHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VmSamplesHistoryRepository extends JpaRepository<VmSamplesHistoryEntity, Integer> {

    // 가상머신 개인 cpu, memory
    VmSamplesHistoryEntity findFirstByVmIdOrderByHistoryDatetimeDesc(UUID vmId);

    @Query(value = "SELECT * FROM Vm_Samples_History v WHERE vm_status=1 and v.history_Datetime = \n" +
            "          (SELECT MAX(v2.history_Datetime) FROM Vm_Samples_History v2 WHERE v2.vm_Id = v.vm_Id)\n" +
            "   ORDER BY v.cpu_Usage_Percent DESC", nativeQuery = true)
    List<VmSamplesHistoryEntity> findVmCpuChart(Pageable page);

    @Query(value = "SELECT * FROM Vm_Samples_History v WHERE vm_status=1 and v.history_Datetime = \n" +
            "          (SELECT MAX(v2.history_Datetime) FROM Vm_Samples_History v2 WHERE v2.vm_Id = v.vm_Id)\n" +
            "   ORDER BY v.memory_Usage_Percent DESC", nativeQuery = true)
    List<VmSamplesHistoryEntity> findVmMemoryChart(Pageable page);

    // vm 사용량 순위
    List<VmSamplesHistoryEntity> findFirstByVmStatusOrderByCpuUsagePercentDesc(int vmStatus);


    /*
    WITH rounded_times AS (
    SELECT
        date_trunc('hour', history_datetime) + (extract(minute FROM history_datetime)::int / 15) * interval '15 minutes' AS rounded_time,
        vm_id,
        cpu_usage_percent,
        history_datetime
    FROM vm_samples_history
    WHERE vm_status = 1
)
SELECT DISTINCT ON (rounded_time, vm_id)
    TO_CHAR(rounded_time, 'YYYY-MM-DD HH24:MI') AS time,
    vm_id,
    cpu_usage_percent
FROM rounded_times
ORDER BY rounded_time desc, vm_id, history_datetime;
     */
}
