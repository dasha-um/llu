import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main {

    public static String mat1Name = "m1.txt";
    public static String mat2Name = "m2.txt";
    public static int state = 0;



    static JTextArea textArea = null;
    static int index = 0;
    static String s;

    public static final int SEED1 = 1;
    public static final int SEED2 = 2;
    public static final int EMPTY_ROW_FRACTION = 10;

    public static final String MATRIX1_NAME = "mg1.txt";
    public static final String MATRIX2_NAME = "mg2.txt";
    public static final int SIZE = 2000;

    //-------Create GUI-------//

    public static void main(String[] args) throws IOException, InterruptedException {
        JFrame form = new JFrame("MATRIX");
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        textArea = new JTextArea(10, 20);
        textArea.setText(".::MATRIX v1.0::.\n\n Enter '0' to generate matrixes\n Enter '1' to multiply matrixes\n\n");
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setCaretPosition(0);
        textArea.setEditable(false);
        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        //JLabel label = new JLabel("Enter expression:");
        //panel.add(label, BorderLayout.NORTH);
        JTextField edit = new JTextField(20);
        panel.add(edit);
        JButton button = new JButton("ENTER");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addText(edit.getText());
                //textArea.setText(textArea.getText()+"\n"+);
                try {
                    buttonClick(edit.getText());
                } catch (IOException | InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    state = 0;
                }
                edit.setText("");
            }
        });
        panel.add(button);
        mainPanel.add(panel, BorderLayout.SOUTH);
        form.getContentPane().add(mainPanel);
        form.setPreferredSize(new Dimension(400, 300));
        form.pack();
        form.setLocationRelativeTo(null);
        form.setVisible(true);


    }


    public static void addText(String str) {
        textArea.setText(textArea.getText()+"\n"+str);
    }

    public static void buttonClick(String str) throws IOException, InterruptedException {
        s = str;
        mat2Name = s;
        if (state == 2) {
            Matrix m1 = new DenseMat(mat1Name);
            Matrix m2 = new DenseMat(s);
            addText("Starting mul dense matrices...\n");
            System.out.println("Starting mul dense matrices...");
            long start = System.currentTimeMillis();
            Matrix r1 = m1.pmul(m2);
            addText("Dense Matrix time: " +String.valueOf(System.currentTimeMillis() - start)+"\n");
            System.out.println("Dense Matrix time: " +(System.currentTimeMillis() - start));
            r1.save("DM.txt");

            m1 = new SparseMat(mat1Name);


            m2 = new SparseMat(mat2Name);


            addText("Starting mul sparse matrices...\n");
            System.out.println("Starting mul sparse matrices...");
            start = System.currentTimeMillis();
            Matrix r2 = m1.pmul(m2);
            addText("Sparse Matrix time: " +String.valueOf(System.currentTimeMillis() - start)+"\n");
            System.out.println("Sparse Matrix time: " +(System.currentTimeMillis() - start));
            r2.save("SM.txt");
            state = 0;
        }

        if (state == 1) {
            mat2Name = s;
            addText("Enter matrix 2 name: \n");
            state = 2;
        }

        if (s.equalsIgnoreCase("0")) {
            MatrixGenerator mg = new MatrixGenerator(SEED1, EMPTY_ROW_FRACTION, MATRIX1_NAME, SIZE);
            mg.run();
            addText("Matrix 1 saved to file:"+MATRIX1_NAME+"\nMatrix 2 saved to file:"+MATRIX2_NAME+": \n\n");
        }

        if (s.equalsIgnoreCase("1")) {
            addText("Enter matrix 1 name: \nf.e.: m1.txt\n");
            state = 1;
        }
    }




}

