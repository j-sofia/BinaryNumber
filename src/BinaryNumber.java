import java.util.Arrays;

public class BinaryNumber {
	private int[] data;
	private int length;

	/**
	 * Initializes a BinaryNumber with length amount of zeroes
	 * 
	 * @param length
	 *            amount of zeroes, ie length = 6 => 000000
	 */
	public BinaryNumber(int length) {
		// create binary with length amount of 0s
		data = new int[length];

		this.length = length;
	}

	/**
	 * Initializes a BinaryNumber with specified value
	 * 
	 * @param str
	 *            Value of the binary number as string, ie "11101"
	 */
	public BinaryNumber(String str) {

		data = new int[str.length()];

		// for each char in the string, set each int in the data to the value of
		// that char

		for (int i = 0; i < str.length(); i++) {
			int num = Character.getNumericValue(str.charAt(i));
			if (num != 1 && num != 0) {
				System.out.println("Binary input invalid. only enter 0s and 1s");
				data = null;
				return;
			} else {
				data[i] = num;
				length += 1;
			}
		}
	}

	/**
	 * Returns length of binary as int, ie. 1001 has length 4
	 * 
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Returns the inner array representing the binary
	 * 
	 */
	public int[] getInnerdata() {
		return data;
	}

	/**
	 * Returns the digit in the index position
	 * 
	 * @param index
	 *            Index of the digit
	 */
	public int getDigit(int index) {
		return data[index];
	}

	/**
	 * Return the decimal value (as int) of the binary number
	 * 
	 */
	public int toDecimal() {

		int n = length;
		int ans = 0;

		// for each digit in the binary number
		for (int i = 0; i < n; i++) {
			// multiply the digit by the value of the position, add to ans
			ans += this.getDigit(i) * Math.pow(2, n - i);
		}

		//
		ans /= 2;

		return ans;
	}

	/**
	 * Binary bit-shift: https://en.wikipedia.org/wiki/Arithmetic_shift
	 * 
	 * @param direction
	 *            Direction to shift, -1 is right, 1 is left
	 * 
	 * @param amount
	 *            Amount of bits to shift
	 */
	public void bitShift(int direction, int amount) {

		// if the amount of shift > length of bin and direction = right, return
		// 0?

		data = Arrays.copyOf(data, length + -direction * amount);

		length += -direction * amount;

	}

	/**
	 * Bitwise OR operation: https://en.wikipedia.org/wiki/Bitwise_operation
	 * 
	 * @param bn1
	 *            Binary number to operate on
	 * 
	 * @param bn2
	 *            binary number to operate on
	 */
	public static int[] bwor(BinaryNumber bn1, BinaryNumber bn2) {

		// get their inner data so we can change their length without
		// changing the actual BinaryNumbers
		int[] bn1_data = bn1.getInnerdata();

		int[] bn2_data = bn2.getInnerdata();

		// make their lengths equal
		if (bn1.getLength() > bn2.getLength()) {

			bn2_data = prepend(bn2_data, bn1.getLength() - bn2.getLength());

		} else if (bn1.getLength() < bn2.getLength()) {

			bn1_data = prepend(bn1_data, bn2.getLength() - bn1.getLength());

		}

		int n = bn1_data.length;
		int[] ans = new int[n];

		// iterate through the binary number and operate
		for (int i = 0; i < n; i++) {
			if (bn1_data[i] == 1 || bn2_data[i] == 1) {
				ans[i] = 1;
			} else {
				ans[i] = 0;
			}
		}

		return ans;
	}

	/**
	 * Bitwise AND operation: https://en.wikipedia.org/wiki/Bitwise_operation
	 * 
	 * @param bn1
	 *            Binary number to operate on
	 * 
	 * @param bn2
	 *            binary number to operate on
	 */
	public static int[] bwand(BinaryNumber bn1, BinaryNumber bn2) {

		// get their inner data so we can operate on their length without
		// changing the actual BinaryNumbers
		int[] bn1_data = bn1.getInnerdata();

		int[] bn2_data = bn2.getInnerdata();

		// make their lengths equal
		if (bn1.getLength() > bn2.getLength()) {

			bn2_data = prepend(bn2_data, bn1.getLength() - bn2.getLength());

		} else if (bn1.getLength() < bn2.getLength()) {

			bn1_data = prepend(bn1_data, bn2.getLength() - bn1.getLength());

		}

		int n = bn1_data.length;
		int[] ans = new int[n];

		// iterate through the binary number and operate
		for (int i = 0; i < n; i++) {
			if (bn1_data[i] == 1 && bn2_data[i] == 1) {
				ans[i] = 1;
			} else {
				ans[i] = 0;
			}
		}

		return ans;
	}

	public String toString() {

		String ans = new String();

		for (int i = 0; i < length; i++) {
			if (this.getDigit(i) == 1) {
				ans += "1";
			} else if (this.getDigit(i) == 0) {
				ans += "0";
			}
		}
		return ans;
	}

	/**
	 * Add the value of aBinaryNumber to the BinaryNumber.
	 * 
	 * @param aBinaryNumber
	 *            Binary number to add
	 * 
	 */
	public void add(BinaryNumber aBinaryNumber) {

		int[] bn_data = reduce(aBinaryNumber.getInnerdata());

		// make the lengths equal
		if (length > bn_data.length) {

			bn_data = prepend(bn_data, length - bn_data.length);

		} else if (length < bn_data.length) {

			this.prepend(bn_data.length - length);

		}

		boolean carry = false;

		for (int i = length - 1; i >= 0; i--) {
			if (carry) {

				if (data[i] == 1 && bn_data[i] == 1) {
					data[i] = 1;
					if (i == 0) {
						this.prepend(1);
						data[0] = 1;
					}
					carry = true;
				} else if (data[i] == 1 || bn_data[i] == 1) {

					data[i] = 0;
					if (i == 0) {
						this.prepend(1);
						data[0] = 1;
					}

					carry = true;
				} else if (data[i] == 0 && bn_data[i] == 0) {
					data[i] = 1;
					carry = false;
				}

			} else if (!carry) {

				if (data[i] == 1 && bn_data[i] == 1) {
					data[i] = 0;
					if (i == 0) {
						this.prepend(1);
						data[0] = 1;
					}
					carry = true;
				} else if (data[i] == 1 || bn_data[i] == 1) {
					data[i] = 1;
					carry = false;
				} else if (data[i] == 0 && bn_data[i] == 0) {
					carry = false;
				}

			}

		}

	}

	/**
	 * Prepend an integer array with amount 0s
	 * 
	 * @param array
	 *            Array to operate on
	 * @param amount
	 *            Amount of 0s to prepend
	 * 
	 */
	private static int[] prepend(int[] array, int amount) {

		int[] ans = new int[array.length + amount];

		for (int i = 0; i < array.length; i++) {
			ans[i + amount] = array[i];
		}

		return ans;
	}

	/**
	 * Prepends amount 0s to the actual BinaryNumber instead of just the array
	 * 
	 * @param amount
	 *            Amount of 0s to prepend
	 * 
	 */
	private void prepend(int amount) {

		int n = data.length;
		int[] temp_data = Arrays.copyOf(data, n);
		data = new int[n + amount];
		length += amount;

		for (int i = 0; i < n + amount; i++) {
			if (i < amount) {
				data[i] = 0;
			} else {
				data[i] = temp_data[i - amount];
			}
		}
	}

	
	/**
	 * Recursively gets rid of leading 0s in an array
	 * 
	 * @param array
	 *            Amount of 0s to prepend
	 * 
	 */
	private static int[] reduce(int[] array) {
		if (array[0] == 1) {
			return array;
		} else {
			return reduce(Arrays.copyOfRange(array, 1, array.length));
		}
	}

	public static void main(String[] args) {
		BinaryNumber myBin = new BinaryNumber("101011010");
		BinaryNumber myBin2 = new BinaryNumber("1111110101010110");
		System.out.println(myBin);
		System.out.println(myBin2);
		myBin.add(myBin2);
		System.out.println(myBin);

	}
}
