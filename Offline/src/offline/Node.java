package offline;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

class Node
{
  protected File m_file;
  DefaultTableModel model;
  JList list;
  
  public Node(File file)
  {
    m_file = file;
  }

  public File getFile() 
  { 
    return m_file;
  }
  
  public DefaultTableModel getModel() 
  { 
    return model;
  }

  public String toString() 
  { 
    return m_file.getName().length() > 0 ? m_file.getName() : 
      m_file.getPath();
  }

  public boolean expand(DefaultMutableTreeNode parent,DefaultTableModel newmodel,JList llist)
  {
      model=newmodel;
      
      list=llist;
    DefaultMutableTreeNode flag = 
      (DefaultMutableTreeNode)parent.getFirstChild();
    if (flag==null)
      return false;
    Object obj = flag.getUserObject();
    if (!(obj instanceof Boolean))
      return false;

    parent.removeAllChildren();
    int number=model.getRowCount();
    for(int i=0;i<number;i++){
        model.removeRow(0);
    }

    File[] files = listFiles();
    list.setListData(files);
    if (files == null)
      return true;

    Vector v = new Vector();

    for (int k=0; k<files.length; k++)
    {
      File f = files[k];
      

      Node newNode = new Node(f);
      
      boolean isAdded=false;
      for (int i=0;i<v.size();i++)
      {
        Node nd=(Node)v.elementAt(i);
        if (newNode.compareTo(nd) < 0)
        {
          v.insertElementAt(newNode, i);
          isAdded=true;
          break;
        }
      }
      if (!isAdded)
        v.addElement(newNode);
    }
    
    Object[] row=new Object[4];

    for (int i=0;i<v.size();i++)
    {
      Node nd = (Node)v.elementAt(i);
      IconData idata = new IconData(Offline.IFOLD, 
        Offline.IEXFOLD, nd);
      row[1]=nd;
      File f=nd.getFile();
      row[0]=FileSystemView.getFileSystemView().getSystemIcon(f);
      long last=f.lastModified();
      Date lastdate=new Date(last);
      row[3]=lastdate;
      if(f.isDirectory()){
          long fsize=0;
          row[2]=fsize;
      }
      else{
          long fsize=f.length();
          row[2]=fsize;
      }
      
      DefaultMutableTreeNode node = new 
        DefaultMutableTreeNode(idata);
      parent.add(node);
      model.addRow(row);
        
      if (nd.hasSubDirs())
        node.add(new DefaultMutableTreeNode( 
          new Boolean(true) ));
    }

    return true;
  }

  public boolean hasSubDirs()
  {
    File[] files = listFiles();
    if (files == null){
      return false;
    }
    else{
        return true;
    }
  }
  
  public int compareTo(Node toCompare)
  { 
    return  m_file.getName().compareToIgnoreCase(
      toCompare.m_file.getName() ); 
  }

  protected File[] listFiles()
  {
    if (!m_file.isDirectory())
      return null;
    try
    {
      return m_file.listFiles();
    }
    catch (Exception ex)
    {
      JOptionPane.showMessageDialog(null, 
        "Error reading directory "+m_file.getAbsolutePath(),
        "Warning", JOptionPane.WARNING_MESSAGE);
      return null;
    }
  }
}



