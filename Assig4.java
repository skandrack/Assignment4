import java.util.*;
import java.io.*;

/*
	Stephen Kandrack
	CS 0401, Monday/Wednesday 3:00-4:15 pm
	This program is designed to allow the user to choose a file containing questions, answers,
	and data about those questions; take the quiz based on those questions; and then see his or 
	her results based on his or her responses. Totals will be kept that keep track of how many
	times questions have been answered correctly as well as how many times they have been attempted.
*/

public class Assig4 {
	public static void main(String[] args) throws IOException {
		
		Scanner sc = new Scanner(System.in);	// Create a scanner to be able to determine the name of the file the user would like to use.
		boolean doesFileExist = false;			// Assume the file does not exist until it is determined to exist.
		String fileName = "file";				// This is just a placeholder until a file name is given by the user.
		
		while (doesFileExist == false) {
			System.out.println("Please enter the name of the file that you would like to use: ");
			fileName = sc.nextLine();
			File possibleQuestions = new File(fileName);
			doesFileExist = possibleQuestions.exists();		// Determine if the file exists.
			if (doesFileExist == false) {
				System.out.println("Please enter a valid text file name!");	// Request that the user gives another file name if the file does not exist.
				fileName = sc.next();
			}
		}
		
		File questionFile = new File(fileName);
		Scanner readFile = new Scanner(questionFile);	// Create a Scanner in order to read from the file.
		ArrayList<Question> questions = new ArrayList<Question>();	// Create an ArrayList to store all of the questions (of type Question) read from the file.
		boolean keepGoing = true;
		
		while (keepGoing == true) {
			String question = readFile.nextLine();		// Read the data from the file
			String numAnswers = readFile.nextLine();
			int numAnswersInt = Integer.parseInt(numAnswers);
			String[] answers = new String[numAnswersInt];
			for (int i=0; i < numAnswersInt; i++) {
				answers[i] = readFile.nextLine();
			}
			String correctAnswer = readFile.nextLine();
			String numAttempts = readFile.nextLine();
			String numCorrect = readFile.nextLine();
			questions.add(new Question(question, numAnswers, answers, correctAnswer, numAttempts, numCorrect));	// Create a new Question object for each question
			keepGoing = readFile.hasNextLine();	// Only continue if there is another line						// read in from the file.
		}
		
		for (int i = 0; i < questions.size(); i++) {	// This loop is used to retrieve the questions and answers from each instance of the Question class
			Question askQuestion = questions.get(i);	// and to prompt the user to give an answer to the question
			askQuestion.printQuestion();
			askQuestion.printAnswers();
			int numberOfAns = askQuestion.getNumAnswers();	// Gets the number of the correct answer.
			System.out.println("What is your answer? (Enter a number): ");
			int checkUserAnswer = sc.nextInt();	// A variable used only as a possible value of the user's answer.
			int userAnswer = checkAnswer(checkUserAnswer, numberOfAns);	// Checks if the user gave an appropriate response
			askQuestion.addPlayerAnswer(userAnswer);
		}
		
		System.out.println("Let's see how you did!");
		
		int numberCorrect = 0;		// All of these are accumulator variables that will store totals.
		int numberWrong = 0;
		int numberQuestions = 0;
		for (int i = 0; i < questions.size(); i++) {
			Question askQuestion = questions.get(i);	// Access the first instance of the Question class stored in the questions ArrayList
			System.out.print("Question: ");
			askQuestion.printQuestion();
			System.out.print("Answer: ");
			int correctAns = askQuestion.getCorrectAnswer();				// Retrieves the correct answer and notifies the user of it.
			String correctAnsString = askQuestion.getAnswerString(correctAns);
			System.out.println(correctAnsString);
			int userAnswer = askQuestion.getPlayerAnswer();					// Retrieves the user's answer and reminds the user of it.
			String userAnswerString = askQuestion.getAnswerString(userAnswer);
			System.out.print("Your answer: ");
			System.out.println(userAnswerString);
			boolean result = checkCorrect(correctAns, userAnswer);	// Determines of the user is correct.
			if (result == true) {			//Adds to the appropriate accumulator variables and congratulates the user if he or she was correct for that question
				askQuestion.addAttempt();
				askQuestion.addCorrect();
				numberCorrect++;
				numberQuestions++;
				System.out.println("GOOD JOB! You got this question right!");
			}
			else {		// Adds to the appropriate accumulator variables and notifies the user that he or she was wrong.
				askQuestion.addAttempt();
				numberWrong++;
				numberQuestions++;
				System.out.println("Sorry, you got this question wrong!");
			}
		}
		
		printPercentCorrect(numberCorrect, numberWrong, numberQuestions);	// Prints the user's final totals, including his or her final percentage.
		System.out.println("Here are the cumulative statistics:");
		
		int hardestQuestionIndex = 0;		// These variables will determine the overall most difficult easiest questions based on total
		int easiestQuestionIndex = 0;		// all-time performance for each question.
		double hardestQuestionPercent = 0;
		double easiestQuestionPercent = 0;
		
		for (int i = 0; i < questions.size(); i++) {
			Question askQuestion = questions.get(i);	// Access each instance of the Question class stored in the questions ArrayList to determine the
			askQuestion.printQuestion();				// statistics involved with each question, as well as to determine the hardest questions as measured by
			int attempts = askQuestion.getAttempts();
			System.out.println("Number of attempts: " + attempts);		// Retrive number of attempts and retrieve number of correct responses for each question
			int corrects = askQuestion.getNumCorrect();					// in order to determine the percentage of correct responses.
			System.out.println("Number of times correct: " + corrects);
			double percentCorrect = askQuestion.getPercentCorrect(corrects, attempts);
			System.out.printf("Percent correct: %.2f\n", percentCorrect);
			if (percentCorrect <= hardestQuestionPercent) {	// if the question has a lower percentage correct than the hardest question so far, then it will be
				hardestQuestionPercent = percentCorrect;	// the new hardest question.
				hardestQuestionIndex = i;
			}
			if (percentCorrect >= easiestQuestionPercent) {	// Similarly, if the question has a higher percentage correct than the easiest question so far, then it
				easiestQuestionPercent = percentCorrect;	// will be the new easiest question
				easiestQuestionIndex = i;
			}
		}
		Question easiestQuestion = questions.get(easiestQuestionIndex);	// Notify the user of the easiest question and hardest question
		Question hardestQuestion = questions.get(hardestQuestionIndex);
		System.out.println("Easiest Question:");
		easiestQuestion.printTotals();
		System.out.println("Hardest Question:");
		hardestQuestion.printTotals();
		
		PrintWriter changeTotals = new PrintWriter(fileName);	// Write the new information (and the old information) back into the same file.
		for (int i = 0; i < questions.size(); i++) {
			Question askQuestion = questions.get(i);
			changeTotals.println(askQuestion.getQuestion());
			int numberOfAnswers = askQuestion.getNumAnswers();
			changeTotals.println(numberOfAnswers);
			String[] answers = askQuestion.getAnswers();
			for (int j = 0; j < answers.length; j++) {
				changeTotals.println(answers[j]);
			}
			changeTotals.println(askQuestion.getCorrectAnswer());
			changeTotals.println(askQuestion.getAttempts());
			changeTotals.println(askQuestion.getNumCorrect());
		}
		changeTotals.close();
	}
	
	public static int checkAnswer(int answer, int number) {	// Used to check if the user's answer is within the range of the question.
		int newAnswer = answer;
		Scanner keyboard = new Scanner(System.in);
		while (newAnswer < 0 || newAnswer >= number) {		// Makes sure the response is greater than 0 and within the range of the question.
			System.out.println("What is your answer? (Enter a number): ");	// If not, allow the user to try again,and continue asking until the user gives
			newAnswer = keyboard.nextInt();									// a valid response.
		}
		return newAnswer;	// Return the new response (or the original one if it was already valid)
	}
	
	public static boolean checkCorrect(int correctAnswer, int userAnswer) {	// Checks if the user's response is correct and returns true if correct, false is it is incorrect.
		if (correctAnswer == userAnswer) {
			return true;
		}
		else {
			return false;
		}
	}
	public static void printPercentCorrect(int correct, int wrong, int questions) {	//Prints the number of correct responses, number of incorrect responses, and the total
		System.out.println("Let's see how you did!");								// percentage for the whole quiz.
		System.out.println("Number correct: " + correct);
		System.out.println("Number incorrect: " + wrong);
		double percentCorrect = ((double) correct / questions) * 100;
		System.out.printf("Percent correct: %.2f\n", percentCorrect);
	}
}