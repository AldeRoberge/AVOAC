package util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Name {

	/*THANKS TO 059 for the list <3
	 */

	Drac(-7), Yangu(-6), Seus(10), Radph(11), Darq(16), Zhiar(17), Uoro(24), Utanu(25), Urake(26), Eashy(27), Vorck(
			38), Orothi(39), Oalei(46), Oshyu(48), Issz(49), Eendi(58), Ril(59), Laen(60), Idrae(61), Ehoni(64), Risrr(
					65), Tal(66), Rayr(67), Vorv(70), Iatho(71), Deyst(72), Eango(82), Rilr(83), Yimi(84), Scheev(
							85), Serl(86), Queq(87), Sek(88), Eati(89), Itani(90), Odaru(91), Gharr(
									92), Saylt(93), Lorz(94), Lauk(95), Iri(96), Iawa(97), Oeti(98), Tiar(99);

	private final int value;

	Name(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	//random name generation

	private static final List<Name> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();

	public static Name randomName() {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}

}
