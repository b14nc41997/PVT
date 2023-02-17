
package Vista;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ColorCelda extends JTable{
    
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int ColumnIndex){
        Component componente = super.prepareRenderer(renderer, rowIndex, ColumnIndex);
        
        if(getValueAt(rowIndex, ColumnIndex).getClass().equals(Integer.class)){
            double valor = Double.parseDouble(this.getValueAt(rowIndex, 8).toString());
            
            if(valor < 330){
                componente.setBackground(Color.green);
                componente.setForeground(Color.black);
            }else{
                componente.setBackground(Color.white);
                componente.setForeground(Color.black);
            }                        
        }
        return componente;
    }
    
}
