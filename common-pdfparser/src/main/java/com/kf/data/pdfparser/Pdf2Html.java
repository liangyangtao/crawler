package com.kf.data.pdfparser;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDCIDFontType2;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType3CharProc;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.pdmodel.font.PDVectorFont;
import org.apache.pdfbox.pdmodel.interactive.pagenavigation.PDThreadBead;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;

/***
 * 
 * @author meidi
 *
 */
public class Pdf2Html extends PDFTextStripper {

	private final String filename;
	static final int SCALE = 4;
	private final PDDocument document;
	private static StringBuffer pageBuffer = new StringBuffer();

	public Pdf2Html(PDDocument document, String filename) throws IOException {
		this.document = document;
		this.filename = filename;
	}

	public static void main(String[] args) throws IOException {
		try (PDDocument document = PDDocument.load(new File("c://abc.pdf"))) {
			Pdf2Html stripper = new Pdf2Html(document, "c://aa.pdf");
			stripper.setSortByPosition(true);
			for (int page = 0; page < 1; page++) {
				stripper.stripPage(page);
			}
			System.out.println(pageBuffer);
		}
	}

	private void stripPage(int page) throws IOException {
		pageBuffer.append("<div class =\"page" + (page + 1) + "\">");
		PDPage pdPage = document.getPage(page);
		PDRectangle cropBox = pdPage.getCropBox();
		int rotation = pdPage.getRotation();
		setStartPage(page + 1);
		setEndPage(page + 1);
		Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
		writeText(document, dummy);
		// beads in green
		List<PDThreadBead> pageArticles = pdPage.getThreadBeads();
		// for (PDThreadBead bead : pageArticles) {
		// PDRectangle r = bead.getRectangle();
		// }
		pageBuffer.append("</div>");

	}

	@Override
	protected void showGlyph(Matrix textRenderingMatrix, PDFont font, int code, String unicode, Vector displacement)
			throws IOException {
		super.showGlyph(textRenderingMatrix, font, code, unicode, displacement);

		// in cyan:
		// show actual glyph bounds. This must be done here and not in
		// writeString(),
		// because writeString processes only the glyphs with unicode,
		// see e.g. the file in PDFBOX-3274
		Shape cyanShape = calculateGlyphBounds(textRenderingMatrix, font, code);

	}

	// this calculates the real (except for type 3 fonts) individual glyph
	// bounds
	private Shape calculateGlyphBounds(Matrix textRenderingMatrix, PDFont font, int code) throws IOException {
		GeneralPath path = null;
		AffineTransform at = textRenderingMatrix.createAffineTransform();
		at.concatenate(font.getFontMatrix().createAffineTransform());
		if (font instanceof PDType3Font) {
			// It is difficult to calculate the real individual glyph bounds for
			// type 3 fonts
			// because these are not vector fonts, the content stream could
			// contain almost anything
			// that is found in page content streams.
			PDType3Font t3Font = (PDType3Font) font;
			PDType3CharProc charProc = t3Font.getCharProc(code);
			if (charProc != null) {
				BoundingBox fontBBox = t3Font.getBoundingBox();
				PDRectangle glyphBBox = charProc.getGlyphBBox();
				if (glyphBBox != null) {
					// PDFBOX-3850: glyph bbox could be larger than the font
					// bbox
					glyphBBox.setLowerLeftX(Math.max(fontBBox.getLowerLeftX(), glyphBBox.getLowerLeftX()));
					glyphBBox.setLowerLeftY(Math.max(fontBBox.getLowerLeftY(), glyphBBox.getLowerLeftY()));
					glyphBBox.setUpperRightX(Math.min(fontBBox.getUpperRightX(), glyphBBox.getUpperRightX()));
					glyphBBox.setUpperRightY(Math.min(fontBBox.getUpperRightY(), glyphBBox.getUpperRightY()));
					path = glyphBBox.toGeneralPath();
				}
			}
		} else if (font instanceof PDVectorFont) {
			PDVectorFont vectorFont = (PDVectorFont) font;
			path = vectorFont.getPath(code);

			if (font instanceof PDTrueTypeFont) {
				PDTrueTypeFont ttFont = (PDTrueTypeFont) font;
				int unitsPerEm = ttFont.getTrueTypeFont().getHeader().getUnitsPerEm();
				at.scale(1000d / unitsPerEm, 1000d / unitsPerEm);
			}
			if (font instanceof PDType0Font) {
				PDType0Font t0font = (PDType0Font) font;
				if (t0font.getDescendantFont() instanceof PDCIDFontType2) {
					int unitsPerEm = ((PDCIDFontType2) t0font.getDescendantFont()).getTrueTypeFont().getHeader()
							.getUnitsPerEm();
					at.scale(1000d / unitsPerEm, 1000d / unitsPerEm);
				}
			}
		} else if (font instanceof PDSimpleFont) {
			PDSimpleFont simpleFont = (PDSimpleFont) font;

			// these two lines do not always work, e.g. for the TT fonts in file
			// 032431.pdf
			// which is why PDVectorFont is tried first.
			String name = simpleFont.getEncoding().getName(code);
			path = simpleFont.getPath(name);
		} else {
			// shouldn't happen, please open issue in JIRA
			System.out.println("Unknown font class: " + font.getClass());
		}
		if (path == null) {
			return null;
		}
		return at.createTransformedShape(path.getBounds2D());
	}

	/**
	 * Override the default functionality of PDFTextStripper.
	 */
	@Override
	protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
		for (TextPosition text : textPositions) {
			System.out.println("String[" + text.getXDirAdj() + "," + text.getYDirAdj() + " fs=" + text.getFontSize()
					+ " xscale=" + text.getXScale() + " height=" + text.getHeightDir() + " space="
					+ text.getWidthOfSpace() + " width=" + text.getWidthDirAdj() + "]" + text.getUnicode());

			PDFont font = text.getFont();
			BoundingBox bbox = font.getBoundingBox();

			// advance width, bbox height (glyph space)
			float xadvance = font.getWidth(text.getCharacterCodes()[0]); // todo:
		}
	}

}
