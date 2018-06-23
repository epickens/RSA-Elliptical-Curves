import java.util.ArrayList;

public class BigInteger {

    public static final BigInteger ZERO = new BigInteger(new int[] {0}, 1);
    public static final BigInteger ONE = new BigInteger(new int[] {1}, 1);
    public static final BigInteger TWO = new BigInteger(new int[] {2}, 1);


    //ints will be stored backwards in bigInt
    //i.e the first digit is the least significant
    //this should hopefully make iteration easier
    private int[] bigInt;
    //1 indicates positive
    //-1 indicates negative
    private int sign;

    //constructor
    public BigInteger(int[] newInt, int s) {
        bigInt = newInt;
        sign = s;
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


    public BigInteger bigAddition(BigInteger intA) {
        int[] newInt;
        int[] cur = this.getInt();
        int[] A = intA.getInt();
        int newSign;

        if (this.getSign() == intA.getSign()) {
            newSign = this.getSign();

            if (this.getInt().length > intA.getInt().length) {
                newInt = new int[this.getInt().length + 1];
            } else {
                newInt = new int[intA.getInt().length + 1];
            }

            int iter;

            if (this.isLonger(intA)) {
                iter = A.length;
            } else {
                iter = cur.length;
            }

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
            return this.bigSubtraction(new BigInteger(A, 1));
        } else {
            return intA.bigSubtraction(new BigInteger(cur, 1));
        }
    }

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
                int temp = greater[i] - lesser[i] - holder;
                if (temp < 0) {
                    temp = 10 + temp;
                    holder = 1;
                } else  {
                    holder = 0;
                    result[i] = temp;
                }
            }

            for (int j = lesser.length; j < greater.length; j++) {
                result[j] = greater[j];
            }

            return new BigInteger(result, final_sign);
        }
    }

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
                    digits.add(j, new Integer(digit % 10));
                } else {
                    digit = digits.get(j) + (intA.getInt()[i] * this.getInt()[j - i]) + holder;
                    holder = digit / 10;
                    //tempProduct.add(digit % 10);
                    digits.set(j, new Integer(digit % 10));
                }
            }

            int fin = this.getInt().length + i;
            if (holder > 0) {
                if (fin >= digits.size()) {
                    digits.add(new Integer(holder));
                } else {
                    digit = digits.get(fin) + holder;
                    holder = digit / 10;
                    digits.set(fin, new Integer(digit % 10));
                }
            }



        }

        int[] temp = new int[digits.size()];
        for (int c = 0; c < digits.size(); c++) {
            temp[c] = digits.get(c);
        }
        BigInteger result = new BigInteger(temp, final_sign);

        return result;
    }
    /*
    public BigInteger bigDivision(BigInteger intA) {

    }

    public BigInteger bigMod() {

    }

    public BigInteger bigModPow(BigInteger intA) {

    }
    */

}
