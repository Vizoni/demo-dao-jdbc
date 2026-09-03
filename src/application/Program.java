import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

void main() {

    SellerDao sellerDao = DaoFactory.createSellerDao();

    System.out.println("---- TEST 1: seller by ID ------");
    Seller seller = sellerDao.findById(3);

    System.out.println(seller);

}