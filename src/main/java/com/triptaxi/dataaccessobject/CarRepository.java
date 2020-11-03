package com.triptaxi.dataaccessobject;

import com.triptaxi.domainobject.CarDO;
import com.triptaxi.domainvalue.EngineType;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Database Access Object for car table.
 */
public interface CarRepository extends CrudRepository<CarDO, Long> {

    List<CarDO> findByDeletedFalse();

    List<CarDO> findByEngineType(EngineType engineType);
}
