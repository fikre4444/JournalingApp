import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class JournalEditor {
    ArrayList<String> time;
    ArrayList<String> oneDayEntry;
    ArrayList<String> oneDayEntryPermanent;
    int sizeEntry;
    File fileToBeSaved;
    int currentEntryIndex = 0;
    JTextArea tEntryArea, tTimeArea;
    JButton nextButton, previousButton;
    String initialEntry; // used for comparing the current entry and the edited one to inform the user if they want to apply the change
    String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    
    public void execute(){
        makeGui();
        initializeArrayLists();
    }
    
    public void makeGui(){
        JFrame frame = new JFrame("Journal Editor");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        
        JPanel panelCenter = new JPanel();
        Box bxL = Box.createVerticalBox(); // box to contain the text areas
        
        Font font = new Font("sanserif", Font.BOLD, 17);
        tTimeArea = new JTextArea(3, 30);
        tTimeArea.setEditable(false);
        tTimeArea.setText("Time:");
        tTimeArea.setFont(font);
        Color cc = new Color(200, 210, 111);
        tTimeArea.setBorder(new LineBorder(cc, 4));
        tEntryArea = new JTextArea(7, 30);// this one is editable so that the user is able to correct some mistakes they might write
        tEntryArea.setText("Load the Entry that you want to edit.");
        tEntryArea.setFont(font);
        tEntryArea.setBorder(new LineBorder(new Color(190, 200, 101), 4));
        tEntryArea.setLineWrap(true);
        tEntryArea.setWrapStyleWord(true);
        JScrollPane jsp = new JScrollPane(tEntryArea);
        
        
        bxL.add(tTimeArea);
        bxL.add(Box.createVerticalStrut(6));
        bxL.add(jsp);
        
        JPanel panelBottom = new JPanel();
        JButton buttonLoad = new JButton("Load Entry");
        buttonLoad.addActionListener(new LoadListener());
        JButton buttonSave = new JButton("Save Entry");
        buttonSave.addActionListener(new SaveListener());
        JButton buttonApplyChanges = new JButton("Apply Changes");
        buttonApplyChanges.addActionListener(new ApplyChangesListener());
        JButton buttonRestore = new JButton("Restore Entry");
        buttonRestore.addActionListener(new RestoreListener());
        JButton buttonToday = new JButton("Today's Entry");
        buttonToday.addActionListener(new TodayListener());
        nextButton = new JButton(">>");
        nextButton.setEnabled(false);  // disable the next button at first
        nextButton.addActionListener(new NextListener());
        previousButton = new JButton("<<");
        previousButton.setEnabled(false);
        previousButton.addActionListener(new PreviousListener());
        JButton deleteButton = new JButton("Delete Entry");
        deleteButton.addActionListener(new DeleteListener());
        panelBottom.add(buttonToday);
        panelBottom.add(buttonLoad);
        panelBottom.add(buttonApplyChanges);
        panelBottom.add(buttonSave);
        panelBottom.add(buttonRestore);
        panelBottom.add(deleteButton);
        panelBottom.add(previousButton);
        panelBottom.add(nextButton);
        
        frame.add(bxL, BorderLayout.CENTER);
        frame.add(panelBottom, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
    
    public class LoadListener implements ActionListener {
        public void actionPerformed(ActionEvent eve){
            File file = new File("Entries"); //create the file where the entries are stored.
            JFileChooser jfc;
            if(file.exists()){  // if the path exists create the jfc where the folder is 
                jfc = new JFileChooser(file.getAbsolutePath());
            } else jfc = new JFileChooser();  //if not, then create the jfc in the documents directory.
            
            int result = jfc.showOpenDialog(null);  // the returned integer is going to be used to know if the user made a selection
            
            if(result == JFileChooser.APPROVE_OPTION){  //if the user made a selection
                File entry = jfc.getSelectedFile();  //get the file the user selected
                clearArrayLists();  // after we know that the user selected a file do we clear the arrayLists and set the currentEntryIndex.
                currentEntryIndex = 0;
                loadFile(entry);
                oneDayEntryPermanent.addAll(oneDayEntry);
                displayCurrentEntry();
                initialEntry = tEntryArea.getText();
                checkButtons();
            }// closing the if brace
        } //closing the method
    } //closing the class
    
    public class PreviousListener implements ActionListener {
        public void actionPerformed(ActionEvent eve){
            boolean value = checkChangesConfirmation(); // has value 'true' if no changes made or the user continued and 'false' if user chose no or closed the optionPane.
            if(value){ // if the user chose yes or no changes were made.
                currentEntryIndex--; 
                displayCurrentEntry();
                checkButtons();
                initialEntry = tEntryArea.getText(); 
            }
        }
    }
    
    public class NextListener implements ActionListener {
        public void actionPerformed(ActionEvent eve){
            boolean value = checkChangesConfirmation(); // has value 'true' if no changes made or the user continued and 'false' if user chose no or closed the optionPane.
            if(value){ // if the user chose yes or no changes were made.
                currentEntryIndex++;
                displayCurrentEntry();
                checkButtons();
                initialEntry = tEntryArea.getText();
            }
        }
    }
    
    public class ApplyChangesListener implements ActionListener {
        public void actionPerformed(ActionEvent eve){
            if(oneDayEntry.size()!=0){
                if(!oneDayEntry.get(currentEntryIndex).equals(tEntryArea.getText())){
                    applyChanges();
                    JOptionPane.showMessageDialog(null, "Change applied!");
                } else JOptionPane.showMessageDialog(null, "No changes to be made!");
            }
            else {
                JOptionPane.showMessageDialog(null, "No Entries loaded!");
            }
        }
    }
    
    public class RestoreListener implements ActionListener {
        public void actionPerformed(ActionEvent eve){
            if(oneDayEntry.size() != 0){ // if there are entries
                String restoreData = oneDayEntryPermanent.get(currentEntryIndex); // get the original entry from the permanent arrayList
                oneDayEntry.set(currentEntryIndex, restoreData); // set the arrayList with the original entry
                displayCurrentEntry(); 
                initialEntry = restoreData; // so that when pressed next, an optionPane doesn't pop up.
            }
        }
    } 
    
    public class TodayListener implements ActionListener {
        public void actionPerformed(ActionEvent eve){
            clearArrayLists();
            currentEntryIndex = 0;
            initialEntry = null;
            GregorianCalendar gcal = new GregorianCalendar(); //creates today date
            String month = months[gcal.get(Calendar.MONTH)];
            String today = "Entries"+"/"+gcal.get(Calendar.YEAR)+"/"+month+"/"+gcal.get(Calendar.DAY_OF_MONTH)+".txt";
            File file = new File(today);
            
            if (!file.exists()){ 
                tEntryArea.setText("No Entries were made Today.");
            }
            else { // if the file exists do the following things
                loadFile(file);
                oneDayEntryPermanent.addAll(oneDayEntry);
                displayCurrentEntry();
                initialEntry = tEntryArea.getText();
                checkButtons();
            }
        }
    }
    
    public class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent eve){
            boolean changesMade = checkChangesList(); // checks if changes have been made to the whole arrayList.
            if(changesMade){ // if changes have been made
                String question = "Are you sure you want to save the changes? After this you won't be able to restore the original entries.";
                int option = JOptionPane.showConfirmDialog(null, question, "Making Sure", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION){
                    saveChanges();
                }
            } 
            else JOptionPane.showMessageDialog(null, "No changes have been made!");
        }
    }
    
    public class DeleteListener implements ActionListener {
        public void actionPerformed(ActionEvent eve){
            if(oneDayEntry.size() == 0){
                JOptionPane.showMessageDialog(null, "No Entries!");
            } else {
                String question = "Are you sure you want to delete this entry? After this you won't be able to restore the entry and if you save the changes you won't be able to recover the entry.";
                int result  = JOptionPane.showConfirmDialog(null, question, "Making sure", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    oneDayEntry.remove(currentEntryIndex);
                    oneDayEntryPermanent.remove(currentEntryIndex);
                    
                    if( oneDayEntry.size() == 0){ // if after deleting, all the entries are deleted.
                        tEntryArea.setText("No entries left.");
                    } else {
                        if(currentEntryIndex >= oneDayEntry.size())
                            currentEntryIndex--;
                        initialEntry = oneDayEntry.get(currentEntryIndex);
                        displayCurrentEntry();
                        checkButtons();
                    }
                }
            }
        }
    }
                    
                        
    
    public void initializeArrayLists(){
        time = new ArrayList<>();
        oneDayEntry = new ArrayList<>();
        oneDayEntryPermanent = new ArrayList<>();
        sizeEntry = oneDayEntry.size();
    }
    public void loadFile(File file){
        fileToBeSaved = file; //assigns the file loaded to the fileToBeSaved incase the user chooses to save the entries changed.
        try {
            FileReader fr = new FileReader(file); //create a filereader 
            BufferedReader br = new BufferedReader(fr); //create a buffered REader
            String line = null;
            while((line = br.readLine())!= null){
                line = shift(line); //shifts it back
                addToArrayList(line);
            }
        } catch(IOException ex){
            System.out.println("Can't read the file.");
        }
    }
    public String shift(String word){
        String result="";
        for(int i = 0; i<word.length(); i++){
            int res = (int)word.charAt(i);
            res--;
            char c = (char)res;
            result += c;
        }
        return result;
    }
    public void clearArrayLists(){
        time.clear();
        oneDayEntry.clear();
        oneDayEntryPermanent.clear();
    }
    public void addToArrayList(String line){
        String ti = line.substring(0, line.indexOf('/')-1);  // this is the date and time part (the '-1' removes the space at the end of the time stamp)
        String en = line.substring(line.indexOf('/')+2);
        time.add(ti);
        oneDayEntry.add(en);
    }
    public void displayCurrentEntry(){
        tTimeArea.setText(time.get(currentEntryIndex));
        tEntryArea.setText(oneDayEntry.get(currentEntryIndex));
    }
    public void checkButtons(){
        if(currentEntryIndex == 0){ // if we're at the first entry
            previousButton.setEnabled(false);
            if(oneDayEntry.size() == 1){ // if there are no other entries
                nextButton.setEnabled(false);
            } else nextButton.setEnabled(true); // otherwise it should be enabled
        } else { // if we're not at the first entry
            previousButton.setEnabled(true); // previous button should be enabled
            if(currentEntryIndex == oneDayEntry.size()-1){ // if we have reached the last entry
                nextButton.setEnabled(false);
            } else nextButton.setEnabled(true);  // if we're not at the last entry
        }
    }
    public boolean checkChangesConfirmation(){
        if(!initialEntry.equals(tEntryArea.getText())){ // if the entry has been changed
            String question = "Are you sure you want to continue without applying the changes you made to this entry?";
            int option = JOptionPane.showConfirmDialog(null, question, "Making sure", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION){
                return true;  // if the user chooses yes
            } else return false; // if the user chooses no or the close option
        }
        return true;  // if there were no changes made to the entry
    }
    public void applyChanges(){
        String entry = tEntryArea.getText();
        oneDayEntry.set(currentEntryIndex, entry);
        initialEntry = entry;
    }
    public boolean checkChangesList(){ // checks if changes have been made to the whole oneDayEntry ArrayList
        boolean changesMade = false;
        for(int i=0; i<oneDayEntry.size(); i++){
            String temp = oneDayEntry.get(i);
            String perm = oneDayEntryPermanent.get(i);
            if(!temp.equals(perm)){  // if there are differences in the entries between the temporary and permanent
                changesMade = true;
                break;
            }
        }
        if(sizeEntry != oneDayEntry.size())
            changesMade = true;
        return changesMade;
    }
    public String shiftsave(String word){
        String result="";
        for(int i = 0; i<word.length(); i++){
            int res = (int)word.charAt(i);
            res++;
            char c = (char)res;
            result += c;
        }
        return result;
    }
    public void saveChanges(){
        try{
            FileWriter fw = new FileWriter(fileToBeSaved);  // creates a fileWriter to overwrite the entry with the new edited entry.
            BufferedWriter bw = new BufferedWriter(fw);
            for(int i=0; i<oneDayEntry.size(); i++){
                String t = time.get(i);  // the time at which the entry was written originally
                String e = oneDayEntry.get(i); // the entry it self
                String line = t+" / "+e;
                line = shiftsave(line);
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }catch(IOException ex){
            System.out.println("Couldn't save changes");
        }
        oneDayEntryPermanent.clear();
        oneDayEntryPermanent.addAll(oneDayEntry);
        sizeEntry = oneDayEntry.size(); // after doing everything we change the value of sizeEntry to the value of the arrayList again.
    }
            
        
                    

}
        
    
        
        
        
        
        
    
    
