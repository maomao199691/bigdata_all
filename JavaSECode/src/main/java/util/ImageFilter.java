package util;

import java.io.*;

/**
 * @Author: MaoMao
 * @date: 2022/10/20 8:32
 * Desc: 识别图片真实格式
 */
public class ImageFilter {

    final static String tag = "ImageFilter";

    public enum Type {
        JPG, // SOI(2 bytes):0xFFD8
        GIF, // Signature(3 bytes) Version(3 bytes): 0x47494638 0x3961____
        PNG, // Signature(8 bytes):0x89504E47 0x0D0A1A0A
        BMP, // type(2 bytes):0x424D(Windows系统格式，而OS/2系统并未普及)
        SVG, // xml文件 读取前256个字节判断是否含有"svg"
        /*----------------------------------------------------------------------------------
         <?xml version="1.0" standalone="no"?>
         <!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN"
         "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
         <svg width="10cm" height="3cm" viewBox="0 0 100 30" version="1.1"
         xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
         <desc>Example Use03 - 'use' with a 'transform' attribute</desc>
         <defs>
         <rect id="MyRect" x="0" y="0" width="60" height="10"/>
         </defs>
         <rect x=".1" y=".1" width="99.8" height="29.8"
         fill="none" stroke="blue" stroke-width=".2" />
         <use xlink:href="#MyRect"
         transform="translate(20,2.5) rotate(10)" />
         </svg>
         --------------------------------------------------------------------------------- */
        ICO, // type(1、2字节保留，第3、4字节为类型):0x00000100
        TIFF, // header(12字节为 II或MM，34字节 值为42)
        UNKNOWN;
    }

    /**
     * 查询图像的真实格式
     *
     * @param filePath
     *            图像文件的绝对路径
     * @return
     */
    static public Type getImageFormat(String filePath)
            throws FileNotFoundException {
        Type typeRet = Type.UNKNOWN;

        do {
            File imageFile = new File(filePath);
            if (null == imageFile || !imageFile.exists() || !imageFile.isFile()) {
                throw new FileNotFoundException("\"" + filePath + "\""
                        + " not exists!");
                // break;
            }
            FileInputStream fis = new FileInputStream(imageFile);
            typeRet = getImageFormat(fis);
        } while (false);

        return typeRet;
    }

    static public Type getImageFormat(InputStream inputStream) {
        Type typeRet = Type.UNKNOWN;
        BufferedInputStream bis = null;
        try {
            do {
                if (null == inputStream || inputStream.available() < 1)
                    break;
                bis = new BufferedInputStream(inputStream);
                byte[] simpleByte = inputStream2Bytes(bis, 8);
                if (null == simpleByte)
                    break;
                typeRet = checkImageFormat(simpleByte);
                if (Type.UNKNOWN == typeRet) {// SVG 处理
                    byte[] detailByte = inputStream2Bytes(bis, 256);
                    if (detailByte.length < 256)
                        break;
                    //
                    if (isSVG(detailByte)) {
                        typeRet = Type.SVG;
                        break;
                    }
                }
            } while (false);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return typeRet;
    }

    static public Type getImageFormat(byte[] imageBytes) {
        Type typeRet = checkImageFormat(imageBytes);

        if (Type.UNKNOWN == typeRet) {
            // 判断是不是svg
            // 1.判断文件大小是否大于256字节，小于则不支持，认为非svg
            // 2.大于等于256字节，再读取前256个字节转换字符串，如果含有svg则是svg
            do {
                // step 1
                if (imageBytes.length < 256)
                    break;
                // step 2
                if (isSVG(imageBytes)) {
                    typeRet = Type.SVG;
                    break;
                }
            } while (false);

        }

        return typeRet;
    }

    /**
     * 读取前8个字节 1.首先判断前两个字节 JPG:0xFFD8 GIF:0x4749 PNG:0x8950 BMP:0x424D
     * ICO:0x0000 SVG:不确定 需要额外判断
     *
     * @param imageBytes
     *            长度为8
     * @return 图像类型
     */
    static private Type checkImageFormat(byte[] imageBytes) {
        Type typeRet = Type.UNKNOWN;

        do {
            if (null == imageBytes || imageBytes.length < 8)
                break;

            // s vlaue = ;

            // Log.d(tag, String.format("value:%x", vlaue));
            System.out.println(String.format("value:%x",
                    (short) (imageBytes[0] << 8 | imageBytes[1])));
            switch ((short) (imageBytes[0] << 8 | imageBytes[1])) {
                case (short) 0xFFD8:// 确定为JPG
                    typeRet = Type.JPG;
                    break;
                case (short) 0x4749: {// 可能为GIF
                    if (imageBytes[2] == 0x46) {// 确定为GIF
                        typeRet = Type.GIF;
                    }
                }
                break;
                case (short) 0x8950: {// 可能为PNG
                    if ((short) (imageBytes[2] << 8 | imageBytes[3]) == 0x4E47
                            && (imageBytes[4] << 24 | imageBytes[5] << 16
                            | imageBytes[6] << 8 | imageBytes[7]) == 0x0D0A1A0A) {
                        // 确定为PNG
                        typeRet = Type.PNG;
                    }

                }
                break;
                case (short) 0x424D: // 确定为 windows平台的BMP
                    typeRet = Type.BMP;
                    break;
                case (short) 0x0000: { // 可能为 ico
                    System.out.println("ico:"
                            + (short) (imageBytes[2] << 8 | imageBytes[3]));
                    if ((short) (imageBytes[2] << 8 | imageBytes[3]) == 0x0100) {
                        // 确定为 ico
                        typeRet = Type.ICO;
                    }
                }
                break;
                case (short) ('M' << 8 | 'M'):
                case (short) ('I' << 8 | 'I'): {
                    if ((short) (imageBytes[2] << 8 | imageBytes[3]) == 42) {
                        // 确定为 tiff
                        typeRet = Type.TIFF;
                    }
                }
                break;
                default:
                    typeRet = Type.UNKNOWN;
            }

        } while (false);

        return typeRet;
    }

    static private boolean isSVG(byte[] svg256bytes) {
        boolean bRet = false;
        do {
            if (null == svg256bytes || svg256bytes.length < 60)
                break;
            try {
                String svg = new String(svg256bytes, "UTF-8");
                System.out.println("SVG:" + svg);
                if (svg != null && svg.contains("svg")) {
                    bRet = true;
                }
            } catch (UnsupportedEncodingException uee) {
                uee.printStackTrace();
            }

        } while (false);

        return bRet;
    }

    static void ListFilesInDirectory(String path) throws FileNotFoundException {
        File file = new File(path);
        File[] files = file.listFiles();
        Type tyep;
        for (File fl : files) {
            if (fl.isDirectory())
                ListFilesInDirectory(fl.toString());
            else {
                System.out.println();

                String filePath = fl.getAbsolutePath();
                tyep = ImageFilter.getImageFormat(filePath);
                String strType = tyep.toString();
                String str = filePath.substring(filePath.lastIndexOf('.') + 1).toUpperCase();
//				System.out.println(str);
                if(!strType.equals(str)){
                    System.out.println("文件名   = " + filePath
                            + "       type = " + strType);
                }

            }
        }
    }

    /**
     * 数据流转成byte[];
     *
     * @param inputStream
     * @param length
     *            要转换几个长度的Byte
     * @return
     */
    static private byte[] inputStream2Bytes(InputStream inputStream, int length) {
        byte[] byteRet = null;

        try {
            do {
                if (null == inputStream || inputStream.available() < length
                        || length < 1)
                    break;
                inputStream.mark(length);
                byteRet = new byte[length];
                inputStream.read(byteRet);
                inputStream.reset();

            } while (false);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return byteRet;
    }

    public static void main(String[] args) {

        try {
            String path = "E:\\github\\MyGitHub\\GitHub\\guitar\\guitarChords\\docs\\imge\\民谣弹唱\\八十年代的歌\\text";

            ListFilesInDirectory(path);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
