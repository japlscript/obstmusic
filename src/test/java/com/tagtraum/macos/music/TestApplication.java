/*
 * =================================================
 * Copyright 2021 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.macos.music;

import com.tagtraum.japlscript.Reference;
import com.tagtraum.japlscript.execution.JaplScriptException;
import com.tagtraum.japlscript.language.Picture;
import com.tagtraum.japlscript.language.Tdta;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * TestApplication.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestApplication {

    @Test
    public void testVersion() {
        final Application application = Application.getInstance();
        final String version = application.getVersion();
        assertNotNull(version);
        assertFalse(version.trim().isEmpty());
    }

    @Test
    public void testLibraryPlaylistCount() {
        final Application application = Application.getInstance();
        final LibraryPlaylist libraryPlaylist = application.getSources()[0].getLibraryPlaylists()[0];
        System.out.println("Tracks: " + libraryPlaylist.countTracks());
    }

    @Test
    public void testCurrentlyPlaying() {
        final Application application = Application.getInstance();
        try {
            final Track currentTrack = application.getCurrentTrack();
            System.out.println("Current track: " + currentTrack.getName() + " by " + currentTrack.getArtist());
        } catch(JaplScriptException e) {
            System.out.println("No track loaded (we assume).");
        }
    }

    @Test
    public void testCurrentlyPlayingArtwork() throws IOException {
        final Application application = Application.getInstance();
        try {
            final Track currentTrack = application.getCurrentTrack();
            final Artwork artwork0 = currentTrack.getArtwork(0);
            System.out.println(artwork0);
            final Artwork artwork1 = currentTrack.getArtwork(1);
            System.out.println(artwork1);
            final Artwork[] artworks = currentTrack.getArtworks();
            if (artworks.length > 0) {
                final Artwork artwork = artworks[0];
                System.out.println(artwork);
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
        } catch(JaplScriptException e) {
            System.out.println("No track loaded (we assume).");
        }
    }

    @Test
    @Disabled("Ill-defined test, just for illustration")
    public void testAddArtworkToCurrentlyPlayingTrack() throws IOException {
        final Application application = Application.getInstance();
        try {
            final Track currentTrack = application.getCurrentTrack();

            // get the index of the artwork *after* the last artwork
            final int addIndex = currentTrack.countArtworks();
            // create a location specifier for the new artwork
            final Artwork newArtwork = currentTrack.getArtwork(addIndex);

            // fill the artwork with data, which effectively stores it
            final Tdta tdta = new Tdta(Paths.get("current_artwork.jpeg"), newArtwork.getApplicationReference());
            newArtwork.setData(tdta.cast(Picture.class));
        } catch(JaplScriptException e) {
            System.out.println("No track loaded (we assume).");
        }
    }

    @Test
    public void testGetTrackProperties() {
        // this is more demo than test code...
        final Application application = Application.getInstance();
        try {
            final LibraryPlaylist library = application.getSources()[0].getLibraryPlaylists()[0];
            System.out.println(library.getTrack(0).getProperties());
        } catch(JaplScriptException e) {
            // oops
            e.printStackTrace();
        }
    }
}
