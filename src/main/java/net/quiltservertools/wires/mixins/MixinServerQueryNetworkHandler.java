package net.quiltservertools.wires.mixins;

import net.minecraft.server.ServerMetadata;
import net.minecraft.server.network.ServerQueryNetworkHandler;
import net.quiltservertools.wires.config.Config;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(ServerQueryNetworkHandler.class)
public abstract class MixinServerQueryNetworkHandler {

    @Shadow
    @Final
    private ServerMetadata metadata;

    @Redirect(method = "onRequest", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerQueryNetworkHandler;metadata:Lnet/minecraft/server/ServerMetadata;"))
    private ServerMetadata showMaintenance(ServerQueryNetworkHandler instance) {

        if(Config.INSTANCE.isMaintenanceMode()) {
            return new ServerMetadata(
                    metadata.description(),
                    metadata.players(),
                    Optional.of(new ServerMetadata.Version("Maintenance", -1)),
                    metadata.favicon(),
                    false
            );
        }

        return metadata;
    }
}
