package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

public class DBUtil {
    // 계정 정보는 Git 에 올리지 않는 db.properties 에서 읽음 (db.properties.example 참고)
    private static final Properties props = loadProps();
    private static final String user = props.getProperty("db.user", "");
    private static final String password = props.getProperty("db.password", "");
    private static final String dbserver = "jdbc:postgresql://localhost:5432/";
    private static final String dbname = "testdb";
    private static final String driverName = "org.postgresql.Driver";

    private static Connection con = null;

    private static Properties loadProps() {
        Properties p = new Properties();
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) p.load(in);
            else System.out.println("db.properties 가 없습니다 (db.properties.example 참고)");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    // DB 연결 메서드
    public static Connection getConnection() throws SQLException {
        try {
            if (con == null || con.isClosed()) {
                // PostgreSQL 드라이버 로드
                Class.forName(driverName);
                // DB 연결
                con = DriverManager.getConnection(dbserver + dbname, user, password);
            }
        } catch (Exception e) {
            System.out.println("DB 연결 실패");
            e.printStackTrace();
            throw new SQLException("DB 연결 실패", e);
        }
        return con;
    }

    // DB 리소스 해제 메서드
    public static void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
