package grant.farnsworth.cminusparcer.assemblygen;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Program {
	private List<String> dataSection;
	private List<String> textSection;
	private LinkedList<String> bssSection;
	public Program() {
		super();
		this.dataSection = new LinkedList<>();
		this.textSection = new LinkedList<>();
		this.bssSection = new LinkedList<>();
		
		dataSection.add(".text");
		dataSection.add(".char_wformat: .string \"%c\\n\"");
		dataSection.add(".int_wformat: .string \"%d\\n\"");
		dataSection.add(".float_wformat: .string \"%f\\n\"");
		dataSection.add(".str_wformat: .string \"%s\\n\"");
		dataSection.add(".char_rformat: .string \"%c\"");
		dataSection.add(".int_rformat: .string \"%d\"");
		dataSection.add(".float_rformat: .string \"%f\"");
		
	}
	
	public void addDataSection(String line) {
		this.dataSection.add(line);
	}
	public void addTextSection(String line) {
		this.textSection.add(line);
	}
	public void addBssSection(String line) {
		this.bssSection.add(line);
	}
	
	public void addAllDataSection(List<String> lines) {
		this.dataSection.addAll(lines);
	}
	public void addAllTextSection(List<String> lines) {
		this.textSection.addAll(lines);
	}
	public void addAllBssSection(List<String> lines) {
		this.bssSection.addAll(lines);
	}
	
	
	public void writeAssemblyFile(File out) throws IOException{
		RandomAccessFile writer = new RandomAccessFile(out,"rw");
		FileChannel outChannel = writer.getChannel();
		
		outChannel.write(ByteBuffer.wrap("section .data".getBytes()));
		for(String s:dataSection) {
			outChannel.write(ByteBuffer.wrap(("\n\t"+s).getBytes()));
		}
		
		outChannel.write(ByteBuffer.wrap("\nsection .bss".getBytes()));
		for(String s:bssSection) {
			outChannel.write(ByteBuffer.wrap(("\n\t"+s+"\n").getBytes()));
		}
		
		outChannel.write(ByteBuffer.wrap("\nsection .text".getBytes()));
		for(String s:textSection) {
			outChannel.write(ByteBuffer.wrap(("\n\t"+s).getBytes()));
		}
		
		outChannel.close();
		
		writer.close();
	}
}
