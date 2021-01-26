/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Entities.Instructor; 
import Entities.Class; 
import Entities.Exam;
import java.awt.Font;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
/**
 *
 * @author yn653
 */
public class ViewExamsInstructor extends javax.swing.JFrame {

    /**
     * Creates new form ViewExams
     */
    Instructor instructor;  
    Class instructorClass;
    public ViewExamsInstructor(Instructor instructor , Class instructorClass ) {
        this.instructor = instructor; 
        this.instructorClass = instructorClass;
        initComponents();
        showExams();
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        topBar = new javax.swing.JPanel();
        examsTitle = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(800, 680));

        topBar.setBackground(new java.awt.Color(134, 171, 161));

        examsTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        examsTitle.setText("Exams");

        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout topBarLayout = new javax.swing.GroupLayout(topBar);
        topBar.setLayout(topBarLayout);
        topBarLayout.setHorizontalGroup(
            topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topBarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(examsTitle)
                .addGap(219, 219, 219)
                .addComponent(logoutButton)
                .addGap(72, 72, 72))
        );
        topBarLayout.setVerticalGroup(
            topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topBarLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logoutButton)
                    .addComponent(examsTitle))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        panel.setBackground(new java.awt.Color(134, 171, 161));

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 823, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 644, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(topBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 647, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        // TODO add your handling code here:
               logoutButton.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent ev){
                new Login();
                dispose();
            }
        });
    }//GEN-LAST:event_logoutButtonActionPerformed
    
    /**
     * It displays the exams of a certain type (e.g. Running exams).
     * @param exams the java.util.Vector containing all the considered exams
     * @param status The status of the exam
     * @param delta The starting y-coordinate for drawing to keep distances between the exams 
     * @return int This returns the new delta (current y-coordinate to draw)
     */
    private int showExams(Vector<Exam> exams, String status, int delta) {
       
        java.awt.Font titleFont = new java.awt.Font("Tahoma", Font.BOLD, 20);
        java.awt.Font myFont = new java.awt.Font("Tahoma", Font.BOLD, 17);
        
        JLabel ExamsLabel = new JLabel(status + " exams:");
        ExamsLabel.setBounds(20, delta, 300, 80);
        ExamsLabel.setFont(titleFont);
        panel.add(ExamsLabel);
        
        for (Exam exam : exams) {
            
            JLabel examName = new JLabel();
            JButton modelsButtons = new JButton();
            examName.setText(exam.getName());
            examName.setBounds(40, 40 + delta, 300, 80);
            examName.setFont(myFont);
            modelsButtons.setText("Show Models");
            modelsButtons.setFont(myFont);
            modelsButtons.setBounds(380 + 87, 65 + delta, 150, 30);
            modelsButtons.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // new Login();
                    dispose();
                }
            });
            panel.add(examName);
            panel.add(modelsButtons);
            if(status.equals("Finished")) {
                modelsButtons.setBounds(380, 65 + delta, 150, 30);
                JButton reportButtons = new JButton();
                reportButtons.setText("Show Report");
                reportButtons.setFont(myFont);
                reportButtons.setBounds(560, 65 + delta, 150, 30);
                reportButtons.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // new Login();
                        dispose();
                    }
                });
                panel.add(reportButtons);
            }
            delta += 50;
            
        }
        
        if(exams.size() == 0) {
            
            JLabel noExams = new JLabel("No " + status.toLowerCase() + " exams.");
            noExams.setBounds(40, 40 + delta, 300, 80);
            noExams.setFont(myFont);
            panel.add(noExams);
            delta += 50;
            
        }
        
        return delta;
        
    }
    
    /**
     * It displays all the exams of the current class ordered according to the following order (Running -> Upcoming -> Previous) 
     */
    private void showExams()
    {
        examsTitle.setText(instructorClass.getCourse().getName() + ' ' + examsTitle.getText());
        Vector<Exam> exams = instructorClass.getExams();
        Vector<Exam> runningExams = new Vector<Exam>();
        Vector<Exam> upcomingExams = new Vector<Exam>();
        Vector<Exam> finishedExams = new Vector<Exam>();

        for (Entities.Exam exam : exams) {
            
            
            if(exam.isRunning()) {
                runningExams.add(exam);
            } else if(exam.isFinished()) {
                finishedExams.add(exam);
            } else {
                upcomingExams.add(exam);
            }
              
        }
        
        Collections.reverse(finishedExams);
        
        int delta = 0;
        
        delta = showExams(runningExams, "Running", delta);
        delta += 50;
        
        delta = showExams(upcomingExams, "Upcoming", delta);
        delta += 50;
        
        delta = showExams(finishedExams, "Finished", delta);
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewExamsInstructor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewExamsInstructor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewExamsInstructor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewExamsInstructor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // For testing
                new ViewExamsInstructor(new Instructor("ibrahamhassan"), new Entities.Class(1,false)).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel examsTitle;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton logoutButton;
    private javax.swing.JPanel panel;
    private javax.swing.JPanel topBar;
    // End of variables declaration//GEN-END:variables

}
