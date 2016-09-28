package hasse.s.sire_simplereader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class Unzipper
{
    public static void unzip(String zipFile, File targetDirectory) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(new File(zipFile)))))
        {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zipInputStream.getNextEntry()) != null)
            {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                try (FileOutputStream fout = new FileOutputStream(file))
                {
                    while ((count = zipInputStream.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                }
            }
        }
    }
}
