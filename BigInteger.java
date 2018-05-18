public class BigInteger {

    private int[] bigInt;
    //1 indicates positive
    //-1 indicates negative
    private int sign;

    //constructor
    public BigInteger(int[] newInt, int s) {
        bigInt = newInt;
        sign = s;
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

            int holder = 0;

            for (int i = 0; i < newInt.length - 1; i++) {
                int temp = cur[i];
                int temp_one = A[i];
                int result;

                if (cur[i] + A[i] + holder >= 10) {
                    holder = 1;
                    result = cur[i] + A[i] - 10;
                    newInt[i] = result;
                } else {
                    holder = 0;
                    result = cur[i] + A[i];
                    newInt[i] = result;
                }
            }
            newInt[newInt.length] = holder;

            return new BigInteger(newInt, newSign);
        } else if (this.getSign() == 1) {
            return this.bigSubtraction(intA);
        } else {
            return intA.bigSubtraction(this);
        }
    }

    public BigInteger bigSubtraction(BigInteger intA) {
        if (this.getSign() == -1 && intA.getSign() == 1) {
            return this.bigAddition(new BigInteger(intA.getInt(), -1));
        } else if (this.getSign() == 1 && intA.getSign() == -1) {
            return this.bigAddition(new BigInteger(intA.getInt(), 1));
        } else if (this.getSign() == -1 && intA.getSign() == -1) {
            return (new ).bigSubtraction(this);
        } else {



            int[] result;
            int holder = 0;

            for (int i = 0; i < result.length; i++) {
                int temp = this.getInt()[i] - intA.getInt()[i] - holder;
                if (temp < 0) {
                    temp = 10 + temp;
                    holder = 1;
                } else  {
                    holder = 0;
                    result[i] = temp;
                }
            }
        }
    }

    public BigInteger bigMultiplication(BigInteger intA) {

    }

    public BigInteger bigDivision(BigInteger intA) {

    }

    public BigInteger bigMod()

}
