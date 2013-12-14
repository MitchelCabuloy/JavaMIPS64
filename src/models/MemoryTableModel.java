package models;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class MemoryTableModel extends AbstractTableModel {
	private HashMap<Integer, Byte> memory;

	public MemoryTableModel(HashMap<Integer, Byte> memory) {
		this.memory = memory;
	}

	@Override
	public String getColumnName(int column) {
		if (column == 0)
			return "Address";
		else
			return "Value";
	}

	@Override
	public int getRowCount() {
		return memory.keySet().size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object[][] tableData = new Object[memory.keySet().size()][2];

		int index = 0;
		for (Integer key : memory.keySet()) {
			tableData[index][0] = key;
			tableData[index][1] = String.format("%02x", memory.get(key))
					.toUpperCase();
			index++;
		}

		return tableData[rowIndex][columnIndex];
	}

}