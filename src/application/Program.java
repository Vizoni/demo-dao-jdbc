import model.entities.Department;
import model.entities.Seller;

void main() {
    Department dpt = new Department(1, "Books");

    Seller seller = new Seller(21, "Bob", "bob@gmail.com", new Date(), 3000.0, dpt);

    System.out.println(dpt);
    System.out.println(seller);
}