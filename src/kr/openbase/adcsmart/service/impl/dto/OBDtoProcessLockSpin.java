package kr.openbase.adcsmart.service.impl.dto;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class OBDtoProcessLockSpin {
	private FileLock fileLock = null;
	private FileChannel fileChannel = null;
	private File file = null;
	private RandomAccessFile randomAccessFile = null;

	@Override
	public String toString() {
		return "OBDtoProcessLockSpin [fileLock=" + fileLock + ", fileChannel=" + fileChannel + ", file=" + file + "]";
	}

	public RandomAccessFile getRandomAccessFile() {
		return randomAccessFile;
	}

	public void setRandomAccessFile(RandomAccessFile randomAccessFile) {
		this.randomAccessFile = randomAccessFile;
	}

	public FileLock getFileLock() {
		return fileLock;
	}

	public void setFileLock(FileLock fileLock) {
		this.fileLock = fileLock;
	}

	public FileChannel getFileChannel() {
		return fileChannel;
	}

	public void setFileChannel(FileChannel fileChannel) {
		this.fileChannel = fileChannel;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
