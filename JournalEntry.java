import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;


public class JournalEntry {
    JButton saveEntry, clearEntry;
    JTextArea tArea;
    BufferedWriter bfw;
    String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    
   
    
    /*public static void main(String[] args){
        JournalEntry jj = new JournalEntry();
        jj.execute();
    }*/
    
    public void execute(){
        
        JFrame frame = new JFrame("Journal Entry");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        JPanel panel = new JPanel();
        JPanel panelTop = new JPanel();
        JPanel panelBottom = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(panelTop);
        panel.add(panelBottom);
        
        tArea = new JTextArea(7, 30);
        Font font = new Font("sanserif", Font.BOLD, 20);
        tArea.setFont(font);
        tArea.setText("Type your entry here.");
        tArea.setLineWrap(true);
        tArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(tArea);
        panelTop.add(scroll);
        
        saveEntry = new JButton("Save Entry");
        clearEntry = new JButton("Clear Entry");
        saveEntry.addActionListener(new saveListener());
        clearEntry.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ev){
                    tArea.setText("");
                }
            });
        
        panelBottom.add(saveEntry);
        panelBottom.add(clearEntry);
        
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        
    }
    
    public void initializeEverything(){
        GregorianCalendar gcal = new GregorianCalendar(); //creates today date
        String month = months[gcal.get(Calendar.MONTH)];
        String todayPath = "Entries"+"/"+gcal.get(Calendar.YEAR)+"/"+month+"/";
        String today = "Entries"+"/"+gcal.get(Calendar.YEAR)+"/"+month+"/"+gcal.get(Calendar.DAY_OF_MONTH)+".txt";
        File fPath = new File(todayPath);
        File file = new File(today);
        if(!fPath.exists())
            fPath.mkdirs();
        
        try{
            file.createNewFile();  //creates a file in the directory specified if it doesn't exist
            FileWriter entryWrite = new FileWriter(file, true); //creates a file writer that appends to the already file.
            bfw = new BufferedWriter(entryWrite);  //creates a buffered writer
        }catch(IOException ex){
            System.out.println("cannot create a fileWriter");
        }
    }
    
    
   public class saveListener implements ActionListener {
       public void actionPerformed(ActionEvent eve){
           initializeEverything(); // creates everything that needs to be created after the user presses the save button.
           String entry = tArea.getText();
           
           GregorianCalendar g = new GregorianCalendar();
           String time = (g.getTime()).toString();
           try{
                String shifted = shift(time+" / "+entry); // shifts the thing that is about to be saved
                bfw.write(shifted);
                bfw.newLine(); 
                bfw.flush();
                tArea.setText("");
            }catch(IOException ex){
                tArea.setText("Cannot Save. I Don't have Permission");
            }
           
       }
   }
   public String shift(String word){
        String result="";
        for(int i = 0; i<word.length(); i++){
            int res = (int)word.charAt(i);
            res++;
            char c = (char)res;
            result += c;
        }
        return result;
    }
     
        
        
}
    
