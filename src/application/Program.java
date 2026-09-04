import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

void main() {

    SellerDao sellerDao = DaoFactory.createSellerDao();

    System.out.println("---- TEST 1: seller findByID ------");
    Seller seller = sellerDao.findById(3);
    System.out.println(seller);

    System.out.println("---- TEST 2: seller findByDepartment ------");
    Department department = new Department(2, null);
    List<Seller> sellerList = sellerDao.findByDepartment(department);
    for (Seller obj: sellerList) {
        System.out.println(obj);
    }

    System.out.println("---- TEST 3: seller findAll ------");
    sellerList = sellerDao.findAll();
    for (Seller obj: sellerList) {
        System.out.println(obj);
    }

    System.out.println("---- TEST 4: seller insert ------");
    Seller newSeller = new Seller(null, "Bob", "bob@gmail.com", new Date(), 4000.0, department);
    sellerDao.insert(newSeller);
    System.out.println("Inserted new ID:" + newSeller.getId() );

    System.out.println("---- TEST 5: seller update ------");
    Seller updatedSeller = new Seller( newSeller.getId(), "Raphael", "rapha@gmail.com", new Date(), 4000.0, department);
    sellerDao.update(updatedSeller);
    System.out.println("Updated seller ID:" + updatedSeller.getId() + " - Nome: " +updatedSeller.getName() );

    System.out.println("---- TEST 6: seller delete ------");
//    sellerDao.deleteById(9999); // via gerar throw new SQLException "Usuário inexistente"
    sellerDao.deleteById(updatedSeller.getId());
    System.out.println("DELETED seller ID:" + updatedSeller.getId() + " - Nome: " +updatedSeller.getName() );
}