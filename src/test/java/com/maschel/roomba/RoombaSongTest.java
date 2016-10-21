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

package com.maschel.roomba;

import com.maschel.roomba.song.RoombaNote;
import com.maschel.roomba.song.RoombaNoteDuration;
import com.maschel.roomba.song.RoombaSongNote;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RoombaSongTest {

    /**
     * RoombaNoteDuration class
     * Test that the GetDurationRelativeToTempo method returns the correct
     * note duration byte relative to the tempo in BPM.
     */
    @Test
    public void testGetDurationRelativeToTempo() {
        final int tempo = 185;
        RoombaNoteDuration durSixteenthNote = RoombaNoteDuration.SixteenthNote;
        RoombaNoteDuration durEightNote = RoombaNoteDuration.EightNote;
        RoombaNoteDuration durQuarterNote = RoombaNoteDuration.QuarterNote;
        RoombaNoteDuration durHalfNote = RoombaNoteDuration.HalfNote;
        RoombaNoteDuration durWholeNote = RoombaNoteDuration.WholeNote;
        double quarterLength = 60.0/tempo;
        int exp_SixteenthNote = (int)(0x40*0.25*quarterLength);
        int exp_eightNote = (int)(0x40*0.5*quarterLength);
        int exp_quarterNote = (int)(0x40*1*quarterLength);
        int exp_halfNote = (int)(0x40*2*quarterLength);
        int exp_wholeNote = (int)(0x40*4*quarterLength);
        assertEquals((byte)exp_SixteenthNote, durSixteenthNote.getDurationByte(tempo));
        assertEquals((byte)exp_eightNote, durEightNote.getDurationByte(tempo));
        assertEquals((byte)exp_quarterNote, durQuarterNote.getDurationByte(tempo));
        assertEquals((byte)exp_halfNote, durHalfNote.getDurationByte(tempo));
        assertEquals((byte)exp_wholeNote, durWholeNote.getDurationByte(tempo));
    }

    /**
     * RoombaSongNote class
     * Test that the songNotesToBytes method returns the correct byte array
     * when given an array of RoombaSongNote's
     */
    @Test
    public void testSongNotesToBytes() {
        final int tempo = 165;
        RoombaSongNote[] notes = {
                new RoombaSongNote(RoombaNote.C1, RoombaNoteDuration.QuarterNote),
                new RoombaSongNote(RoombaNote.C2, RoombaNoteDuration.EightNote)
        };
        byte[] expect = {
                notes[0].getNote().getNoteByte(), notes[0].getDuration().getDurationByte(tempo),
                notes[1].getNote().getNoteByte(), notes[1].getDuration().getDurationByte(tempo)
        };
        assertArrayEquals(RoombaSongNote.songNotesToBytes(notes, tempo), expect);
    }
}
