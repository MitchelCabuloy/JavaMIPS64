package models;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

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
		return memory.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		int i = 0;
		for (Entry<Integer, Byte> entry : memory.entrySet()) {
			if (i == rowIndex) {
				if (columnIndex == 0) {
					return entry.getKey();
				} else {
					return String.format("%02x",entry.getValue()).toUpperCase();
				}
			}
			i++;
		}
		return null;
	}

}