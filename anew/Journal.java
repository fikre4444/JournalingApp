import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Journal extends JFrame {
    public Journal(){
        super("Journal");
        JPanel panel = new JPanel();
        JButton writeJournal = new JButton("Write on Journal");
        JButton readJournal = new JButton("Read Journal");
        JButton editJournal = new JButton("Edit Journal");
        writeJournal.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent eve){
                JournalEntry entry = new JournalEntry();
                entry.execute();
            }
        });
        
        readJournal.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent eve){
                JournalReader reader = new JournalReader();
                reader.execute();
            }
        });
        
        editJournal.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent eve){
                JournalEditor editor = new JournalEditor();
                editor.execute();
            }
        });
        
        panel.add(writeJournal);
        panel.add(readJournal);
        panel.add(editJournal);
        this.add(panel);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
    
    public static void main(String[] args){
        Journal jj = new Journal();
    }
}
