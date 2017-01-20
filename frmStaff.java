package Hosiptal;

import java.awt.EventQueue;

import javax.swing.JFrame;
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.JXDatePicker;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.im.InputContext;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.ButtonGroup;
import javax.swing.DropMode;
import javax.swing.Icon;

import org.jdatepicker.util.JDatePickerUtil;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class frmStaff {

	private JFrame frame;
	private JTextField txtID;
	private JTextField txtPosition;
	private JTextField txtAddress;
	private JTextField txtSalary;
	private JTextField txtName;
	private JTextField txtPhone;
	private JTextField txtSearch;
	private JTable tbStaff;
	private JRadioButton rdbMale;
	private JRadioButton rdbFemale;
	private	DefaultTableModel model;
	private JXDatePicker dpDOB;
	private JXDatePicker dpWorkDate;
	private JLabel lblPhoto;
	private File f;
	private JFileChooser chooser=new JFileChooser();
	private Connection con;
	private ResultSet rss;
	private Statement stm;
	private InputStream isa=null; 
	private JButton btnBrowse = new JButton("\u1791\u17B6\u1789\u1799\u1780");
	private JButton btnSave = new JButton("\u179A\u1780\u17D2\u179F\u17B6\u179A\u1791\u17BB\u1780");
	private JButton btnEdit = new JButton("\u1780\u17C2\u1794\u17D2\u179A\u17C2");
	private JButton btnDelete = new JButton("\u179B\u17BB\u1794");
	private JButton btnAdd = new JButton("\u1794\u1793\u17D2\u1790\u17C2\u1798\u1790\u17D2\u1798\u17B8");
	private DefaultTableModel dtm = new DefaultTableModel(new Object[]{"ID", "Name","Gender","DOB","Position","Address","Salary","WorkDate","Phone Number","Photo"}, 0);
    String FileName=null;
	String s="";
	Blob blob=null;
	byte[] img=null;
	//private JScrollPane scrTable = new JScrollPane(tblDisplay);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmStaff window = new frmStaff();
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
	public frmStaff() {
		Connection();
		initialize();
		btnSave.setEnabled(false);
		btnEdit.setEnabled(false);
		btnDelete.setEnabled(false);
		RefreshTable();
		CloseControl();
	}
	
	void Connection()
	{
		try {
			con = DriverManager.getConnection("jdbc:mysql:" + "//localhost:3306/database?characterEncoding=utf-8","root","12345");
			//con = DriverManager.getConnection("jdbc:mysql:" + "//192.168.88.162/database","Hospital","12345");
			stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		}
		catch(Exception ex)
		{
			System.out.print(ex);
		}
	}
	
	void RefreshTable()
	{
		
		try {
			rss = stm.executeQuery("SELECT * FROM tbstaff");
			while(rss.next()) {	
				byte[] img=rss.getBytes("Photo");
				ImageIcon image=new ImageIcon(img);
				Image im=image.getImage().getScaledInstance(95,70,Image.SCALE_SMOOTH);
				//Image my=im.getScaledInstance(tbStaff.getWidth(),tbStaff.getHeight(), Image.SCALE_SMOOTH);
				//ImageIcon newImage=new ImageIcon(my);
				ImageIcon ime=new ImageIcon(im);
				/*ImageIcon image=null;
				img=rss.getBytes(10);
				image=new ImageIcon(img);
				Image im=image.getImage();*/
				dtm.addRow(new Object[]{
					rss.getString(1),
					rss.getString(2),
					rss.getString(3),
					rss.getString(4),
					rss.getString(5),
					rss.getString(6),
					rss.getDouble(7),
					rss.getString(8),
					rss.getString(9),
					ime
					//newImage
					//rss.getBlob(10)
				});
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
	
	void ClearControl()
	{
		txtID.setText("");
		txtName.setText("");
		rdbFemale.setSelected(false);
		rdbMale.setSelected(false);
		dpDOB.setDate(null);
		txtPosition.setText("");
		txtAddress.setText("");
		txtSalary.setText("");
		dpWorkDate.setDate(null);
		txtPhone.setText("");
		lblPhoto.setIcon(null);
	}
	
	void CloseControl()
	{
		txtID.setEnabled(false);
		txtName.setEnabled(false);
		rdbMale.setEnabled(false);
		rdbFemale.setEnabled(false);
		dpDOB.setEnabled(false);
		txtPosition.setEnabled(false);
		txtAddress.setEnabled(false);
		txtSalary.setEnabled(false);
		dpWorkDate.setEnabled(false);
		txtPhone.setEnabled(false);
		btnBrowse.setEnabled(false);
	}
	
	void EnableControl()
	{
		txtID.setEnabled(true);
		txtName.setEnabled(true);
		rdbMale.setEnabled(true);
		rdbFemale.setEnabled(true);
		dpDOB.setEnabled(true);
		txtPosition.setEnabled(true);
		txtAddress.setEnabled(true);
		txtSalary.setEnabled(true);
		dpWorkDate.setEnabled(true);
		txtPhone.setEnabled(true);
		btnBrowse.setEnabled(true);
		btnSave.setEnabled(true);
		
	}
	
	void Search()
	{
		try {
			dtm.setRowCount(0);
			String str=txtSearch.getText();
			rss = stm.executeQuery("SELECT * FROM tbstaff WHERE Name LIKE '%"+str+"%' OR ID LIKE '%"+str+"%';");
			while(rss.next()) 
			{	
				byte[] img=rss.getBytes("Photo");
				ImageIcon image=new ImageIcon(img);
				Image im=image.getImage().getScaledInstance(95,70,Image.SCALE_SMOOTH);
				ImageIcon ime=new ImageIcon(im);
				dtm.addRow(new Object[]{
					rss.getString(1),
					rss.getString(2),
					rss.getString(3),
					rss.getString(4),
					rss.getString(5),
					rss.getString(6),
					rss.getDouble(7),
					rss.getString(8),
					rss.getString(9),
					ime
					//newImage
					//rss.getBlob(10)
				});
				
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	
	private void initialize() {
		frame = new JFrame();
		frame.setType(Type.POPUP);
		frame.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		frame.setBounds(100, 100, 950, 733);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);		
		frame.setLocationRelativeTo(null);
		
		JLabel lblTitle = new JLabel("\u1796\u178F\u17CC\u1798\u17B6\u1793\u1794\u17BB\u1782\u17D2\u1782\u179B\u17B7\u1780");
		lblTitle.setBounds(0, 0, 952, 70);
		lblTitle.setBackground(new Color(173, 216, 230));
		//lblTitle.setOpaque(true);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(new Color(210, 180, 140));
		lblTitle.setFont(new Font("Khmer OS", Font.PLAIN, 45));
		frame.getContentPane().add(lblTitle);
		
		JLabel lblNewLabel = new JLabel("\u179B\u17C1\u1781\u179F\u1798\u17D2\u1782\u17B6\u179B\u17CB");
		lblNewLabel.setBounds(22, 84, 159, 32);
		lblNewLabel.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		frame.getContentPane().add(lblNewLabel);
		
		JLabel label = new JLabel("\u1788\u17D2\u1798\u17C4\u17C7");
		label.setBounds(22, 130, 159, 32);
		label.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("\u1797\u17C1\u1791");
		label_1.setBounds(22, 173, 159, 32);
		label_1.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		frame.getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("\u1790\u17D2\u1784\u17C3\u1781\u17C2\u1786\u17D2\u1793\u17B6\u17C6\u1780\u17C6\u178E\u17BE\u178F");
		label_2.setBounds(22, 216, 159, 32);
		label_2.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		frame.getContentPane().add(label_2);
		
		JLabel label_3 = new JLabel("\u178F\u17BC\u1793\u17B6\u1791\u17B8");
		label_3.setBounds(22, 259, 159, 32);
		label_3.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		frame.getContentPane().add(label_3);
		
		JLabel label_4 = new JLabel("\u17A2\u17B6\u179F\u1799\u178A\u17D2\u178B\u17B6\u1793");
		label_4.setBounds(22, 302, 159, 32);
		label_4.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		frame.getContentPane().add(label_4);
		
		JLabel label_5 = new JLabel("\u1794\u17D2\u179A\u17B6\u1780\u17CB\u1781\u17C2");
		label_5.setBounds(22, 345, 159, 32);
		label_5.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		frame.getContentPane().add(label_5);
		
		JLabel lblAsdasdasd = new JLabel("\u1790\u17D2\u1784\u17C3\u1785\u17BC\u179B\u1794\u1798\u17D2\u179A\u17BE\u1780\u17B6\u179A\u1784\u17B6\u179A");
		lblAsdasdasd.setBounds(22, 388, 159, 32);
		lblAsdasdasd.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		frame.getContentPane().add(lblAsdasdasd);
		
		JLabel label_7 = new JLabel("\u179B\u17C1\u1781\u1791\u17BC\u179A\u179F\u1796\u17D2\u1791");
		label_7.setBounds(22, 431, 159, 32);
		label_7.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		frame.getContentPane().add(label_7);
		
		txtID = new JTextField();
		txtID.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				InputContext c = InputContext.getInstance();
	            boolean b = c.selectInputMethod(Locale.FRENCH);
			}
		});
		txtID.setBounds(182, 81, 211, 38);
		txtID.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		frame.getContentPane().add(txtID);
		txtID.setColumns(10);
		
		rdbMale = new JRadioButton("\u1794\u17D2\u179A\u17BB\u179F");
		rdbMale.setBounds(210, 174, 59, 23);
		rdbMale.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		frame.getContentPane().add(rdbMale);
		
		rdbFemale = new JRadioButton("\u179F\u17D2\u179A\u17B8");
		rdbFemale.setBounds(294, 174, 59, 23);
		rdbFemale.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		frame.getContentPane().add(rdbFemale);
		
		txtPosition = new JTextField();
		txtPosition.setBounds(182, 255, 211, 38);
		txtPosition.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		txtPosition.setColumns(10);
		frame.getContentPane().add(txtPosition);
		
		txtAddress = new JTextField();
		txtAddress.setBounds(182, 298, 211, 38);
		txtAddress.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		txtAddress.setColumns(10);
		frame.getContentPane().add(txtAddress);
		
		txtSalary = new JTextField();
		txtSalary.setBounds(182, 341, 211, 38);
		txtSalary.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		txtSalary.setColumns(10);
		frame.getContentPane().add(txtSalary);
		
		txtName = new JTextField();
		txtName.setBounds(182, 127, 211, 38);
		txtName.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		txtName.setColumns(10);
		frame.getContentPane().add(txtName);
		
		dpDOB = new JXDatePicker();
		dpDOB.setBounds(182, 212, 211, 37);
		dpDOB.setFormats(new SimpleDateFormat("dd.MM.yyyy"));
		frame.getContentPane().add(dpDOB);
		
		dpWorkDate = new JXDatePicker();
		dpWorkDate.setBounds(182, 384, 211, 37);
		dpWorkDate.setFormats(new SimpleDateFormat("dd.MM.yyyy"));
		frame.getContentPane().add(dpWorkDate);
		
		txtPhone = new JTextField();
		txtPhone.setBounds(182, 427, 211, 38);
		txtPhone.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		txtPhone.setColumns(10);
		frame.getContentPane().add(txtPhone);
		
		JLabel label_8 = new JLabel("\u179F\u17D2\u179C\u17C2\u1784\u179A\u1780\u1794\u17BB\u1782\u17D2\u1782\u179B\u17B7\u1780");
		label_8.setBounds(22, 477, 159, 32);
		label_8.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		frame.getContentPane().add(label_8);
		
		txtSearch = new JTextField();
		
		txtSearch.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				Search();
			}
		
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				dtm.setRowCount(0);
				RefreshTable();
			}
			
		});

		txtSearch.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				txtSearch.setText("\u179F\u17D2\u179C\u17C2\u1784\u179A\u1780\u178F\u17B6\u1798\u179A\u1799\u17C7\u179B\u17C1\u1781\u179F\u1798\u17D2\u1782\u17B6\u179B\u17CB\u17AC\u1788\u17D2\u1798\u17C4\u17C7");
				RefreshTable();
			}
		});
		
		txtSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtSearch.setText("");
			}
		});
		
		txtSearch.setBounds(182, 474, 396, 38);
		txtSearch.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		txtSearch.setColumns(10);
		txtSearch.setText("\u179F\u17D2\u179C\u17C2\u1784\u179A\u1780\u178F\u17B6\u1798\u179A\u1799\u17C7\u179B\u17C1\u1781\u179F\u1798\u17D2\u1782\u17B6\u179B\u17CB\u17AC\u1788\u17D2\u1798\u17C4\u17C7");
		frame.getContentPane().add(txtSearch);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 520, 944, 184);
		frame.getContentPane().add(scrollPane);
		
		tbStaff = new JTable(dtm){
				public Class getColumnClass(int column)
        {
           switch (column)
                    {
                        case 9: return Icon.class;
                        default: return Object.class;
                    }
                }
		}; 
		tbStaff.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		tbStaff.setModel(dtm);
		tbStaff.setRowHeight(70);
		
		tbStaff.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e)
			{
				
				btnAdd.setText("\u1794\u1793\u17D2\u1790\u17C2\u1798\u1790\u17D2\u1798\u17B8");
				btnSave.setEnabled(false);
				btnEdit.setEnabled(true);
				btnDelete.setEnabled(true);
				btnEdit.setText("\u1780\u17C2\u1794\u17D2\u179A\u17C2");
				rdbMale.setSelected(false);
				rdbFemale.setSelected(false);
				
				if(tbStaff.getSelectedRow()>=0)
				{
				txtID.setText((String) tbStaff.getModel().getValueAt(tbStaff.getSelectedRow(),0));
				
				try
				{
					rss = stm.executeQuery("SELECT * FROM tbstaff WHERE  ID='" + txtID.getText() + "'");
					while(rss.next())
					{
						txtID.setText(rss.getString(1));
						txtName.setText(rss.getString(2));
						String gender=rss.getString(3);
						System.out.print(gender);
						if(Objects.equals(gender,"F"))
						{
							rdbFemale.setSelected(true);
						}
						else
							rdbMale.setSelected(true);
						String d=rss.getString(4);
						SimpleDateFormat dt=new SimpleDateFormat("dd.MM.yyyy");
						try{
						Date date=dt.parse(d);
						dpDOB.setDate(date);
						}catch(Exception ex){
							System.out.print(ex);
						}
						txtPosition.setText(rss.getString(5));
						txtAddress.setText(rss.getString(6));
						txtSalary.setText(rss.getString(7));
						String de=rss.getString(8);
						try{
						Date date=dt.parse(de);
						dpWorkDate.setDate(date);
						}catch(Exception ex){
							System.out.print(ex);
						}
						txtPhone.setText(rss.getString(9));
						img=rss.getBytes("Photo");
						ImageIcon image=new ImageIcon(img);
						Image im=image.getImage();
						Image newimage=im.getScaledInstance(lblPhoto.getWidth(),lblPhoto.getHeight(),Image.SCALE_SMOOTH);
						ImageIcon ime=new ImageIcon(newimage);
						lblPhoto.setIcon(ime); 
						/*isa = rss.getBinaryStream("Photo"); 
						// Decode the inputstream as BufferedImage
						BufferedImage bufImg = null;
						bufImg = ImageIO.read(isa);
						Image image = bufImg.getScaledInstance(lblPhoto.getWidth(),lblPhoto.getHeight(),Image.SCALE_SMOOTH);
						ImageIcon icon =new ImageIcon(image);
						lblPhoto.setIcon(icon);*/ 
					}	 
				}catch(SQLException ex){
					ex.printStackTrace();
				}
				}
			}
		});
		
		
		/*tbStaff.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				 "ID", "Name","Gender","DOB","Position","Address","Salary","WorkDate","Phone Number","Photo"
			}
		));
		*/
		scrollPane.setViewportView(tbStaff);
		

		lblPhoto = new JLabel("");
		lblPhoto.setOpaque(true);
		lblPhoto.setHorizontalAlignment(SwingConstants.CENTER);
		lblPhoto.setForeground(Color.RED);
		lblPhoto.setFont(new Font("Khmer OS", Font.PLAIN, 50));
		lblPhoto.setBackground(UIManager.getColor("Button.shadow"));
		lblPhoto.setBounds(476, 81, 169, 144);
		frame.getContentPane().add(lblPhoto);
		//\u1794\u17C4\u17C7\u1794\u1784\u17CB

		btnAdd.setIcon(new ImageIcon("add new.png"));
		btnAdd.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnAdd.setIconTextGap(27);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ButtonGroup group=new ButtonGroup();
				group.add(rdbMale);
				group.add(rdbFemale);
				if(btnAdd.getText()=="\u1794\u1793\u17D2\u1790\u17C2\u1798\u1790\u17D2\u1798\u17B8")
				{	
				btnEdit.setEnabled(false);
				btnDelete.setEnabled(false);
				ClearControl();
				txtID.requestFocus();	
				EnableControl();
				btnAdd.setText("\u1794\u17C4\u17C7\u1794\u1784\u17CB");
				}
				else
				{
					btnAdd.setText("\u1794\u1793\u17D2\u1790\u17C2\u1798\u1790\u17D2\u1798\u17B8");
					btnEdit.setEnabled(true);
					btnDelete.setEnabled(true);
					btnSave.setEnabled(true);
					ClearControl();
					CloseControl();
				}
			}
		});
		btnAdd.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		btnAdd.setBounds(747, 81, 169, 39);
		frame.getContentPane().add(btnAdd);
		lblTitle.setOpaque(true);
		
		//JButton btnBrowse = new JButton("\u1791\u17B6\u1789\u1799\u1780");
		btnBrowse.setIcon(new ImageIcon("browse.png"));
		btnBrowse.setHorizontalAlignment(SwingConstants.RIGHT);
		btnBrowse.setIconTextGap(40);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				FileNameExtensionFilter filter=new FileNameExtensionFilter("*.Image","jpg","png");
				chooser.addChoosableFileFilter(filter);
				chooser.showOpenDialog(null);
				f=chooser.getSelectedFile();
				if(f!=null)
				FileName=f.getAbsolutePath();
				s=FileName;
				ImageIcon icon=new ImageIcon(FileName);
				if(FileName!=null){
					Image img = icon.getImage(); 
					Image newimg = img.getScaledInstance(lblPhoto.getWidth(), lblPhoto.getHeight(),  java.awt.Image.SCALE_SMOOTH);
		            icon = new ImageIcon(newimg);
		            lblPhoto.setIcon(icon);
		            lblPhoto.setText(null);
				}
				
			}
		});
		btnBrowse.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		btnBrowse.setBounds(476, 236, 169, 39);
		frame.getContentPane().add(btnBrowse);
		
		//JButton btnSave = new JButton("\u179A\u1780\u17D2\u179F\u17B6\u179A\u1791\u17BB\u1780");
		btnSave.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnSave.setIconTextGap(25);
		btnSave.setIcon(new ImageIcon("save.png"));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int Choice=JOptionPane.showOptionDialog(null,"Do you want to save?","Save",JOptionPane.YES_NO_OPTION,
														JOptionPane.QUESTION_MESSAGE,null,null,null);
				if(Choice==JOptionPane.YES_OPTION)
				{
				String ID=txtID.getText();
				String name=txtName.getText();
				String Gender="";
				if(rdbFemale.isSelected())
				  {
					Gender="F";
				  }
				else
			 	  {
			 		Gender="M";
			 	  }
				
				Date d=dpDOB.getDate();
				SimpleDateFormat dt=new SimpleDateFormat("dd.MM.yyyy");
				String DOB = dt.format(d);
				//System.out.print(DOB);
				String pos=txtPosition.getText();
				String address=txtAddress.getText();
				String salary=txtSalary.getText();
				Date da=dpWorkDate.getDate();
				SimpleDateFormat dte=new SimpleDateFormat("dd.MM.yyyy");
				String workdate = dte.format(da);
				//System.out.print(workdate);
				String phone=txtPhone.getText();
				
			    //sql = "INSERT INTO tbstaff (ID,Name,Gender,DOB,Position,Address,Salary,WorkDate,PhoneNumber,photo) VALUES ('" + id + "', '"
				//+ name + "','" + Gender + "','" + DOB + "' , '" + pos+ "','"+address+"','"+salary+"','"+workdate+"','"+
				//phone+"','"+photo+"')";
				try{
				    PreparedStatement ps=con.prepareStatement("INSERT INTO tbstaff(ID,Name,Gender,DOB,Position,Address,Salary,WorkDate,PhoneNumber,photo) value(?,?,?,?,?,?,?,?,?,?)");
				    InputStream is=new FileInputStream(new File(FileName));
				    ps.setString(1, ID);
				    ps.setString(2, name);
				    ps.setString(3, Gender);
				    ps.setString(4,DOB);
				    ps.setString(5, pos);
				    ps.setString(6, address);
				    ps.setString(7, salary);
				    ps.setString(8, workdate);
				    ps.setString(9, phone);
				    ps.setBlob(10, is);
				    ps.executeUpdate();
				    JOptionPane.showMessageDialog(null,"saved");
				}catch(Exception ex){
					System.out.print(ex);
				}
		    	/*try {
		    		System.out.println(sql);
					stm.execute(sql);
				} catch (SQLException e) {
					e.printStackTrace();
				}*/
		    	dtm.setRowCount(0);
		    	FileName=null;
		    	btnEdit.setEnabled(true);
		    	btnDelete.setEnabled(true);
				ClearControl();
		    	RefreshTable();
		    	btnAdd.setText("\u1794\u1793\u17D2\u1790\u17C2\u1798\u1790\u17D2\u1798\u17B8");
			  }
				else
					return;
			}
		});
		btnSave.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		btnSave.setBounds(747, 130, 169, 39);
		frame.getContentPane().add(btnSave);
		//\u1792\u17D2\u179C\u17BE\u1794\u1785\u17D2\u1785\u17BB\u1794\u17D2\u1794\u1793\u17D2\u1793\u1797\u17B6\u1796
		
		//JButton btnEdit = new JButton("\u1780\u17C2\u1794\u17D2\u179A\u17C2");
		btnEdit.setIcon(new ImageIcon("edit-file-icon.png"));
		btnEdit.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnEdit.setIconTextGap(45);
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(tbStaff.getSelectedRow()==-1)
				{
					JOptionPane.showMessageDialog(null,"Please select row in table");
					return;
				}
				String id=txtID.getText();
				String name=txtName.getText();
				String Gender="";
				if(rdbFemale.isSelected())
				  {
					Gender="F";
				  }
				else
			 	  {
			 		Gender="M";
			 	  }
				
				Date d=dpDOB.getDate();
				SimpleDateFormat dt=new SimpleDateFormat("dd.MM.yyyy");
				String DOB = dt.format(d);
				//System.out.print(DOB);
				String pos=txtPosition.getText();
				String address=txtAddress.getText();
				String salary=txtSalary.getText();
				Date da=dpWorkDate.getDate();
				SimpleDateFormat dte=new SimpleDateFormat("dd.MM.yyyy");
				String workdate = dte.format(da);
				//System.out.print(workdate);
				String phone=txtPhone.getText();
				if(btnEdit.getText()=="\u1780\u17C2\u1794\u17D2\u179A\u17C2")
				{
					btnAdd.setEnabled(true);
					btnDelete.setEnabled(false);
					btnSave.setEnabled(false);
					EnableControl();
					txtID.setEnabled(false);
					btnAdd.setText("\u1794\u17C4\u17C7\u1794\u1784\u17CB");
					btnEdit.setText("\u1792\u17D2\u179C\u17BE\u1794\u1785\u17D2\u1785\u17BB\u1794\u17D2\u1794\u1793\u17D2\u1793\u1797\u17B6\u1796");					
					btnEdit.setIconTextGap(5);
				    
				}
				else
				{
					btnAdd.setEnabled(true);
					btnDelete.setEnabled(true);
					btnSave.setEnabled(true);
		
					try {
						//String is=null;
						/*String sql=("UPDATE tbstaff set Name= '" + name + "',Gender ='" + Gender + "',DOB='"
							+ DOB + "',Position='" + pos + "',Address='" + address + "',Salary= " + salary + ",WorkDate='" + workdate
							+"',PhoneNumber='" + phone + "',Photo='" + is +"' WHERE ID= " + id + ";");*/
						//ps.setBlob(10,is)
						PreparedStatement ps=con.prepareStatement("UPDATE tbstaff set Name=?,Gender =?,DOB=?,Position=?,Address=?,Salary=?,WorkDate=?,PhoneNumber=?,Photo=? WHERE ID= " + id + ";");
						
						
						/*PreparedStatement ps=con.prepareStatement("UPDATE tbstaff set Name= '" + name + "',Gender ='" + Gender + "',DOB='"
							+ DOB + "',Position='" + pos + "',Address='" + address + "',Salary= " + salary + ",WorkDate='" + workdate
							+"',PhoneNumber='" + phone + "',Photo='" + is +"' WHERE ID= " + id + ";");*/
						if(Objects.equals(null, FileName))
						{
					    ps.setString(1, name);
					    ps.setString(2, Gender);
					    ps.setString(3, DOB);
					    ps.setString(4, pos);
					    ps.setString(5, address);
					    ps.setString(6, salary);
					    ps.setString(7, workdate);
					    ps.setString(8, phone);	
					    blob=new SerialBlob(img);
				    	ps.setBlob(9, blob);
				    	ps.executeUpdate();
					    }
						else
						{
							ps.setString(1, name);
						    ps.setString(2, Gender);
						    ps.setString(3, DOB);
						    ps.setString(4, pos);
						    ps.setString(5, address);
						    ps.setString(6, salary);
						    ps.setString(7, workdate);
						    ps.setString(8, phone);	
						    InputStream is=new FileInputStream(FileName);
					    	ps.setBlob(9, is);
					    	ps.executeUpdate();
						}
					   
						//System.out.println(ps);;
						//stm.execute(ps);
						JOptionPane.showMessageDialog(null,"updated");
					} catch (SQLException | FileNotFoundException  e1) {
						e1.printStackTrace();
					}
					//tbStaff.clearSelection();
					
					ClearControl();
					dtm.setRowCount(0);
					RefreshTable();
					btnEdit.setText("\u1780\u17C2\u1794\u17D2\u179A\u17C2");
					CloseControl();
					btnEdit.setIconTextGap(35);
				}
			}
		});
		btnEdit.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		btnEdit.setBounds(747, 180, 169, 39);
		frame.getContentPane().add(btnEdit);
		
		//JButton btnDelete = new JButton("\u179B\u17BB\u1794");
		btnDelete.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnDelete.setIconTextGap(55);
		btnDelete.setIcon(new ImageIcon("recycling-bin.png"));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			int choice=JOptionPane.showOptionDialog(null,"Do you want to delete?","Delete",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
			if(choice==JOptionPane.YES_OPTION)
			{
			 String sql="DELETE FROM tbstaff WHERE ID= "+ txtID.getText() +";";
			 try{
				 System.out.println(sql);;
					stm.execute(sql);
					JOptionPane.showMessageDialog(null,"Deleted");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			 	dtm.setRowCount(0);
			 	ClearControl();
			 	RefreshTable();
				}
			else
				return;
			}
		});
		btnDelete.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		btnDelete.setBounds(747, 231, 169, 39);
		frame.getContentPane().add(btnDelete);
		
		JButton btnExit = new JButton("\u1785\u17B6\u1780\u1785\u17C1\u1789");
		btnExit.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnExit.setIcon(new ImageIcon("close_new.png"));
		btnExit.setIconTextGap(20);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setFont(new Font("Khmer OS", Font.PLAIN, 15));
		btnExit.setBounds(747, 283, 169, 39);
		frame.getContentPane().add(btnExit);
		
		JPanel panel = new JPanel();
		panel.setBounds(181, 173, 212, 32);
		frame.getContentPane().add(panel);
	}
}
