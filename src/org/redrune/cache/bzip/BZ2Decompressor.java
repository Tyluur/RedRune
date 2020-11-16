package org.redrune.cache.bzip;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BZ2Decompressor {
	
	public static void decompress(int slen, int off, byte[] in, byte[] out) throws IOException {
		byte in2[] = new byte[slen];
		System.arraycopy(in, off, in2, 0, slen);
		try (DataInputStream ins = new DataInputStream(new CBZip2InputStream(new ByteArrayInputStream(in2), 0))) {
			ins.readFully(out);
		}
	}
}