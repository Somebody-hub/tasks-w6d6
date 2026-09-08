package repository;

import model.Identifiable;

import java.util.*;

public class LocalRepository<T extends Identifiable<ID>, ID> implements Repository<T, ID> {
    private final List<T> items = new ArrayList<>();
    private final Map<ID, T> itemsById = new HashMap<>();


    @Override
    public T save(T entity) {
        if (entity == null || entity.getId() == null){
            throw new IllegalArgumentException("Entity or ID can not be null");
        }
        if(findById(entity.getId()).isPresent()){
            int index = items.indexOf(entity);
            if(index != -1){
                items.set(index, entity);
            }
        }
        else{
            items.add(entity);
        }
        itemsById.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(itemsById.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public boolean deleteById(ID id) {
        T removed = itemsById.remove(id);
        if(removed!= null){
            items.remove(removed);
            return true;
        }
        return false;
    }

    @Override
    public boolean existsById(ID id) {
        return itemsById.containsKey(id);
    }

}
