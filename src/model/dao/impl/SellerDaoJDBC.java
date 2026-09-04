package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller sellerParam) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO seller" +
                    " (Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                    " VALUES" +
                    " (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1,sellerParam.getName());
            st.setString(2, sellerParam.getEmail());
            st.setDate(3, new java.sql.Date(sellerParam.getBirthDate().getTime()));
            st.setDouble(4, sellerParam.getBaseSalary());
            st.setInt(5,sellerParam.getDepartment().getId());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    sellerParam.setId((id));
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Erro inesperado! Nenhuma linha afetada");
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE seller" +
                    " SET name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?" +
                    " WHERE Id = ?", Statement.RETURN_GENERATED_KEYS );
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            st.setInt(6, seller.getId());
            st.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                        "FROM seller INNER JOIN department " +
                        "ON seller.DepartmentId = department.Id " +
                        "WHERE seller.Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            // rs.next se for true é que tem algum resultado no banco
            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                Seller sel = instantiateSeller(rs, dep);
                return sel;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT seller.*,department.Name as DepName" +
                    " FROM seller INNER JOIN department" +
                    " ON seller.DepartmentId = department.Id" +
                    " WHERE DepartmentId = ?" +
                    " ORDER BY Name");
            st.setInt(1,department.getId());
            rs = st.executeQuery();

            List<Seller> sellerList = new ArrayList<>();

            Map<Integer, Department> departmentMap = new HashMap<>();
            while (rs.next()) {
                // verifica se o departamento ja foi instanciado no map
                Department myDep = departmentMap.get(rs.getInt("DepartmentId"));
                if (myDep == null) {
                    myDep = instantiateDepartment(rs);
                    // adiciona o departmaento no map pra nao criar mais de uma vez
                    departmentMap.put(rs.getInt("DepartmentId"), myDep);
                }
                Seller sel = instantiateSeller(rs, myDep);
                sellerList.add(sel);
            }
            return sellerList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "ORDER BY Name");
            rs = st.executeQuery();

            List<Seller> sellerList = new ArrayList<>();

            Map<Integer, Department> departmentMap = new HashMap<>();
            while (rs.next()) {
                // verifica se o departamento ja foi instanciado no map
                Department myDep = departmentMap.get(rs.getInt("DepartmentId"));
                if (myDep == null) {
                    myDep = instantiateDepartment(rs);
                    // adiciona o departmaento no map pra nao criar mais de uma vez
                    departmentMap.put(rs.getInt("DepartmentId"), myDep);
                }
                Seller sel = instantiateSeller(rs, myDep);
                sellerList.add(sel);
            }
            return sellerList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller sel = new Seller();
        sel.setId(rs.getInt("Id"));
        sel.setName(rs.getString("Name"));
        sel.setEmail(rs.getString("Email"));
        sel.setBaseSalary(rs.getDouble("BaseSalary"));
        sel.setBirthDate(rs.getDate("BirthDate"));
        sel.setDepartment(dep);
        return sel;
    }

    // o throws sql exception vai fazer o erro daqui propagar
    // para o local que está chamando essa função, já q lá tem o try catch
    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }
}
