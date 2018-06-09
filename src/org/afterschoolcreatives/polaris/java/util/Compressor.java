/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.afterschoolcreatives.polaris.java.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZ;
import org.tukaani.xz.XZInputStream;
import org.tukaani.xz.XZOutputStream;

/**
 *
 * @author Jhon Melvin
 */
public class Compressor {

    public final static int mebiByteToByte(int mib) {
        return mib * 1048576;
    }

    public final static void compressXZ(String input, String output) throws FileNotFoundException, IOException {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        XZOutputStream xzos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(input));

            fos = new FileOutputStream(output);
            //------------------------------------------------------------------
            LZMA2Options options = new LZMA2Options();
            options.setDictSize(Compressor.mebiByteToByte(64)); //32 MiB
            options.setMode(LZMA2Options.MODE_NORMAL);

            options.setPb(LZMA2Options.PB_MAX);
            options.setPreset(9);
            //------------------------------------------------------------------
            xzos = new XZOutputStream(fos, options, XZ.CHECK_SHA256);
            //
            byte[] buf = new byte[8192];
            int size;
            while ((size = bis.read(buf)) != -1) {
                xzos.write(buf, 0, size);
            }

            xzos.finish();

        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                // Log Here
            }

            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                // Log Here
            }

            try {
                if (xzos != null) {
                    xzos.close();
                }
            } catch (IOException e) {
                // Log Here
            }
        }
    }

    public final static void decompressXZ(String input, String output) throws FileNotFoundException, IOException {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        XZInputStream xzis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(input));

            xzis = new XZInputStream(bis);

            fos = new FileOutputStream(output);

            final byte[] buffer = new byte[8192];
            int n = 0;
            while (-1 != (n = xzis.read(buffer))) {
                fos.write(buffer, 0, n);
            }
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                // Log Here
            }

            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                // Log Here
            }

            try {
                if (xzis != null) {
                    xzis.close();
                }
            } catch (IOException e) {
                // Log Here
            }
        }

    }

    public static void main(String[] args) {
        try {
            decompressXZ("doc.pdf.xz", "doc.pdf");
//            compressXZ("doc.docx", "doc.docx.xz");
//            decompressXZ("doc.docx.xz", "doc.docx");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
