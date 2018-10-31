package com.hnjk.core.foundation.m3u8.utils;

import java.net.URI;

/**
 * Contains information about media encryption.
 */
public interface EncryptionInfo {
    public URI getURI();

    public String getMethod();
}
