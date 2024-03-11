import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JournalReader {
    ArrayList<String> oneDayEntry;
    JTextArea tArea;
    JButton load, previousEntry, nextEntry, firstEntry, lastEntry;
    JButton loadToday;
    String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    int currentEntryIndex = 0;
    
    
    /*public static void main(String[] args){
        JournalReader j = new JournalReader();
        j.execute();
    }*/
    
    public void execute(){
        JFrame frame = new JFrame("Journal Reader");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        tArea = new JTextArea(12, 35);
        Font font = new Font("sanserif", Font.BOLD, 17);
        tArea.setFont(font);
        tArea.setText("Load an entry file by pressing the \"Load\" button");
        tArea.setEditable(false);
        tArea.setLineWrap(true);
        tArea.setWrapStyleWord(true);
        JScrollPane jsp = new JScrollPane(tArea);
        frame.add(BorderLayout.CENTER, jsp);
        
        JPanel panelButton = new JPanel();
        load = new JButton("Load");
        nextEntry = new JButton(">>");
        previousEntry = new JButton("<<");
        firstEntry = new JButton("First Entry");
        lastEntry = new JButton("Last Entry");
        
        load.addActionListener(new LoadListener());
        nextEntry.addActionListener(new NextListener());
        previousEntry.addActionListener(new PreviousListener());
        firstEntry.addActionListener(new FirstListener());
        lastEntry.addActionListener(new LastListener());
        
        nextEntry.setEnabled(false);
        previousEntry.setEnabled(false);
        firstEntry.setEnabled(false);
        lastEntry.setEnabled(false);
        
        loadToday = new JButton("Today's Entries");
        loadToday.addActionListener(new TodayListener());
        
        
        panelButton.add(loadToday);
        panelButton.add(load);
        panelButton.add(previousEntry);
        panelButton.add(nextEntry);
        panelButton.add(firstEntry);
        panelButton.add(lastEntry);
        
        frame.add(BorderLayout.SOUTH, panelButton);
        
        
        
        frame.pack();
        frame.setVisible(true);
        
        oneDayEntry = new ArrayList<>();
    }
 
    public class TodayListener implements ActionListener {
        public void actionPerformed(ActionEvent eve){
            oneDayEntry.clear();
            GregorianCalendar gcal = new GregorianCalendar(); //creates today date
            String month = months[gcal.get(Calendar.MONTH)];
            String todayPath = "Entries"+"/"+gcal.get(Calendar.YEAR)+"/"+month+"/";
            String today = "Entries"+"/"+gcal.get(Calendar.YEAR)+"/"+month+"/"+gcal.get(Calendar.DAY_OF_MONTH)+".txt";
            File fPath = new File(todayPath);
            File file = new File(today);
            
            if (!file.exists()){
                tArea.setText("No Entries Today.");
            }
            else {
                try {
                    currentEntryIndex = 0;
                    FileReader fr = new FileReader(file);
                    BufferedReader bfr = new BufferedReader(fr);
                    String oneEntry = null;
                    while((oneEntry = bfr.readLine())!=null){
                        oneDayEntry.add(oneEntry);
                    }
                    setTextArea(oneDayEntry.get(0));
                    if(oneDayEntry.size()!=1){
                        nextEntry.setEnabled(true);
                        lastEntry.setEnabled(true); 
                    }
                    firstEntry.setEnabled(false);
                    previousEntry.setEnabled(false);
                }catch(IOException ex){
                    System.out.println("can't find the file");
                }
            }
        }
    }
                    
                
            
    
    public class LoadListener implements ActionListener {
        public void actionPerformed(ActionEvent eve){
            
            previousEntry.setEnabled(false);
            firstEntry.setEnabled(false);
            File file = new File("Entries");
            JFileChooser jfc;
            if(file.exists())
                jfc = new JFileChooser(file.getAbsolutePath()); 
            else jfc = new JFileChooser();
                
            int result = jfc.showOpenDialog(null);
            
            if (result == JFileChooser.APPROVE_OPTION){
                File entry = jfc.getSelectedFile();
                oneDayEntry.clear();
                currentEntryIndex = 0;
                try {
                FileReader fr = new FileReader(entry);
                BufferedReader bfr = new BufferedReader(fr);
                String oneEntry = null;
                while((oneEntry = bfr.readLine())!=null){
                    oneDayEntry.add(oneEntry);
                }
                setTextArea(oneDayEntry.get(0));
                if(oneDayEntry.size()!=1){
                    nextEntry.setEnabled(true);
                    lastEntry.setEnabled(true); 
                }
                                         
                }catch(IOException ex){
                System.out.println("can't find the file");
                }
            }
        }
        
    }
    
    public class NextListener implements ActionListener {
        public void actionPerformed(ActionEvent eve){
            currentEntryIndex++;
            setTextArea(oneDayEntry.get(currentEntryIndex));
            previousEntry.setEnabled(true);
            firstEntry.setEnabled(true);
            if (currentEntryIndex == (oneDayEntry.size()-1)){
                lastEntry.setEnabled(false);
                nextEntry.setEnabled(false);
            }
        }
    }
    
    public class PreviousListener implements ActionListener {
        public void actionPerformed(ActionEvent eve){
            currentEntryIndex--;
            setTextArea(oneDayEntry.get(currentEntryIndex));
            nextEntry.setEnabled(true);
            lastEntry.setEnabled(true);
            if (currentEntryIndex == 0){
                firstEntry.setEnabled(false);
                previousEntry.setEnabled(false);
            }
        }
    }
    
    public class FirstListener implements ActionListener {
        public void actionPerformed(ActionEvent eve){
            currentEntryIndex = 0;
            setTextArea(oneDayEntry.get(currentEntryIndex));
            firstEntry.setEnabled(false);
            previousEntry.setEnabled(false);
            nextEntry.setEnabled(true);
            lastEntry.setEnabled(true);
        }
    }
    
    public class LastListener implements ActionListener {
        public void actionPerformed(ActionEvent eve){
            currentEntryIndex = oneDayEntry.size() - 1;
            setTextArea(oneDayEntry.get(currentEntryIndex));
            nextEntry.setEnabled(false);
            lastEntry.setEnabled(false);
            firstEntry.setEnabled(true);
            previousEntry.setEnabled(true);
        }
    }
        
    
    
    public void setTextArea(String en){
        //String[] tuple = en.split("/");
        String date = en.substring(0, en.indexOf("/")) + "\n";                              //tuple[0]+"\n";
        String entry =  en.substring(en.indexOf("/")+2);                                //tuple[1].substring(1);
        tArea.setText("Date: "+date+"Entry: "+entry+"\n\nEntry: "+(currentEntryIndex+1)+"\n\n\n\nTotal Number of Entries: "+oneDayEntry.size());
        
    }
    
}
        
