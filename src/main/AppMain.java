package main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AppMain extends JFrame{
	JTextField t_name, t_age, t_weight, t_height;
	JButton bt;
	DBManager manager=DBManager.getInstance();
	Connection con;
	
	
	public AppMain() {
		setLayout(new FlowLayout());
		t_name=new JTextField(13);
		t_age=new JTextField(13);
		t_weight=new JTextField(13);
		t_height=new JTextField(13);
		bt=new JButton("등록");
		con=manager.getConnection();
		
		add(t_name);
		add(t_age);
		add(t_weight);
		add(t_height);
		add(bt);
		
		bt.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				regist();
			}
		});
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(200,190);
		setVisible(true);
	}
	
	//사원 등록시 두개의 테이블로 분리되어 있으므로, 만일의 사태(데이터 무결성 깨짐)에 대비해 트랜잭션을  적용한 프로그램을
	//작성해본다!!
	//"사원등록"이라는 업무는 몇개의 세부업무로 이루어진 트랜잭션인가? 2개 
	public void regist(){
		PreparedStatement pstmt=null;
		StringBuffer sql=new StringBuffer();
		
		//둘 중에 하나라도 입력에 실패하면, 처음부터 없었던 일로 디돌려 놓자!!
		//트랜잭션의 rollback!!
		try {
			//Connection 객체에는 setAutoCommit(); 
			//이 메서드가 디폴트로 true로 되어 있기 때문에 
			//JDBC를 이용한  DML은 개발자가 별도의  commit을 하지 않아도 되었었다..
			
			con.setAutoCommit(false); //트랜잭션 시작!!
			
			//staff 테이블에 insert 
			sql.append("insert into staff(staff_id, name, age)");
			sql.append(" values(seq_staff.nextval, ?,?)");
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, t_name.getText());
			pstmt.setInt(2,Integer.parseInt( t_age.getText()));
			pstmt.executeUpdate();
		
			sql.delete(0, sql.length()); //버퍼 지우기!
			
			//bio 테이블에 insert
			sql.append("insert into bio(staff_id, weight, height)");
			sql.append(" values(seq_staff.currval, ?,?)");
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setInt(1, Integer.parseInt(t_weight.getText()));
			pstmt.setInt(2,Integer.parseInt( t_height.getText()));
			pstmt.executeUpdate();
			JOptionPane.showMessageDialog(this, "등록완료");
			con.commit();
			
		} catch (SQLException e) {
			try{
				con.rollback();
			}catch(SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			//connection 객체의 autoCommit 속성을 다시 돌려놓자!!
			//con이 공유되고 있으며, 다른 메서드에 사용할 때는 트랜잭션을 적용하지 않을 경우도 있으므로..
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new AppMain();
	}
}
