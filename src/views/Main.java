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
	private JMenu mnFile;
	private JTextArea code;
	private JTable opcode;
	private JTable pipelineMap;
	private JTable memory;
	private JMenuItem mntmLoadProgram;
	private JMenuItem mntmSaveProgram;

	/**
	 * Create the frame.
	 */
	public Main() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1185, 685);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnFile = new JMenu("File");
		mnFile.setForeground(new Color(51, 51, 51));
		menuBar.add(mnFile);
		
		mntmLoadProgram = new JMenuItem("Load Program");
		mnFile.add(mntmLoadProgram);
		
		mntmSaveProgram = new JMenuItem("Save Program");
		mnFile.add(mntmSaveProgram);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(51, 51, 51));
		contentPane.setBackground(SystemColor.control);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		code = new JTextArea();
		code.setBounds(30, 44, 250, 150);
		contentPane.add(code);

		JLabel lblRegisters = new JLabel("Registers");
		lblRegisters.setBounds(30, 230, 100, 15);
		lblRegisters.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblRegisters.setForeground(new Color(51, 51, 51));
		contentPane.add(lblRegisters);

		JLabel lblCode = new JLabel("Code");
		lblCode.setBounds(30, 24, 100, 15);
		lblCode.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCode.setForeground(new Color(51, 51, 51));
		contentPane.add(lblCode);

		JLabel lblPipelineMap = new JLabel("Pipeline Map");
		lblPipelineMap.setBounds(30, 410, 100, 15);
		lblPipelineMap.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblPipelineMap.setForeground(new Color(51, 51, 51));
		contentPane.add(lblPipelineMap);

		JScrollPane spRegisters = new JScrollPane();
		spRegisters.setBounds(30, 250, 350, 150);
		contentPane.add(spRegisters);

		registers = new JTable();
		registers.setEnabled(false);
		registers.setForeground(new Color(51, 51, 51));
		registers.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		registers.setModel(new DefaultTableModel(
				new Object[][] { { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null }, },
				new String[] { "Registers", "Value" }));
		spRegisters.setViewportView(registers);

		JButton btnLoad = new JButton("Load");
		btnLoad.setBounds(30, 200, 80, 20);
		btnLoad.setForeground(new Color(51, 51, 51));
		btnLoad.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(btnLoad);

		JScrollPane spSpecialRegisters = new JScrollPane();
		spSpecialRegisters.setBounds(415, 250, 350, 150);
		contentPane.add(spSpecialRegisters);

		specialRegisters = new JTable();
		specialRegisters.setEnabled(false);
		specialRegisters.setForeground(new Color(51, 51, 51));
		specialRegisters.setBorder(new BevelBorder(BevelBorder.LOWERED, null,
				null, null, null));
		specialRegisters.setModel(new DefaultTableModel(new Object[][] {
				{ null, null }, { null, null }, { null, null }, { null, null },
				{ null, null }, { null, null }, { null, null }, { null, null },
				{ null, null }, }, new String[] { "Registers", "Value" }));
		specialRegisters.setBounds(605, 40, 250, 144);
		spSpecialRegisters.setViewportView(specialRegisters);

		JLabel lblSpecialRegisters = new JLabel("Special Registers");
		lblSpecialRegisters.setBounds(415, 230, 111, 15);
		lblSpecialRegisters.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSpecialRegisters.setForeground(new Color(51, 51, 51));
		contentPane.add(lblSpecialRegisters);
		
		JLabel lblMemory = new JLabel("Memory");
		lblMemory.setBounds(800, 230, 111, 15);
		lblMemory.setForeground(new Color(51, 51, 51));
		lblMemory.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPane.add(lblMemory);
		
		JScrollPane spMemory = new JScrollPane();
		spMemory.setBounds(800, 250, 350, 150);
		contentPane.add(spMemory);
		
		memory = new JTable();
		memory.setEnabled(false);
		memory.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		memory.setModel(new DefaultTableModel(
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
			},
			new String[] {
				"Address", "Value"
			}
		));
		spMemory.setViewportView(memory);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(300, 44, 850, 150);
		contentPane.add(scrollPane);
		
		opcode = new JTable();
		opcode.setEnabled(false);
		opcode.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		opcode.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
			},
			new String[] {
				"Address", "Instruction", "Opcode (Hex)", "IR0..5", "IR6..10", "IR11..15", "IR16..31"
			}
		));
		scrollPane.setViewportView(opcode);
		
		JLabel lblOpcode = new JLabel("Opcode Translation");
		lblOpcode.setForeground(new Color(51, 51, 51));
		lblOpcode.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblOpcode.setBounds(300, 24, 100, 15);
		contentPane.add(lblOpcode);
		
		JScrollPane spPipeline = new JScrollPane();
		spPipeline.setBounds(30, 430, 1120, 175);
		contentPane.add(spPipeline);
		
		pipelineMap = new JTable();
		pipelineMap.setShowGrid(false);
		pipelineMap.setEnabled(false);
		pipelineMap.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
			},
			new String[] {
				"Instruction", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
			}
		));
		spPipeline.setViewportView(pipelineMap);
	}

	public JMenu getMnFile() {
		return mnFile;
	}

	public JTextArea getCode() {
		return code;
	}

	public JTable getRegisters() {
		return registers;
	}

	public JTable getSpecialRegisters() {
		return specialRegisters;
	}
	public JTable getOpcode() {
		return opcode;
	}
	public JTable getMemory() {
		return memory;
	}
	public JTable getPipelineMap() {
		return pipelineMap;
	}
	public JMenuItem getMntmLoadProgram() {
		return mntmLoadProgram;
	}
	public JMenuItem getMntmSaveProgram() {
		return mntmSaveProgram;
	}
}
