package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class PipelineMapTableModel extends AbstractTableModel{

	private ArrayList<Code> codes;
	private ArrayList<ArrayList<Object>> pipelineMap;
	private int maxCycles = 0;
	
	public PipelineMapTableModel(ArrayList<Code> codes){
		this.codes = codes;
		this.pipelineMap = new ArrayList<ArrayList<Object>>();
	}
	
	public void updateData(int clockCycle, HashMap<Long, String> pipeline){
		maxCycles = clockCycle + 2;
		
		for(Entry<Long, String> entry : pipeline.entrySet()){
			boolean found = false;
			
			for(ArrayList<Object> array : pipelineMap){
				
				// If it's already there, add new columns
				if(((Long) array.get(0)).intValue() == entry.getKey().intValue()){
//					array.ensureCapacity(clockCycle + 2); 
//					for(int i = 0; i < clockCycle + 2; i++)
//						if(array.get(i) == null)
//						tempArray.add(null);
					
					array.add(clockCycle + 2, entry.getValue());
					found = true;
				}
			}
			
			// If an instruction is not in the map, add
			if(!found){				
				ArrayList<Object> tempArray = new ArrayList<Object>(clockCycle + 3);
				
				for(int i = 0; i < clockCycle + 3; i++)
					tempArray.add(null);
				
				// Add key and first value
				
				for(Code code : codes){
					if(code.getInstruction() == entry.getKey().intValue()){
						tempArray.set(1, code.getCodeString());
						break;
					}
				}
				tempArray.set(0, entry.getKey());
				tempArray.set(clockCycle + 2, entry.getValue());
				
				pipelineMap.add(tempArray);
			}
		}
	}
	
	@Override
	public String getColumnName(int column) {
		if(column == 0){
			return "Opcode";
		}
		if(column == 1){
			return "Instruction";
		}
		return String.format("%d", column - 1);
	}
	
	@Override
	public int getRowCount() {
		return pipelineMap.size();
	}

	@Override
	public int getColumnCount() {
		return maxCycles;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			if(columnIndex == 0){
				return String.format("%08X", (Long)pipelineMap.get(rowIndex).get(columnIndex));
			}
			return pipelineMap.get(rowIndex).get(columnIndex);
		} catch (Exception e) {
			return null;
		}
	}

}
