package com.grosir.sembako.model;

/**
 * Jenis metode pembayaran transaksi grosir.
 * Mendukung Tunai (Cash) dan Tempo (Kredit).
 */
public enum PaymentType {
    CASH("Cash (Tunai)", "Cash"),
    CREDIT("Credit (Tempo)", "Tempo");

    private final String displayName;
    private final String shortName;

    PaymentType(String displayName, String shortName) {
        this.displayName = displayName;
        this.shortName = shortName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getShortName() {
        return shortName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static PaymentType fromString(String text) {
        if (text == null) return CASH;
        for (PaymentType type : PaymentType.values()) {
            if (type.name().equalsIgnoreCase(text) ||
                type.displayName.equalsIgnoreCase(text) ||
                type.shortName.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return CASH;
    }
}
