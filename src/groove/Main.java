package groove;

import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;

public class Main {

	
	static class Rgb {
		public byte r;
		public byte g;
		public byte b;
	}
	
	static Rgb wordToRgb(short word) {
		Rgb rgb = new Rgb();
	
		// rgb 565
		/*
		rgb.r = (byte) (( (word & 0xF800 ) >> 11) << 3);	
		rgb.g = (byte) (( (word & 0x7E0 ) >> 5) << 2);
		rgb.b = (byte) (( (word & 0x1F ) ) << 3);
		*/
		
		// rgb 555
		rgb.r = (byte) (( (word & 0x7C00 ) >> 10) << 3);	
		rgb.g = (byte) (( (word & 0x3E0 ) >> 5) << 3);
		rgb.b = (byte) (( (word & 0x1F ) ) << 3);
		
		
		
		return rgb;
	}

	static DataInputStream  in;

	static void skip(long len) throws IOException {
		
		
		byte[] buf = new byte[(int)len];
		int act = in.read(buf);
		
		
	}
	
	
	static void dump(long len, String dumpFile) throws IOException {
		/*
		long actuallyRead = 0;
		
		while(actuallyRead < len) {
			long read = in.skip(len - actuallyRead);
			
		//	print("skip: " + read);
			
			actuallyRead += read;
		}
		*/
		
		byte[] buf = new byte[(int)len];
		int act = in.read(buf);
		
		
		FileOutputStream fos = new FileOutputStream(dumpFile);
		fos.write(buf);
		fos.close();
		
		
		
	}
	
	public static String hex(byte n) {
		
		return (Integer.toString((n & 0xff) + 0x100, 16).substring(1));
		
	//	
		//  return Integer.toHexString(n);
	}
	
	private static String toHex(final int val) {
		return String.format("%02X", val);
	}
	
	static String readString(int len) throws IOException {
		
		byte[] buf = new byte[len];
		
		in.read(buf, 0, len);
		
		
		return new String(buf, "ascii");
	}
	
	public static int readInt() throws IOException {
		final byte b1 = in.readByte();
		final byte b2 = in.readByte();
		final byte b3 = in.readByte();
		final byte b4 = in.readByte();

		return toInt(b1,b2,b3,b4);
	}

	public static short readShort() throws IOException {
		final byte b1 = in.readByte();
		final byte b2 = in.readByte();

		return toShort(b1,b2);
	}
	
	public static int toInt(final byte b1, final byte b2, final byte b3, final byte b4) {

		return ((b4&0xFF) << 24) | ((b3&0xFF) << 16) | ((b2&0xFF) << 8) | (b1&0xFF);
	}

	public static short toShort(final byte b1, final byte b2) {

		return (short) (((b2&0xFF) << 8) | (b1&0xFF));
	}
	
	
	static void print(String str) {
		System.out.println(str);
	}
	
	static void createDir(String dir) {

		File theDir = new File(dir);

		// if the directory does not exist, create it

		if (!theDir.exists()) {
			theDir.mkdir();
		}

	}
	
	public static void main(String [ ] args) throws IOException, NoSuchAlgorithmException{
	
		String path = "D:\\Setup/eng/cd/wads/";
		
		
	//	String target =  "t1l6m003.wad";
	//	String target =  "t1l1m001.wad";
		
		String target =  "t6l6m002.wad";
		
		
		
	//	String target =  "t1l1m001.wad";
	//	String target =  "t1l1m002.wad";
		//String target =  "t1l1m004.wad";
		
		//String target =  "t1l2m001.wad";
		//String target =  "t1l2m003.wad";
		
		
		String file = path + target;
		//String file = path + "T0i0M000.wad";
		
		String basefile = target.substring(0, target.length() -4);
		print("base: "+ basefile);
		
	    in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
	    
	    // skip checksum(?)
	    skip(4);
	    
	    createDir(basefile);
	    
	    String chunkName;
	    long chunkLen;
	    
	    do {
	    
	    	
	    	chunkName = readString(4);
	    	
	    	print(chunkName);
		    
		     chunkLen = readInt();
		     
		     print("chunkLen: " + chunkLen);
		     
		     if(chunkName.equals("TXET") ) {
		    	// print("IFFFF");
		    	 chunkLen += 0;
		     }
		    	
		     
		     dump(chunkLen,  basefile + "/" + basefile + "." + chunkName);
		     
		     
		     
	    }while( !chunkName.equals( " DNE") );
	    
	    
	    // now unpack CPMS-chunk.
	    
	    String cpmsFile = basefile + ".CPMS";
		print("now unpacking CPMS: "+ cpmsFile);
		
		in = new DataInputStream(new BufferedInputStream(new FileInputStream(basefile + "/" + cpmsFile)));
	    
		
		int numChunks = readInt();
	    
		print("num chunks: "+ numChunks);
		
		
		
		
		createDir(basefile + "/" + "cpms" + "/");
	    
		
		for(int i = 0; i < numChunks; ++i) {
		
			readInt(); // skip cvg header.
			
			chunkLen = readInt();
			
			print("chunk len: "+ chunkLen);
			
			dump(chunkLen,  basefile + "/" + "cpms" + "/" + cpmsFile  + "." + i );
		     
		}
		
		
		
		
		
		
		
		  
		String textFile = basefile + ".TXET";
			
		print("now unpacking TEXT: "+ textFile);
		in = new DataInputStream(new BufferedInputStream(new FileInputStream(basefile + "/" + textFile)));
	    
		readInt(); // skip zero.
	    
		createDir(basefile + "/" + "text" + "/");
	    
		int numTexts = readInt();
		
		
	    print("num: " +numTexts);
	    
	    
	    
	    for(int i = 0; i < numTexts; ++i) {
	    
	    	int flags = readInt();
		    int width = readInt();
		    int height = readInt();
		    int size = readInt();
		    
		    print("texture: " + i);
	    	
	    	print(" flags: " + flags + "(" + toHex(flags) + ")" );
		    print(" wdith: " + width);
		    print(" height: " + height);
		    print(" size: " + size);
		    
		    
		    
		    
		    byte[] buf = new byte[(int)size];
			int act = in.read(buf);
			
			
			
			FileOutputStream fos = new FileOutputStream( basefile + "/" + "text" + "/" + textFile  + "." + i );
			fos.write(buf);
			fos.close();
			
			if(/*i == 0*/  flags == 132 || flags == 135 || flags == 129 || flags==130 || flags == 133 ) {
				
				DataInputStream old = in;
	
				in = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(buf)));
			
				//int c= 0;
				
				int wordCount = 0;
				
				 
				
				int uncompressedSize = width * height;
				
				 
				byte[] bytes = new byte[uncompressedSize*3];
				int iBytes = 0;
				   
				
				while(in.available() != 0) {
					
					short runLength = readShort();
					
				    //print("package hex "  + toHex(runLength) );
					
					if( (runLength & 0x8000) != 0 ) {
						
						short repeatedWord = readShort();
					    final Rgb rgb = wordToRgb(repeatedWord);

					    final short max = (short) 0xFFFF;
					    
					   // print("diff: " +  (max - runLength) );
					    
					    //short repeatedLength = (short) ((max - runLength + 1) * 2);
					    int repeatedLength = (int) ((max - runLength + 1));
					    
					    
					    /*
					    if(   ((max - runLength + 1) & 0x0001)  != 0 ) {
					    	l += 2;
					    }*/
					    
					    //print("compress package, len = : " + l + " data =  " + toHex(repeatedWord) );
					    
					    wordCount +=repeatedLength;
					    
					    for(int j = 0; j < repeatedLength; ++j ) {
					    	bytes[iBytes++] = rgb.r;
					    	bytes[iBytes++] = rgb.g;
					    	bytes[iBytes++] = rgb.b;
					    }
					 
					    
					} else {
						//print("uncompressed package " + runLength);
						
						for(int j = 0; j < runLength; ++j) {
							
							short word = readShort();
						    final Rgb rgb = wordToRgb(word);
						    
						    bytes[iBytes++] = rgb.r;
					    	bytes[iBytes++] = rgb.g;
					    	bytes[iBytes++] = rgb.b;

							wordCount += 1;
							//print("skip: " + toHex(readShort())   );
							
						}
						
					}
					/*
					++c;
					
					if(c > 3)
						break;
					*/
				}

				print("count: " + wordCount);
				print("s1: " + wordCount * 3);
				print("s2: " + iBytes);
				
				 
			    writeImage(bytes, width, height,  basefile + "/" + "text" + "/" + textFile  + "." + i + ".png");
				 
				 
				 in = old;
				 
			}
		    
		    //dump(size,  basefile + "/" + "text" + "/" + textFile  + "." + i  );
		     
		    
		    
		    print("");
		    
		   // skip(size);
	    }

	    /*
	    int rem = readInt();

	    print("rem: " + toHex(rem));
	    
	    skip(rem);

	    rem = readInt();

	    print("rem: " + toHex(rem));
	    */
	    
	    
	    
	    
	    /*
	    String oneTextFile = basefile + "/" + "text" + "/" + textFile  + "." + 0 ;
	    
	    print("now reading " + oneTextFile);
	    
	    in = new DataInputStream(new BufferedInputStream(new FileInputStream(oneTextFile)));
	    */
	    
	    /*
	    short runLength = readShort();
	    
	    
	    
	    
	    */
	    
	    //byte[] aByteArray = {0xa,0x2,0xf,(byte)0xff,(byte)0xff,(byte)0xff};
	    

	    /*
	    int width = 256;
	    int height = 256;
	    
	  byte[] bytes = new byte[width * height*3];
	    
	    
	
	  for(int i = 0; i < width * height; ++i ) {
		  
		  bytes[3*i + 0] = i < 2000 ?  (byte)0xff : (byte)0x66;
		  bytes[3*i + 1] = (byte)0x00;
		  bytes[3*i + 2] = (byte)0x00;
	  }
	  

	    
	    writeImage(bytes, width, height);
	    */
	}
	
	
	static void writeImage(byte[] bytes, int width, int height, String outImage ) throws IOException {
		
	    DataBuffer buffer = new DataBufferByte(bytes, bytes.length);

	    //3 bytes per pixel: red, green, blue
	    WritableRaster raster = Raster.createInterleavedRaster(buffer, width, height, 3 * width, 3, new int[] {0, 1, 2}, (Point)null);
	    ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE); 
	    BufferedImage image = new BufferedImage(cm, raster, true, null);

	    ImageIO.write(image, "png", new File(outImage));
		
	}
		
	
}
