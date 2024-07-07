package com.itinfo.itcloud.repository;

import com.itinfo.itcloud.model.entity.HostSamplesHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HostRepository extends JpaRepository<HostSamplesHistory, UUID> {


}
