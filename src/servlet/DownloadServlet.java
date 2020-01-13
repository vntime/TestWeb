package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;

/**
 * Servlet implementation class DownloadServlet
 */
@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String UPLOAD_DIRECTORY = "C:\\TEMP";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        byte[] bytes = new byte[1024];
        int length;

        String fileName = "mytemplate.xlsx";
        String fileNameWithoutExt = FilenameUtils.removeExtension(fileName);

        File fileToZip = new File(UPLOAD_DIRECTORY + File.separator + fileName);
        FileInputStream fisZip = new FileInputStream(fileToZip);

        String fileNameZipOut = System.currentTimeMillis() + "_" + fileNameWithoutExt + ".zip";
        FileOutputStream fosZip = new FileOutputStream(new File(UPLOAD_DIRECTORY + File.separator + fileNameZipOut));
        ZipOutputStream zos = new ZipOutputStream(fosZip);

        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
        zos.putNextEntry(zipEntry);
        while ((length = fisZip.read(bytes)) > 0) {
            zos.write(bytes, 0, length);
            // sos.flush();
        }
        zos.close();
        fisZip.close();
        fosZip.close();

        FileInputStream fis = new FileInputStream(new File(UPLOAD_DIRECTORY + File.separator + fileNameZipOut));
        ServletOutputStream sos = response.getOutputStream();

        response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameZipOut + "\";");
        while ((length = fis.read(bytes)) > 0) {
            sos.write(bytes, 0, length);
            // sos.flush();
        }
        fis.close();
        sos.close();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
