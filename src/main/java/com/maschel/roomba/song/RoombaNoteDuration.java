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

/**
 * Enum of most common note durations
 * Usage: RoombaNoteDuration.QuarterNote (for quarter note duration).
 */
public enum RoombaNoteDuration {
    SixteenthNote(0.25),
    EightNote(0.5),
    QuarterNote(1),
    HalfNote(2),
    WholeNote(4);

    private double duration;

    RoombaNoteDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Get the byte representation of the relative note duration
     * @param tempo Tempo in BPM
     * @return Note duration relative to BPM in byte representation
     */
    public byte getDurationByte(int tempo) {
        return (byte)(getDurationRelativeToTempo(tempo));
    }

    /**
     * Gives the duration of a note relative to the tempo in BPM, using the
     * Quarternote as base note.
     * @param tempo Tempo in BPM
     * @return The note duration relative to the tempo in BPM
     */
    private int getDurationRelativeToTempo(int tempo) {
        double quarterLength = 60.0/tempo;
        int relDuration = (int)(0x40*duration*quarterLength);

        return relDuration;
    }
}
