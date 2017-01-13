
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import java.awt.event.*;
import java.awt.print.PrinterException;

import javax.swing.filechooser.FileFilter;

public class Notepad extends JFrame implements ActionListener {
	
	JTextArea ta;
	JMenuBar mb;
	JMenu file, edit, help;
	JMenuItem cut, copy, paste, open, exit, save, saveAs, undo, redo, selectAll, print;
	Container con;
	JFileChooser jf;
	JLabel lineCount;
	JScrollPane jS = new JScrollPane();
	
	boolean isSave = false;
	
	UndoManager undoManager = new UndoManager();
	
	public void input() throws Exception{
		
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		
		this.setTitle("Notepad");
		
		con=getContentPane();
		con.setLayout(new BorderLayout());
		
		setVisible(true);
		
		mb = new JMenuBar();
		
		ta = new JTextArea();
		
		file = new JMenu("File");
		edit = new JMenu("Edit");
		help = new JMenu("About");
		
		//File menu items
		open = new JMenuItem("Open");
		
		exit = new JMenuItem("Exit");
		save = new JMenuItem("Save");
		saveAs = new JMenuItem("Save as");
		print = new JMenuItem("Print...");
		
		
		//Edit menu items
		cut = new JMenuItem("Cut");
		copy = new JMenuItem("Copy");
		paste = new JMenuItem("Paste");
		undo = new JMenuItem("Undo");
		redo = new JMenuItem("Redo");
		selectAll = new JMenuItem("SelectAll");
		
		//Adding menu bar
		mb.add(file);
		mb.add(edit);
		//mb.add(help);
		
		//Adding file options
		file.add(open);
		file.add(save);
		file.add(saveAs);
		file.add(print);
		file.add(exit);
		
		//Adding edit options
		edit.add(cut);
		edit.add(copy);
		edit.add(paste);
		edit.add(selectAll);
		edit.add(undo);
		edit.add(redo);
		
		con.add(mb, BorderLayout.NORTH);
		
		this.setBounds(150, 100, 800, 500);
		setDefaultCloseOperation(3);
		
		open.addActionListener(this);
		save.addActionListener(this);
		saveAs.addActionListener(this);
		print.addActionListener(this);
		exit.addActionListener(this);
		copy.addActionListener(this);
		cut.addActionListener(this);
		paste.addActionListener(this);
		undo.addActionListener(this);
		redo.addActionListener(this);
		selectAll.addActionListener(this);
		
		/* Undo and Redo */
		
		ta.getDocument().addUndoableEditListener(new UndoableEditListener() {
		          public void undoableEditHappened(UndoableEditEvent e) {
		            undoManager.addEdit(e.getEdit());
		          }
		 });
		
       /*----------------------------------------Line counter----------------------------------------*/		
		
		ta.addCaretListener(new CaretListener() {

			public void caretUpdate(CaretEvent e) {
                JTextArea editArea = (JTextArea)e.getSource();

                int linenum = 1;
                int columnnum = 1;

                try {
                    int caretpos = editArea.getCaretPosition();
                    linenum = editArea.getLineOfOffset(caretpos);

                    columnnum = caretpos - editArea.getLineStartOffset(linenum);

                    linenum += 1;
                }
                catch(Exception ex) { }

                updateStatus(linenum, columnnum);
            }
        });

		con.add(new JScrollPane(ta), BorderLayout.CENTER);

		lineCount = new JLabel();
		con.add(lineCount, BorderLayout.SOUTH);

        updateStatus(1,0);
    }

    private void updateStatus(int linenumber, int columnnumber) {
    	lineCount.setText(" Ln  "+linenumber+", Col  "+columnnumber);
    }
		
		/*-------------------------------------------------------------------------------------------------------*/		
			

	
	
	public static void main(String...strings) throws Exception{
		Notepad obj = new Notepad();
		obj.input();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String data = new String();
		
		if(e.getSource()==open){
			jf = new JFileChooser(".");
			
			/***************************************/
			
			jf.addChoosableFileFilter(new FileFilter() {
				 
			    public String getDescription() {
			    	String file = "Text Documents (*.txt)"; 
			        return file;
			    }
			 
			    public boolean accept(File f) {
			        if (f.isDirectory()) {
			            return true;
			        } else {
			            return f.getName().toLowerCase().endsWith(".txt");
			        }
			    }
			});
			
			/***************************************/
			
			int result = jf.showOpenDialog(this);
			if(result==jf.APPROVE_OPTION){
				File showFile = jf.getSelectedFile();
				try {
					FileReader fw = new FileReader(showFile);
					BufferedReader br = new BufferedReader(fw);
					
					ta.setText("");
					
					while((data=br.readLine())!=null){
						ta.append(data+"\n");
					}
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "File is not selected");
			}
		}
			
		
		else if(e.getSource()==print){
			try {
				
				boolean complete = ta.print();
				if(complete){
					JOptionPane.showMessageDialog(null, "Printing is done","Print", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Error in printing","Print", JOptionPane.ERROR_MESSAGE);
				}
				
			} catch (PrinterException e1) {
				e1.printStackTrace();
			}
		}
		
		else if(e.getSource()==save){
			jf =new JFileChooser();
			int result = jf.showSaveDialog(this);
			
			if(result==jf.APPROVE_OPTION){
				try {
					File saveFile = jf.getSelectedFile();
					PrintWriter out = new PrintWriter(new FileOutputStream(saveFile));
					String[] lines=ta.getText().split("\n");
					
					for(int i=0 ;i<lines.length; i++){
						out.println(lines[i]);
					}
					out.close();
					
					isSave = true;
					
				} catch(Exception Ex){
					System.out.println(Ex);
				}	
			}
		}
		
		
		else if(e.getSource()==saveAs){
			jf =new JFileChooser();
			int result = jf.showSaveDialog(this);
			
			if(result==jf.APPROVE_OPTION){
				try {
					File saveFile = jf.getSelectedFile();
					PrintWriter out = new PrintWriter(new FileOutputStream(saveFile));
					String[] lines=ta.getText().split("\n");
					
					for(int i=0 ;i<lines.length; i++){
						out.println(lines[i]);
					}
					out.close();
					isSave = true;
					
				} catch(Exception Ex){
					System.out.println(Ex);
				}	
			}
		}
		
		
		else if(e.getSource()==exit){
			
			if(!(ta.getText()).equals("")){
				
				if(!(isSave)){
					int answer = JOptionPane.showConfirmDialog(null, "You didnot save your file. Do you want to close ?");
				    if (answer == JOptionPane.YES_OPTION) {
				    	System.exit(0);
				    } else if (answer == JOptionPane.NO_OPTION) {
				      // User clicked NO.
				    }
				} else {
					System.exit(0);
				}
				
			} else {
				int answer = JOptionPane.showConfirmDialog(null, "Do you want to exit ?");
			    if (answer == JOptionPane.YES_OPTION) {
			    	System.exit(0);
			    } else if (answer == JOptionPane.NO_OPTION) {
			      // User clicked NO.
			    }
			}
		}
	
			
		else if(e.getSource()==cut){
			ta.cut();
		}
		
		else if(e.getSource()==copy){
			ta.copy();
		}
		
		else if(e.getSource()==paste){
			ta.paste();
		}
		
		else if(e.getSource()==selectAll){
			ta.selectAll();
		}
		
		else if(e.getSource()==undo){
			try {
		          undoManager.undo();
		    } catch (CannotRedoException cre) {
		          cre.printStackTrace();
		    }
		}
		
		else if(e.getSource()==redo){
			try {
		          undoManager.redo();
		    } catch (CannotRedoException cre) {
		          cre.printStackTrace();
		    }
		}
	}		
}

