package com.hnjk.core.foundation.m3u8.utils;

import java.net.URI;

/**
 * @author dkuffner
 */
final class ElementImpl implements Element {
    private final PlaylistInfo playlistInfo;
    private final EncryptionInfo encryptionInfo;
    private final int duration;
    private final URI uri;
    private final String title;
    private final long programDate;
    private String rateName;
    private int rate;
    private final int LOW_BANDWIDTH = 400000;
    private final int MIDDLE_BANDWIDTH = 700000;
    private final int HIGHT_BANDWIDTH = 1024000;

    public ElementImpl(PlaylistInfo playlistInfo, EncryptionInfo encryptionInfo, int duration, URI uri, String title, long programDate) {
        if (uri == null) {
            throw new NullPointerException("uri");
        }

        if (duration < -1) {
            throw new IllegalArgumentException();
        }
        if (playlistInfo != null && encryptionInfo != null) {
            throw new IllegalArgumentException("Element cannot be a encrypted playlist.");
        }
        this.playlistInfo = playlistInfo;
        this.encryptionInfo = encryptionInfo;
        this.duration = duration;
        this.uri = uri;
        this.title = title;
        this.programDate = programDate;
        if(playlistInfo == null) {
			return;
		}
        
        if(playlistInfo.getBandWitdh() >= HIGHT_BANDWIDTH) {
			rate = HIGHT_RATE;//rateName = "超清";
		} else if(playlistInfo.getBandWitdh() >= MIDDLE_BANDWIDTH) {
			rate = MIDDLE_RATE;//rateName = "高清";
		} else if(playlistInfo.getBandWitdh() >= LOW_BANDWIDTH) {
			rate = LOW_RATE;//rateName = "标清";
		} else {
			rateName = "流畅";
		}
    }

    @Override
    public int getRate() {
		return rate;
	}

	@Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public boolean isEncrypted() {
        return encryptionInfo != null;
    }

    @Override
    public boolean isPlayList() {
        return playlistInfo != null;
    }

    @Override
    public boolean isMedia() {
        return playlistInfo == null;
    }

    @Override
    public EncryptionInfo getEncryptionInfo() {
        return encryptionInfo;
    }

    @Override
    public PlaylistInfo getPlayListInfo() {
        return playlistInfo;
    }

    @Override
    public long getProgramDate() {
        return programDate;
    }

    @Override
    public String toString() {
        return "ElementImpl{" +
                "playlistInfo=" + playlistInfo +
                ", encryptionInfo=" + encryptionInfo +
                ", duration=" + duration +
                ", uri=" + uri +
                ", rateName=" + rateName +
                ", title='" + title + '\'' +
                '}';
    }

    static final class PlaylistInfoImpl implements PlaylistInfo {
        private final int programId;
        private final int bandWidth;
        private final String codec;

        public PlaylistInfoImpl(int programId, int bandWidth, String codec) {
            this.programId = programId;
            this.bandWidth = bandWidth;
            this.codec = codec;
        }

        @Override
        public int getProgramId() {
            return programId;
        }

        @Override
        public int getBandWitdh() {
            return bandWidth;
        }

        @Override
        public String getCodecs() {
            return codec;
        }

        @Override
        public String toString() {
            return "PlaylistInfoImpl{" +
                    "programId=" + programId +
                    ", bandWidth=" + bandWidth +
                    ", codec='" + codec + '\'' +
                    '}';
        }
    }

    static final class EncryptionInfoImpl implements EncryptionInfo {
        private final URI uri;
        private final String method;

        public EncryptionInfoImpl(URI uri, String method) {
            this.uri = uri;
            this.method = method;
        }

        @Override
        public URI getURI() {
            return uri;
        }

        @Override
        public String getMethod() {
            return method;
        }

        @Override
        public String toString() {
            return "EncryptionInfoImpl{" +
                    "uri=" + uri +
                    ", method='" + method + '\'' +
                    '}';
        }
    }
}
