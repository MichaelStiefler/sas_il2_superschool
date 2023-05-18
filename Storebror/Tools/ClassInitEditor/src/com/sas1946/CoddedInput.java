package com.sas1946;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CoddedInput extends FilterInputStream {

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

    public CoddedInput(InputStream inputstream) {
        super(inputstream);
        this.sw = 0;
        this.sw = 0;
    }

    public CoddedInput(InputStream inputstream, int ai[]) {
        super(inputstream);
        this.sw = 0;
        this.key = ai;
        if ((ai != null) && (ai.length == 0)) {
            this.key = null;
        }
        this.sw = 0;
    }

    public boolean markSupported() {
        return false;
    }

    public int read() throws IOException {
        int i = this.in.read();
        if (this.key == null) {
            return i;
        }
        this.sw = (this.sw + 1) % this.key.length;
        if (i != -1) {
            i ^= this.key[this.sw];
        }
        return i;
    }

    public int read(byte abyte0[], int i, int j) throws IOException {
        int k = this.in.read(abyte0, i, j);
        if ((this.key == null) || (k <= 0)) {
            return k;
        }
        for (int l = 0; l < k; l++) {
            this.sw = (this.sw + 1) % this.key.length;
            abyte0[i + l] = (byte) (abyte0[i + l] ^ this.key[this.sw]);
        }

        return k;
    }

    private int key[] = { 255, 170 };
    private int sw;
}
