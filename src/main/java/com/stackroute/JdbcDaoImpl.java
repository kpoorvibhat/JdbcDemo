package com.stackroute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Component
public class JdbcDaoImpl {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DataSource getDataSource() {
        return dataSource;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
//
//    public Circle getCircle(int circleId) {
//        Connection conn = null;
//        try {
//            conn = dataSource.getConnection();
//
//            PreparedStatement ps = conn.prepareStatement("SELECT * FROM circle where id =?");
//            ps.setInt(1, circleId);
//
//            Circle circle = null;
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                circle = new Circle(circleId, rs.getString("name"));
//            }
//            rs.close();
//            ps.close();
//            return circle;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        finally {
//            try{
//                conn.close();
//            }
//            catch(Exception e) {}
//        }
//      }
      public Integer getCircleCount() {
        String sql = "SELECT COUNT(*) FROM circle";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public String getCircleName(int circleId) {
        String sql = "SELECT NAME FROM circle WHERE ID = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{circleId}, String.class);

    }
    public Circle getCircleforId(int circleId) {
        String sql = "SELECT * FROM circle where id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{circleId}, new CircleMapper());

    }
    public List<Circle> getAllCircles() {
        String sql = "select * from circle";
        return jdbcTemplate.query(sql, new CircleMapper());
    }
//    public void insertCircle(Circle circle) {
//        String sql = "insert into circle (id, name) values (?, ?)";
//        jdbcTemplate.update(sql, circle.getId(), circle.getName());
//    }

    public void insertCircle(Circle circle) {
        String sql = "insert into circle (id, name) values (:id, :name)";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", circle.getId())
                                                            .addValue("name", circle.getName());
        namedParameterJdbcTemplate.update(sql, namedParameters);

    }

    public void createTriangleTable() {
        String sql = "CREATE TABLE triangle (id integer, name varchar(50))";
        jdbcTemplate.execute(sql);
    }


    private static final class CircleMapper implements RowMapper<Circle> {
        @Override
        public Circle mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Circle circle = new Circle();
            circle.setId(resultSet.getInt("ID"));
            circle.setName(resultSet.getString("NAME"));
            return circle;
        }
    }
}