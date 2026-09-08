package repository;

import java.util.*;

public interface Repository<T, ID> {
    T save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    boolean deleteById(ID id);

    boolean existsById(ID id);
}
