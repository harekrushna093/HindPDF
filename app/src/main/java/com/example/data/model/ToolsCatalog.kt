package com.example.data.model

object ToolsCatalog {
  val allTools: List<PdfTool> = listOf(
    // 1. Core PDF Tools
    PdfTool(
      id = "merge",
      name = "Merge PDF",
      desc = "Combine multiple PDFs into one document in your exact order.",
      category = ToolCategory.CORE,
      tier = ToolTier.FREE,
      color = 0xFFE11D48,
      filePrefix = "Merged_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "split",
      name = "Split PDF",
      desc = "Split a PDF into separate pages or extract selected ranges.",
      category = ToolCategory.CORE,
      tier = ToolTier.FREE,
      color = 0xFFF59E0B,
      filePrefix = "Split_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "compress",
      name = "Compress PDF",
      desc = "Reduce PDF file size with extreme stream optimization and exact targeting.",
      category = ToolCategory.CORE,
      tier = ToolTier.FREEMIUM,
      color = 0xFF10B981,
      filePrefix = "Compressed_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "pdf-to-jpg",
      name = "PDF to JPG",
      desc = "Extract each page of your PDF into high-quality JPG images.",
      category = ToolCategory.CORE,
      tier = ToolTier.FREE,
      color = 0xFFEC4899,
      filePrefix = "Images_",
      fileExt = ".zip",
      mimeType = "application/zip"
    ),
    PdfTool(
      id = "jpg-to-pdf",
      name = "JPG to PDF",
      desc = "Convert JPG images into high-resolution PDF document.",
      category = ToolCategory.CORE,
      tier = ToolTier.FREE,
      color = 0xFF9333EA,
      filePrefix = "Converted_",
      fileExt = ".pdf",
      mimeType = "application/pdf",
      acceptedExtensions = listOf("jpg", "jpeg", "png", "webp")
    ),
    PdfTool(
      id = "pdf-to-png",
      name = "PDF to PNG",
      desc = "Extract crisp PNG image assets from documents.",
      category = ToolCategory.CORE,
      tier = ToolTier.FREE,
      color = 0xFF06B6D4,
      filePrefix = "Images_PNG_",
      fileExt = ".zip",
      mimeType = "application/zip"
    ),
    PdfTool(
      id = "png-to-pdf",
      name = "PNG to PDF",
      desc = "Combine transparent PNG images into PDF.",
      category = ToolCategory.CORE,
      tier = ToolTier.FREE,
      color = 0xFF3B82F6,
      filePrefix = "Images_to_",
      fileExt = ".pdf",
      mimeType = "application/pdf",
      acceptedExtensions = listOf("png")
    ),
    PdfTool(
      id = "word-to-pdf",
      name = "Word to PDF",
      desc = "Convert DOCX word documents directly to PDF.",
      category = ToolCategory.CORE,
      tier = ToolTier.FREE,
      color = 0xFF2563EB,
      filePrefix = "Word_Converted_",
      fileExt = ".pdf",
      mimeType = "application/pdf",
      acceptedExtensions = listOf("docx", "doc")
    ),
    PdfTool(
      id = "pdf-to-word",
      name = "PDF to Word",
      desc = "Convert PDF documents to editable Word text and DOCX.",
      category = ToolCategory.CORE,
      tier = ToolTier.FREEMIUM,
      color = 0xFF1D4ED8,
      filePrefix = "Editable_",
      fileExt = ".docx",
      mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    ),
    PdfTool(
      id = "excel-to-pdf",
      name = "Excel to PDF",
      desc = "Convert Excel spreadsheets (.xlsx, .csv) to PDF.",
      category = ToolCategory.CORE,
      tier = ToolTier.FREE,
      color = 0xFF107C41,
      filePrefix = "Spreadsheet_",
      fileExt = ".pdf",
      mimeType = "application/pdf",
      acceptedExtensions = listOf("xlsx", "xls", "csv")
    ),
    PdfTool(
      id = "pdf-to-excel",
      name = "PDF to Excel",
      desc = "Extract structured PDF tables to CSV/Excel spreadsheet.",
      category = ToolCategory.CORE,
      tier = ToolTier.FREEMIUM,
      color = 0xFF059669,
      filePrefix = "Extracted_Sheet_",
      fileExt = ".csv",
      mimeType = "text/csv"
    ),
    PdfTool(
      id = "ppt-to-pdf",
      name = "PowerPoint to PDF",
      desc = "Convert PPT presentation slides into easy-to-share PDF format.",
      category = ToolCategory.CORE,
      tier = ToolTier.FREE,
      color = 0xFFEA580C,
      filePrefix = "Slides_",
      fileExt = ".pdf",
      mimeType = "application/pdf",
      acceptedExtensions = listOf("pptx", "ppt")
    ),
    PdfTool(
      id = "pdf-to-ppt",
      name = "PDF to PowerPoint",
      desc = "Convert PDF pages into presentation slides.",
      category = ToolCategory.CORE,
      tier = ToolTier.FREEMIUM,
      color = 0xFFC2410C,
      filePrefix = "Presentation_",
      fileExt = ".pptx",
      mimeType = "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    ),

    // 2. Organize PDF
    PdfTool(
      id = "rotate",
      name = "Rotate PDF",
      desc = "Rotate pages clockwise with interactive preview.",
      category = ToolCategory.ORGANIZE,
      tier = ToolTier.FREE,
      color = 0xFFF97316,
      filePrefix = "Rotated_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "delete-pages",
      name = "Delete PDF Pages",
      desc = "Visually select and remove unwanted PDF sheets.",
      category = ToolCategory.ORGANIZE,
      tier = ToolTier.FREE,
      color = 0xFFEF4444,
      filePrefix = "Trimmed_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "extract-pages",
      name = "Extract PDF Pages",
      desc = "Extract selected page ranges into a clean standalone PDF.",
      category = ToolCategory.ORGANIZE,
      tier = ToolTier.FREE,
      color = 0xFF0D9488,
      filePrefix = "Extracted_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "reorder-pages",
      name = "Reorder PDF Pages",
      desc = "Rearrange the sequence of pages in your PDF.",
      category = ToolCategory.ORGANIZE,
      tier = ToolTier.FREE,
      color = 0xFFF59E0B,
      filePrefix = "Reordered_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "page-numbers",
      name = "Add Page Numbers",
      desc = "Insert bottom-page numbering automatically.",
      category = ToolCategory.ORGANIZE,
      tier = ToolTier.FREE,
      color = 0xFF8B5CF6,
      filePrefix = "Numbered_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "crop-pdf",
      name = "Crop PDF",
      desc = "Trim document margins and remove blank borders.",
      category = ToolCategory.ORGANIZE,
      tier = ToolTier.FREE,
      color = 0xFF64748B,
      filePrefix = "Cropped_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),

    // 3. Edit & Security
    PdfTool(
      id = "edit-pdf",
      name = "Edit PDF",
      desc = "Add notes, highlights and annotations to pages.",
      category = ToolCategory.EDIT_SECURITY,
      tier = ToolTier.FREEMIUM,
      color = 0xFF3B82F6,
      filePrefix = "Edited_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "add-text",
      name = "Add Text to PDF",
      desc = "Overlay custom typography & text fields on pages.",
      category = ToolCategory.EDIT_SECURITY,
      tier = ToolTier.FREE,
      color = 0xFF6366F1,
      filePrefix = "Updated_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "add-watermark",
      name = "Watermark PDF",
      desc = "Stamp custom text watermark protection on documents.",
      category = ToolCategory.EDIT_SECURITY,
      tier = ToolTier.FREEMIUM,
      color = 0xFF7C3AED,
      filePrefix = "Watermarked_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "protect-password",
      name = "Protect PDF",
      desc = "Lock PDF with 256-bit bank level password encryption.",
      category = ToolCategory.EDIT_SECURITY,
      tier = ToolTier.PRO,
      color = 0xFF4F46E5,
      filePrefix = "Secured_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "unlock",
      name = "Unlock PDF",
      desc = "Remove encryption and security restrictions.",
      category = ToolCategory.EDIT_SECURITY,
      tier = ToolTier.FREE,
      color = 0xFFD97706,
      filePrefix = "Unlocked_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "sign",
      name = "Sign PDF",
      desc = "Draw and stamp digital e-signatures onto pages.",
      category = ToolCategory.EDIT_SECURITY,
      tier = ToolTier.FREEMIUM,
      color = 0xFF111827,
      filePrefix = "Signed_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "redact",
      name = "Redact PDF",
      desc = "Permanently blackout and sanitize sensitive portions.",
      category = ToolCategory.EDIT_SECURITY,
      tier = ToolTier.PRO,
      color = 0xFF1F2937,
      filePrefix = "Redacted_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),

    // 4. Scan & Smart Tools
    PdfTool(
      id = "scan-to-pdf",
      name = "Scan to PDF",
      desc = "Capture paper documents via camera into clean PDF.",
      category = ToolCategory.SCAN_SMART,
      tier = ToolTier.FREE,
      color = 0xFF10B981,
      filePrefix = "Scanned_",
      fileExt = ".pdf",
      mimeType = "application/pdf",
      acceptedExtensions = listOf("jpg", "jpeg", "png", "pdf")
    ),
    PdfTool(
      id = "ocr",
      name = "OCR PDF",
      desc = "Recognize scanned text in documents via AI OCR engine.",
      category = ToolCategory.SCAN_SMART,
      tier = ToolTier.PRO,
      color = 0xFF6366F1,
      filePrefix = "Searchable_OCR_",
      fileExt = ".txt",
      mimeType = "text/plain",
      acceptedExtensions = listOf("pdf", "jpg", "png")
    ),
    PdfTool(
      id = "pdf-reader",
      name = "PDF Reader / Viewer",
      desc = "Fast, private on-device document inspection.",
      category = ToolCategory.SCAN_SMART,
      tier = ToolTier.FREE,
      color = 0xFF0EA5E9,
      filePrefix = "Inspected_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "repair-pdf",
      name = "Repair PDF",
      desc = "Recover damaged, unreadable, and corrupted PDF files.",
      category = ToolCategory.SCAN_SMART,
      tier = ToolTier.FREE,
      color = 0xFFF43F5E,
      filePrefix = "Repaired_",
      fileExt = ".pdf",
      mimeType = "application/pdf"
    ),
    PdfTool(
      id = "html-to-pdf",
      name = "HTML to PDF",
      desc = "Convert HTML web documents and code snippets to PDF.",
      category = ToolCategory.SCAN_SMART,
      tier = ToolTier.FREE,
      color = 0xFF84CC16,
      filePrefix = "Web_Document_",
      fileExt = ".pdf",
      mimeType = "application/pdf",
      acceptedExtensions = listOf("html", "htm", "txt")
    )
  )

  fun getToolById(id: String): PdfTool {
    return allTools.find { it.id == id } ?: allTools.first()
  }

  val popularTools: List<PdfTool> = listOf(
    getToolById("merge"),
    getToolById("split"),
    getToolById("compress"),
    getToolById("ocr"),
    getToolById("pdf-to-word"),
    getToolById("redact"),
    getToolById("protect-password"),
    getToolById("pdf-to-jpg")
  )
}
