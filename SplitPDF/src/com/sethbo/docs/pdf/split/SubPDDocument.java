package com.sethbo.docs.pdf.split;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class SubPDDocument extends PDDocument {
	
    public static final String PDF = "pdf";

	private final PDDocument source;
	private final int startPageNumber;
	private final int finishPageNumber;
	private final String fileName;

	public SubPDDocument(PDDocument source, int startPageNumber, int finishPageNumber, String fileName) {
		this.source = source;
		/* Adjusted down one for internal representation zero based, inclusive*/
		this.startPageNumber = startPageNumber - 1;
		/* Not adjusted for internal representation zero based, exclusive*/
		this.finishPageNumber = finishPageNumber;
		this.fileName = fileName;
	}

	public PDDocument getSource() {
		return source;
	}

	public int getStartPageNumber() {
		return startPageNumber;
	}

	public int getFinishPageNumber() {
		return finishPageNumber;
	}

	public String getFileName() {
		return fileName;
	}

	protected void addPages() {
		for (int pageNum = startPageNumber; pageNum < finishPageNumber; pageNum++) {
			Object page = source.getDocumentCatalog().getAllPages().get(pageNum);
			if (page instanceof PDPage) {
				this.addPage((PDPage)page);
			}
		}
	}

	public static void splitDocuments(List<SubPDDocument> subDocuments, File outputDir) throws IOException, COSVisitorException {
		for (SubPDDocument subDocument : subDocuments) {
			try {
				subDocument.addPages();
				subDocument.save(outputDir.getAbsolutePath() + File.separator + subDocument.getFileName() + "." + PDF);
				subDocument.close();
			} catch (Exception ex) {
				System.err.println("splt error\t" + ex);
			} finally {
				if (subDocument != null) {
					subDocument.close();
				}
			}
		}
	}
}
