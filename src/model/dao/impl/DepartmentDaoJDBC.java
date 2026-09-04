package model.dao.impl;

import db.DB;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department dpt) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, dpt.getName());

            int affectedRows = st.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    dpt.setId((id));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Department dpt) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, dpt.getName());
            st.setInt(2, dpt.getId());
            st.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM department WHERE Id = ?", Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, id);
            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Departamento não encontrado");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM department WHERE Id = ?");
            st.setInt(1, id);

            rs = st.executeQuery();
            if (rs.next()) {
                return instantiateDepartment(rs);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM department");

            rs = st.executeQuery();
            List<Department> dptoList = new ArrayList<>();
            while (rs.next()) {
//                return instantiateDepartment(rs);
                dptoList.add(instantiateDepartment(rs));
            }
            return dptoList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }

//        return List.of();
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));
        return dep;
    }
}
