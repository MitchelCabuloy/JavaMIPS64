package models;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class SpecialRegistersTableModel extends AbstractTableModel {
	HashMap<String, Long> registers;

	public SpecialRegistersTableModel(HashMap<String, Long> registers) {
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
		return 12;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			switch (rowIndex) {
			case 0:
				return "PC";
			case 1:
				return "IF/ID.IR";
			case 2:
				return "ID/EX.A";
			case 3:
				return "ID/EX.B";
			case 4:
				return "ID/EX.Imm";
			case 5:
				return "ID/EX.IR";
			case 6:
				return "EX/MEM.ALUOutput";
			case 7:
				return "EX/MEM.IR";
			case 8:
				return "EX/MEM.B";
			case 9:
				return "MEM/WB.ALUOutput";
			case 10:
				return "MEM/WB.LMD";
			case 11:
				return "MEM/WB.IR";
			}
		} else {
			switch (rowIndex) {
			case 0:
				return String.format("%016x", registers.get("PC"))
						.toUpperCase();
			case 1:
				return String.format("%016x", registers.get("IF/ID.IR"))
						.toUpperCase();
			case 2:
				return String.format("%016x", registers.get("ID/EX.A"))
						.toUpperCase();
			case 3:
				return String.format("%016x", registers.get("ID/EX.B"))
						.toUpperCase();
			case 4:
				return String.format("%016x", registers.get("ID/EX.IMM"))
						.toUpperCase();
			case 5:
				return String.format("%016x", registers.get("ID/EX.IR"))
						.toUpperCase();
			case 6:
				return String
						.format("%016x", registers.get("EX/MEM.ALUOUTPUT"))
						.toUpperCase();
			case 7:
				return String.format("%016x", registers.get("EX/MEM.IR"))
						.toUpperCase();
			case 8:
				return String.format("%016x", registers.get("EX/MEM.B"))
						.toUpperCase();
			case 9:
				return String
						.format("%016x", registers.get("MEM/WB.ALUOUTPUT"))
						.toUpperCase();
			case 10:
				return String.format("%016x", registers.get("MEM/WB.LMD"))
						.toUpperCase();
			case 11:
				return String.format("%016x", registers.get("MEM/WB.IR"))
						.toUpperCase();
			}
		}

		return null;
	}
}