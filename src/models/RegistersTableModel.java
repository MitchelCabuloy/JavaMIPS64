package models;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class RegistersTableModel extends AbstractTableModel {
	private long[] registers;

	public RegistersTableModel(long[] registers) {
		this.registers = registers;
	}

	@Override
	public String getColumnName(int column) {
		if (column == 0)
			return "Register";
		else
			return "Value";
	}

	@Override
	public int getRowCount() {
		return 32;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			return "R" + rowIndex;
		else
			return String.format("%016x", registers[rowIndex]).toUpperCase();
	}

}