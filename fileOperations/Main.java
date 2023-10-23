package com.company;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.sql.Timestamp;
import java.util.*;

public class Main {

    public static String convertToTrEng(String text) {
        String result = text;

        char[] firstChar = new char[]{'İ', 'ı', 'ü', 'Ü', 'ç', 'Ç', 'Ğ', 'ğ', 'Ş', 'ş', 'ö', 'Ö', ','};
        char[] newChar = new char[]{'I', 'i', 'u', 'U', 'c', 'C', 'G', 'g', 'S', 's', 'o', 'O', '-'};

        for (int count = 0; count < firstChar.length; count++) {
            result = result.replace(firstChar[count], newChar[count]);
        }

        return result;
    }

    public static String createFile(String pathName) {
        File file = new File(pathName);

        if (file.exists()) {
            return pathName;
        }
        /*try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        if (file.mkdir()) {
            System.out.println(pathName + " Directory is created");
            return pathName;
        } else {
            System.out.println(pathName + " Directory cannot be created");
            return "";
        }
    }

    public static void main(String[] args) throws IOException {

        createFile("C:\\javaProjeler\\fileOperations\\src\\com\\company\\trainDeneme");
        createFile("C:\\javaProjeler\\fileOperations\\src\\com\\company\\pdfTest");

        Scanner input = new Scanner(System.in);
        int varOlanPdfMi;

        System.out.println("Pdf dosyalarını excel dosyasından içeri aktararak işlem yapmak istiyorsanız 0 sayısını tuşlayınız." +
                "\nKayıtlı olan pdf dosyaları üzerinden işlem yapmak istiyorsanız 1 sayısını tuşlayınız. " +
                "\nKayıtlı olan pdf dosyalarının boş sayfalarını silmek istiyorsanız 2 sayısını tuşlayınız. ");
        varOlanPdfMi = input.nextInt();

        if (varOlanPdfMi == 0) {
            FileInputStream fis = new FileInputStream("C:\\javaProjeler\\fileOperations\\src\\com\\company\\veri.xlsx");
            XSSFWorkbook xssfWb = new XSSFWorkbook(fis);
            XSSFSheet xssfSheet = xssfWb.getSheetAt(0);     //creating a Sheet object to retrieve object
            Iterator<Row> itr = xssfSheet.iterator();    //iterating over excel file
            String value = null;
            Sheet sheet = xssfWb.getSheetAt(0);   //getting the XSSFSheet object at given index


            File fldr = new File("C:\\javaProjeler\\fileOperations\\src\\com\\company\\pdf");
            File[] lstOfFiles = fldr.listFiles();

            File fldr2 = new File("C:\\javaProjeler\\fileOperations\\src\\com\\company\\train");
            File[] lstOfFiles2 = fldr2.listFiles();

            /*for (int k = 0; k < lstOfFiles.length; k++) {
                if (lstOfFiles[k].isFile()) {
                    System.out.println("File " + lstOfFiles[k].getName());
                } else if (lstOfFiles[k].isDirectory()) {
                    //System.out.println("Directory " + listOfFiles[i].getName());
                    File directoryPdf = new File(fldr.getPath() + "\\" + lstOfFiles[k].getName());
                    FileUtils.cleanDirectory(directoryPdf);
                }
            }*/


            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row2 = sheet.getRow(i); //returns the logical row
                Cell cell = row2.getCell(1);
                value = cell.getStringCellValue();
                Locale trlocale= Locale.forLanguageTag("tr-TR");

                value = value.toLowerCase(trlocale);

                String fileName = convertToTrEng(value).replaceAll("\\s+", "-");
                String filePath = "C:\\javaProjeler\\fileOperations\\src\\com\\company\\";
                String pdfFileDirPath = createFile(filePath + "pdfTest\\" + fileName);
                String pngFileDirPath = createFile(filePath + "train\\" + fileName);
                String pdfUrl = row2.getCell(0).getStringCellValue();

                //Silme işlemi
                /*File directory = new File(filePath + "pdf\\" + fileName);
                FileUtils.cleanDirectory(directory);*/

                String pdfFileName = pdfFileDirPath + "\\" + new Timestamp(System.currentTimeMillis()).getTime() + ".pdf";
                //System.out.println(pdfUrl + " İndiriliyor.." + pdfFileName);
                URL url = new URL(pdfUrl);

               /* File directoryPdf = new File(filePath + "pdf\\" + fileName);
                FileUtils.cleanDirectory(directoryPdf);

                File directoryPng = new File(filePath + "train\\" + fileName);
                FileUtils.cleanDirectory(directoryPng);*/

                System.out.println(pdfUrl + " İndiriliyor.." + pdfFileName);
                try (InputStream in = url.openStream()) {
                    Files.copy(in, Paths.get(pdfFileName), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    // handle exception
                }

            }

        } else if (varOlanPdfMi == 1) {


            File fldrTrain2 = new File("C:\\javaProjeler\\fileOperations\\src\\com\\company\\train2");
            File[] lstOfFilesTrain2 = fldrTrain2.listFiles();
            for (int k = 0; k < lstOfFilesTrain2.length; k++) {
                if (lstOfFilesTrain2[k].isFile()) {
                    System.out.println("File " + lstOfFilesTrain2[k].getName());
                } else if (lstOfFilesTrain2[k].isDirectory()) {
                    //System.out.println("Directory " + listOfFiles[i].getName());
                    File directoryPdf = new File(fldrTrain2.getPath() + "\\" + lstOfFilesTrain2[k].getName());
                    FileUtils.cleanDirectory(directoryPdf);
                }
            }


            File folder = new File("C:\\javaProjeler\\fileOperations\\src\\com\\company\\pdf2");
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isDirectory()) {
                    //System.out.println("Directory " + listOfFiles[i].getName());
                    File folder1 = new File("C:\\javaProjeler\\fileOperations\\src\\com\\company\\pdf2\\" + listOfFiles[i].getName());
                    File[] listOfFiles1 = folder1.listFiles();
                    for (int j = 0; j < listOfFiles1.length; j++) {
                        if (listOfFiles1[j].isFile()) {
                            String pngFilePath = "C:\\javaProjeler\\fileOperations\\src\\com\\company\\train2\\" + listOfFiles[i].getName() + "\\";
                            PDDocument document = PDDocument.load(new File("C:\\javaProjeler\\fileOperations\\src\\com\\company\\pdf2\\" + listOfFiles[i].getName() + "\\" + listOfFiles1[j].getName()));
                            PDFRenderer pdfRenderer1 = new PDFRenderer(document);
                            Float scalee = 1.75f;

                            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                                BufferedImage bim = pdfRenderer1.renderImage(
                                        page, scalee, ImageType.RGB);

                                String pngFileName = pngFilePath + "\\" + listOfFiles1[j].getName() + "-" + String.valueOf(scalee) + "-" + page + ".jpg";
                                System.out.println(pngFileName);
                                ImageIOUtil.writeImage(
                                        bim, pngFileName, 300);
                            }
                            document.close();
                        }
                    }
                }
            }

            /*DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
                @Override
                public boolean accept(Path file) throws IOException {
                    return (Files.isDirectory(file));
                }
            };

            Path dir = FileSystems.getDefault().getPath("C:\\javaProjeler\\fileOperations\\src\\com\\company\\pdf");
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, filter)) {
                for (Path path : stream) {
                    System.out.println(path.getFileName());
                    final File folder = new File("C:\\javaProjeler\\fileOperations\\src\\com\\company\\pdf\\");
                    listFilesForFolder(folder);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        } else {
            //////// Silme ////////////
            File fldr = new File("C:\\javaProjeler\\fileOperations\\src\\com\\company\\pdfRemove2");
            File[] lstOfFiles = fldr.listFiles();
            for (int k = 0; k < lstOfFiles.length; k++) {
                if (lstOfFiles[k].isFile()) {
                    System.out.println("File " + lstOfFiles[k].getName());
                } else if (lstOfFiles[k].isDirectory()) {
                    //System.out.println("Directory " + listOfFiles[i].getName());
                    File directoryPdf = new File(fldr.getPath() + "\\" + lstOfFiles[k].getName());
                    FileUtils.cleanDirectory(directoryPdf);
                }
            }
            //////// Silme ////////////

            File folder = new File("C:\\javaProjeler\\fileOperations\\src\\com\\company\\pdf2");
            File[] listOfFiles = folder.listFiles();
            //PDDocument document = PDDocument.load(folder);

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isDirectory()) {
                    File folder1 = new File("C:\\javaProjeler\\fileOperations\\src\\com\\company\\pdf2\\" + listOfFiles[i].getName());
                    File[] listOfFiles1 = folder1.listFiles();
                    for (int j = 0; j < listOfFiles1.length; j++) {
                        if (listOfFiles1[j].isFile()) {
                            String pngFilePath = "C:\\javaProjeler\\fileOperations\\src\\com\\company\\train\\" + listOfFiles[i].getName() + "\\";
                            PDDocument document1 = PDDocument.load(new File("C:\\javaProjeler\\fileOperations\\src\\com\\company\\pdf2\\" + listOfFiles[i].getName() + "\\" + listOfFiles1[j].getName()));
                            PDFRenderer pdfRenderer1 = new PDFRenderer(document1);
                            for (int page = 0; page < document1.getNumberOfPages(); page++) {
                                if (isBlank(pdfRenderer1.renderImage(page))) {
                                    System.out.println("Blank Page Number : " + "C:\\javaProjeler\\fileOperations\\src\\com\\company\\pdfRemove2\\" + listOfFiles[i].getName() + "\\" + listOfFiles1[j].getName() + "  " + (page + 1));
                                    document1.removePage(page);
                                }
                            }
                            document1.save("C:\\javaProjeler\\fileOperations\\src\\com\\company\\pdfRemove2\\"+listOfFiles[i].getName() + "\\" +  listOfFiles1[j].getName());
                            document1.close();
                        }
                    }
                }
            }

            // Listing the number of existing pages
           /* int noOfPages = document.getNumberOfPages();
            System.out.println(noOfPages);

            // Removing the pages
            document.removePage(1);

            System.out.println("page removed");
            // Saving the document
            document.save("/home/mayur/gfgTemp.pdf");

            // Closing the document
            document.close();*/
        }

    }

    private static Boolean isBlank(BufferedImage pageImage) throws IOException {
        BufferedImage bufferedImage = pageImage;
        long count = 0;
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();
        Double areaFactor = (width * height) * 0.99;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color c = new Color(bufferedImage.getRGB(x, y));
                if (c.getRed() == c.getGreen() && c.getRed() == c.getBlue() && c.getRed() >= 248) {
                    count++;
                }
            }
        }
        if (count >= areaFactor) {
            return true;
        }
        return false;
    }


    /*public static void listFilesForFolder;(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println("-----" + fileEntry.getName());
            }
        }
    }*/


}