/*
 *  roombajssc
 *
 *  MIT License
 *
 *  Copyright (c) 2016 Geoffrey Mastenbroek, geoffrey@maschel.com
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.maschel.roomba.song;

public enum RoombaNote {
    // Pause (no sound)
    Pause(0),

    // Octave 0
    G0(31),
    G0Sharp(32), A0(33), A0Sharp(34), B0(35),

    // Octave 1
    C1(36), C1Sharp(37), D1(38), D1Sharp(39),
    E1(40), F1(41), F1Sharp(42), G1(43),
    G1Sharp(44), A1(45), A1Sharp(46), B1(47),

    // Octave 2
    C2(48), C2Sharp(49), D2(50),D2Sharp(51),
    E2(52), F2(53), F2Sharp(54), G2(55),
    G2Sharp(56), A2(57), A2Sharp(58), B2(59),

    // Octave 3
    C3(60), C3Sharp(61), D3(62), D3Sharp(63),
    E3(64), F3(65), F3Sharp(66), G3(67),
    G3Sharp(68), A3(69), A3Sharp(70), B3(71),

    // Octave 4
    C4(72), C4Sharp(73), D4(74), D4Sharp(75),
    E4(76), F4(77), F4Sharp(78), G4(79),
    G4Sharp(80), A4(81), A4Sharp(82), B4(83),

    // Octave 5
    C5(84), C5Sharp(85), D5(86), D5Sharp(87),
    E5(88), F5(89), F5Sharp(90), G5(91),
    G5Sharp(92), A5(93), A5Sharp(94), B5(95),

    // Octave 6
    C6(96), C6Sharp(97), D6(98), D6Sharp(99),
    E6(100), F6(101), F6Sharp(102), G6(103),
    G6Sharp(104), A6(105), A6Sharp(106), B6(107),

    // Octave 7
    C7(108), C7Sharp(109), D7(110), D7Sharp(111),
    E7(112), F7(113), F7Sharp(114), G7(115),
    G7Sharp(116), A7(117), A7Sharp(118), B7(119),

    // Octave 8
    C8(120), C8Sharp(121), D8(122), D8Sharp(123),
    E8(124), F8(125), F8Sharp(126), G8(127);

    private int note;

    RoombaNote(int note) {
        this.note = note;
    }

    public byte getNoteByte() {
        return (byte)note;
    }
}
