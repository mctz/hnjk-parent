package com.hnjk.core.foundation.m3u8.utils;

import lombok.Cleanup;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.List;

/**
 * @author dkuffner
 */
public final class Playlist implements Iterable<Element> {

    public static Playlist parse(Readable readable) throws ParseException {
        if (readable == null) {
            throw new NullPointerException("playlist");
        }
        return PlaylistParser.create(PlaylistType.M3U8).parse(readable);
    }

    public static Playlist parse(String playlist) throws ParseException {
        if (playlist == null) {
            throw new NullPointerException("playlist");
        }
        return parse(new StringReader(playlist));
    }

    public static Playlist parse(InputStream playlist) throws ParseException {
        if (playlist == null) {
            throw new NullPointerException("playlist");
        }
        try{
			@Cleanup InputStreamReader inputStreamReader = new InputStreamReader(playlist);
			return parse(inputStreamReader);
		}catch (Exception e){
			throw new NullPointerException("");
		}

    }

    public static Playlist parse(ReadableByteChannel playlist) throws ParseException {
        if (playlist == null) {
            throw new NullPointerException("playlist");
        }
        return parse(makeReadable(playlist));
    }

    private static Readable makeReadable(ReadableByteChannel source) {
        if (source == null) {
            throw new NullPointerException("source");
        }


        return Channels.newReader(source,
                java.nio.charset.Charset.defaultCharset().name());
    }

    private final List<Element> elements;
    private final boolean endSet;
    private final int targetDuration;
    private int mediaSequenceNumber;
    private boolean isList = false;
    private boolean isRate = false;

    Playlist(List<Element> elements, boolean endSet, int targetDuration, int mediaSequenceNumber) {
        this(elements, endSet, targetDuration, mediaSequenceNumber, false);
    }
    
    Playlist(List<Element> elements, boolean endSet, int targetDuration, int mediaSequenceNumber, boolean isList) {
    	if (elements == null) {
            throw new NullPointerException("elements");
        }
    	if(elements.size() > 0){
    		if(elements.get(0).getRate() != Element.UNKOWN_RATE) {
				isRate = true;
			}
    	}
    	this.isList = isList;
        this.targetDuration = targetDuration;
        this.elements = elements;
        this.endSet = endSet;
        this.mediaSequenceNumber = mediaSequenceNumber;
    }
    
    
    public boolean isRate() {
		return isRate;
	}

	public void setRate(boolean isRate) {
		this.isRate = isRate;
	}

	public boolean isList() {
		return isList;
	}

	public void setList(boolean isList) {
		this.isList = isList;
	}

	public int getTargetDuration() {
        return targetDuration;
    }

    @Override
	public Iterator<Element> iterator() {
        return elements.iterator();
    }

    public List<Element> getElements() {
        return elements;
    }

    public boolean isEndSet() {
        return endSet;
    }

    public int getMediaSequenceNumber() {
        return mediaSequenceNumber;
    }

    @Override
    public String toString() {
        return "PlayListImpl{" +
                "elements=" + elements +
                ", endSet=" + endSet +
                ", targetDuration=" + targetDuration +
                ", mediaSequenceNumber=" + mediaSequenceNumber +
                '}';
    }
}