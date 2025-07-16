package dao;

import entity.MonAn;
import java.util.List;

public interface MonAnDAO {
    MonAn create(MonAn entity);
    void update(MonAn entity);
    void deleteById(Integer id);
    List<MonAn> findAll();
    MonAn findById(Integer id);
}
