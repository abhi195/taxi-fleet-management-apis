package com.triptaxi.dataaccessobject;

import com.triptaxi.domainobject.ManufacturerDO;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Database Access Object for manufacturer table.
 */
public interface ManufacturerRepository extends CrudRepository<ManufacturerDO, Long> {

    List<ManufacturerDO> findByDeletedFalse();
}
