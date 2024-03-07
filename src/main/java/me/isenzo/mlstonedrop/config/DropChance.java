package me.isenzo.mlstonedrop.config;

import lombok.Data;

@Data
public class DropChance {
    private double defaultChance;
    private double vipChance;
    private double svipChance;
    private double sponsorChance;
    private int minY;
    private int maxY;
}
