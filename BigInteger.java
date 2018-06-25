import java.util.ArrayList;
import java.util.Arrays;

public class BigInteger {

    public static final BigInteger ZERO = new BigInteger(new int[] {0}, 1);
    public static final BigInteger ONE = new BigInteger(new int[] {1}, 1);
    public static final BigInteger TWO = new BigInteger(new int[] {2}, 1);
    public static final BigInteger TEN = new BigInteger(new int[] {10}, 1);


    //ints will be stored backwards in bigInt
    //i.e the first digit is the least significant
    //this should hopefully make iteration easier
    private int[] bigInt;
    //1 indicates positive
    //-1 indicates negative
    private int sign;

    //constructor
    //I may want to create an extra constructor
    //to handle string inputs
    public BigInteger(int[] newInt, int s) {
        int zeroIndex = 0;
        for (int i = 0; i < newInt.length; i++) {
            if (newInt[i] != 0) {
                zeroIndex = i + 1;
            }
        }

        if (zeroIndex == 0) {
            bigInt = new int[] {0};
            sign = 1;
        } else {
            sign = s;
            bigInt = Arrays.copyOfRange(newInt, 0, zeroIndex);
        }
    }

    //overloaded constructor
    //used to take in small ints (0-9)
    //will always be positive
    //this is only really used in the division algorithm
    public  BigInteger(int smallNum) {
        bigInt = new int[] {smallNum};
        sign = 1;
    }

    public void printInt() {
        for (int i = this.getInt().length-1; i >= 0; i--) {
            System.out.print(this.getInt()[i]);
        }
        System.out.println();
    }

    //getters and setter
    public int[] getInt() {
        return bigInt;
    }

    public int getSign() {
        return sign;
    }

    //return true if this = intA
    //this method should be fixed to account
    //for numbers with an inflated number of zeros
    //same for > < operations (maybe I should fix this in the constructor?
    //currently I'm just going to assume proper user inputs (not extra zeros)
    //this currently causes errors in addition and subtraction
    public boolean isEqual(BigInteger intA) {
        if (intA.getSign() != this.getSign()) {
            return false;
        }

        if (intA.getInt().length != this.getInt().length) {
            return false;
        } else {
            for (int i = 0; i < this.getInt().length; i++) {
                if (intA.getInt()[i] != this.getInt()[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    //check is this is greater than intA
    public boolean isGreater(BigInteger intA) {
        if (this.getSign() != intA.getSign()) {
            if (this.getSign() == 1) {
                return true;
            } else {
                return false;
            }
        }

        if (this.isEqual(intA)) {
            return false;
        }

        if (intA.getInt().length < this.getInt().length) {
            return true;
        } else if (intA.getInt().length > this.getInt().length) {
            return false;
        } else {
            for (int i = this.getInt().length - 1; i > 0; i--) {
                if (intA.getInt()[i] < this.getInt()[i]) {
                    return true;
                }
            }
            return false;
        }
    }

    //return true if this < intA
    public boolean isSmaller(BigInteger intA) {
        if (!(isGreater(intA) || isEqual(intA))) {
            return true;
        } else {
            return false;
        }
    }

    //return true if this has more digits than intA
    public boolean isLonger(BigInteger intA) {
        if (intA.getInt().length < this.getInt().length) {
            return true;
        } else {
            return false;
        }
    }

    //check if the absolute value of this is greater than that of intA
    public boolean isGreaterABS(BigInteger intA) {
        if (this.isEqual(intA)) {
            return false;
        }

        if (this.isLonger(intA)) {
            return true;
        } else if (intA.isLonger(this)) {
            return false;
        } else {
            for (int i = this.getInt().length - 1; i > 0; i--) {
                if (intA.getInt()[i] < this.getInt()[i]) {
                    return true;
                }
            }
            return false;
        }
    }

    //BigInt addition
    public BigInteger bigAddition(BigInteger intA) {
        int[] newInt;
        int[] cur;
        int[] A;
        int newSign;

        if (this.getSign() == intA.getSign()) {
            newSign = this.getSign();

            if (this.getInt().length > intA.getInt().length) {
                newInt = new int[this.getInt().length + 1];
                cur = this.getInt();
                A = new int[cur.length];
                System.arraycopy(intA.getInt(),0,  A, 0, intA.getInt().length);
            } else {
                newInt = new int[intA.getInt().length + 1];
                A = intA.getInt();
                cur = new int[A.length];
                System.arraycopy(this.getInt(),0,  cur, 0, this.getInt().length);
            }

            int iter = cur.length;
            /*
            if (this.isLonger(intA)) {
                iter = A.length;
            } else {
                iter = cur.length;
            }
            */
            int holder = 0;

            for (int i = 0; i < iter; i++) {
                int temp = cur[i];
                int temp_one = A[i];
                int result;


                if (cur[i] + A[i] + holder >= 10) {
                    result = (cur[i] + A[i] + holder) % 10;
                    newInt[i] = result;
                    holder = 1;
                } else {
                    holder = 0;
                    result = cur[i] + A[i];
                    newInt[i] = result;
                }
            }
            newInt[newInt.length - 1] = holder;

            return new BigInteger(newInt, newSign);
        } else if (this.getSign() == 1) {
            return this.bigSubtraction(new BigInteger(intA.getInt(), 1));
        } else {
            return intA.bigSubtraction(new BigInteger(this.getInt(), 1));
        }
    }


    //BigInt subtraction
    public BigInteger bigSubtraction(BigInteger intA) {
        if (this.getSign() == -1 && intA.getSign() == 1) {
            return this.bigAddition(new BigInteger(intA.getInt(), -1));
        } else if (this.getSign() == 1 && intA.getSign() == -1) {
            return this.bigAddition(new BigInteger(intA.getInt(), 1));
        } else {
            if (this.isEqual(intA)) {
                int[] temp = {0};
                return new BigInteger(temp, sign = 1);
            }

            int final_sign;
            int[] greater;
            int[] lesser;

            if (this.isGreaterABS(intA)) {
                greater = this.getInt();
                lesser = intA.getInt();
                final_sign = this.getSign();
            } else {
                greater = intA.getInt();
                lesser = this.getInt();
                final_sign = intA.getSign();
            }


            int[] result = new int[greater.length];
            int holder = 0;

            for (int i = 0; i < lesser.length; i++) {
                result[i] = greater[i] - lesser[i] - holder;
                if (result[i] < 0) {
                    result[i] = 10 + result[i];
                    holder = 1;
                } else  {
                    holder = 0;
                    //result[i] = temp;
                }
            }

            for (int j = lesser.length; j < greater.length; j++) {
                result[j] = greater[j] - holder;
                if (result[j] < 0) {
                    result[j] = 10 + result[j];
                    holder = 1;
                } else  {
                    holder = 0;
                }
            }

            return new BigInteger(result, final_sign);
        }
    }

    //multiply two BigInts together using long multiplication method
    public BigInteger bigMultiplication(BigInteger intA) {

        if (intA.isEqual(ZERO) || this.isEqual(ZERO)) {
            return ZERO;
        } else if (intA.isEqual(ONE)) {
            return this;
        } else if (this.isEqual(ONE)) {
            return intA;
        }

        int final_sign;
        if (this.getSign() == intA.getSign()) {
            final_sign = 1;
        } else {
            final_sign = -1;
        }
        ArrayList<Integer> digits = new ArrayList<>();
        //digits.add(0, new Integer(0));

        for (int i = 0; i < intA.getInt().length; i++) {

            int holder = 0;
            //ArrayList<Integer> tempProduct = new ArrayList<>();
            int digit;

            for (int j = i; j < this.getInt().length + i; j++) {
                if (j >= digits.size()) {
                    digit = (intA.getInt()[i] * this.getInt()[j - i]) + holder;
                    holder = digit / 10;
                    digits.add(j, digit % 10);
                } else {
                    digit = digits.get(j) + (intA.getInt()[i] * this.getInt()[j - i]) + holder;
                    holder = digit / 10;
                    //tempProduct.add(digit % 10);
                    digits.set(j, digit % 10);
                }
            }

            int fin = this.getInt().length + i;
            if (holder > 0) {
                if (fin >= digits.size()) {
                    digits.add(holder);
                } else {
                    digit = digits.get(fin) + holder;
                    holder = digit / 10;
                    digits.set(fin, digit % 10);
                }
            }



        }

        int[] temp = new int[digits.size()];
        for (int c = 0; c < digits.size(); c++) {
            temp[c] = digits.get(c);
        }


        return new BigInteger(temp, final_sign);
    }

    //to be used in the division algorithm
    //assumes that the arr being input into the function
    //is an arr that is used to hold the digits of a BigInteger
    private int[] rightShift(int[] arr, int places) {
        if (places <= 0) {
            return arr;
        }

        int[] temp = new int[arr.length + places];

        for (int i = arr.length; i > 0; i--) {
            temp[i + places - 1] = arr[i - 1];
        }

        return temp;
    }

    //for divHelp
    //used to avoid making a divide by 10 call
    //shifts left by one place
    //this method is very inflexible but it's only
    //used in a very specific context so I'm not too worried
    //might change it later though
    private int[] leftShift(int[] arr) {
        int[] temp = new int[arr.length - 1];

        for (int i = 1; i < arr.length; i++) {
            temp[i - 1] = arr[i];
        }

        return temp;
    }

    private int[] helperMult(int[] a, int[] b) {
        ArrayList<Integer> digits = new ArrayList<>();

        for (int i = 0; i < b.length; i++) {

            int holder = 0;
            //ArrayList<Integer> tempProduct = new ArrayList<>();
            int digit;

            for (int j = i; j < a.length + i; j++) {
                if (j >= digits.size()) {
                    digit = (b[i] * a[j - i]) + holder;
                    holder = digit / 10;
                    digits.add(j, digit % 10);
                } else {
                    digit = digits.get(j) + (b[i] * a[j - i]) + holder;
                    holder = digit / 10;
                    digits.set(j, digit % 10);
                }
            }

            int fin = a.length + i;
            if (holder > 0) {
                if (fin >= digits.size()) {
                    digits.add(holder);
                } else {
                    digit = digits.get(fin) + holder;
                    holder = digit / 10;
                    digits.set(fin, digit % 10);
                }
            }
        }

        int[] temp = new int[digits.size()];
        for (int c = 0; c < digits.size(); c++) {
            temp[c] = digits.get(c);
        }
        return temp;
    }

    //a > b
    private boolean helperGreaterThan(int[] a, int[] b) {
        if (a.length < b.length) {
            return false;
        } else if (a.length > b.length) {
            return true;
        } else {
            for (int i = a.length - 1; i >= 0; i++) {
                if(a[i] > b[i]) {
                    return true;
                }
            }
            return false;
        }
    }

    //BigInt division and mod helper function
    //divide 'this' by intA
    //return the quotient and remainder
    public BigInteger[] divHelper(BigInteger intA) {

        //store results (0th index is quotient, 1st is remainder)
        BigInteger[] results = new BigInteger[] {ZERO, ZERO};
        int finalSign = this.getSign() * intA.getSign();

        if (this.getInt().length < intA.getInt().length) {
            results[1] = this;
            return results;
        }

        //number of zeros added to allow for long division

        int[] dividend = this.getInt();
        int[] divisor = intA.getInt();
        int zeroInf = dividend.length - divisor.length;
        divisor = rightShift(divisor, zeroInf);

        //create BigIntegers so we can use previously defined functions
        BigInteger num = new BigInteger(this.getInt(), 1);
        BigInteger denom = new BigInteger(divisor, 1);

        int temp;
        int divis = divisor[divisor.length - 1];
        int numor;
        BigInteger holder;

        for (int i = 0; i <= zeroInf; i++) {
            numor = dividend[dividend.length - 1];
            temp = (numor*10 + dividend[dividend.length - 2]) / divis;

            if (temp > 10) {
                temp = 9;
                holder = denom.bigMultiplication(new BigInteger(temp));
            } else {
                holder = denom.bigMultiplication(TEN);
            }

            while (holder.isGreater(num)) {
                temp -= 1;
                holder = denom.bigMultiplication(new BigInteger(temp));
            }

            results[0] = results[0].bigMultiplication(TEN);
            results[0] = results[0].bigAddition(new BigInteger(temp));
            num = num.bigSubtraction(holder);
            denom = new BigInteger(leftShift(denom.getInt()), 1);
            divis = denom.getInt()[denom.getInt().length - 1];
        }

        results[1] = num;

        return results;
    }


    public BigInteger bigDivMod(BigInteger intA, boolean div){
        if (div) {

        }

        if (intA.isEqual(ZERO)) {
            System.out.println("Cannot divide or mod a BigInteger by zero!");
            System.exit(0);
        } else if (this.isEqual(ZERO)) {
            return ZERO;
        } else if (intA.isEqual(ONE)) {
            return this;
        } else if (this.isEqual(intA)) {
            return ONE;
        }

        return this.divHelper(intA)[0];
    }

    public BigInteger bigMod(BigInteger intA) throws Exception {
        if (intA.isEqual(ZERO)) {
            throw new Exception("Cannot mod by zero!");
        } else if (this.isEqual(ZERO)) {
            return ZERO;
        } else if (intA.isEqual(ONE)) {
            return this;
        } else if (this.isEqual(intA)) {
            return ZERO;
        }


    }
/*
    public BigInteger bigModPow(BigInteger intA) {

    }
*/

}
