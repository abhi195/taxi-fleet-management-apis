package com.triptaxi.dataaccessobject;

import com.triptaxi.domainobject.DriverDO;
import com.triptaxi.domainvalue.OnlineStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Database Access Object for driver table.
 */
public interface DriverRepository extends PagingAndSortingRepository<DriverDO, Long>,
    JpaSpecificationExecutor<DriverDO> {

    List<DriverDO> findByOnlineStatus(OnlineStatus onlineStatus);
}
