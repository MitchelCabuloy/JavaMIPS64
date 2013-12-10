package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenu;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Color;
import javax.swing.UIManager;
import java.awt.Font;

public class Main extends JFrame {

	private JPanel contentPane;
	private JTable registers;
	private JTable specialRegisters;

	/**
	 * Create the frame.
	 */
	public Main() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setForeground(new Color(51, 51, 51));
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.setForeground(new Color(51, 51, 51));
		mnFile.add(mntmNew);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As");
		mntmSaveAs.setForeground(new Color(51, 51, 51));
		mnFile.add(mntmSaveAs);
		
		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setForeground(new Color(51, 51, 51));
		menuBar.add(mnEdit);
		
		JMenuItem mntmEditRegisters = new JMenuItem("Edit Registers");
		mntmEditRegisters.setForeground(new Color(51, 51, 51));
		mnEdit.add(mntmEditRegisters);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(51, 51, 51));
		contentPane.setBackground(SystemColor.control);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextArea code = new JTextArea();
		code.setBounds(35, 40, 250, 300);
		contentPane.add(code);
		
		JTextArea pipelineMap = new JTextArea();
		pipelineMap.setBounds(35, 420, 825, 180);
		contentPane.add(pipelineMap);
		
		JLabel lblNewLabel = new JLabel("Registers");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel.setForeground(new Color(51, 51, 51));
		lblNewLabel.setBounds(320, 20, 100, 15);
		contentPane.add(lblNewLabel);
		
		JLabel label = new JLabel("Code");
		label.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label.setForeground(new Color(51, 51, 51));
		label.setBounds(35, 20, 100, 15);
		contentPane.add(label);
		
		JLabel lblPipelineMap = new JLabel("Pipeline Map");
		lblPipelineMap.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblPipelineMap.setForeground(new Color(51, 51, 51));
		lblPipelineMap.setBounds(35, 400, 100, 15);
		contentPane.add(lblPipelineMap);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(320, 40, 250, 300);
		contentPane.add(scrollPane);
		
		registers = new JTable();
		registers.setEnabled(false);
		registers.setForeground(new Color(51, 51, 51));
		registers.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		registers.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
			},
			new String[] {
				"Registers", "Value"
			}
		));
		scrollPane.setViewportView(registers);
		
		JButton btnNewButton = new JButton("Submit");
		btnNewButton.setForeground(new Color(51, 51, 51));
		btnNewButton.setBackground(UIManager.getColor("Button.background"));
		btnNewButton.setBounds(35, 350, 89, 23);
		contentPane.add(btnNewButton);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(605, 40, 250, 144);
		contentPane.add(scrollPane_1);
		
		specialRegisters = new JTable();
		specialRegisters.setEnabled(false);
		specialRegisters.setForeground(new Color(51, 51, 51));
		specialRegisters.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		specialRegisters.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
			},
			new String[] {
				"Registers", "Value"
			}
		));
		specialRegisters.setBounds(605, 40, 250, 144);
		scrollPane_1.setViewportView(specialRegisters);
		
		JLabel lblSpecialRegisters = new JLabel("Special Registers");
		lblSpecialRegisters.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSpecialRegisters.setForeground(new Color(51, 51, 51));
		lblSpecialRegisters.setBounds(605, 20, 111, 15);
		contentPane.add(lblSpecialRegisters);
	}
}
