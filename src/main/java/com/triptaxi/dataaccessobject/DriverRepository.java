package com.triptaxi.dataaccessobject;

import com.triptaxi.domainobject.DriverDO;
import com.triptaxi.domainobject.OnlineStatus;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Database Access Object for driver table.
 */
public interface DriverRepository extends CrudRepository<DriverDO, Long> {

    List<DriverDO> findByOnlineStatus(OnlineStatus onlineStatus);
}
