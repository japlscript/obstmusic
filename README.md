[![LGPL 2.1](https://img.shields.io/badge/License-LGPL_2.1-blue.svg)](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.tagtraum/obstmusic/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.tagtraum/obstmusic)
[![Build and Test](https://github.com/japlscript/obstmusic/workflows/Build%20and%20Test/badge.svg)](https://github.com/japlscript/obstmusic/actions)


# Obstmusic

*Obstmusic* is a Java API for the Apple Music app (macOS) based on
[JaplScript](https://github.com/japlscript/japlscript).


## Installation

Obstmusic is released via [Maven](https://maven.apache.org).
You can install it via the following dependency:

```xml
<dependency>
    <groupId>com.tagtraum</groupId>
    <artifactId>obstmusic</artifactId>
</dependency>
```

The Maven artifacts also contain sources and javadocs. 

If you are using [modules](https://en.wikipedia.org/wiki/Java_Platform_Module_System),
its name is `tagtraum.obstmusic`.


## Usage
                           
To use the generated code, do something like this:

```java
import com.tagtraum.macos.music.Application;

public class PlayPause {

    public static void main(final String[] args) {
        Application app = Application.getInstance();
        // then use app, for example, to toggle playback (if a track is in the player)
        app.playpause();
    }
}
```

Or this:

```java
import com.tagtraum.macos.music.Application;
import com.tagtraum.japlscript.Reference;
import com.tagtraum.japlscript.execution.JaplScriptException;
import com.tagtraum.japlscript.language.Tdta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GetCurrentArtwork {

    public static void main(final String[] args) throws IOException {
        final Application application = Application.getInstance();
        try {
            final Track currentTrack = application.getCurrentTrack();
            final Artwork[] artworks = currentTrack.getArtworks();
            if (artworks.length > 0) {
                final Artwork artwork = artworks[0];
                final Reference rawData = artwork.getRawData();
                if (rawData != null) {

                    // get image data
                    final Tdta tdta = rawData.cast(Tdta.class);
                    final byte[] imageData = tdta.getTdta();

                    // get image format
                    final String format = artwork.getFormat().getObjectReference().toLowerCase();
                    final String extension;
                    if (format.contains("png")) extension = ".png";
                    else if (format.contains("tiff")) extension = ".tiff";
                    else if (format.contains("gif")) extension = ".gif";
                    else if (format.contains("bmp")) extension = ".bmp";
                    else if (format.contains("pdf")) extension = ".pdf";
                    else extension = ".jpeg";

                    // write image file
                    final Path artworkPath = Paths.get("current_artwork" + extension);
                    System.out.println("Writing artwork to file " + artworkPath);
                    Files.write(artworkPath, imageData);
                }
            }
        } catch (JaplScriptException e) {
            System.out.println("No track loaded (we assume).");
        }
    }
}
```

Or this:

```java
import com.tagtraum.macos.music.Application;
import com.tagtraum.macos.music.Track;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AddArtworkToCurrentTrack {

    public static void main(final String[] args) throws IOException {
        // path to artwork should be the first argument
        final Path newArtworkFile = Paths.get(args[0]);
        
        final Application application = Application.getInstance();
        
        // get currently playing track
        final Track currentTrack = application.getCurrentTrack();

        // add artwork
        currentTrack.addArtwork(newArtworkFile);
    }
}
```

## API

You can find the complete [API here](https://japlscript.github.io/obstmusic/com/tagtraum/macos/music/package-summary.html). 


## Shipping

For information about shipping apps with this library, please see
the corresponding notes about signing, notarization etc. in the
[JaplScript documentation](https://github.com/japlscript/japlscript/blob/main/README.md).
