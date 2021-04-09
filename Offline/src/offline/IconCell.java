package offline;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

class IconCell
  extends    JLabel 
  implements TreeCellRenderer
{

    
  protected Color m_textSelectionColor;
  protected Color m_textNonSelectionColor;
  protected Color m_bkSelectionColor;
  protected Color m_bkNonSelectionColor;
  protected Color m_borderSelectionColor;

  protected boolean m_selected;

  public IconCell()
  {
    super();
    m_textSelectionColor = UIManager.getColor(
      "Tree.selectionForeground");
    m_textNonSelectionColor = UIManager.getColor(
      "Tree.textForeground");
    m_bkSelectionColor = UIManager.getColor(
      "Tree.selectionBackground");
    m_bkNonSelectionColor = UIManager.getColor(
      "Tree.textBackground");
    m_borderSelectionColor = UIManager.getColor(
      "Tree.selectionBorderColor");
    setOpaque(false);
  }

  public Component getTreeCellRendererComponent(JTree tree, 
    Object value, boolean sel, boolean expanded, boolean leaf, 
    int row, boolean hasFocus) 
    
  {
    DefaultMutableTreeNode node = 
      (DefaultMutableTreeNode)value;
    Object obj = node.getUserObject();
    setText(obj.toString());

                if (obj instanceof Boolean)
                  setText("Retrieving data...");

    if (obj instanceof IconData)
    {
      IconData idata = (IconData)obj;
      if (expanded)
        setIcon(idata.getExpandedIcon());
      else
        setIcon(idata.getIcon());
    }
    else
      setIcon(null);

    setFont(tree.getFont());
    setForeground(sel ? m_textSelectionColor : 
      m_textNonSelectionColor);
    setBackground(sel ? m_bkSelectionColor : 
      m_bkNonSelectionColor);
    m_selected = sel;
    return this;
  }
}
