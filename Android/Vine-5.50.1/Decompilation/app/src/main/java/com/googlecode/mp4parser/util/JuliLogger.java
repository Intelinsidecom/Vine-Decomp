package com.googlecode.mp4parser.util;

import java.util.logging.Level;

/* loaded from: classes2.dex */
public class JuliLogger extends Logger {
    java.util.logging.Logger logger;

    public JuliLogger(String name) {
        this.logger = java.util.logging.Logger.getLogger(name);
    }

    @Override // com.googlecode.mp4parser.util.Logger
    public void logDebug(String message) {
        this.logger.log(Level.FINE, message);
    }

    @Override // com.googlecode.mp4parser.util.Logger
    public void logError(String message) {
        this.logger.log(Level.SEVERE, message);
    }
}
