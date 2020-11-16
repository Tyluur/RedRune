package org.redrune.utility.backend;

import org.cryptonode.jncryptor.AES256JNCryptor;
import org.cryptonode.jncryptor.CryptorException;
import org.cryptonode.jncryptor.JNCryptor;
import org.redrune.utility.tool.Misc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * This class handles the operations that encrypt/decrypt, as well as compress/decompress data. The usage is mostly for
 * strings but implementation for other forms of data can easily be extended. This operation is necessary to reduce the
 * size of transmission over the network. The procedure for transmission is as follows <ol> <li>Compress the
 * data</li><li>Encrypt the data using a secure key</li> <li>Send the smaller data over the network</li></ol> Now once
 * the server has received the data, it will be converted to real text like this: <ol><li>Decompress the data to produce
 * the encrypted data</li> <li>Decrypt the encrypted data with our secure key to ensure we can decode it</li><li>Once we
 * have the plain text data, we can safely convert it to a player file.</li></ol>
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public class SecureOperations {
	
	/**
	 * The instance of the cryptor
	 */
	private static final JNCryptor CRYPTOR = new AES256JNCryptor();
	
	public static void main(String[] args) throws CryptorException, IOException, DataFormatException {
		String text = Misc.getText("./data/saves/tyluur.json");
		String key = "KEYKEYKEYKEYKEYKEYKEYKEYKEY";
		
		byte[] compressedEncrypted = getCompressedEncrypted(text, key);
		
		byte[] decryptedDecompressed = getDecryptedDecompressed(compressedEncrypted, key);
		
		String newText = new String(decryptedDecompressed, "UTF-8");
		System.out.println(newText.equals(text) ? "SUCCESS" : "FAILURE");
	}
	
	/**
	 * This method will first compress the text, and then encrypt it to return a much smaller and safe-to-transmit byte
	 * array data form
	 *
	 * @param text
	 * 		The text
	 * @param key
	 * 		The key to use during the encryption procedure
	 */
	public static byte[] getCompressedEncrypted(String text, String key) {
		try {
			byte[] compressed = getCompressedFromBytes(text.getBytes());
			return getEncryptedFromBytes(compressed, key);
		} catch (CryptorException | IOException e) {
			throw new RuntimeException("Unable to compress and encrypt file!", e);
		}
	}
	
	/**
	 * This method will first decrypt the data and then decompress it.
	 *
	 * @param data
	 * 		The data to decrypt
	 * @param key
	 * 		The key to use when decrypting
	 */
	public static byte[] getDecryptedDecompressed(byte[] data, String key) {
		try {
			byte[] decrypted = getDecryptedFromBytes(data, key);
			return getDecompressedFromBytes(decrypted);
		} catch (CryptorException | IOException | DataFormatException e) {
			throw new RuntimeException("Unable to decompress and decrypt file!", e);
		}
	}
	
	/**
	 * Encrypts the text using AES-256 encryption
	 *
	 * @param text
	 * 		The text
	 * @param key
	 * 		The key to compress with
	 */
	public static byte[] getEncryptedFromText(String text, String key) throws CryptorException {
		return CRYPTOR.encryptData(text.getBytes(), key.toCharArray());
	}
	
	/**
	 * Encrypts the bytes using AES-256 encryption
	 *
	 * @param bytes
	 * 		The text
	 * @param key
	 * 		The key to compress with
	 */
	public static byte[] getEncryptedFromBytes(byte[] bytes, String key) throws CryptorException {
		return CRYPTOR.encryptData(bytes, key.toCharArray());
	}
	
	/**
	 * Compresses a byte array
	 *
	 * @param data
	 * 		The byte array
	 * @return A compressed form of the byte array
	 */
	public static byte[] getCompressedFromBytes(byte[] data) throws IOException {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		deflater.finish();
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer); // returns the generated code... index
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();
//		System.out.println("Original: " + data.length);
//		System.out.println("Compressed: " + output.length);
		return output;
	}
	
	/**
	 * Decompresses a byte array
	 *
	 * @param data
	 * 		The byte array
	 * @return A decompressed byte array
	 */
	public static byte[] getDecompressedFromBytes(byte[] data) throws IOException, DataFormatException {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!inflater.finished()) {
			int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();
//		System.out.println("Original: " + data.length);
//		System.out.println("Decompressed: " + output.length);
		return output;
	}
	
	/**
	 * Decrypts a byte array using AES-256 for algorithmic purposes
	 *
	 * @param data
	 * 		The data to decrypt
	 * @param key
	 * 		The key to use when decrypting
	 */
	public static byte[] getDecryptedFromBytes(byte[] data, String key) throws CryptorException {
		return CRYPTOR.decryptData(data, key.toCharArray());
	}
	
}