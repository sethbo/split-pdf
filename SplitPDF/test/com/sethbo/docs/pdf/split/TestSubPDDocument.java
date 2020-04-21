package com.sethbo.docs.pdf.split;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;

public class TestSubPDDocument {
	
	@Test
	public void testSplit() throws IOException, COSVisitorException {
		Path pathResource = Paths.get("./resources/Resource1.pdf");
		Path pathOutput = Paths.get("./resources/output/");
		PDDocument source = null;
		List<SubPDDocument> subDocuments = new ArrayList<>();
		try {
			source = PDDocument.load(pathResource.toFile());
			
			subDocuments.add(new SubPDDocument(source, 5, 6, "Lesson 1")); 
			subDocuments.add(new SubPDDocument(source, 7, 9, "Lesson 2")); 
			subDocuments.add(new SubPDDocument(source, 10, 11, "Lesson 3")); 
			
			SubPDDocument.splitDocuments(subDocuments, pathOutput.toFile());
			
			// verify number of split documents
			assertEquals(3, subDocuments.size());
			
			File file1 = Paths.get("./resources/output/", "Lesson 1.pdf").toFile();
			File file2 = Paths.get("./resources/output/", "Lesson 2.pdf").toFile();
			File file3 = Paths.get("./resources/output/", "Lesson 3.pdf").toFile();
			
			// verify split document exist
			assertTrue(file1.exists());
			assertTrue(file2.exists());
			assertTrue(file3.exists());
			
			// verify page numbers of split document			
			assertEquals(2, countPDFPages(file1));
			assertEquals(3, countPDFPages(file2));
			assertEquals(2, countPDFPages(file3));
			
			// remove split documents
			file1.deleteOnExit();
			file2.deleteOnExit();
			file3.deleteOnExit();
		} finally {
			if (source != null) {
				source.close();
			}
		}
	}

	private int countPDFPages(File file) throws IOException {
		PDDocument document = PDDocument.load(file);
		return document.getDocumentCatalog().getAllPages().size();		
	}
}
