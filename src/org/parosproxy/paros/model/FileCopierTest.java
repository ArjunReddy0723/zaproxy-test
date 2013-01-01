package org.parosproxy.paros.model;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

public class FileCopierTest {
	
	private FileCopier fileCopier;
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		fileCopier = new FileCopier();
	}

	@Test
	public void shouldCopyFileViaPreJava7IO() throws Exception {
		// Given
		File source = tempFolder.newFile();
		FileUtils.writeStringToFile(source, "Test");
		File target = tempFolder.newFile();
		// When
		fileCopier.copyLegacy(source, target);
		// Then
		assertTrue(FileUtils.contentEquals(source, target));
	}

	@Test
	public void shouldCopyFileViaNIO() throws Exception {
		// Given
		File source = tempFolder.newFile();
		FileUtils.writeStringToFile(source, "Test");
		File target = tempFolder.newFile();
		// When
		fileCopier.copyNIO(source, target);
		// Then
		assertTrue(FileUtils.contentEquals(source, target));
	}
	
	@Test
	public void shouldFallbackToPreJava7IOIfNIOFails() throws Exception {
		// Given
		FileCopier fileCopierStub = Mockito.spy(fileCopier);
		doThrow(IOException.class).when(fileCopierStub).copyNIO(any(File.class), any(File.class));
		File source = tempFolder.newFile();
		FileUtils.writeStringToFile(source, "Test");
		File target = tempFolder.newFile();
		// When
		fileCopierStub.copy(source, target);
		// Then
		assertTrue(FileUtils.contentEquals(source, target));
	}

}
