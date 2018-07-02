package com.clikshow.ClikBIlheteria.Impressora;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import zj.com.customize.sdk.Other;

public class PrinterCommands {
    public static final byte HT = 0x9;
    public static final byte LF = 0x0A;
    public static final byte CR = 0x0D;
    public static final byte ESC = 0x1B;
    public static final byte DLE = 0x10;
    public static final byte GS = 0x1D;
    public static final byte FS = 0x1C;
    public static final byte STX = 0x02;
    public static final byte US = 0x1F;
    public static final byte CAN = 0x18;
    public static final byte CLR = 0x0C;
    public static final byte EOT = 0x04;

    public static final byte[] INIT = {27, 64};
    public static byte[] FEED_LINE = {10};

    public static byte[] SELECT_FONT_A = {20, 33, 0};

    public static byte[] SET_BAR_CODE_HEIGHT = {29, 104, 100};
    public static byte[] PRINT_BAR_CODE_1 = {29, 107, 2};
    public static byte[] SEND_NULL_BYTE = {0x00};

    public static byte[] SELECT_PRINT_SHEET = {0x1B, 0x63, 0x30, 0x02};
    public static byte[] FEED_PAPER_AND_CUT = {0x1D, 0x56, 66, 0x00};

    public static byte[] SELECT_CYRILLIC_CHARACTER_CODE_TABLE = {0x1B, 0x74, 0x11};

    public static byte[] SELECT_BIT_IMAGE_MODE = {0x1B, 0x2A, 33, -128, 0};
    public static byte[] SET_LINE_SPACING_24 = {0x1B, 0x33, 24};
    public static byte[] SET_LINE_SPACING_30 = {0x1B, 0x33, 30};

    public static byte[] TRANSMIT_DLE_PRINTER_STATUS = {0x10, 0x04, 0x01};
    public static byte[] TRANSMIT_DLE_OFFLINE_PRINTER_STATUS = {0x10, 0x04, 0x02};
    public static byte[] TRANSMIT_DLE_ERROR_STATUS = {0x10, 0x04, 0x03};
    public static byte[] TRANSMIT_DLE_ROLL_PAPER_SENSOR_STATUS = {0x10, 0x04, 0x04};

    public static final byte[] ESC_FONT_COLOR_DEFAULT = new byte[] { 0x1B, 'r',0x00 };
    public static final byte[] FS_FONT_ALIGN = new byte[] { 0x1C, 0x21, 1, 0x1B,
            0x21, 1 };
    public static final byte[] ESC_ALIGN_LEFT = new byte[] { 0x1b, 'a', 0x00 };
    public static final byte[] ESC_ALIGN_RIGHT = new byte[] { 0x1b, 'a', 0x02 };
    public static final byte[] ESC_ALIGN_CENTER = new byte[] { 0x1b, 'a', 0x01 };
    public static final byte[] ESC_CANCEL_BOLD = new byte[] { 0x1B, 0x45, 0 };


    /*********************************************/
    public static final byte[] ESC_HORIZONTAL_CENTERS = new byte[] { 0x1B, 0x44, 20, 28, 00};
    public static final byte[] ESC_CANCLE_HORIZONTAL_CENTERS = new byte[] { 0x1B, 0x44, 00 };
    /*********************************************/

    public static final byte[] ESC_ENTER = new byte[] { 0x1B, 0x4A, 0x40 };
    public static final byte[] PRINTE_TEST = new byte[] { 0x1D, 0x28, 0x41 };


    public static byte[] POS_PrintBMP(Bitmap mBitmap, int nWidth, int nMode) {
        int width = ((nWidth + 7) / 8) * 8;
        int height = mBitmap.getHeight() * width / mBitmap.getWidth();
        height = ((height + 7) / 8) * 12;

        Bitmap rszBitmap = mBitmap;
        if (mBitmap.getWidth() != width){
            rszBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
        }

        Bitmap grayBitmap = Other.toGrayscale(rszBitmap);

        byte[] dithered = Other.thresholdToBWPic(grayBitmap);

        byte[] data = Other.eachLinePixToCmd(dithered, width, nMode);

        return data;
    }

    public static byte[] POS_PrintQr(Bitmap mBitmap, int nWidth, int nMode) {
        int width = ((nWidth + 7) / 8) * 8;
        int height = mBitmap.getHeight() * width / mBitmap.getWidth();
        height = ((height + 7) / 8) * 8;

        Bitmap rszBitmap = mBitmap;
        if (mBitmap.getWidth() != width){
            rszBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
        }

        Bitmap grayBitmap = Other.toGrayscale(rszBitmap);

        byte[] dithered = Other.thresholdToBWPic(grayBitmap);

        byte[] data = Other.eachLinePixToCmd(dithered, width, nMode);

        return data;
    }

    public static Bitmap printQRCode(String texto){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(texto, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

}
