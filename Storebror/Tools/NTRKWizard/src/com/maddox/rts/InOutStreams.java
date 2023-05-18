package com.maddox.rts;

import com.maddox.util.HashMapExt;
import java.io.*;
import java.util.*;

public class InOutStreams
{
    static class OutputStreamOfRandomAccessFile extends OutputStream
    {

    	public void write(int i)
            throws IOException
        {
            file.seek(curPos);
            file.write(i);
            curPos++;
        }

    	public void write(byte abyte0[])
            throws IOException
        {
            file.seek(curPos);
            file.write(abyte0);
            curPos += abyte0.length;
        }

    	public void write(byte abyte0[], int i, int j)
            throws IOException
        {
            file.seek(curPos);
            file.write(abyte0, i, j);
            curPos += j;
        }

    	public void flush()
            throws IOException
        {
        }

    	public void close()
            throws IOException
        {
            file.close();
        }

        private RandomAccessFile file;
        private long curPos;

        private OutputStreamOfRandomAccessFile(RandomAccessFile randomaccessfile)
            throws IOException
        {
            file = randomaccessfile;
            curPos = randomaccessfile.getFilePointer();
        }
    }

    static class InputStreamOfRandomAccessFile extends InputStream
    {

    	public int read()
            throws IOException
        {
            file.seek(curPos);
            int i = file.read();
            if(i >= 0)
                curPos++;
            return i;
        }

    	public int read(byte abyte0[], int i, int j)
            throws IOException
        {
            file.seek(curPos);
            int k = file.read(abyte0, i, j);
            if(k >= 0)
                curPos += k;
            return k;
        }

    	public long skip(long l)
            throws IOException
        {
            if(l <= 0L)
                return 0L;
            long l1 = file.length() - curPos;
            if(l > l1)
                l = l1;
            curPos += l;
            return l;
        }

    	public int available()
            throws IOException
        {
            return (int)(file.length() - curPos);
        }

    	public void reset()
            throws IOException
        {
            if(markPos < 0L)
            {
                throw new IOException("Method mark() not invoked");
            } else
            {
                curPos = markPos;
                markPos = -1L;
                return;
            }
        }

    	public void mark(int i)
        {
            markPos = curPos;
        }

    	public boolean markSupported()
        {
            return true;
        }

    	public void close()
            throws IOException
        {
            file.close();
        }

        private RandomAccessFile file;
        private long curPos;
        private long markPos;

        private InputStreamOfRandomAccessFile(RandomAccessFile randomaccessfile)
        {
            file = randomaccessfile;
            curPos = 0L;
            markPos = -1L;
        }
    }

    class OutputStreamThread extends OutputStream
    {

        private void flushBlock(boolean flag)
            throws IOException
        {
            int i;
            if(flag)
                i = header.blockSize;
            else
                i = len % header.blockSize;
            int j = Compress.code(header.compressMethod, buf, i);
            IndexEntry indexentry = new IndexEntry();
            indexentry.offsetInFile = (int)outputOffset;
            indexentry.lenInFile = j;
            outputFile.write(buf, 0, j);
            outputOffset += (long)j;
            indexLst.add(indexentry);
            index.add(new Integer(indexLst.size() - 1));
        }

        public void write(int i)
            throws IOException
        {
            buf[len % header.blockSize] = (byte)i;
            len++;
            if(len % header.blockSize == 0)
                flushBlock(true);
        }

        public void write(byte abyte0[], int i, int j)
            throws IOException
        {
            if(abyte0 == null)
                throw new NullPointerException();
            if(i < 0 || i > abyte0.length || j < 0 || i + j > abyte0.length || i + j < 0)
                throw new IndexOutOfBoundsException();
            if(j == 0)
                return;
            int k = len % header.blockSize;
            while(j > 0) 
            {
                int l = j;
                int i1 = header.blockSize - k;
                if(l > i1)
                    l = i1;
                System.arraycopy(abyte0, i, buf, k, l);
                j -= l;
                i += l;
                len += l;
                k = len % header.blockSize;
                if(k == 0)
                    flushBlock(true);
            }
        }

        public void flush()
            throws IOException
        {
        }

        public void _close()
            throws IOException
        {
            if(len % header.blockSize != 0)
                flushBlock(false);
            Entry entry = new Entry();
            entry.name = name;
            entry.len = len;
            int i = index.size();
            entry.index = new int[i];
            for(int j = 0; j < i; j++)
                entry.index[j] = ((Integer)index.get(j)).intValue();

            entryHash.put(name, entry);
        }

        public void close()
            throws IOException
        {
            _close();
            entryNewHash.remove(name);
            buf = null;
        }

        private String name;
        private int len;
        private ArrayList index;
        private byte buf[];

        private OutputStreamThread(String s)
            throws IOException
        {
            if(entryNewHash.containsKey(s))
            {
                throw new IOException("Stream '" + s + "' alredy creating");
            } else
            {
                name = s;
                len = 0;
                index = new ArrayList();
                buf = new byte[header.blockSize];
                entryNewHash.put(name, this);
                return;
            }
        }
    }

    class InputStreamThread extends InputStream
    {

    	public int read()
            throws IOException
        {
            if(curPos >= entry.len)
            {
                return -1;
            } else
            {
                byte abyte0[] = getBlock(entry, curPos / header.blockSize);
                byte byte0 = abyte0[curPos % header.blockSize];
                curPos++;
                return byte0 & 0xff;
            }
        }

    	public int read(byte abyte0[], int i, int j)
            throws IOException
        {
            if(abyte0 == null)
                throw new NullPointerException();
            if(i < 0 || i > abyte0.length || j < 0 || i + j > abyte0.length || i + j < 0)
                throw new IndexOutOfBoundsException();
            if(j == 0)
                return 0;
            if(curPos >= entry.len)
                return -1;
            if(j > entry.len - curPos)
                j = entry.len - curPos;
            int k = j;
            for(int l = curPos % header.blockSize; j > 0; l = 0)
            {
                byte abyte1[] = getBlock(entry, curPos / header.blockSize);
                int i1 = j;
                if(i1 > header.blockSize - l)
                    i1 = header.blockSize - l;
                System.arraycopy(abyte1, l, abyte0, i, i1);
                i += i1;
                j -= i1;
                curPos += i1;
            }

            return k;
        }

    	public long skip(long l)
            throws IOException
        {
            if(l <= 0L)
                return 0L;
            long l1 = entry.len - curPos;
            if(l > l1)
                l = l1;
            curPos += (int)l;
            return l;
        }

    	public int available()
            throws IOException
        {
            if(entry == null)
                return 0;
            else
                return entry.len - curPos;
        }

    	public void reset()
            throws IOException
        {
            if(markPos < 0)
            {
                throw new IOException("Method mark() not invoked");
            } else
            {
                curPos = markPos;
                markPos = -1;
                return;
            }
        }

    	public void mark(int i)
        {
            markPos = curPos;
        }

    	public boolean markSupported()
        {
            return true;
        }

    	public void close()
            throws IOException
        {
            entry = null;
        }

        private Entry entry;
        private int curPos;
        private int markPos;

        private InputStreamThread(Entry entry1)
        {
            entry = entry1;
            curPos = 0;
            markPos = -1;
        }
    }

    static class Block
    {

        private Block next;
        private Entry entry;
        private int index;
        private byte buf[];

        Block()
        {
            index = -1;
        }
    }

    static class Entry
    {

        private String name;
        private int len;
        private int index[];

        Entry()
        {
        }
    }

    static class IndexEntry
    {

        private static int sizeInFile()
        {
            return 4;
        }

        private int offsetInFile;
        private int lenInFile;

        IndexEntry()
        {
        }
    }

    static class Tailer
    {

        private static int sizeInFile()
        {
            return 8;
        }

        private int indexesSizeInFile;
        private int entrysSizeInFile;

        Tailer()
        {
        }
    }

    static class Header
    {

        private static int sizeInFile()
        {
            return 16;
        }

        private int signature;
        private int version;
        private int compressMethod;
        private int blockSize;

        Header()
        {
        }
    }


    private void freeResorces()
    {
        header = null;
        tailer = null;
        indexLst.clear();
        indexLst = null;
        entryHash.clear();
        entryHash = null;
        while(listBlocks != null) 
        {
            Block block = listBlocks;
            listBlocks = block.next;
            block.next = null;
        }
        if(entryNewHash != null)
        {
            entryNewHash.clear();
            entryNewHash = null;
        }
    }

    private void readBlock(Entry entry, int i, byte abyte0[])
        throws IOException
    {
        inputFile.reset();
        inputFile.mark(inputFile.available());
        IndexEntry indexentry = (IndexEntry)indexLst.get(entry.index[i]);
        inputFile.skip(indexentry.offsetInFile);
        inputFile.read(abyte0, 0, indexentry.lenInFile);
        int j = header.blockSize;
        if(header.blockSize * (i + 1) > entry.len)
            j = entry.len % header.blockSize;
        if(header.compressMethod != 0 && j > indexentry.lenInFile)
            Compress.decode(header.compressMethod, abyte0, indexentry.lenInFile);
    }

    private byte[] getBlock(Entry entry, int i)
        throws IOException
    {
        Block block = listBlocks;
        Block block1 = null;
        if(block == null)
        {
            int j = readCacheSize / header.blockSize;
            if(j < 1)
                j = 1;
            for(int k = 0; k < j; k++)
            {
                block = new Block();
                block.buf = new byte[header.blockSize];
                block.next = listBlocks;
                listBlocks = block;
            }

        } else
        {
            for(; block.next != null; block = block.next)
            {
                if(block.entry == entry && block.index == i)
                    break;
                block1 = block;
            }

            if(block1 != null)
            {
                block1.next = block.next;
                block.next = listBlocks;
                listBlocks = block;
            }
        }
        if(block.entry != entry || block.index != i)
        {
            readBlock(entry, i, block.buf);
            block.entry = entry;
            block.index = i;
        }
        return block.buf;
    }

    public InputStream openStream(String s)
    {
        Entry entry = (Entry)entryHash.get(s);
        if(entry == null)
            return null;
        else
            return new InputStreamThread(entry);
    }

    public OutputStream createStream(String s)
        throws IOException
    {
        return new OutputStreamThread(s);
    }

    public void getEntryNames(List list)
    {
        if(list == null)
            return;
        if(entryHash == null)
            return;
        for(java.util.Map.Entry entry = entryHash.nextEntry(null); entry != null; entry = entryHash.nextEntry(entry))
            list.add(entry.getKey());

    }

    private void outputFlush()
        throws IOException
    {
        for(java.util.Map.Entry entry = entryNewHash.nextEntry(null); entry != null; entry = entryNewHash.nextEntry(entry))
        {
            OutputStreamThread outputstreamthread = (OutputStreamThread)entry.getValue();
            outputstreamthread._close();
        }

        entryNewHash.clear();
        DataOutputStream dataoutputstream = new DataOutputStream(outputFile);
        tailer = new Tailer();
        int i = indexLst.size();
        for(int j = 0; j < i; j++)
        {
            IndexEntry indexentry = (IndexEntry)indexLst.get(j);
            dataoutputstream.writeInt(indexentry.lenInFile);
        }

        tailer.indexesSizeInFile = i * IndexEntry.sizeInFile();
        tailer.entrysSizeInFile = dataoutputstream.size();
        for(java.util.Map.Entry entry1 = entryHash.nextEntry(null); entry1 != null; entry1 = entryHash.nextEntry(entry1))
        {
            Entry entry2 = (Entry)entry1.getValue();
            dataoutputstream.writeUTF(entry2.name);
            dataoutputstream.writeInt(entry2.len);
            dataoutputstream.writeInt(entry2.index.length);
            for(int k = 0; k < entry2.index.length; k++)
                dataoutputstream.writeInt(entry2.index[k]);

        }

        tailer.entrysSizeInFile = dataoutputstream.size() - tailer.entrysSizeInFile;
        dataoutputstream.writeInt(tailer.indexesSizeInFile);
        dataoutputstream.writeInt(tailer.entrysSizeInFile);
        dataoutputstream.flush();
    }

    private void open()
        throws IOException
    {
        DataInputStream datainputstream = new DataInputStream(inputFile);
        header = new Header();
        header.signature = datainputstream.readInt();
        if(header.signature != 0x4a3b2c1d)
            throw new IOException("File corrupted: bad signature");
        header.version = datainputstream.readInt();
        if(header.version != 101)
            throw new IOException("File corrupted: unknown version");
        header.compressMethod = datainputstream.readInt();
        if(header.compressMethod < 0 || header.compressMethod > 2)
            throw new IOException("File corrupted: unknown compression method");
        header.blockSize = datainputstream.readInt();
        if(header.blockSize < 2048 || header.blockSize > 0x20000)
            throw new IOException("File corrupted: bad block size");
        inputFile.mark(inputFile.available());
        inputFile.skip(inputFile.available() - Tailer.sizeInFile());
        tailer = new Tailer();
        tailer.indexesSizeInFile = datainputstream.readInt();
        tailer.entrysSizeInFile = datainputstream.readInt();
        inputFile.reset();
        inputFile.mark(inputFile.available());
        inputFile.skip(inputFile.available() - Tailer.sizeInFile() - tailer.entrysSizeInFile - tailer.indexesSizeInFile);
        int i = tailer.indexesSizeInFile / IndexEntry.sizeInFile();
        indexLst = new ArrayList(i);
        int j = 0;
        for(int k = 0; k < i; k++)
        {
            IndexEntry indexentry = new IndexEntry();
            indexentry.offsetInFile = j;
            indexentry.lenInFile = datainputstream.readInt();
            j += indexentry.lenInFile;
            indexLst.add(indexentry);
        }

        entryHash = new HashMapExt();
        Entry entry;
        for(; inputFile.available() > Tailer.sizeInFile(); entryHash.put(entry.name, entry))
        {
            entry = new Entry();
            entry.name = datainputstream.readUTF();
            entry.len = datainputstream.readInt();
            int l = datainputstream.readInt();
            entry.index = new int[l];
            for(int i1 = 0; i1 < l; i1++)
                entry.index[i1] = datainputstream.readInt();

        }

    }

    private void create(int i, int j)
        throws IOException
    {
        if(i < 0 || i > 2)
            throw new IOException("unknown compression method");
        if(j < 2048 || j > 0x20000)
        {
            throw new IOException("bad block size");
        } else
        {
            DataOutputStream dataoutputstream = new DataOutputStream(outputFile);
            header = new Header();
            header.signature = 0x4a3b2c1d;
            header.version = 101;
            header.compressMethod = i;
            header.blockSize = j;
            dataoutputstream.writeInt(header.signature);
            dataoutputstream.writeInt(header.version);
            dataoutputstream.writeInt(header.compressMethod);
            dataoutputstream.writeInt(header.blockSize);
            dataoutputstream.flush();
            outputOffset = 0L;
            indexLst = new ArrayList();
            entryHash = new HashMapExt();
            entryNewHash = new HashMapExt();
            return;
        }
    }

    private void open(InputStream inputstream)
        throws IOException
    {
        if(randomAccessFile != null || inputFile != null || outputFile != null)
            throw new IOException("file alredy opened");
        if(!inputstream.markSupported())
        {
            throw new IOException("inputFile mark/reset not supported");
        } else
        {
            inputFile = inputstream;
            open();
            return;
        }
    }

    private void create(OutputStream outputstream, int i, int j)
        throws IOException
    {
        if(randomAccessFile != null || inputFile != null || outputFile != null)
        {
            throw new IOException("file alredy opened");
        } else
        {
            outputFile = outputstream;
            create(i, j);
            return;
        }
    }

    private void create(OutputStream outputstream)
        throws IOException
    {
        create(outputstream, 2, 32768);
    }

    public void open(File file, boolean flag)
        throws IOException
    {
        if(randomAccessFile != null || inputFile != null || outputFile != null)
            throw new IOException("file alredy opened");
        if(flag)
        {
            RandomAccessFile randomaccessfile = new RandomAccessFile(file, "rw");
            if(randomaccessfile.length() == 0L)
            {
                OutputStreamOfRandomAccessFile outputstreamofrandomaccessfile = new OutputStreamOfRandomAccessFile(randomaccessfile);
                create(outputstreamofrandomaccessfile);
            } else
            {
                InputStreamOfRandomAccessFile inputstreamofrandomaccessfile = new InputStreamOfRandomAccessFile(randomaccessfile);
                open(((InputStream) (inputstreamofrandomaccessfile)));
                randomaccessfile.seek(randomaccessfile.length() - (long)Tailer.sizeInFile() - (long)tailer.entrysSizeInFile - (long)tailer.indexesSizeInFile);
                outputFile = new OutputStreamOfRandomAccessFile(randomaccessfile);
                outputOffset = randomaccessfile.length() - (long)Header.sizeInFile() - (long)Tailer.sizeInFile() - (long)tailer.entrysSizeInFile - (long)tailer.indexesSizeInFile;
                entryNewHash = new HashMapExt();
            }
            randomAccessFile = randomaccessfile;
        } else
        {
            RandomAccessFile randomaccessfile1 = new RandomAccessFile(file, "r");
            InputStreamOfRandomAccessFile inputstreamofrandomaccessfile1 = new InputStreamOfRandomAccessFile(randomaccessfile1);
            open(((InputStream) (inputstreamofrandomaccessfile1)));
            randomAccessFile = randomaccessfile1;
        }
    }

    public void close()
        throws IOException
    {
        if(randomAccessFile == null && inputFile == null && outputFile == null)
            return;
        if(randomAccessFile != null)
        {
            if(outputFile != null)
                outputFlush();
            randomAccessFile.close();
        } else
        if(outputFile != null)
        {
            outputFlush();
            outputFile.close();
        } else
        {
            inputFile.close();
        }
        randomAccessFile = null;
        inputFile = null;
        outputFile = null;
        freeResorces();
    }

    public InOutStreams()
    {
        listBlocks = null;
        readCacheSize = 32768;
    }

    private RandomAccessFile randomAccessFile;
    private InputStream inputFile;
    private OutputStream outputFile;
    private long outputOffset;
    private Header header;
    private Tailer tailer;
    private ArrayList indexLst;
    private HashMapExt entryHash;
    private HashMapExt entryNewHash;
    private Block listBlocks;
    private int readCacheSize;








}
