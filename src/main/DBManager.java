/*1. ������ �Ѱ��� �α�
 * -�����ͺ��̽� ���������� �ߺ��ؼ� �������� �ʱ� ����
 * (db������ �ϴ� ������ Ŭ��������)
 * 
 * 2. �ν��Ͻ��� ������ �Ѱ��� �α�!
 * -���ø����̼� ������ �����Ǵ� connection ��ü�� �ϳ��� �����ϱ� ����
 * */

package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager { //���⼭ class�� Ŭ���� �����ϴ� �����
	static private DBManager instance;
	private String driver="oracle.jdbc.driver.OracleDriver";
	private String url="jdbc:oracle:thin:@localhost:1521:XE";
	private String user="bread";
	private String password="bread";
	
	private Connection con;
	
	
	private DBManager(){
		/*1.����̹� �ε�
		 *2. ����
		 *3. ������ ����
		 *4. �ݳ�, ��ü*/
		try {
			Class.forName(driver); //Class class�� Ŭ���������� ������ ������ �ִ�. � �޼ҵ�� ������ �����Ǿ� �ִ��� �� �� �ִ�.
			con=DriverManager.getConnection(url, user, password);
			if (con!=null) {
				System.out.println("DB���Ӽ���");
			} else {
				System.out.println("DB���ӽ���");			
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	static public DBManager getInstance() {
		if (instance==null) {
			instance=new DBManager();
		}		
		
		return instance;
	}

	public Connection getConnection() {
		return con;
	}

	public void disConnect(Connection con) {
		if (con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	
	
}