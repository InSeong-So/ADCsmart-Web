package kr.openbase.adcsmart.service.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.postgresql.util.PSQLException;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

//import kr.openbase.adcms.service.dto.*;
//import kr.openbase.adcms.service.utility.*;

/**
 * 커넥션을 랩핑하여 데이터베이스를 이용하게 해주는 클래스입니다.
 * 
 * @author Choi, Young-jo
 */
public class OBDatabase
{
//    protected ResultSet resultSet;
    private Connection conn=null;
    private Statement sql=null;
    private PreparedStatement ps;
	
	/**
	 * 데이터베이스에 작업을 위한 인스턴스 생성자. DB이름, 사용자이름, 비밀번호는 기본값을 사용한다.
	 */
	public OBDatabase()
	{
	}
	
    /**
     * DB을 오픈한다.
     * 
     * @throws SQLException
     *             -- DB 관련 오류 발생시.
     */
    public Connection openDB() throws OBException
    {
        if(this.conn != null)
        {
            closeDB();
        }
        try
        {
            // db pool을 안 쓰는 코드. 3.0.9p2까지 씀
//          String url = OBDBConnectionPool.DBCP_PROPS.getProperty("url");
//          String userName = OBDBConnectionPool.DBCP_PROPS.getProperty("user");
//          String password = OBDBConnectionPool.DBCP_PROPS.getProperty("password");
//          this.conn = DriverManager.getConnection(url, userName, password);
            
            // db pool을 쓰는 코드 3.1.1부터
            this.conn = OBDBConnectionPool.getInstance().getConnection();

            this.sql = this.conn.createStatement(); // create a statement that we can use later
        }
        catch(PSQLException e)
        {
            if(this.conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(Exception e1){}
            }
            throw new OBException(OBException.ERRCODE_DB_OPEN, "PSQLException: "+e.getMessage());
        }
        catch(SQLException e)
        {
            if(this.conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(Exception e1){}
            }
            throw new OBException(OBException.ERRCODE_DB_OPEN, "SQLException: "+e.getMessage());
        }
        catch(Exception e)
        {
            if(this.conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(Exception e1){}
            }
            throw new OBException(OBException.ERRCODE_DB_OPEN, "Exception: "+e.getMessage());
        }
        finally
        {

        }
        return conn;
    }

    public boolean isClosed()
    {
        /* try { executeQuery("SELECT PG_DATABASE.DATNAME FROM PG_DATABASE;"); // 쿼리 수행 후 결과가 있으면 open 상태로 간주한다. } catch(SQLException e) { return true; } catch(Exception e) { return true; } return false; */

        try
        {
            executeQuery("SELECT 1;");
        }
        catch(Exception e)
        {
            return true;
        }

        return false;
        /* boolean ret = false;
         * 
         * try { ret = connection.isClosed(); } catch(SQLException e) { }
         * 
         * return ret; */
    }

    
    
    /**
     * 오픈된 DB을 close 한다.
     * 
     * @throws SQLException
     *             -- DB 관련 오류 발생시.
     */
    public synchronized void closeDB()
    {
        if(this.ps != null)
        {
            try
            {
                this.ps.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        if(this.sql != null)
        {
            try
            {
                this.sql.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        if(this.conn != null)
        {
            try
            {
                this.conn.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        this.ps = null;
        this.sql = null;
        this.conn = null;
    }

    public static void closeRS(ResultSet rs)
    {
        try
        {
            if(rs != null)
                rs.close();
        }
        catch(SQLException e)
        {
        }
    }

    /**
     * select 쿼리을 수행한다.
     * 
     * @param sqlText
     *            -- 수행하고자 하는 쿼리 문자열.
     * @return select 쿼리 수행 결과 데이터 셋.
     * @throws SQLException
     *             -- DB 관련 오류 발생시.
     */
    public ResultSet executeQuery(String sqlText) throws SQLException
    {
        ResultSet rs = null;
        if(sql != null)
        {
            OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, "sqlText:" + sqlText);
            rs = sql.executeQuery(sqlText);
        }
        return rs;
    }

    public boolean execute(String sqlText) throws SQLException
    {
        boolean ret = false;
        if(sql != null)
        {
            OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, "sqlText:" + sqlText);
            ret = sql.execute(sqlText);
        }
        return ret;
    }

    public void setAutoCommit(boolean flag) throws SQLException
    {
        if(conn != null)
        {
            conn.setAutoCommit(flag);
        }
    }
    
    public void commit() throws SQLException
    {
        if(conn != null)
        {
            conn.commit();
        }
    }
    
    
    public void rollback() throws SQLException
    {
        if(conn != null)
        {
            conn.rollback();
        }
    }
    
    public int getUpdateCount() throws SQLException
    {
        int ret = -1;
        if(sql != null)
        {
            OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, "ERROR::getUpdateCount()");
            ret = sql.getUpdateCount();
        }
        return ret;
    }

    /**
     * insert 쿼리를 수행한다.
     * 
     * @param sqlText
     *            -- 수행하고자 하는 쿼리 문자열.
     * @throws SQLException
     *             -- DB 관련 오류 발생시.
     */
    public void executeUpdate(String sqlText) throws SQLException
    {
        if(sql != null)
        {
            OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, "sqlText:" + sqlText);
            sql.executeUpdate(sqlText);
        }
    }

    /**
     * 쿼리 결과 셋에서 Timestamp 형태로 데이터를 추출하여 제공한다.
     * 
     * @param rs
     *            -- 쿼리 결과 셋.
     * @param columnLabel
     *            -- 추출하기 위한 컬럼명.
     * @return Timestamp.
     * @throws SQLException
     *             -- DB 관련 오류 발생시.
     */
    public Timestamp getTimestamp(ResultSet rs, String columnLabel) throws SQLException
    {
        return rs.getTimestamp(columnLabel);
    }

    /**
     * ResultSet에서 Int 형태의 컬럼 데이터를 제공한다.
     * 
     * @param rs
     *            -- 쿼리결과.
     * @param columnLabel
     *            -- 조회하고자 하는 컬럼 이름.
     * @return 컬럼 데이터. Integer 형태.
     * @throws SQLException
     *             -- DB 관련 오류 발생시.
     */
    public int getInteger(ResultSet rs, String columnLabel) throws SQLException
    {
        return rs.getInt(columnLabel);
    }

    public long getLong(ResultSet rs, String columnLabel) throws SQLException
    {
        return rs.getLong(columnLabel);
    }

    /**
     * ResultSet에서 입력된 colum에 해당되는 문자열을 제공한다.
     * 
     * @param rs
     *            -- 쿼리결과.
     * @param columnLabel
     *            -- 조회하고자 하는 컬럼 이름.
     * @return 조회된 문자열
     * @throws SQLException
     *             -- DB 관련 오류 발생시.
     */
    public String getString(ResultSet rs, String columnLabel) throws SQLException
    {
        return rs.getString(columnLabel);
    }

//    public String getString(ResultSet rs, int columnIndex) throws SQLException
//    {
//        return rs.getString(columnIndex);
//    }

    public void initPreparedStatement(String sqlText) throws SQLException
    {
        this.ps = this.conn.prepareStatement(sqlText);
    }

    public void setPreparedStatementInt(int parameterIndex, int value) throws SQLException
    {
        this.ps.setInt(parameterIndex, value);
    }

//    public void setPreparedStatementLong(int parameterIndex, Long value) throws SQLException
//    {
//        this.ps.setLong(parameterIndex, value);
//    }

    public void setPreparedStatementString(int parameterIndex, String value) throws SQLException
    {
        this.ps.setString(parameterIndex, value);
    }

    public void setPreparedStatementTimestamp(int parameterIndex, Timestamp value) throws SQLException
    {
        this.ps.setTimestamp(parameterIndex, value);
    }

//    public void setPreparedStatementBinaryStream(int parameterIndex, InputStream value) throws SQLException
//    {
//        this.ps.setBinaryStream(parameterIndex, value);
//    }

    public void setPreparedStatementObject(int parameterIndex, Object obj) throws SQLException, IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(obj);

        byte[] bytes = bos.toByteArray();
        oos.close();
        bos.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        this.ps.setBinaryStream(parameterIndex, inputStream, bytes.length);
        OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "set object parameter index = " + parameterIndex + " / bytes = " + bytes.length);
    }

    public void executeUpdatePreparedStreamt() throws SQLException
    {
        this.ps.executeUpdate();
    }

    public void deInitPreparedStatement() throws SQLException
    {
        this.ps.close();
    }
}
