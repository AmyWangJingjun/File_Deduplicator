package Dedu_V2_2;

import Dedu_V2_2.OneEntity;
import Dedu_V2_2.UnZip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.nio.charset.StandardCharsets;
import java.beans.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;


public class Gui extends JPanel implements ActionListener {


    private JButton chooseFileButton, chooseLockButton, setNewLockButton, retrieveButton, openButton, openButton2, startButton, zipButton, unzipButton;
    private JLabel inputone, inputtwo, output;
    private JFileChooser fcFile, fcLock, fcTar, fcDec, fcZip, fcUnZip ;

    private JTextField  newLockdir;
    private JTextArea taskOutput;
    private Task task;

    private int retrieve = 0;
    private int addfile = 0;
    private int setNewLock = 0;
    private int start = 0;
    private File fileNames;
    private File lockName;
    private File DeFileName;
    private File DeLockName;
    private String newlock;
    private int zip = 0;
    private int unzip = 0;
    private File zipFile;
    private File unzipFile;



    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            if (start == 1) {
                try {

                    String[] args = new String[5];
                    if (addfile == 1) {
                        String currentFile = fileNames.getAbsolutePath();
                        args[1] = "-file";
                        args[2] = currentFile;
                        args[3] = "-lock";
                        args[0] = "addfile";
                        args[4] = lockName.getAbsolutePath() + '/';
                        Entrance.main(args);
                    } else if (setNewLock == 1) {
                        String currentFile = fileNames.getAbsolutePath();
                        args[1] = "-file";
                        args[2] = currentFile;
                        args[3] = "-lock";
                        args[0] = "setnewlocker";
                        args[4] = newLockdir.getText();
                        Entrance.main(args);
                    }
                    if (retrieve == 1) {
                        args[0] = "retrieve";
                        args[1] = "-file";
                        args[2] = DeLockName.getAbsolutePath();
                        args[3] = "-lock";
                        args[4] = DeFileName.getAbsolutePath();
                        Entrance.main(args);
                    }
                    if (zip == 1) {
                        OneEntity a = new OneEntity();
                        a.main(new String[]{zipFile.toString()});
                    }
                    if (unzip == 1) {
                        UnZip b = new UnZip();
                        b.main(new String[]{unzipFile.getAbsolutePath()});
                    }

                } catch (Exception e) {
                    System.out.print(e);
                }
            }

            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            taskOutput.removeAll();
            setCursor(null); //turn off the wait cursor
            if (start == 1) {
                start = 0;
                if (addfile == 1) {
                    chooseFileButton.setEnabled(true);
                    chooseLockButton.setEnabled(true);
                    taskOutput.append("Compressed Files:" + "\n");
                    taskOutput.append(fileNames.getName() + "\n");
                    
                    Zip_Lib storage = new Zip_Lib(fileNames.getName(), lockName.getAbsolutePath() + '/');
                    String sto = storage.displayStorage(lockName.getAbsolutePath() + '/');
                    taskOutput.append(sto);
                    addfile = 0;
                }
                if(setNewLock == 1) {
                    chooseFileButton.setEnabled(true);
                    newLockdir.setEnabled(false);
                    taskOutput.append("Compressed Files:" + "\n");
                    taskOutput.append(fileNames.getName() + "\n");
                    taskOutput.append("In new locker:" + "\n");
                    taskOutput.append(newLockdir.getText() + "\n");
                    Zip_Lib storage = new Zip_Lib(fileNames.getName(), newLockdir.getText());
                    String sto = storage.displayStorage(newLockdir.getText());

                    taskOutput.append(sto + "\n");
                    setNewLock = 0;

                }
                if (retrieve == 1) {
                    taskOutput.append("Decompressed Files:");
                    taskOutput.append(DeFileName.getAbsolutePath() + "\n");
                    taskOutput.append("to dir: " + DeLockName.getAbsolutePath() + "\n");
                    retrieve = 0;
                }
                if(zip == 1) {
                    zipButton.setEnabled(true);
                    taskOutput.append("Zip" + zipFile.getName() + "to same directory" + "\n");
                    zip = 0;
                }
                if(unzip == 1) {
                    unzipButton.setEnabled(true);
                    taskOutput.append("UnZip" + unzipFile.getName() + "to same directory" + "\n");
                    unzip = 0;
                }

            }
        }
    }


    public Gui() {
        super(new BorderLayout());
        //Create the demo's UI.
        Font font = new Font("Courier",Font.PLAIN, 15);
        chooseFileButton = new JButton("Choose a File");
        chooseFileButton.setActionCommand("file");
        chooseFileButton.addActionListener(this);

        chooseLockButton = new JButton("Choose a Locker");
        chooseLockButton.setActionCommand("lock");
        chooseLockButton.addActionListener(this);

        setNewLockButton = new JButton("Set a new Locker");
        setNewLockButton.setActionCommand("set");
        setNewLockButton.addActionListener(this);

        retrieveButton = new JButton("Retrieve");
        retrieveButton.setActionCommand("retrieve");
        retrieveButton.addActionListener(this);

        zipButton = new JButton("Zip");
        zipButton.setActionCommand("zip");
        zipButton.addActionListener(this);

        unzipButton = new JButton("Unzip");
        unzipButton.setActionCommand("unzip");
        unzipButton.addActionListener(this);

        newLockdir = new JTextField(10);
        newLockdir.setBackground(Color.WHITE);
        newLockdir.setMargin(new Insets(5,5,5,5));
        newLockdir.addActionListener(this);
        newLockdir.setEnabled(false);
        newLockdir.setSize(new Dimension(50, 5));


        fcFile = new JFileChooser();
        fcFile.setMultiSelectionEnabled(true);
        fcFile.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        openButton = new JButton("Open a File...");
        openButton.addActionListener(this);

        fcLock = new JFileChooser();
        fcLock.setMultiSelectionEnabled(true);
        fcLock.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        openButton2 = new JButton("Choose a locker...");
        openButton2.addActionListener(this);

        fcTar = new JFileChooser();
        fcTar.setMultiSelectionEnabled(true);
        fcTar.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        fcDec = new JFileChooser();
        fcDec.setMultiSelectionEnabled(true);
        fcDec.setFileSelectionMode(JFileChooser.FILES_ONLY);

        fcZip = new JFileChooser();
        fcZip.setMultiSelectionEnabled(true);
        fcZip.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        fcUnZip = new JFileChooser();
        fcUnZip.setMultiSelectionEnabled(true);
        fcUnZip.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);


        startButton = new JButton("Start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);

        taskOutput = new JTextArea(5, 20);
        taskOutput.removeAll();
        taskOutput.setBackground(Color.WHITE);
        taskOutput.setSize(new Dimension(50, 20));
//        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
        taskOutput.setFont(font);

        JPanel leftpanel = new JPanel();
        JPanel rightpanel = new JPanel();
        leftpanel.setLayout(new GridLayout(7,1, 10, 10));

        leftpanel.setBackground(Color.WHITE);
        leftpanel.add(chooseFileButton, 0);
        leftpanel.add(setNewLockButton, 1);
        leftpanel.add(chooseLockButton, 2 );
        leftpanel.add(retrieveButton, 3);
        leftpanel.add(zipButton, 4);
        leftpanel.add(unzipButton, 5);
        leftpanel.add(startButton, 6);
        rightpanel.setLayout(new GridLayout(3,1, 10, 10));
        rightpanel.setBackground(Color.WHITE);
        inputone = new JLabel("Enter dir of new locker :");
        inputone.setFont(font);
        rightpanel.add(inputone,0);
        rightpanel.add(newLockdir,1);
        rightpanel.add(new JScrollPane(taskOutput),2);





        add(leftpanel, BorderLayout.WEST);
        add(rightpanel, BorderLayout.EAST);


        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setSize(new Dimension(100,50));
        setBackground(Color.WHITE);

    }

    /**
     * Invoked when the user presses the start button.
     */

    public void actionPerformed(ActionEvent evt) {

        if (evt.getSource() == chooseFileButton) {
            chooseFileButton.setEnabled(false);
            fcFile.setDialogTitle("Select Files to be Deduplicated");
            int returnVal = fcFile.showOpenDialog(Gui.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File[] files = fcFile.getSelectedFiles();
                fileNames = files[0];
                //This is where a real application would open the file
            }

        }
        if (evt.getSource() == chooseLockButton) {
            chooseLockButton.setEnabled(false);
            fcLock.setDialogTitle("Select Locker Dir");
            int returnVal2 = fcLock.showOpenDialog(Gui.this);
            if (returnVal2 == JFileChooser.APPROVE_OPTION) {
                File[] files = fcLock.getSelectedFiles();
                lockName = files[0];
                //This is where a real application would open the file
            }
            addfile = 1;
        }

        if (evt.getSource() == setNewLockButton) {
            newLockdir.setEnabled(true);
            newlock = newLockdir.getText();
            setNewLock = 1;
        }

        if (evt.getSource() == retrieveButton){
            fcDec.setDialogTitle("Select the target decompress file");
            int returnVal2 = fcDec.showOpenDialog(Gui.this);
            if (returnVal2 == JFileChooser.APPROVE_OPTION) {
                File[] Defile = fcDec.getSelectedFiles();
                DeFileName = Defile[0];
                System.out.println(DeFileName.getAbsolutePath());
                //This is where a real application would open the file
            }
            fcTar.setDialogTitle("Select the target dir for decompress file");
            int returnVal = fcTar.showOpenDialog(Gui.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File[] Defiles = fcTar.getSelectedFiles();
                DeLockName = Defiles[0];
                System.out.println(DeLockName.getAbsolutePath());
                //This is where a real application would open the file
            }

            retrieve = 1;
        }
        if (evt.getSource() == zipButton) {
            fcZip.setDialogTitle("Select file to zip");
            int returnVal = fcZip.showOpenDialog(Gui.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                zipButton.setEnabled(false);
                File[] files = fcZip.getSelectedFiles();
                zipFile = files[0];
                zip = 1;
                //This is where a real application would open the file
            }

        }
        if (evt.getSource() == unzipButton) {

            fcUnZip.setDialogTitle("Select file to unzip");
            int returnVal = fcUnZip.showOpenDialog(Gui.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                unzipButton.setEnabled(false);
                File[] files = fcUnZip.getSelectedFiles();
                unzipFile = files[0];
                unzip = 1;
                //This is where a real application would open the file
            }

        }
        if (evt.getSource() == startButton) {
            start = 1;
        }


        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        task = new Task();
        //task.addPropertyChangeListener(this);
        task.execute();
    }

    /**
     * Invoked when task's progress property changes.
     */
//        public void propertyChange(PropertyChangeEvent evt) {
//            if ("progress"==evt.getPropertyName()) {
//                int progress = (Integer) evt.getNewValue();
//                progressBar.setValue(progress);
//                taskOutput.append(String.format(
//                        "Completed %d%% of task.\n", progress));
//            }
//        }


    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Deduplicator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new Gui();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }



    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }


}
