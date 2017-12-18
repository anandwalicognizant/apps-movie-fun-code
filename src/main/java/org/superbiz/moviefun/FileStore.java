package org.superbiz.moviefun;


import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.lang.ClassLoader.getSystemResource;

/**
 * Created by aw169 on 12/14/17.
 */
public class FileStore implements BlobStore{

    @Override
    public void put(Blob blob) throws IOException {

        File targetFile = new File(blob.name);
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(IOUtils.toByteArray(blob.inputStream));
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        Path path;
        try {
            path = getExistingCoverPath(name);
            InputStream stream = Files.newInputStream(path, null);
            return Optional.of(new Blob(name, stream, new Tika().detect(stream)));

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public void deleteAll() {

    }

    private Path getExistingCoverPath(String name) throws URISyntaxException {
        File coverFile = new File(name);

        Path coverFilePath;

        if (coverFile.exists()) {
            coverFilePath = coverFile.toPath();
        } else {
            coverFilePath = Paths.get(getSystemResource("default-cover.jpg").toURI());
        }

        return coverFilePath;
    }
}
