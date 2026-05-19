package dev.ryanhcode.sable.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.sublevel.plot.LevelPlot;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.lighting.LevelLightEngine;

public class SableLightCommand {

    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher, final CommandBuildContext buildContext) {
        dispatcher.register(Commands.literal("light")
                .then(Commands.literal("get")
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes(ctx -> {
                                    final CommandSourceStack source = ctx.getSource();

                                    final BlockPos pos = BlockPosArgument.getBlockPos(ctx, "pos");

                                    final ServerLevel level = source.getLevel();
                                    final ServerSubLevelContainer container = SubLevelContainer.getContainer(level);

                                    final int chunkX = pos.getX() >> SectionPos.SECTION_BITS;
                                    final int chunkZ = pos.getZ() >> SectionPos.SECTION_BITS;

                                    LevelLightEngine lightEngine = level.getLightEngine();
                                    if (container.inBounds(chunkX, chunkZ)) {
                                        final LevelPlot plot = container.getPlot(chunkX, chunkZ);
                                        if (plot != null) {
                                            lightEngine = plot.getLightEngine();
                                        }
                                    }

                                    final int blockLight = lightEngine.getLayerListener(LightLayer.BLOCK).getLightValue(pos);
                                    final int skyLight = lightEngine.getLayerListener(LightLayer.SKY).getLightValue(pos);

                                    source.sendSuccess(() -> Component.literal("Light at %d %d %d:\n".formatted(pos.getX(), pos.getY(), pos.getZ())).append(
                                            Component.literal("  %d block\n  %d sky".formatted(blockLight, skyLight))
                                                    .withStyle(ChatFormatting.GRAY)), false);
                                    return Command.SINGLE_SUCCESS;
                                })
                        ))
        );
    }
}
