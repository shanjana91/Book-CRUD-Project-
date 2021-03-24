package com.swing;

import java.sql.*;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BookCRUD {

	private JFrame frame;
	private JTextField txtbname;
	private JTextField txtedition;
	private JTextField txtprice;
	private JTable table;
	private JTextField txtsearch;
	
	Connection con;
	PreparedStatement smt;
	ResultSet rs;
	private JTextField txtid;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookCRUD window = new BookCRUD();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BookCRUD() {
		initialize();
		connect();
		table_show();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 704, 447);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Book CRUD");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblNewLabel.setBounds(245, 21, 173, 37);
		frame.getContentPane().add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(29, 69, 291, 172);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Book Name");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(20, 48, 85, 17);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Edition");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(20, 90, 85, 17);
		panel.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Price");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1_1.setBounds(20, 128, 85, 17);
		panel.add(lblNewLabel_1_1_1);
		
		txtbname = new JTextField();
		txtbname.setBounds(129, 48, 137, 20);
		panel.add(txtbname);
		txtbname.setColumns(10);
		txtbname.requestFocus();
		
		txtedition = new JTextField();
		txtedition.setColumns(10);
		txtedition.setBounds(129, 90, 137, 20);
		panel.add(txtedition);
		
		txtprice = new JTextField();
		txtprice.setColumns(10);
		txtprice.setBounds(129, 128, 137, 20);
		panel.add(txtprice);
		
		JLabel lblNewLabel_1_1_2_1 = new JLabel("Book ID");
		lblNewLabel_1_1_2_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1_2_1.setBounds(20, 19, 85, 17);
		panel.add(lblNewLabel_1_1_2_1);
		
		txtid = new JTextField();
		txtid.setColumns(10);
		txtid.setBounds(129, 19, 137, 20);
		panel.add(txtid);
		
		JButton btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name=txtbname.getText();
				String edition=txtedition.getText();
				String price=txtprice.getText();
				
				try {
					smt=con.prepareStatement("insert into book(name,edition,price) values(?,?,?)");
					smt.setString(1, name);
					smt.setString(2, edition);
					smt.setString(3, price);
					smt.executeUpdate();
					JOptionPane.showMessageDialog(null,"Record added Sucessfully!");
					
					table_show(); //method call to view the updates ,inserts and deletes.
					
					txtbname.setText("");
					txtid.setText("");
					txtedition.setText("");
					txtprice.setText("");
					txtbname.requestFocus(); //to get the input focus on this textbox 
				}
				catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(29, 256, 89, 47);
		frame.getContentPane().add(btnNewButton);
		
		JScrollPane rstable = new JScrollPane();
		rstable.setBounds(367, 69, 275, 245);
		frame.getContentPane().add(rstable);
		
		table = new JTable();
		rstable.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(29, 325, 291, 65);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("Book ID");
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1_2.setBounds(10, 23, 85, 17);
		panel_1.add(lblNewLabel_1_1_2);
		
		txtsearch = new JTextField();
		txtsearch.setColumns(10);
		txtsearch.setBounds(105, 24, 71, 17);
		panel_1.add(txtsearch);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				String id=txtsearch.getText();
				smt=con.prepareStatement("select name,edition,price from book where id=?");
				smt.setString(1, id);
				rs=smt.executeQuery();
				
				if(rs.next()==true) {
					String name=rs.getString(1);
					String edition=rs.getString(2);
					String price=rs.getString(3);
					txtbname.setText(name);
					txtedition.setText(edition);
					txtprice.setText(price);
				}
				else {
					txtbname.setText("");
					txtedition.setText("");
					txtprice.setText("");
					JOptionPane.showMessageDialog(null, "No records Found!");
				}
				}
				catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnSearch.setBounds(186, 15, 85, 36);
		panel_1.add(btnSearch);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(395, 325, 89, 47);
		frame.getContentPane().add(btnExit);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id=txtid.getText();
				String name=txtbname.getText();
				String edition=txtedition.getText();
				String price=txtprice.getText();
				try {
					smt=con.prepareStatement("update book set name=?,edition=?,price=? where id=?");
					smt.setString(1, name);
					smt.setString(2, edition);
					smt.setString(3, price);
					smt.setString(4, id);
					smt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record Updated Succesfully");
					table_show();
					txtbname.setText("");
					txtid.setText("");
					txtedition.setText("");
					txtprice.setText("");
					txtbname.requestFocus();
				}
				catch(SQLException EX) {
					EX.printStackTrace();
				}
			}
		});
		btnUpdate.setBounds(128, 262, 89, 37);
		frame.getContentPane().add(btnUpdate);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtbname.setText("");
				txtid.setText("");
				txtedition.setText("");
				txtprice.setText("");
				txtsearch.setText("");
			}
		});
		btnClear.setBounds(532, 325, 89, 47);
		frame.getContentPane().add(btnClear);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id=txtid.getText();
				try {
					smt=con.prepareStatement("delete from book where id=?");
					smt.setString(1, id);
					smt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Deleted Successfully");
					table_show();
					txtid.setText("");
					txtbname.setText("");
					txtedition.setText("");
					txtprice.setText("");
					txtbname.requestFocus();
				}
				catch(SQLException ex) {
					
				}
			}
		});
		btnDelete.setBounds(227, 252, 89, 47);
		frame.getContentPane().add(btnDelete);
	}
	
	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root","#Shan2334");
		}
		catch(ClassNotFoundException ex){
			ex.printStackTrace();
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
		
	}
	public void table_show() {
		try {
			smt=con.prepareStatement("select * from book");
			rs=smt.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
		
	}
}
