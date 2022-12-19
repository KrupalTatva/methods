public class CalculatorBrain {
	private double operand = 0;
	private double waitingOperand = 0;
	private String waitingOperator = "";
	private double calculatorMemory = 0;

	public void setOperand(double operand) {
		this.operand = operand;
	}

	public double getResult() {
		return operand;
	}

	// used on screen orientation change
	public void setMemory(double calculatorMemory) {
		this.calculatorMemory = calculatorMemory;
	}

	// used on screen orientation change
	public double getMemory() {
		return calculatorMemory;
	}

	public String toString() {
		return Double.toString(operand);
	}

	protected double performOperation(String operator) {

		/*
		 * If you are using Java 7, then you can use switch in place of if
		 * statements
		 * 
			 * switch (operator) { case "MC": calculatorMemory = 0; break; case
		 * "M+": calculatorMemory = calculatorMemory + operand; break; }
		 */

		if (operator.equals("MC")) {
			calculatorMemory = 0;
		} else if (operator.equals("M+")) {
			calculatorMemory = calculatorMemory + operand;
		} else if (operator.equals("M-")) {
			calculatorMemory = calculatorMemory - operand;
		} else if (operator.equals("MR")) {
			operand = calculatorMemory;
		} else if (operator.equals("C")) {
			operand = 0;
			waitingOperator = "";
			waitingOperand = 0;
			calculatorMemory = 0;
		} else if (operator.equals("Sqrt")) {
			operand = Math.sqrt(operand);
		} else if (operator.equals("1/x")) {
			if (operand != 0) {
				operand = 1 / operand;
			}
		} else if (operator.equals("+/-")) {
			operand = -operand;
		} else if (operator.equals("sin")) {
			operand = Math.sin(operand);
		} else if (operator.equals("cos")) {
			operand = Math.cos(operand);
		} else {
			performWaitingOperation();
			waitingOperator = operator;
			waitingOperand = operand;
		}

		return operand;
	}

	protected void performWaitingOperation() {

		if (waitingOperator.equals("+")) {
			operand = waitingOperand + operand;
		} else if (waitingOperator.equals("X")) {
			operand = waitingOperand * operand;
		} else if (waitingOperator.equals("-")) {
			operand = waitingOperand - operand;
		} else if (waitingOperator.equals("/")) {
			if (operand != 0) {
				operand = waitingOperand / operand;
			}
		}

	}
}
