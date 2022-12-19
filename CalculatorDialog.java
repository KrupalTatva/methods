public class CustomCalculatorDialog extends Dialog implements OnClickListener
{
    private Activity activity;
    private Button buttonDone = null, mBtnCancel = null, mBtnZero = null,
            mBtnOne = null, mBtnTwo = null, mBtnThree = null, mBtnFour = null,
            mBtnFive = null, mBtnSix = null, mBtnSeven = null,
            mBtnEight = null, mBtnNine = null, mBtnClear = null,
            mBtnEqual = null, mBtnAdd = null, mBtnSubtract = null,
            mBtnMultiply = null, mBtnDivide = null, mBtnDecimal = null;
    private EditText mEditFinal = null, mEditValue = null;
    private static final String DIGITS = "0123456789.";
    private Boolean userIsInTheMiddleOfTypingANumber = false;

    DecimalFormat df = new DecimalFormat("@###########");

    CalculatorBrain brain;


    public CustomCalculatorDialog(
            Activity activity, int customdialogtheme,
            EditText mEditFinal)
    {
        super(activity, customdialogtheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // setCancelable(false);
        setContentView(R.layout.layout_calculator);
        this.activity = activity;
        this.mEditFinal = mEditFinal;
        brain = new CalculatorBrain();

        initControls();

        InputMethodManager imm = (InputMethodManager) this.activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.mEditFinal.getWindowToken(), 0);
    }

    private void initControls()
    {
        try
        {
            Typeface mTf = Typeface.createFromAsset(this.activity.getAssets(),
                    "Chalet_NewYorkSeventy.otf");

            mEditValue = (EditText) findViewById(R.id.valueTxt);

            buttonDone = (Button) findViewById(R.id.buttonDone);
            buttonDone.setOnClickListener(this);
            buttonDone.setTypeface(mTf);

            mBtnCancel = (Button) findViewById(R.id.buttonCancel);
            mBtnCancel.setOnClickListener(this);
            mBtnCancel.setTypeface(mTf);

            mBtnZero = (Button) findViewById(R.id.button0);
            mBtnZero.setOnClickListener(this);
            mBtnZero.setTypeface(mTf);

            mBtnOne = (Button) findViewById(R.id.button1);
            mBtnOne.setOnClickListener(this);
            mBtnOne.setTypeface(mTf);

            mBtnTwo = (Button) findViewById(R.id.button2);
            mBtnTwo.setOnClickListener(this);
            mBtnTwo.setTypeface(mTf);

            mBtnThree = (Button) findViewById(R.id.button3);
            mBtnThree.setOnClickListener(this);
            mBtnThree.setTypeface(mTf);

            mBtnFour = (Button) findViewById(R.id.button4);
            mBtnFour.setOnClickListener(this);
            mBtnFour.setTypeface(mTf);

            mBtnFive = (Button) findViewById(R.id.button5);
            mBtnFive.setOnClickListener(this);
            mBtnFive.setTypeface(mTf);

            mBtnSix = (Button) findViewById(R.id.button6);
            mBtnSix.setOnClickListener(this);
            mBtnSix.setTypeface(mTf);

            mBtnSeven = (Button) findViewById(R.id.button7);
            mBtnSeven.setOnClickListener(this);
            mBtnSeven.setTypeface(mTf);

            mBtnEight = (Button) findViewById(R.id.button8);
            mBtnEight.setOnClickListener(this);
            mBtnEight.setTypeface(mTf);

            mBtnNine = (Button) findViewById(R.id.button9);
            mBtnNine.setOnClickListener(this);
            mBtnNine.setTypeface(mTf);

            mBtnClear = (Button) findViewById(R.id.buttonClear);
            mBtnClear.setOnClickListener(this);
            mBtnClear.setTypeface(mTf);

            mBtnEqual = (Button) findViewById(R.id.buttonEquals);
            mBtnEqual.setOnClickListener(this);

            mBtnAdd = (Button) findViewById(R.id.buttonAdd);
            mBtnAdd.setOnClickListener(this);

            mBtnSubtract = (Button) findViewById(R.id.buttonSubtract);
            mBtnSubtract.setOnClickListener(this);

            mBtnMultiply = (Button) findViewById(R.id.buttonMultiply);
            mBtnMultiply.setOnClickListener(this);

            mBtnDivide = (Button) findViewById(R.id.buttonDivide);
            mBtnDivide.setOnClickListener(this);

            mBtnDecimal = (Button) findViewById(R.id.buttonDecimalPoint);
            mBtnDecimal.setOnClickListener(this);

        }
        catch (Exception e)
        {
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.buttonDone:
                if (mEditValue.getText().toString().trim().length() > 0)
                    this.mEditFinal.setText(Utility.roundOfDecimal(Float
                            .parseFloat(mEditValue.getText().toString().trim())));
                dismiss();
                break;
            case R.id.buttonCancel:
                dismiss();
                break;
            default:
                try
                {
                    String buttonPressed = ((Button) v).getText().toString();
                    // String digits = "0123456789.";

                    if (DIGITS.contains(buttonPressed))
                    {
                        // digit was pressed
                        if (userIsInTheMiddleOfTypingANumber)
                        {
                            mEditValue.append(buttonPressed);
                        }
                        else
                        {
                            mEditValue.setText(buttonPressed);
                            userIsInTheMiddleOfTypingANumber = true;
                        }
                    }
                    else
                    {
                        // operation was pressed
                        if (userIsInTheMiddleOfTypingANumber)
                        {
                            brain.setOperand(Double.parseDouble(mEditValue.getText()
                                    .toString()));
                            userIsInTheMiddleOfTypingANumber = false;
                        }

                        brain.performOperation(buttonPressed);
                        double result = brain.getResult();
                        if (result < 0)
                        {
                            alertBox(activity.getString(R.string.ok), activity.getString(
                                    R.string.value_can_not_be_negative));
                            mEditValue.setText("0.0");
                            brain.setOperand(0);
                        }
                        else
                        {
                            mEditValue.setText(df.format(brain.getResult()));
                        }
                    }
                    mEditValue.setSelection(mEditValue.getText().toString().trim().length());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void alertBox(String btnText, String message)
    {

        AlertDialog.Builder alertDialog = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1)
        {
            alertDialog = new AlertDialog.Builder(activity, R.style.AlertDialog);
        }
        else
        {
            alertDialog = new AlertDialog.Builder(activity);
        }


        alertDialog.setTitle(R.string.app_name)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnText,
                        new OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        }).show();
    }
}
