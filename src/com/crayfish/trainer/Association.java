package com.crayfish.trainer;

/*
 * The Association - one line in the memory.
 * Both 'answer' and 'question' can be asked. Statistics are kept.
 */
public class Association {
	private long id;
	private String question;
	private String answer;
	private String kindof_question;
	private String kindof_answer;
	private long answer_correct_count;
	private long answer_wrong_count;

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	public String getAnswer() {
		return answer;
	}

	public String getQuestion() {
		return question;
	}

		public String getAnswerKind() {
		return kindof_answer;
	}

	public String getQuestionKind() {
		return kindof_question;
	}
	
	
	public void setAssotiation(String question, String answer, String kindof_question, String kindof_answer) {
		this.question = question;
		this.answer = answer;
		this.kindof_question = kindof_question;
		this.kindof_answer = kindof_answer;
	}

	long getStatisticCorrect()
	{
		return answer_correct_count;
	}

	long getStatisticWrong()
	{
		return answer_wrong_count;		
	}
	
	public void setStatistic(long correct, long wrong)
	{
		this.answer_correct_count = correct;
		this.answer_wrong_count = wrong;
	}
	
	public String toString() {
		return question + " - " + answer + " ( " + kindof_question + " - " + kindof_answer + " ) " + " correct: "
				+ answer_correct_count + " wrong: " + answer_wrong_count;
	}

	public int isAnswerGood(String answer)
	{
		return this.answer.compareTo(answer);
	}
	
	public int isQuestionGood(String question)
	{
		return this.question.compareTo(question);
	}
	
}
