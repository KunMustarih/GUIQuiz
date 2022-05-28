import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class GUIQuiz implements ActionListener {

    JFrame frame = new JFrame("Game Quiz"); //Frame is created
    JPanel choice_panel = new JPanel(); //Panel to hold the choices
    JPanel option_panel = new JPanel(); //Panel to hold control option
    JLabel textArea = new JLabel(); //Label to show the question

    //Buttons to control the quiz
    private final JButton Restart = new JButton("Restart");
    private final JButton Back = new JButton("Back");
    private final JButton Next = new JButton("Next");
    private final JButton Submit = new JButton("Submit");

    //Radio button to store user choices
    private JRadioButton option1 = new JRadioButton();
    private JRadioButton option2 = new JRadioButton();
    private JRadioButton option3 = new JRadioButton();
    private JRadioButton option4 = new JRadioButton();
    private final ArrayList<QuizQuestion> quizgame = new ArrayList<>(); //Arraylist to hold objects of QuizQuestion
    private final ButtonGroup group = new ButtonGroup(); //ButtonGroup to hold the radiobuttons in group
    int[] chosen_answers = new int[]{-1, -1, -1, -1, -1}; //Array to store the answers chosen by user
    int question_number = 0; //To hold the question number
    int correct_answers = 0; //To hold the index of correct answer
    int amount_of_question = 0; //To hold the number of questions


    //Constructor
    public GUIQuiz() {

        try {
            File mcq_file = new File("mcq.txt");
            String question;
            String answers;
            Scanner in = new Scanner(mcq_file);
            while (in.hasNext()) {
                question = in.nextLine().substring(1);
                amount_of_question++;

                QuizQuestion quiz = new QuizQuestion(question);
                for (int i = 0; i < quiz.MAX_QUESTION; i++) {
                    answers = in.nextLine();
                    if (answers.contains(">")) {
                        quiz.setCorrectAnswer(i); //Gets the index of the answer
                        answers = answers.substring(1); //Assigns the answer to the variable
                    }
                    quiz.addQuestion(i, answers); //Places the answer in the array of answers;
                }
                quizgame.add(quiz); //Add the quiz object to arraylist
            }
            Collections.shuffle(quizgame); //Shuffles the order of the questions and answers
            frame.setLayout(new BorderLayout()); //Sets layout to BorderLayout
            textArea.setPreferredSize(new Dimension(650, 200)); //Set area to hold question

            textArea.setText("Question  " + (question_number + 1) + " of 5 " + quizgame.get(0).getQuestionText()); //Set the question on textArea


            //RadioButton are assigned values
            option1 = new JRadioButton(quizgame.get(0).displayAnswer(0));
            option2 = new JRadioButton(quizgame.get(0).displayAnswer(1));
            option3 = new JRadioButton(quizgame.get(0).displayAnswer(2));
            option4 = new JRadioButton(quizgame.get(0).displayAnswer(3));


            //RadioButton added to group
            group.add(option1);
            group.add(option2);
            group.add(option3);
            group.add(option4);

            //Action Listen attached on the buttons
            option1.addActionListener(this);
            option2.addActionListener(this);
            option3.addActionListener(this);
            option4.addActionListener(this);
            Restart.addActionListener(this);
            Back.addActionListener(this);
            Next.addActionListener(this);
            Submit.addActionListener(this);


            //choices are addded to the panel
            choice_panel.setPreferredSize(new Dimension(650, 200));
            choice_panel.add(option1);
            choice_panel.add(option2);
            choice_panel.add(option3);
            choice_panel.add(option4);

            //Control options are added to the panel
            option_panel.setPreferredSize(new Dimension(650, 200));
            option_panel.add(Restart);
            option_panel.add(Back);
            option_panel.add(Next);
            option_panel.add(Submit);


            Back.setEnabled(false); //disables the back button
            Next.setEnabled(true); //enables the next button

            //Panels and labes are added to the frame
            frame.add(textArea, BorderLayout.NORTH);
            textArea.setHorizontalAlignment(JLabel.CENTER); //Question is centered
            frame.add(choice_panel, BorderLayout.CENTER);
            frame.add(option_panel, BorderLayout.SOUTH);

            //Set the functionality of the close button to exit the frame
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //Makes sure all components are at their preferred size
            frame.pack();
            //Shows frame object on the screen
            frame.setVisible(true);
            //Centers the gui on the screen
            frame.setLocationRelativeTo(null);

        }
        //Exception is caught
        catch (IOException exception) {
            System.out.println("File not found");
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("Index is out of range. Array might be empty");
        }

    }

    //actionPerformed overriden
    @Override
    public void actionPerformed(ActionEvent e) {
        if (question_number < 4) {
            //Checks if the Next button is clicked
            if (e.getSource() == Next) {
                question_number++; //Increments question number
                generate_QA(); //function to generate questions and answers
            }
        }


        if (question_number > 0) {
            //Checks if the back button is clicked
            if (e.getSource() == Back) {
                question_number--;
                generate_QA();
            }
        }

        //Checks if the restart button is selected
        if (e.getSource() == Restart) {
            Collections.shuffle(quizgame);
            question_number = 0;
            generate_QA();
            for (int i = 0; i < chosen_answers.length - 1; i++) {
                chosen_answers[i] = -1;
            }
        }

        //Checks which answer is selected and stores the index of the answer in the array, indexed by question number
        if (option1.isSelected()) {
            chosen_answers[question_number] = 0;
        }
        if (option2.isSelected()) {
            chosen_answers[question_number] = 1;
        }
        if (option3.isSelected()) {
            chosen_answers[question_number] = 2;
        }
        if (option4.isSelected()) {
            chosen_answers[question_number] = 3;
        }

        Next.setEnabled(question_number != 4);

        Back.setEnabled(question_number != 0);
        //Checks if the restart button is selected
        if (e.getSource() == Submit) {
            //Loops through the selected answers and checks the number of correct answers
            for (int i = 0; i < chosen_answers.length; i++) {
                if (quizgame.get(i).getCorrectAnswer() == chosen_answers[i]) {
                    correct_answers++;
                }
            }
            //Displays the final score
            JOptionPane.showMessageDialog(null, "Congratulation, Your score is: " + (correct_answers * 10) + "/50");
            correct_answers = 0;
        }


    }

    public void generate_QA() {
        textArea.setText("Question  " + (question_number + 1) + " of 5: " + quizgame.get(question_number).getQuestionText());
        option1.setText(quizgame.get(question_number).displayAnswer(0));
        option2.setText(quizgame.get(question_number).displayAnswer(1));
        option3.setText(quizgame.get(question_number).displayAnswer(2));
        option4.setText(quizgame.get(question_number).displayAnswer(3));
        group.clearSelection();
    }

    public static void main(String[] args) {
        //GUIQuiz class is initialized
        new GUIQuiz();
    }

}
