package offline;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;


public class Offline extends JFrame{
    
    public static final ImageIcon ICOM=new ImageIcon("MyComputer.jpg");
    public static final ImageIcon IDRIVE=new ImageIcon("driveicon.jpg");   
    public static final ImageIcon IFOLD=new ImageIcon("foldericon.jpg");
    public static final ImageIcon IEXFOLD=new ImageIcon("expandfoldericon.jpg");

    public JTree  newtree;
    public DefaultTreeModel newmodel;
    public JLabel directo;
    JTable newtable;
    JList list;
    JButton btn;
    JButton newbtn;
    private JTextField direc;
    int viewflag=0;
    JScrollPane newpane;
    
    public Offline()
  {
    setLayout(new FlowLayout());
   
    directo=new JLabel();
    directo.setText("Directory: ");
    add(directo);
    btn=new JButton("Tiles View");
    add(btn);
    btn.addActionListener(new Action());
    
    direc=new JTextField(50);
    //direc.setText("");
    add(direc);
    
    String userdir=System.getProperty("user.dir");
    list = new JList(new File(userdir).listFiles());
    
    list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    list.setCellRenderer(new Cellrenderer());
    list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    list.setVisibleRowCount(-1);

    Object[] column={"Icon","Name","Size","Last Modified"};

    DefaultMutableTreeNode dmtn=new DefaultMutableTreeNode(new IconData(ICOM,null,"My Computer"));

    DefaultMutableTreeNode node;
    File[] roots=File.listRoots();
    
    Object[][] row=new Object[roots.length][4];
    for (int i=0;i<roots.length;i++)
    {
        node=new DefaultMutableTreeNode(new IconData(IDRIVE,null,new Node(roots[i])));
      
      row[i][0]=IDRIVE;
      row[i][1]=roots[i];
      row[i][2]=roots[i].getTotalSpace();
      long last=roots[i].lastModified();
      Date lastdate=new Date(last);
      row[i][3]=lastdate;
      dmtn.add(node);
      node.add(new DefaultMutableTreeNode(new Boolean(true)));
    }
    
    
    DefaultTableModel model=new DefaultTableModel(row,column);
    
    newtable=new JTable(model){
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }    
        };
    newtable.setRowHeight(40);
    
    newmodel = new DefaultTreeModel(dmtn);
    newtree = new JTree(newmodel); 

    TreeCellRenderer rend = new IconCell();
    newtree.setCellRenderer(rend);

    newtree.addTreeExpansionListener(new DirExpansionListener(model));

    JScrollPane apane = new JScrollPane();
    apane.getViewport().add(newtree);
    apane.setPreferredSize(new Dimension(200,300));
    add(apane);

    WindowListener wndCloser = new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) 
      {
        System.exit(0);
      }
    };
    
    newpane=new JScrollPane(newtable);
    newpane.setPreferredSize(new Dimension(500,500));
    
     JPanel gui=new JPanel(new BorderLayout(5,5));
     gui.setBorder(new EmptyBorder(5,5,5,5));
     JSplitPane splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,apane,newpane);
     gui.add(splitPane, BorderLayout.CENTER);
     gui.add(btn, BorderLayout.NORTH);
     //gui.add(direc, BorderLayout.SOUTH);
     add(gui);
     setVisible(true);
  }
    
    class Action implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            if(viewflag==0){
                newpane.setViewportView(list);
                btn.setText("Table View");
                viewflag=1;
            }
            else{
                newpane.setViewportView(newtable);
                btn.setText("Tiles View");
                viewflag=0;
            }
        }
    
    }
    
    DefaultMutableTreeNode getTreeNode(TreePath tpath)
  {
      //Object[] v=path.getPath();
     //direc.setText(""+v);
    return (DefaultMutableTreeNode)(tpath.getLastPathComponent());
  }
    
    Node getFileNode(DefaultMutableTreeNode node)
  {
    if (node == null){
      return null;}
    Object obj = node.getUserObject();
    if (obj instanceof IconData){
      obj = ((IconData)obj).getObject();}
    if (obj instanceof Node){
      return (Node)obj;}
    else{
      return null;}
  }
    
    class DirExpansionListener implements TreeExpansionListener
    {
        DefaultTableModel model;
        public DirExpansionListener(DefaultTableModel newmodel){
            model=newmodel;
        }
        public void treeExpanded(TreeExpansionEvent event)
        {
            final DefaultMutableTreeNode node = getTreeNode(
                event.getPath());
            direc.setText(event.getPath().toString());
            
            final Node fnode = getFileNode(node);

            if(fnode!=null&& fnode.expand(node,model,list))
            {
                newmodel.reload(node);
            }
        }

        public void treeCollapsed(TreeExpansionEvent event) {}
    }

    public static void main(String[] args) {
        // TODO code application logic here
        Offline fe=new Offline();
        fe.setTitle("File Explorer");
        fe.setSize(900,650);
        fe.setVisible(true);
        fe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
