package repository;

import model.Identifiable;

import java.util.*;

public class LocalRepository<T extends Identifiable<ID>, ID> implements Repository<T, ID> {
    private final List<T> items = new ArrayList<>();
    private final Map<ID, T> itemsById = new HashMap<>();


    //Принимает сущность
    //Сохраняет сущность в две коллекции
    //Возвращает сущность
    @Override
    public T save(T entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Entity or ID can not be null");
        }
        if (findById(entity.getId()).isEmpty()) {
            items.add(entity);
        }
        itemsById.put(entity.getId(), entity);
        return entity;
    }

    //Принимает id определённого типа
    //Ищет объект в коллекции по id
    //Возвращает обёртку Optional (не)найденного объекта
    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(itemsById.get(id));
    }

    //Ничего не принимает
     //Возвращает список всх задач
    @Override
    public List<T> findAll() {
        return new ArrayList<>(items);
    }

    //Принимает id
    //Удаляет объект с id
    //Возвращает true, если такой объект был и он удалён
    @Override
    public boolean deleteById(ID id) {
        T removed = itemsById.remove(id);
        if (removed != null) {
            items.remove(removed);
            return true;
        }
        return false;
    }

    //Принимает id
    //Проверяет, есть ли объект с данным id в коллекции
    //Возвращает ответ
    @Override
    public boolean existsById(ID id) {
        return itemsById.containsKey(id);
    }

}
