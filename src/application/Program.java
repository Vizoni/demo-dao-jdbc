import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

void main() {

    SellerDao sellerDao = DaoFactory.createSellerDao();

    System.out.println("---- TEST 1: seller by ID ------");
    Seller seller = sellerDao.findById(3);
    System.out.println(seller);

    System.out.println("---- TEST 2: seller by Department ID ------");
    Department department = new Department(2, null);
    List<Seller> sellerList = sellerDao.findByDepartment(department);

    for (Seller obj: sellerList) {
        System.out.println(obj);
    }

}