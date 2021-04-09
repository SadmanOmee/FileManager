package offline;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

 class Cellrenderer extends JLabel implements ListCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof File) {
            File file = (File) value;
            setText(file.getName());
            setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setPreferredSize(new Dimension(200,100));
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
        }
        return this;
    }
}
