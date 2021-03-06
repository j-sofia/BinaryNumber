
/*
 * Joseph Sofia
 * CS284-C
 * 2/12/2020
 * I pledge my honor that I have abided by the Stevens Honor System
 * 
 */

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
	public BinaryNumber(String str) throws Exception {

		data = new int[str.length()];

		// for each char in the string, set each int in the data to the value of
		// that char

		for (int i = 0; i < str.length(); i++) {
			int num = Character.getNumericValue(str.charAt(i));
			if (num != 1 && num != 0) {
				throw new Exception("non-binary input");
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
	public int getDigit(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index > length)
			throw new IndexOutOfBoundsException("Index is: " + index + ", but length is: " + length);
		return data[index];
	}

	/**
	 * Return the decimal value (as int) of the binary number
	 * 
	 */
	public int toDecimal() throws Exception {

		int ans = 0;

		// for each digit in the binary number
		for (int i = 0; i < length; i++) {
			// multiply the digit by the value of the position, add to ans
			ans += data[i] * Math.pow(2, (length - 1) - i);
		}

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
	public void bitShift(int direction, int amount) throws Exception {

		// if the amount of shift > length of bin and direction = right, return
		// 0?

		if (direction != 0 && direction != 1)
			throw new Exception("bitShift: direction must be -1 for right or 1 for left");
		if (direction * amount > length)
			throw new Exception("bitShift: The amount to shift is larger than the data.length");

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
	 * @throws Exception
	 */
	public static int[] bwor(BinaryNumber bn1, BinaryNumber bn2) throws Exception {

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
	 * @throws Exception
	 */
	public static int[] bwand(BinaryNumber bn1, BinaryNumber bn2) throws Exception {

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
	 * @throws Exception
	 * 
	 */
	public void add(BinaryNumber aBinaryNumber) throws Exception {

		int[] bn_data = reduce(aBinaryNumber.getInnerdata());

		// make the lengths equal
		if (length > bn_data.length) {

			bn_data = prepend(bn_data, length - bn_data.length);

		} else if (length < bn_data.length) {

			this.prepend(bn_data.length - length);

		}

		int carry = 0;

		for (int i = length - 1; i >= 0; i--) {

			switch (data[i] + bn_data[i] + carry) {
			case 3:
				data[i] = 1;
				if (i == 0) { // carry at the end. allocate more space and set
								// value
					this.prepend(1);
					data[0] = 1;
				}
				carry = 1;
				break;
			case 2:
				data[i] = 0;
				if (i == 0) { // carry at the end
					this.prepend(1);
					data[0] = 1;
				}
				carry = 1;
				break;
			case 1:
				data[i] = 1;
				carry = 0;
				break;
			case 0:
				data[i] = 0;
				carry = 0;
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
	private static int[] prepend(int[] array, int amount) throws Exception {

		if (amount < 0)
			throw new Exception("prepend: can't prepend negative amount");

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

	public static void main(String[] args) throws Exception {
		BinaryNumber myBin = new BinaryNumber("10111");
		// BinaryNumber myBin2 = new BinaryNumber("1111110101010110");
		System.out.println(myBin);
		// System.out.println(myBin2);

		System.out.println(myBin.toDecimal());

	}
}
