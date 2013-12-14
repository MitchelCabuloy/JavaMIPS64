package models;

import javax.swing.table.AbstractTableModel;

import util.ByteUtils;

public class OpcodeTableModel extends AbstractTableModel {
	private Program program;
	private String[] columnNames;

	public OpcodeTableModel(Program program) {
		this.program = program;
		this.columnNames = new String[] { "Address", "Instruction",
				"Opcode (Hex)", "IR0..5", "IR6..10", "IR11..15", "IR16..31" };
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		return program.getCodes().size();
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Code code = program.getCodes().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return ByteUtils.getBinaryString(code.getAddress()).substring(28);
		case 1:
			return code.getCodeString();
		case 2:
			return String.format("%08x", code.getInstruction());
		case 3:
			return ByteUtils.getBinaryString(code.getOpcode()).substring(26);
		case 4:
			return ByteUtils.getBinaryString(code.getRS()).substring(27);
		case 5:
			return ByteUtils.getBinaryString(code.getRT()).substring(27);
		case 6:
			return ByteUtils.getBinaryString(code.getImmediate()).substring(16);
		}
		return null;
	}

}