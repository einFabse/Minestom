package net.minestom.server.snapshot;

import net.minestom.server.instance.block.Block;
import net.minestom.server.tag.TagReadable;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public non-sealed interface ChunkSnapshot extends Snapshot, Block.Getter, Biome.Getter, TagReadable {
    int chunkX();

    int chunkZ();

    @NotNull List<@NotNull EntitySnapshot> entities();

    @NotNull List<@NotNull PlayerSnapshot> players();
}
