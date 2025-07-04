package finalProj.service.finance.baseServiceInterfaces;

import java.util.List;

public interface BaseService<EntityType, PrimaryKeyType> {
    List<EntityType> findAll();

    EntityType findById(PrimaryKeyType id);

    EntityType save(EntityType entity);

    void deleteById(PrimaryKeyType id);
}
