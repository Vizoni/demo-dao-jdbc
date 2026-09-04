import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

void main() {

    DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

    System.out.println("---- TEST 1: department findByID ------");
    Department department = departmentDao.findById(3);
    System.out.println(department);

    System.out.println("---- TEST 2: department findAll ------");
    List<Department> departmentList = new ArrayList<>();
    departmentList = departmentDao.findAll();
    for (Department obj: departmentList) {
        System.out.println(obj);
    }

    System.out.println("---- TEST 3: department insert ------");
    Department newDpt = new Department(null, "Book");
    departmentDao.insert(newDpt);
    System.out.println("Inserted new ID:" + newDpt.getId() );

    System.out.println("---- TEST 4: department update ------");
    Department updatedDepartment = new Department( newDpt.getId(), "Novo nome");
    departmentDao.update(updatedDepartment);
    System.out.println("Updated department ID:" + updatedDepartment.getId() + " - Nome: " + updatedDepartment.getName() );

    System.out.println("---- TEST 5: department delete ------");
//    departmentDao.deleteById(9999); // via gerar throw new SQLException "Departamento inexistente"
    departmentDao.deleteById(updatedDepartment.getId());
    System.out.println("DELETED department ID:" + updatedDepartment.getId() + " - Nome: " +updatedDepartment.getName() );
}