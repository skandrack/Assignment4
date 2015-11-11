public class Question {
	private String question;		// variable to hold the question itself as a string
	private int numAnswers;			// variable to hold the number of answers to the question as an int
	private String[] answers;		// Variable to hold all of the answers to the question as an array of strings
	private int correctAnswer;		// Variable to hold the value of the correct answer as an int
	private int numAttempts;		// Variable to hold the number of times the question has been attempted
	private int numCorrect;			// Variable to hold the number of times the question was answered correctly.
	private int playerAnswer;		// Variable to hold the player's answer to the question.
	private double percentCorrect;	// Variable to hold the percentage correct for the question overall.
	public Question(String newQuestion, String numOfAnswers, String[] arrayOfAnswers, String theCorrectAnswer, String numOfAttempts, String numCorrectAttempts) {
		question = newQuestion;								// This method simply takes the values read in from the question file and applies them
		numAnswers = Integer.parseInt(numOfAnswers);		// to their respective variables within the class, converting strings to intergers as necessary.
		deepCopy(arrayOfAnswers);
		correctAnswer = Integer.parseInt(theCorrectAnswer);
		numAttempts = Integer.parseInt(numOfAttempts);
		numCorrect = Integer.parseInt(numCorrectAttempts);
	}
	
	public void deepCopy(String[] oldOne) {			// This method is used to do a deep copy of the array of answers read in from the file
		answers = new String[oldOne.length];		// in order to create a separate array to be used within the instance of the Question class
		for (int i = 0; i < oldOne.length; i++) {
			answers[i] = oldOne[i];
		}
	}

	public void printQuestion() {	// Prints the question stored within the instance of the class.
		System.out.println(question);
	}
	
	public String getQuestion() {	// returns the question string
		return question;
	}
	
	public String[] getAnswers() {	// returns the answers as an array of strings.
		return answers;
	}
	
	public void printAnswers() {	// Reads from the array of the answers and prints them out, listing the number associated with each one.
		for (int i = 0; i < answers.length; i++) {
			System.out.println(i + ": " + answers[i]);
		}
	}
	
	public int getCorrectAnswer() {	// Returns the integer value of the correct answer.
		return correctAnswer;
	}
	
	public int getNumAnswers() {	// Returns the number of answers for the particular question
		return numAnswers;
	}
	
	public void addAttempt() {		// Adds one onto the total number of attempts stored within the class after a question has been attempted.
		numAttempts++;
	}
	
	public void addPlayerAnswer(int answer) {	// stores the player's answer to a question as an integer within the player answer variable.
		playerAnswer = answer;
	}
	
	public String getAnswerString(int number) {	// Returns a particular answer from the answer array as a string
		String answerString = answers[number];
		return answerString;
	}
	
	public int getPlayerAnswer() {	// Returns the player's answer that they have previously entered.
		return playerAnswer;
	}
	
	public void addCorrect() {		// If the player guesses an answer correctly, this method adds one to the total number of correct responses.
			numCorrect++;
	}
	
	public int getAttempts() {		// Returns the number of attempts for a particular question.
		return numAttempts;
	}
	
	public int getNumCorrect() {	// Returns the total number of correct responses to a particular question.
		return numCorrect;
	}
	
	public double getPercentCorrect(int correct, int attempts) {	// Determines the percent of correct responses to a question and returns that value.
		percentCorrect = ((double) correct/attempts) * 100;
		return percentCorrect;
	}
	
	public void printTotals() {		// This method will print the question, the total number of attempts, the total correct attempts, and the percentage of correct responses.
		System.out.println("Question: " + question);
		System.out.println("Times attempted: " + numAttempts);
		System.out.println("Times correct: " + numCorrect);
		System.out.printf("Percent correct: %.2f\n", percentCorrect);
	}
}