package realEstatistic.service;

import realEstatistic.mapper.DemoModelDao;
import realEstatistic.model.DemoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoModelService {

    private DemoModelDao demoModelDao;

    @Autowired
    public DemoModelService(DemoModelDao demoModelDao){
        this.demoModelDao = demoModelDao;
    }

    public boolean add(DemoModel user) {
        return demoModelDao.insert(user) > 0;
    }

    public DemoModel getOne(int id) {
        return demoModelDao.select(id);
    }

    public List<DemoModel> getAll() {
        return demoModelDao.selectAll();
    }

    public boolean modify(DemoModel user) {
        return demoModelDao.update(user) > 0;
    }

    public boolean remove(Integer id) {
        return demoModelDao.delete(id) > 0;
    }
}