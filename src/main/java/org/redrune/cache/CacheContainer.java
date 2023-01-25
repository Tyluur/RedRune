package org.redrune.cache;

import org.redrune.cache.bzip.BZ2Decompressor;
import org.redrune.cache.bzip.GZIPDecompressor;
import org.redrune.utility.tool.BufferUtils;

import java.io.IOException;

public class CacheContainer {
	
	private byte[] bytes;
	
	private int length;
	
	private byte compression;
	
	private int decompressedLength;
	
	public CacheContainer(byte[] bytes) throws IOException {
		this.bytes = bytes;
		compression = bytes[0];
		length = BufferUtils.readInt(1, bytes);
		if (compression != 0) {
			decompressedLength = BufferUtils.readInt(5, bytes);
		} else {
			decompressedLength = length;
		}
	}
	
	public byte[] decompress() throws Exception {
		if (decompressedLength > 1000000 || decompressedLength < 0) {
			return null;
		}
		byte[] data = new byte[decompressedLength];
		switch (compression) {
			case 0:
				System.arraycopy(bytes, 5, data, 0, decompressedLength);
				break;
			case 1:
				BZ2Decompressor.decompress(length, 9, bytes, data);
				break;
			default:
				GZIPDecompressor.decompress(length, 9, bytes, data);
				break;
		}
		return data;
	}
	
}