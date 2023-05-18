package com.sas1946;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CoddedOutput extends FilterOutputStream {

    public void coddedResetSwitch() {
        this.sw = 0;
    }

    public int[] coddedGetKey() {
        return this.key;
    }

    public void coddedSetKey(int ai[]) {
        this.key = ai;
        if ((this.key != null) && (this.key.length == 0)) {
            this.key = null;
        }
        this.sw = 0;
    }

    public CoddedOutput(OutputStream outputstream) {
        super(outputstream);
        this.sw = 0;
        this.sw = 0;
    }

    public CoddedOutput(OutputStream outputstream, int ai[]) {
        super(outputstream);
        this.sw = 0;
        this.key = ai;
        if ((ai != null) && (ai.length == 0)) {
            this.key = null;
        }
        this.sw = 0;
    }

    public void write(int i) throws IOException {
        if (this.key != null) {
            this.sw = (this.sw + 1) % this.key.length;
            this.out.write(i ^ this.key[this.sw]);
        } else {
            this.out.write(i);
        }
    }

    public void write(byte abyte0[], int i, int j) throws IOException {
        if (this.key != null) {
            for (int k = 0; k < j; k++) {
                this.sw = (this.sw + 1) % this.key.length;
                this.out.write(abyte0[i + k] ^ this.key[this.sw]);
            }

        } else {
            this.out.write(abyte0, i, j);
        }
    }

    private int key[] = { 255, 170 };
    private int sw;
}
