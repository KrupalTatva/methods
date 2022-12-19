
     */
    public static double getRetirementIncomeFromSSAndPensionsForGoalCost(int currentYear, int nextYear, int preRetirementYear, int currentAge, double getAnnualRateOfRetirementIncomeFromSocialSecurityAndPension, double annualAmountOfRetirementIncomeFromSocialSecurityAndPensions, double preIncomeFromSSAndPensionForGoalCostSheet)
    {
        double retirementIncome = 0;

        if (nextYear == (preRetirementYear + 1))
        {
            retirementIncome = annualAmountOfRetirementIncomeFromSocialSecurityAndPensions * (1 + (getAnnualRateOfRetirementIncomeFromSocialSecurityAndPension / 100));
        }
        else
        {
            if (nextYear > (preRetirementYear + 1) && (nextYear <= (currentYear + CONSTANT_YEAR) - currentAge))
            {
                retirementIncome = preIncomeFromSSAndPensionForGoalCostSheet * (1 + (getAnnualRateOfRetirementIncomeFromSocialSecurityAndPension / 100));
            }
            else
                retirementIncome = 0;
        }
        return Double.parseDouble(roundOfDecimalUpto6Digits(retirementIncome));
    }
//    public static double getRetirementIncomeFromSSAndPensionsForGoalCost(int currentYear, int nextYear, int preRetirementYear, int currentAge, double fvValue)
//    {
//        double retirementIncome = 0;
//
//        if (nextYear >= (preRetirementYear + 1) && (nextYear <= (currentYear + CONSTANT_YEAR) - currentAge))
//        {
//            retirementIncome = -(fvValue);
//        }
//        else
//        {
//            retirementIncome = 0;
//        }
//        return Double.parseDouble(roundOfDecimalUpto6Digits(retirementIncome));
//    }

	/*
	 * Calculate Pre Retirement Living Expenses For Goal Cost Sheet
	 */

    public static double getPreRetirementLivingExpensesForGoalCost(int currentYear, int nextYear, int preRetirementYear, double annualRateOfGrowthOfAnnualFamilyIncome, double preRetirementLivingExpensesInPercentage, int currentAge, double preRetirementLivingExpensesForCurrentYear)
    {
        double preRetirementLivingExpense = 0;
        if ((nextYear > preRetirementYear) && (nextYear <= (currentYear + CONSTANT_YEAR - currentAge)))
        {
            preRetirementLivingExpense = -(Utility.calculateFV(annualRateOfGrowthOfAnnualFamilyIncome, (nextYear - preRetirementYear), 0, ((preRetirementLivingExpensesInPercentage / 100) * preRetirementLivingExpensesForCurrentYear), 0));
        }
        else
        {
            preRetirementLivingExpense = 0;
        }
        return Double.parseDouble(roundOfDecimalUpto6Digits(preRetirementLivingExpense));
    }

	/*
	 * Calculate major Goals Values
	 */

    public static double calculateMajorGoalsForGoalCost(int nextYear, ArrayList<GoalData> majorAssets, int size)
    {
        double majorGoalsValue = 0;
        if (nextYear == majorAssets.get(size).getIntYearPaymentBegins())
        {
            if (majorAssets.get(size).getIsBuying() == 1)
            {
                majorGoalsValue = -majorAssets.get(size).getFltAmountOfCashPaid();
            }
            else
            {
                majorGoalsValue = majorAssets.get(size).getFltAmountOfCashPaid();
            }
        }
        else
        {
            majorGoalsValue = 0;
        }
        return Double.parseDouble(roundOfDecimalUpto6Digits(majorGoalsValue));
    }

	/*
	 * Calculate Minor Goals Value
	 */

    public static double calculateMinorGoalsForGoalCost(int nextYear, ArrayList<GoalData> majorAssets, int size)
    {
        double majorGoalsValue = 0;
        if (nextYear == majorAssets.get(size).getIntYearPaymentBegins())
        {
            if (majorAssets.get(size).getIsSingle() == 1)
            {
                majorGoalsValue = -majorAssets.get(size).getFltAmountOfCashPaid();
            }
            else
            {
                majorGoalsValue = majorAssets.get(size).getFltAmountOfCashPaid();
            }
        }
        else
        {
            majorGoalsValue = 0;

        }

        return Double.parseDouble(roundOfDecimalUpto6Digits(majorGoalsValue));
    }

	/*
	 * Calculate Round Up Value as Excel ROUNDUP function
	 */

    public static double roundUp(double number, int scale)
    {
        return new BigDecimal((double) number).setScale(scale, BigDecimal.ROUND_UP).doubleValue();
    }

	/*
	 * Calculate Round Up Value as Excel ROUNDUP function
	 */

    public static float roundUpFloat(double number, int scale)
    {
        return (float) new BigDecimal((double) number).setScale(scale, BigDecimal.ROUND_UP).doubleValue();
    }

    /*
     * Calculate Major Goal Loan Payment
     *
     * if ( m.isbuying == 1 && Amount of purchase price financed >0 && nexyYear <= (m.yearofbeginning+Term of loan (in months)/12) )
    -(monthly loan payment *12 )

    else if ( nexyYear == (m.yearofbeginning+Term of loan (in months)/12)  && (Term of loan (in months) - (m.yearofbeginning+Term of loan (in 	months)/12) ) > 0))
    -(Term of loan (in months) - (m.yearofbeginning+Term of loan (in 	months)/12) ) * Monthly loan payment )

    else
    0
     */
    public static double getMajorGoalLoanPaymentValue(int nextYear, ArrayList<GoalData> majorAssets, int index)
    {
        GoalData majorGoalData = majorAssets.get(index);
        double goalLoanPayment = 0;
        if (majorGoalData.getIsBuying() == 1 && majorGoalData.getFltAmountFinanced() > 0 && nextYear > majorGoalData.getIntYearPaymentBegins() && nextYear <= (majorGoalData.getIntYearPaymentBegins() + (majorGoalData.getIntTermsofLeanMonths() / 12)))
        {
            goalLoanPayment = -majorGoalData.getFltMonthlyloanPayment() * 12;
        }
        else if (nextYear == (majorGoalData.getIntYearPaymentBegins() + (majorGoalData.getIntTermsofLeanMonths() / 12) + 1) && (majorGoalData.getIntTermsofLeanMonths() - majorGoalData.getIntTermsofLeanMonths() * 12) > 0)
        {
            goalLoanPayment = -(majorGoalData.getIntTermsofLeanMonths() - (majorGoalData.getIntYearPaymentBegins() + majorGoalData.getIntYearPaymentBegins() / 12)) * majorGoalData.getFltMonthlyloanPayment();
        }

        else
        {
            goalLoanPayment = 0;
        }
        return Double.parseDouble(roundOfDecimalUpto6Digits(goalLoanPayment));
    }//    public static double getMajorGoalLoanPaymentValue(int nextYear, ArrayList<GoalData> majorAssets, int index)
//    {
//        double goalLoanPayment = 0;
//        if (nextYear == majorAssets.get(index).getIntYearPaymentBegins() && majorAssets.get(index).getIsBuying() == 0 && majorAssets.get(index).getStrGoalName().equalsIgnoreCase("House"))
//        {
//            goalLoanPayment = (majorAssets.get(index).getFltTotalAmmount() - majorAssets.get(index).getFltAmountOfCashPaid());
//        }
//        else if (!majorAssets.get(index).getStrGoalName().equalsIgnoreCase("House") && majorAssets.get(index).getIsBuying() == 0 && nextYear >= majorAssets.get(index).getIntYearPaymentBegins() && nextYear <= (majorAssets.get(index).getIntYearPaymentBegins() + 4))
//        {
//            goalLoanPayment = (majorAssets.get(index).getFltTotalAmmount() - majorAssets.get(index).getFltAmountOfCashPaid()) / 5;
//        }
//        else if (nextYear >= (majorAssets.get(index).getIntYearPaymentBegins() + 1))
//        {
//            if (nextYear <= (majorAssets.get(index).getIntYearPaymentBegins() + roundUp((double) majorAssets.get(index).getIntTermsofLeanMonths() / 12, 0)))
//            {
//                goalLoanPayment = -(majorAssets.get(index).getFltMonthlyloanPayment() * 12);
//            }
//            else
//            {
//                goalLoanPayment = 0;
//            }
//        }
//        else
//        {
//            goalLoanPayment = 0;
//        }
//        return Double.parseDouble(roundOfDecimalUpto6Digits(goalLoanPayment));
//    }

	/*
	 * Calculate Major Goal Loan Payment
	 */

    public static double getNonMajorGoalLoanPaymentValue(int nextYear, ArrayList<GoalData> majorAssets, int index)
    {
        double goalLoanPayment = 0;

        if (majorAssets.get(index).getIsSingle() == 0 && nextYear >= majorAssets.get(index).getIntYearPaymentBegins() && nextYear <= majorAssets.get(index).getIntYearPaymentEnds())
        {
            goalLoanPayment = -majorAssets.get(index).getFltTotalAmmount();
        }
        else if ((nextYear >= (majorAssets.get(index).getIntYearPaymentBegins() + 1)) && (nextYear <= Math.ceil(roundUp(majorAssets.get(index).getIntYearPaymentBegins() + (double) majorAssets.get(index).getIntTermsofLeanMonths() / 12, 2))))
        {
            goalLoanPayment = -(12 * majorAssets.get(index).getFltMonthlyloanPayment());
        }
        else
        {
            goalLoanPayment = 0;
        }
        return Double.parseDouble(roundOfDecimalUpto6Digits(goalLoanPayment));
    }

	/*
	 * Calculate Cash In or Cash Out of Major Goals for Goal Coast sheet
	 */

    public static double calculateCashInOrCashoutForMajorGoals(double majorGoalsValue, double majorGoalLoanPayment)
    {
        double cashInCashOut = 0;
        cashInCashOut = -(majorGoalsValue + majorGoalLoanPayment);
        return Double.parseDouble(roundOfDecimalUpto6Digits(cashInCashOut));
    }

    /*
     * Calculate Assets value for Goal Cost Sheet(New)
     */
    public static double getAssetValueOfMaGoal(int nextYear, int deathYear, GoalData majorGoalData, double homeRate, double majorAssetRate, double previousAssetValue, boolean ownHome)
    {
        double assetsValue = 0;
        if (majorGoalData.getIsBuying() == 1 && nextYear == majorGoalData.getIntYearPaymentBegins())
        {
            assetsValue = majorGoalData.getFltTotalAmmount();
        }
        else if (nextYear > majorGoalData.getIntYearPaymentBegins() && nextYear <= deathYear && majorGoalData.getStrGoalName().equalsIgnoreCase("house"))
        {
//            if (ownHome)
                assetsValue = previousAssetValue * (1 + (homeRate / 100));
//            else
//                assetsValue = previousAssetValue;
        }
        else if (nextYear > majorGoalData.getIntYearPaymentBegins() && nextYear <= deathYear)
        {
            assetsValue = previousAssetValue * (1 + (majorAssetRate / 100));
        }
        else
            assetsValue = 0;
        return Double.parseDouble(roundOfDecimalUpto6Digits(assetsValue));
    }

	/*
	 * Calculate Loan Balance Increase Or Decrease of Major Goals for Goal Cost
	 * Sheet
	 */

    public static double calculateLoanBalanceIncreaseOrDecrease(int nextYear, double previousLoanBalanceIncreaseOrDecrease, double majorGoalsValue, double majorGoalLoanPayment, ArrayList<GoalData> majorAssets, int index)
    {
        double loanBalanceIncreaseOrDecrease = 0;
        if (majorAssets.get(index).getIsBuying() == 0)
        {
            loanBalanceIncreaseOrDecrease = -majorGoalLoanPayment;
        }
        else if (nextYear == majorAssets.get(index).getIntYearPaymentBegins())
        {
            loanBalanceIncreaseOrDecrease = majorAssets.get(index).getFltTotalAmmount() + majorGoalsValue + majorGoalLoanPayment;
        }

//        else if ((nextYear > majorAssets.get(index).getIntYearPaymentBegins()) && (nextYear <= (majorAssets.get(index).getIntYearPaymentBegins() + roundUp((double) majorAssets.get(index).getIntTermsofLeanMonths() / 12, 0))))
        else if ((nextYear > majorAssets.get(index).getIntYearPaymentBegins()) && (nextYear <= (majorAssets.get(index).getIntYearPaymentBegins() + Math.round((double) majorAssets.get(index).getIntTermsofLeanMonths() / 12))))
        {
            loanBalanceIncreaseOrDecrease = Math.max((previousLoanBalanceIncreaseOrDecrease + majorGoalsValue + majorGoalLoanPayment), 0);
        }
        else
        {
            loanBalanceIncreaseOrDecrease = 0;
        }
        return Double.parseDouble(roundOfDecimalUpto6Digits(loanBalanceIncreaseOrDecrease));
    }

	/*
	 * Calculate Loan Balance Increase Or Decrease of Major Goals for Goal Cost
	 * Sheet
	 */

    public static double calculateLoanBalanceIncreaseOrDecreaseForNonMajorGoals(int nextYear, double previousLoanBalanceIncreaseOrDecrease, double majorGoalsValue, double majorGoalLoanPayment, double cashInCashOut, ArrayList<GoalData> majorAssets, int index)
    {
        double loanBalanceIncreaseOrDecrease = 0;
        if (nextYear == majorAssets.get(index).getIntYearPaymentBegins() && majorAssets.get(index).getIsSingle() == 1)
        {
            loanBalanceIncreaseOrDecrease = majorAssets.get(index).getFltTotalAmmount() + majorGoalsValue + majorGoalLoanPayment;
        }
        else if (majorAssets.get(index).getIsSingle() == 1 && nextYear > majorAssets.get(index).getIntYearPaymentBegins() && (nextYear <= (majorAssets.get(index).getIntYearPaymentBegins() + roundUp((double) majorAssets.get(index).getIntTermsofLeanMonths() / 12, 0))))
        {
            loanBalanceIncreaseOrDecrease = Math.max((previousLoanBalanceIncreaseOrDecrease + majorGoalsValue + majorGoalLoanPayment), 0);
        }
        else
        {
            loanBalanceIncreaseOrDecrease = cashInCashOut;
        }

        return Double.parseDouble(roundOfDecimalUpto6Digits(loanBalanceIncreaseOrDecrease));
    }

	/*
	 * Calculate Purchase Summary Of Goals
	 */

    public static double calculatePurchaseSummaryOfGoals(int currentYear, int nextYear, ArrayList<GoalData> majorAssets, int index)
    {
        double purchaseSummary = 0;

        if (nextYear == currentYear)
        {
            purchaseSummary = 0;
        }
        else if ((nextYear == majorAssets.get(index).getIntYearPaymentBegins()) && majorAssets.get(index).getIsBuying() == 1)
        {
            purchaseSummary = majorAssets.get(index).getFltTotalAmmount();
        }
        else
        {
            purchaseSummary = 0;
        }
        return Double.parseDouble(roundOfDecimalUpto6Digits(purchaseSummary));
    }

	/*
	 * Calculate Sales Summary Of Goals
	 * Change YEs
	 */

    public static double calculateSaleSummaryOfGoals(int currentYear, int nextYear, ArrayList<GoalData> majorAssets, int index)
    {
        double saleSummary = 0;
        if (nextYear == currentYear)
        {
            saleSummary = 0;
        }
        else if ((nextYear == majorAssets.get(index).getIntYearPaymentBegins()) && majorAssets.get(index).getIsBuying() == 0)
        {
            saleSummary = majorAssets.get(index).getFltTotalAmmount();
        }
        else
        {
            saleSummary = 0;
        }
        return Double.parseDouble(roundOfDecimalUpto6Digits(saleSummary));
    }

	/*
	 * Calculate Increase Or Decrease Assets Balance Summary For Goal Cost Sheet
	 * Change YEs
	 */

    public static double calculateIncreaseOrDecreaseAssetsBalanceSummayForGoalCostSheet(int nextYear, int deathYear, double purchaseSummaryForGoals, double salesSummaryForGoals, double previosuIncreaseOrDecreaseAssetsBalance)
    {
        double increaseOrDecreaseAssetsBalance = 0;
        if (nextYear <= deathYear)
        {
            increaseOrDecreaseAssetsBalance = previosuIncreaseOrDecreaseAssetsBalance + purchaseSummaryForGoals - salesSummaryForGoals;
        }
        else
        {
            increaseOrDecreaseAssetsBalance = 0;
        }
        return Double.parseDouble(roundOfDecimalUpto6Digits(increaseOrDecreaseAssetsBalance));
    }

    /*
     * Calculate Installment Of Old Loan For Goal Cost Sheet
     * change yes
     */
    public static double calculateInstallmentOfOldLoanForGoalCostSheet(double totalOutstandingBalanceOfInstallmentLoans, double totalAmountOfMonthlyInstallment)
    {
        double installmentOfOldLoans = 0;
        if ((totalOutstandingBalanceOfInstallmentLoans > 0) && (totalOutstandingBalanceOfInstallmentLoans >= (12 * totalAmountOfMonthlyInstallment)))
        {
            installmentOfOldLoans = totalOutstandingBalanceOfInstallmentLoans - (12 * totalAmountOfMonthlyInstallment);
        }
        else if (totalOutstandingBalanceOfInstallmentLoans < (12 * totalAmountOfMonthlyInstallment))
        {
            installmentOfOldLoans = 0;
        }
        else
        {
            installmentOfOldLoans = totalOutstandingBalanceOfInstallmentLoans;
        }
        return Double.parseDouble(roundOfDecimalUpto6Digits(installmentOfOldLoans));
    }
//    public static double calculateInstallmentOfOldLoanForGoalCostSheet(double totalOutstandingBalanceOfInstallmentLoans, double totalAmountOfMonthlyInstallment)
//    {
//        double installmentOfOldLoans = 0;
//        if ((totalOutstandingBalanceOfInstallmentLoans > 0) && (totalOutstandingBalanceOfInstallmentLoans >= (12 * totalAmountOfMonthlyInstallment)))
//        {
//            installmentOfOldLoans = totalOutstandingBalanceOfInstallmentLoans - (12 * totalAmountOfMonthlyInstallment);
//        }
//        else if (totalOutstandingBalanceOfInstallmentLoans > 0)
//        {
//            installmentOfOldLoans = 0;
//        }
//        else
//        {
//            installmentOfOldLoans = 0;
//        }
//        return Double.parseDouble(roundOfDecimalUpto6Digits(installmentOfOldLoans));
//    }

	/*
	 * Calculate Installment of Loan Payments for Current Year
	 */

    public static double calculateInstallmentOfOldLoansLoanPaymentForCurrentYear(double totalAmountOfMonthlyInstallment)
    {
        return Double.parseDouble(roundOfDecimalUpto6Digits(totalAmountOfMonthlyInstallment * 12));
    }

	/*
	 * Calculate Installment of Loan Payments
	 */

    public static double calculateInstallmentOfOldLoansLoanPayment(double previousYearInstallmentOfOldLoansOfGoals, double currentYearInstallmentOfOldLoansOfGoals)
    {
        return Double.parseDouble(roundOfDecimalUpto6Digits(previousYearInstallmentOfOldLoansOfGoals - currentYearInstallmentOfOldLoansOfGoals));
    }

	/*
	 * Calculate Typical Cash balance for goal cost sheet
	 */

    public static double getTypicalCashBalanceForGoalCostSheet(int currentYear, int nextYear, double typicalCashBalance, int currentAge)
    {
        double cashBalance = 0;
        if (nextYear == currentYear)
        {
            cashBalance = typicalCashBalance;
        }
        else if (nextYear >= currentYear && (nextYear <= currentYear + CONSTANT_YEAR - currentAge))
        {
            cashBalance = typicalCashBalance;
        }
        else
        {
            cashBalance = 0;
        }
        return Double.parseDouble(roundOfDecimalUpto6Digits(cashBalance));
    }

    /**
     * calculates the Net Present Value of a principal amount given the discount
     * rate and a sequence of cash flows (supplied as an array). If the amounts
     * are income the value should be positive, else if they are payments and
     * not income, the value should be negative.
     */

    public static double npv(double discountRate, double[] cashFlowAmounts)
    {
        double npv = 0;
        for (int i = 0; i < cashFlowAmounts.length; i++)
        {
            npv = npv + (cashFlowAmounts[i]) / (Math.pow(1 + (discountRate / 100), i));
        }
        return Double.parseDouble(roundOfDecimalUpto6Digits(npv));
    }

	/*
	 * Calculate Rank for Analysis sheet
	 */

    public static ArrayList<Integer> Rank(ArrayList<Double> values)
    {
        ArrayList<Double> sortedValues = new ArrayList<Double>(values);
        Collections.sort(sortedValues);

        ArrayList<Integer> ranks = new ArrayList<Integer>();

        for (int i = 0; i < values.size(); i++)
            ranks.add(sortedValues.indexOf(values.get(i)) + 1);

        return ranks;
    }

	/*
	 * Calculate Rank for Analysis sheet
	 */

    public static int RankForValue(double value, ArrayList<Double> values)
    {
        ArrayList<Double> sortedValues = new ArrayList<Double>(values);
        Collections.sort(sortedValues, Collections.reverseOrder());

        for (int i = 0; i < sortedValues.size(); i++)
        {
            if (sortedValues.get(i) == value)
            {
                return i + 1;
            }
        }

        return 0;
    }

	/*
	 * Calculate Rank for Analysis sheet
	 */

    public static int RankForValueAscending(double value, ArrayList<Double> values)
    {
        ArrayList<Double> sortedValues = new ArrayList<Double>(values);
        ArrayList<Double> newSortedArray = new ArrayList<Double>();

        Collections.sort(sortedValues);

        for (int i = 0; i < sortedValues.size(); i++)
        {
            if (sortedValues.get(i) != 0)
            {
                newSortedArray.add(sortedValues.get(i));
            }
        }

        for (int i = 0; i < newSortedArray.size(); i++)
        {
            if (newSortedArray.get(i) == value)
            {
                return i + 1;
            }
        }

        return 0;
    }

	/*
	 * Calculate Rank Calculation Worksheet For Step 2
	 */

    public static String getRankCalculationForStep2(double value, int rankValue)
    {
        if (value != 0)
        {
            return String.valueOf(rankValue);
        }
        else
        {
            return "";
        }
    }

	/*
	 * Calculate Cost Rank for analysis sheet
	 */

    public static int getCostRank(String rankCalculationValueofStep2, ArrayList<Double> values)
    {
        int rank = 0;
        if (rankCalculationValueofStep2.length() == 0)
        {
            rank = 0;
        }
        else
        {
            rank = RankForValueAscending(Double.parseDouble(rankCalculationValueofStep2), values) + (Collections.frequency(values, Double.parseDouble(rankCalculationValueofStep2)) - 1);
        }
        return rank;
    }

	/*
	 * Get Investment and Debt Rate for Retirement Year
	 */

    public static String getInvetstmentAndDebtRateForRetirementYear(int currentYear, int retirementYear, double currentYearInvestmentValue, double retirementYearInvestmentValue)
    {
        String rate = null;

        if (currentYearInvestmentValue == 0)
        {
            rate = "NoCalc";
        }
        else if (currentYearInvestmentValue < 0 && retirementYearInvestmentValue < 0)
        {
            rate = String.valueOf(RATE((double) (retirementYear - currentYear), 0, currentYearInvestmentValue, -retirementYearInvestmentValue, 0, 0.15));
        }
        else if ((currentYearInvestmentValue > 0 && retirementYearInvestmentValue < 0) || (currentYearInvestmentValue < 0 && retirementYearInvestmentValue > 0))
        {
            rate = "NoCalc";
        }
        else
        {
            rate = String.valueOf(RATE((double) (retirementYear - currentYear), 0, currentYearInvestmentValue, -retirementYearInvestmentValue, 0, 0.15));
        }

        return rate;
    }

	/*
	 * Excel Based rate function
	 */

    public static double RATE(double nper, double pmt, double pv, double fv, double type, double guess)
    {
        double FINANCIAL_MAX_ITERATIONS = 128, FINANCIAL_PRECISION = 1.0e-08;
        double rate = 0, y = 0, f = 0, y0 = 0, y1 = 0, i = 0, x0 = 0, x1 = 0;

        rate = guess;

        if (Math.abs(rate) < FINANCIAL_PRECISION)
        {
            y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
        }
        else
        {
            //f = Math.exp(nper * Math.log(1 + rate));
            f = Math.pow(1 + rate, nper);
            y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
        }
        y0 = pv + pmt * nper + fv;
        y1 = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;

        // find root by secant method
        i = x0 = 0.0;
        x1 = rate;
        while ((Math.abs(y0 - y1) > FINANCIAL_PRECISION) && (i < FINANCIAL_MAX_ITERATIONS))
        {
            rate = (y1 * x0 - y0 * x1) / (y1 - y0);
            x0 = x1;
            x1 = rate;
            if ((nper * Math.abs(pmt)) > (pv - fv))
                x1 = Math.abs(x1);

            if (Math.abs(rate) < FINANCIAL_PRECISION)
            {
                y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
            }
            else
            {
                f = Math.exp(nper * Math.log(1 + rate));
                y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
            }

            y0 = y1;
            y1 = y;
            ++i;
        }
        return rate * 100;
    } // function RATE()

	/*
	 * Get Positive Networth at retirement Observation 1
	 */

    public static String getPositiveNetworthAtRetirement(double retirementNetWorthValue)
    {
        if (retirementNetWorthValue > 0)
        {
            return "Your positive Net Worth at retirement indicates that you can achieve your pre-retirement goals.";
        }
        else
        {
            return "Your negative Net Worth at retirement indicates that you will be unable to achieve your pre-retirement goals.";
        }
    }

	/*
	 * Get Negative Cashflow at retirement Observation 2
	 */

    public static String getNegativeCashFlowAtRetirement(double retirementCashflowValue)
    {
        if (retirementCashflowValue > 0)
        {
            return "Your positive Cash Flow at retirement indicates that you can achieve your pre-retirement goals.";
        }
        else
        {
            return "Your negative Cash Flow  at retirement indicates that you will be unable to achieve your pre-retirement goals.";
        }
    }

    /**
     * Get Negative net worth at death Observation 3
     */

    public static String getNegativeNetWorthAtDeath(double deathNetworthValue)
    {
        if (deathNetworthValue > 0)
        {
            return "Your positive Net Worth at death indicates that you can achieve your post-retirement goals.";
        }
        else
        {
            return "Your negative Net Worth at death indicates that you will be unable to achieve your post-retirement goals.";
        }
    }

	/*
	 * Get Negative Cashflow at death Observation 4
	 */

    public static String getNegativeCashflowAtDeath(double deathCashflowValue)
    {
        if (deathCashflowValue > 0)
        {
            return "Your positive Cash Flow at death indicates that you can achieve your post-retirement goals.";
        }
        else
        {
            return "Your negative Cash Flow at death indicates that you will be unable to achieve your post-retirement goals.";
        }
    }

	/*
	 * Observation 5
	 */

    public static String getNegativeNetworthAtDeathAndPositiveNetworthAtRetirement(double retirementNetworth, double deathNetworth)
    {
        if (retirementNetworth > 0 && deathNetworth > 0)
        {
            return "Your positive Net Worth at both retirement and death indicates that you will be able to achieve your lifetime goals.";
        }
        else if (retirementNetworth < 0 && deathNetworth > 0)
        {
            return "Your positive Net Worth at death indicates that you can achieve your lifetime goals although your negative pre-retirement Net Worth indicates a need to moderate your pre-retirement goals.";
        }
        else if ((retirementNetworth > 0) && (deathNetworth < 0))
        {
            return "Your negative Net Worth at death and positive Net Worth at retirement indicates that although you will be able to achieve your pre-retirement goals, you need to moderate your post-retirement goals.";
        }
        else
        {
            return "Your negative Net Worth at both retirement and death indicate that you cannot achieve all of your lifetime goals and need to moderate them.";
        }
    }

	/*
	 * Get Negative Cashflow at Both Retirement and Death Observation 6
	 */

    public static String getNegativeCashFlowAtBothRetirementAndDeath(double retirementCashFlowValue, double deathCashFlowValue)
    {
        if (retirementCashFlowValue > 0 && deathCashFlowValue > 0)
        {
            return "Your positive Cash Flow at both retirement and death indicates that you will be able to achieve your lifetime goals.";
        }
        else if (retirementCashFlowValue < 0 && deathCashFlowValue > 0)
        {
            return "Your positive Cash Flow at death indicates that you can achieve your lifetime goals although your negative pre-retirement Cash Flow indicates a need to moderate your pre-retirement goals.";
        }
        else if (retirementCashFlowValue > 0 && deathCashFlowValue < 0)
        {
            return "Your negative Cash Flow at death and positive Cash Flow at retirement indicates that although you will be able to achieve your pre-retirement goals, you need to moderate your post-retirement goals.";
        }
        else
        {
            return "Your negative Cash Flow at both retirement and death indicate that you cannot achieve all of your lifetime goals and need to moderate them.";
        }
    }

	/*
	 * Observation 7
	 */

    public static String getNegativeValueOfInvestment(double currentYearInvestment, double retirementYearInvestment, double deathYearInvestment)
    {
        if (currentYearInvestment < 0 && retirementYearInvestment < 0 && deathYearInvestment < 0)
        {
            return "The negative value of your Investments today, at retirement, and at death indicate that you have consistently generated negative cash flows by spending more than you receive.";
        }
        else if (currentYearInvestment > 0 && retirementYearInvestment > 0 && deathYearInvestment > 0)
        {
            return "The positive value of your Investments today, at retirement, and at death indicte that you have generated positive cash flows by spending less than you receive.";
        }
        else
        {
            return "The negative value of your Investments at certain times over your lifetime indicate that you are sometimes spending more than you receive.";
        }
    }

	/*
	 * Observation 8
	 */

    public static String getNegativeInvestmentAtCertainTime(double retirementInvestment, double deathInvestment)
    {
        if (retirementInvestment < 0 || deathInvestment < 0)
        {
            return "Your negative Investments at certain times can be eliminated by retiring later, eliminating goals, increasing cash inflows, or reducing cash outflows.";
        }
        else
        {
            return "Your positive Investments indicate that your cash flows are adequate for meeting all goals and financial obligations.";
        }
    }

	/*
	 * Get Pre-retirment goal adjustment Observation Number - 9
	 */

    public static String getPreRetirementGoalAdjustment(double retirementNetworthValue, double retirementFutureValueOfAllGoals)
    {
        if (retirementNetworthValue < 0 && retirementFutureValueOfAllGoals > 0 && retirementFutureValueOfAllGoals > Math.abs(retirementNetworthValue))
        {
            return "You can generate positive pre-retirement Net Worth by eliminating certain asset and/or non-asset goals.";
        }
        else if (retirementNetworthValue > 0)
        {
            return "No pre-retirement goal adjustments are needed to generate positive pre-retirement Net Worth.";
        }
        else
        {
            return "The elimination of all pre-retirement goals will not be adequate to generate positive pre-retirement Net Worth.";
        }
    }

	/*
	 * Get Pre retirement positive cashflow Observation Number - 10
	 */

    public static String getPreRetirementPositiveCashFlow(double retirementCashFlowValue, double retirementFutureValueOfAllGoals)
    {
        if (retirementCashFlowValue < 0 && retirementFutureValueOfAllGoals > 0 && retirementFutureValueOfAllGoals > Math.abs(retirementCashFlowValue))
        {
            return "You can generate positive pre-retirement Cash Flow by eliminating certain asset and/or non-asset goals.";
        }
        else if (retirementCashFlowValue > 0)
        {
            return "No pre-retirement goal adjustments are needed to generate positive pre-retirement Cash Flow.";
        }
        else
        {
            return "The elimination of all pre-retirement goals will not be adequate to generate positive pre-retirement Cash Flow.";
        }
    }

	/*
	 * Get Post retirement Goals Observation Number 11
	 */

    public static String getPostRetirementGoals(double deathNetworthValue, double deathFutureValueOfAllGoals)
    {
        if (deathNetworthValue < 0 && deathFutureValueOfAllGoals > 0 && deathFutureValueOfAllGoals > Math.abs(deathNetworthValue))
        {
            return "You can generate positive Net Worth at death by eliminating certain asset and/or non-asset goals.";
        }
        else if (deathNetworthValue > 0)
        {
            return "No post-retirement goal adjustments are needed to generate positive Net Worth at death.";
        }
        else
        {
            return "The elimination of all post-retirement goals will not be adequate to generate positive Net Worth at death.";
        }
    }

	/*
	 * Get Positive Cashflow at Death Observation 12
	 */

    public static String getPositiveCashFlowAtDeath(double deathCashflowValue, double deathFutureValueOfAllGoals)
    {
        if (deathCashflowValue < 0 && deathFutureValueOfAllGoals > 0 && deathFutureValueOfAllGoals > Math.abs(deathCashflowValue))
        {
            return "You can generate positive Cash Flow at death by eliminating certain asset and/or non-asset goals.";
        }
        else if (deathCashflowValue > 0)
        {
            return "No post-retirement goal adjustments are needed to generate positive Cash Flow at death.";
        }
        else
        {
            return "The elimination of all post-retirement goals will not be adequate to generate positive Cash Flow at death.";
        }
    }

	/*
	 * Elinimate Pre Retirement Goals Suggestion 1
	 */

    public static String getElinimatePreRetirementGoals(double retirementNetworthValue, double retirementCashflowValue)
    {
        if (retirementNetworthValue > 0 && retirementCashflowValue > 0)
        {
            return "Congratulations: You do not need to eliminate any specified pre-retirement goals.";
        }
        else
        {
            return "You need to eliminate some of your pre-retirement goals.";
        }
    }

	/*
	 * Elinimate Post Retirement Goals Suggestion 2
	 */

    public static String getElinimatePostRetirementGoals(double deathNetworthValue, double deathCashflowValue)
    {
        if (deathNetworthValue > 0 && deathCashflowValue > 0)
        {
            return "Congratulations: You do not need to eliminate any specified post-retirement goals.";
        }
        else
        {
            return "You need to eliminate some of your post-retirement goals.";
        }
    }

	/*
	 * Suggestion 3
	 */

    public static String getPlanToRetire(double retirementInvestment, double deathInvestment)
    {
        if (retirementInvestment < 0 || deathInvestment < 0)
        {
            return "You need to plan to retire later, eliminate goals, increase cash inflows (increase income or sell assets), and/or reduce cash outflows (reduce living expenses, asset purchases, and debt) in order to create positive Investments.";
        }
        else
        {
            return "Congratulations: Your positive Investments do not require adjustment.";
        }
    }

	/*
	 * Plan Retire Late Suggestions 4
	 */

    public static String planToRetireLater(double deathNetworthValue, double deathFutureValueOfAllGoals)
    {
        if (deathNetworthValue < 0 && deathFutureValueOfAllGoals > 0 && deathFutureValueOfAllGoals > Math.abs(deathNetworthValue))
        {
            return "You need to eliminate certain asset and/or non-asset goals.";
        }
        else if (deathNetworthValue > 0)
        {
            return "Congratulations: Your positive Net Worth does not require adjustment.";
        }
        else
        {
            return "You need to plan to retire later, eliminate goals, increase cash inflows (increase income or sell assets), and/or reduce cash outflows (reduce living expenses, asset purchases, and debt) in order to  generate positive Net Worth.";
        }
    }

	/*
	 * Elinimate Certain assets and non assets goals Suggestion 5
	 */

    public static String elinimateCertainAssetsAndNonAssetsForGoals(double deathCashflowValue, double deathFutureValueOfAllGoals)
    {
        if (deathCashflowValue < 0 && deathFutureValueOfAllGoals > 0 && deathFutureValueOfAllGoals > Math.abs(deathCashflowValue))
        {
            return "You need to eliminate certain asset and/or non-asset goals.";
        }
        else if (deathCashflowValue > 0)
        {
            return "Congratulations: Your positive Cash Flow does not require adjustment.";
        }
        else
        {
            return "You need to plan to retire later, eliminate goals, increase cash inflows (increase income or sell assets), and/or reduce cash outflows (reduce living expenses, asset purchases, and debt) in order to  generate positive Cash Flow.";
        }
    }

	/*
	 * Calculate rate
	 */

    public static double calculateRate(double nper, double pmt, double pv, double fv, double type, double guess)
    {
        // FROM MS
        // http://office.microsoft.com/en-us/excel-help/rate-HP005209232.aspx
        int FINANCIAL_MAX_ITERATIONS = 20;// Bet accuracy with 128
        double FINANCIAL_PRECISION = 0.0000001;// 1.0e-8

        double y, y0, y1, x0, x1 = 0, f = 0, i = 0;
        double rate = guess;

        if (Math.abs(rate) < FINANCIAL_PRECISION)
        {
            y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
        }
        else
        {
            f = Math.exp(nper * Math.log(1 + rate));
            y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
        }

        y0 = pv + pmt * nper + fv;
        y1 = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;

        // find root by Newton secant method
        i = x0 = 0.0;
        x1 = rate;

        while ((Math.abs(y0 - y1) > FINANCIAL_PRECISION) && (i < FINANCIAL_MAX_ITERATIONS))
        {
            rate = (y1 * x0 - y0 * x1) / (y1 - y0);
            x0 = x1;
            x1 = rate;

            if (Math.abs(rate) < FINANCIAL_PRECISION)
            {
                y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
            }
            else
            {
                f = Math.exp(nper * Math.log(1 + rate));
                y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
            }

            y0 = y1;
            y1 = y;
            ++i;
        }
        return rate;
    }

    public static String customFormat(String pattern, double value)
    {
        Locale locale = Locale.ENGLISH;
        NumberFormat nf = NumberFormat.getInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern(pattern);
//        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = df.format(value);
        return output;
    }

    public static boolean chkConvert(String s)
    {
        String tempArray[] = s.toString().split("\\.");
        if (tempArray.length > 1)
        {
            if (Integer.parseInt(tempArray[1]) > 0)
            {
                return true;
            }
            else
                return false;
        }
        else
        {
            return false;
        }
    }

    public static void insertCommaIntoNumber(EditText etText, CharSequence s)
    {
        try
        {
            if (s.toString().length() > 0)
            {
                String convertedStr = s.toString();
                if (s.toString().contains("."))
                {
                    if (Utility.chkConvert(s.toString()))
                        convertedStr = Utility.customFormat("###,###.##", Double.parseDouble(s.toString().replace(",", "")));
                }
                else
                {
                    convertedStr = Utility.customFormat("###,###.##", Double.parseDouble(s.toString().replace(",", "")));
                }

                if (!etText.getText().toString().equals(convertedStr) && convertedStr.length() > 0)
                {
                    etText.setText(convertedStr);
                    etText.setSelection(etText.getText().length());
                }
            }

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }
    public static void displayMessage(Context context, String message)
    {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }


    public static int getCurrentYear(long timeStamp)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return calendar.get(Calendar.YEAR);
    }

    public static String getCurrentMonth(long timeStamp)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return new SimpleDateFormat("MMM",Locale.ENGLISH).format(calendar.getTime());
    }

    public static long getAffordableLoanSize(long monthlyPayment , double percentage , long months){
        double rate = (percentage/12)/100;
        double val = Math.pow(rate+1,months);
        return (long) Math.round((monthlyPayment*((val -1)/rate))/val) ;
    }
}
